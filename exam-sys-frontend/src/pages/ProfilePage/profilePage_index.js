import React, { useState } from 'react';
import { useSelector } from "react-redux";

import { 
    Button,
    Card, 
    Form, 
    Input,
    Space,
    Upload,
    message,
    Modal
} from "antd";
import { PlusOutlined } from '@ant-design/icons';

import { touristRequest, userRequest } from '../../utils/request';
import { useNavigate } from "react-router-dom";

import storageUtils from '../../utils/storage'
import memoryUtils from '../../utils/memory'

import { useDispatch } from "react-redux";
import { avatar_setValue } from "../../store/modules/avatarStore";
import { email_setValue } from "../../store/modules/emailStore";
import { permissionLevel_setValue } from "../../store/modules/permissionLevelStore";
import { phone_setValue } from "../../store/modules/phoneStore";
import { realname_setValue } from "../../store/modules/realnameStore";
import { token_setValue } from "../../store/modules/tokenStore";
import { userid_setValue } from "../../store/modules/useridStore";
import { username_setValue } from "../../store/modules/usernameStore";

import "./profilePage_index.css"

const ProfilePage = () =>{
    const userid = useSelector(state => state.userid.value)
    const avatar = useSelector(state => state.avatar.value)
    const username = useSelector(state => state.username.value)
    const realname = useSelector(state => state.realname.value)
    const email = useSelector(state => state.email.value)
    const phone = useSelector(state => state.phone.value)
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const [isUsernameModalOpen, setIsUsernameModalOpen] = useState(false);
    const [isRealnameModalOpen, setIsRealnameModalOpen] = useState(false);
    const [isPhoneModalOpen, setIsPhoneModalOpen] = useState(false);
    const [isEmailModalOpen, setIsEmailModalOpen] = useState(false);
    const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);

    const [newAvatar, setNewAvatar] = useState(avatar)
    const [newUsername, setNewUsername] = useState(username)
    const [newRealname, setNewRealname] = useState(realname)
    const [newPhone, setNewPhone] = useState(phone)
    const [newEmail, setNewEmail] = useState(email)
    const [newPassword, setNewPassword] = useState("NOT_CHANGE")
    const [newPassword_, setNewPassword_] = useState("NOT_CHANGE")

    const [form] = Form.useForm(); // 对表单的引用
    const [imageUrl, setImageUrl] = useState(newAvatar); // 头像的url（base64）
    const navigate = useNavigate()
    const dispatch = useDispatch()


    // 发送更改用户信息的请求
    const submitChangeProfileForm = () => {
        console.log(userid)
        console.log(imageUrl)
        console.log(newUsername)
        console.log(newRealname)
        console.log(newPhone)
        console.log(newEmail)
        console.log(newPassword)

        if(permissionLevel === 1){
            // 学生更改个人信息
            userRequest.post("/student/update-profile", {
                userid: userid,
                avatar: imageUrl,
                username: newUsername,
                realname: newRealname,
                phone: newPhone,
                email: newEmail,
                password: newPassword
            })
            .then( function(response) {
                console.log(response)
                if(response.resultCode === 12000){
                    // 用户信息更改成功
                    message.success("用户信息修改成功")
                    // 在这里把返回的用户数据存入到redux中
                    dispatch(avatar_setValue(response.avatar))
                    dispatch(email_setValue(response.email))
                    dispatch(permissionLevel_setValue(response.permissionLevel))
                    dispatch(phone_setValue(response.phone))
                    dispatch(realname_setValue(response.realname))
                    dispatch(token_setValue(response.token))
                    dispatch(userid_setValue(response.userid))
                    dispatch(username_setValue(response.username))
    
                    const user = response
                    user.expiration = new Date().getTime() + (24 * 60 * 60 * 1000);
                    // 如何显示用户信息呢？需要储存起来
                    memoryUtils.user = JSON.stringify(user)     //保存在内存中
                    storageUtils.saveUser(user) //保存到local中
    
                    localStorage.setItem("exam-sys-login-token", response.token)
                    
                    navigate("/")
                }
                else if(response.resultCode === 12001){
                    // 用户信息更改失败
                    message.warning("用户信息修改失败")
                }
                else if(response.resultCode === 10022){
                    // 用户不存在
                    message.warning("10022,请尝试重新登录,或联系管理员")
                }
            })
            .catch( function (error) {
                console.log(error)
            })
        }
        else if(permissionLevel === 2){
            // 老师更改个人信息
            userRequest.post("/teacher/update-profile", {
                userid: userid,
                avatar: imageUrl,
                username: newUsername,
                realname: newRealname,
                phone: newPhone,
                email: newEmail,
                password: newPassword
            })
            .then( function(response) {
                console.log(response)
                if(response.resultCode === 12000){
                    // 用户信息更改成功
                    message.success("用户信息修改成功")
                    // 在这里把返回的用户数据存入到redux中
                    dispatch(avatar_setValue(response.avatar))
                    dispatch(email_setValue(response.email))
                    dispatch(permissionLevel_setValue(response.permissionLevel))
                    dispatch(phone_setValue(response.phone))
                    dispatch(realname_setValue(response.realname))
                    dispatch(token_setValue(response.token))
                    dispatch(userid_setValue(response.userid))
                    dispatch(username_setValue(response.username))
    
                    const user = response
                    user.expiration = new Date().getTime() + (24 * 60 * 60 * 1000);
                    // 如何显示用户信息呢？需要储存起来
                    memoryUtils.user = JSON.stringify(user)     //保存在内存中
                    storageUtils.saveUser(user) //保存到local中
    
                    localStorage.setItem("exam-sys-login-token", response.token)
                    
                    navigate("/")
                }
                else if(response.resultCode === 12001){
                    // 用户信息更改失败
                    message.warning("用户信息修改失败")
                }
                else if(response.resultCode === 10022){
                    // 用户不存在
                    message.warning("10022,请尝试重新登录,或联系管理员")
                }
            })
            .catch( function (error) {
                console.log(error)
            })
        }
    };


    // 上传头像占位按钮
    const uploadButton = (
        <div>
            <PlusOutlined />
            <div style={{marginTop: 8}}>
                上传
            </div>
        </div>
    );

    // 图片转成base64
    const getBase64 = (img, callback) => {
        const reader = new FileReader();
        reader.addEventListener('load', () => callback(reader.result));
        reader.readAsDataURL(img);
    };
    // 选择图片后，进行基础判断，并将图片显示出来
    const beforeUpload = (file) => {
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            message.error('您只能上传 JPG / PNG 格式的图片 !');
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
            message.error('头像文件大小需小于 2MB !');
        }
        if(isJpgOrPng && isLt2M){
            getBase64(file, (url) => {
                setImageUrl(url);
                console.log(url)
            });
        }
        return false;
    };

    const changeUsername = () =>{
        setIsUsernameModalOpen(true)
    }
    const changeRealname = () =>{
        setIsRealnameModalOpen(true)
    }
    const changePhone = () =>{
        setIsPhoneModalOpen(true)
    }
    const changeEmail = () =>{
        setIsEmailModalOpen(true)
    }
    const changePassword = () =>{
        setNewPassword("")
        setNewPassword_("")
        setIsPasswordModalOpen(true)
    }

    const newUsernameInputChange = (event) => {
        setNewUsername(event.target.value)
    }
    const newRealnameInputChange = (event) => {
        setNewRealname(event.target.value)
    }
    const newPhoneInputChange = (event) => {
        setNewPhone(event.target.value)
    }
    const newEmailInputChange = (event) => {
        setNewEmail(event.target.value)
    }   
    const newPasswordInputChange = (event) => {
        setNewPassword(event.target.value)
    }
    const newPassword_InputChange = (event) => {
        setNewPassword_(event.target.value)
    }

    // 密码表单验证
    const handlePasswordOk = () => {
        if(newPassword !== newPassword_){
            // 两次输入的密码不相同
            message.error("两次输入的密码不相同")
        }
        else if(newPassword === ""){
            // 密码为空
            message.error("密码不能为空")
        }
        else if(newPassword.length < 8){
            // 密码过短
            message.error("密码过短")
        }
        else{
            setIsPasswordModalOpen(false)
        }
    }
    const handleModalClose = () =>{
        setIsUsernameModalOpen(false)
        setIsRealnameModalOpen(false)
        setIsPhoneModalOpen(false)
        setIsEmailModalOpen(false)
        setIsPasswordModalOpen(false)
    }
    const handlePasswordModalClose = () => {
        setNewPassword("NOT_CHANGE")
        setNewPassword_("NOT_CHANGE")
        setIsPasswordModalOpen(false)
    }

    return (
        <div>
            <Card title="个人资料" bordered={true} className='card-class'>
                <Form
                    name="profileForm"
                    form={form}
                    labelCol={{span: 8}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    autoComplete="off"
                >
                    <Form.Item
                        label="头像"
                        name="avatar"
                        rules={[{required: true,message: '请选择头像'}]}>
                        <Upload
                            name="avatar"
                            listType="picture-card"
                            className="avatar-uploader"
                            showUploadList={false}
                            action=""
                            beforeUpload={beforeUpload}
                            >
                            {imageUrl ? (
                                <img src={imageUrl} alt="avatar" style={{width: '100%',}}/>
                            ) : (
                                uploadButton
                            )}
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        label="用户名"
                        name="username"
                        rules={[{ required: true, message: '请输入用户名'}]}>
                        <div className='profile-form-item-content'>
                            <Input placeholder='用户名' value={newUsername} disabled/>
                            <Button className='profile-form-item-content-btn' onClick={changeUsername}>修改</Button>
                            <Modal title="更改用户名" open={isUsernameModalOpen} onOk={handleModalClose} onCancel={handleModalClose}>
                                <Input value={newUsername} onChange={newUsernameInputChange}/>
                            </Modal>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="真名"
                        name="realname"
                        rules={[{ required: true, message: '请输入真名'}]}>
                        <div className='profile-form-item-content'>
                            <Input placeholder='真名' value={newRealname} disabled/>
                            <Button className='profile-form-item-content-btn' onClick={changeRealname}>修改</Button>
                            <Modal title="更改真名" open={isRealnameModalOpen} onOk={handleModalClose} onCancel={handleModalClose}>
                                <Input value={newRealname} onChange={newRealnameInputChange}/>
                            </Modal>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="电话"
                        name="phone"
                        rules={[{ required: true, message: '请输入电话'}]}>
                        <div className='profile-form-item-content'>
                            <Input placeholder='电话' value={newPhone} disabled/>
                            <Button className='profile-form-item-content-btn' onClick={changePhone}>修改</Button>
                            <Modal title="更改电话" open={isPhoneModalOpen} onOk={handleModalClose} onCancel={handleModalClose}>
                                <Input value={newPhone} onChange={newPhoneInputChange}/>
                            </Modal>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="邮箱"
                        name="email"
                        rules={[{ required: true, message: '请输入邮箱'}]}>
                        <div className='profile-form-item-content'>
                            <Input placeholder='邮箱' value={newEmail} disabled/>
                            <Button className='profile-form-item-content-btn' onClick={changeEmail}>修改</Button>
                            <Modal title="更改邮箱" open={isEmailModalOpen} onOk={handleModalClose} onCancel={handleModalClose}>
                                <Input value={newEmail} onChange={newEmailInputChange}/>
                            </Modal>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="密码"
                        name="password"
                        rules={[{ required: true, message: '请输入密码'}]}>
                        <div className='profile-form-item-content'>
                            <Input placeholder='密码' defaultValue={"********"} disabled/>
                            <Button className='profile-form-item-content-btn' onClick={changePassword}>修改</Button>
                            <Modal title="更改密码" open={isPasswordModalOpen} onOk={handlePasswordOk} onCancel={handlePasswordModalClose}>
                                <Input value={newPassword} onChange={newPasswordInputChange}/>
                                <Input value={newPassword_} onChange={newPassword_InputChange}/>
                            </Modal>
                        </div>
                    </Form.Item>
                    <Form.Item
                        wrapperCol={{offset: 8,span: 16}}>
                        <Space>
                            <Button type="primary" onClick={submitChangeProfileForm}>
                                保存
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    )
}

export default ProfilePage