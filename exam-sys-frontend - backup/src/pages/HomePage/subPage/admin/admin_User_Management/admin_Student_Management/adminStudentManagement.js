import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../../utils/request';

import UserInfo_Component from '../../../../../../components/UserInfoComponent/userinfo_component_index';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined, ExclamationCircleFilled, PlusOutlined } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message, Affix, Modal, Input, InputNumber, Table, Space, Select, Form, Upload, Switch } from 'antd';

import './adminStudentManagement.css'

const Admin_StudentManagement = () => {
    const state = useLocation()
    const navigate = useNavigate()
    const { confirm } = Modal;
    const { TextArea } = Input;
    const [form] = Form.useForm(); // 对表单的引用

    const [searchStr, setSearcStr] = useState("")
    const [studentList, setStudentList] = useState("")

    const [isOpenModal, setIsOpenMadal] = useState(false)
    const [curUserId, setCurUserId] = useState("")
    const [newAvatar, setNewAvatar] = useState('')
    const [newUsername, setNewUsername] = useState('')
    const [newRealname, setNewRealname] = useState('')
    const [newPhone, setNewPhone] = useState('')
    const [newEmail, setNewEmail] = useState('')
    const [resetPwd, setResetPwd] = useState(false)


    const columns = [
        {
            title: '老师ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '老师头像',
            dataIndex: 'avatar',
            key: 'avatar',
            render: (text) => <img className='admin-student-management-icon' src={text}/>,
        },
        {
            title: '用户名',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: '真名',
            dataIndex: 'realname',
            key: 'realname',
        },
        {
            title: '电话',
            dataIndex: 'phone',
            key: 'phone',
        },
        {
            title: '邮箱',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <Space>
                    <Button onClick={(e) => openModal(record)} type='primary'>编辑</Button>
                </Space>
            ),
        },
    ];

    const openModal = (record) => {
        setCurUserId(record.id)
        setNewAvatar(record.avatar)
        setNewUsername(record.username)
        setNewRealname(record.realname)
        setNewPhone(record.phone)
        setNewEmail(record.email)
        setResetPwd(false)

        setIsOpenMadal(true)
    }

    const changeSearchStr = (e) => {
        setSearcStr(e.target.value)
    }

    const search = () => {
        console.log(searchStr)
        userRequest.post("/admin/get-student", {
            searchStr: searchStr
        })
        .then(function(response) {
            console.log(response)
            
            setStudentList(response)
        })
        .catch(function (error) {
            console.log(error)
        })
    }

    const handleOk = () => {
        console.log(curUserId)
        console.log(newAvatar)
        console.log(newUsername)
        console.log(newRealname)
        console.log(newPhone)
        console.log(newEmail)
        console.log(resetPwd)

        if(newAvatar == ""){
            message.info("头像不能为空")
        }
        else if(newUsername == ""){
            message.info("用户名不能为空")
        }
        else if(newRealname == ""){
            message.info("真名不能为空")
        }
        else if(newPhone == ""){
            message.info("电话不能为空")
        }
        else if(newEmail == ""){
            message.info("邮箱不能为空")
        }
        else{
            userRequest.post("/admin/update-student-info", {
                curUserId: curUserId,
                newAvatar: newAvatar,
                newUsername: newUsername,
                newRealname: newRealname,
                newPhone: newPhone,
                newEmail: newEmail,
                resetPwd: resetPwd
            })
            .then(function(response) {
                console.log(response)
                if(response.resultCode == 12070){
                    message.success(response.msg)
                }
                else if(response.resultCode == 12071){
                    message.error(response.msg)
                }
                
                search()
                setIsOpenMadal(false)
            })
            .catch(function (error) {
                console.log(error)
            })
        }

        
    }
    const handleCancel = () => {
        setCurUserId('')
        setNewAvatar('')
        setNewUsername('')
        setNewRealname('')
        setNewPhone('')
        setNewEmail('')
        setResetPwd(false)

        setIsOpenMadal(false)
    }

    // 上传图片占位按钮
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
                setNewAvatar(url);
                console.log(url)
            });
        }
        return false;
    };


    const changeUsernameInput = (e) => {
        setNewUsername(e.target.value)
    }
    const changeRealnameInput = (e) => {
        setNewRealname(e.target.value)
    }
    const changePhoneInput = (e) => {
        setNewPhone(e.target.value)
    }
    const changeEmailInput = (e) => {
        setNewEmail(e.target.value)
    }
    const onChange = (checked) => {
        setResetPwd(checked)
    }

    return(
        <div>
            {/* 管理员-学生管理 */}

            <div className="admin-student-management-div">

                <div className='admin-student-management-filter-outer-div'>
                    <div className='admin-student-management-filter-inner-div'>
                        <label>真名：</label>
                        <Input className='admin-student-management-search-input' onChange={changeSearchStr}></Input>
                    </div>
                    
                    <Button type='primary' onClick={search} className='admin-student-management-search-button'>搜索</Button>
                </div>

                <Table className='admin-student-management-table' dataSource={studentList} columns={columns} />

            </div>


            <Modal
                title='编辑学生信息'
                open={isOpenModal}
                onOk={handleOk}
                onCancel={handleCancel}
                okText="确认"
                cancelText="取消"
            >
                <Form
                    name="studentInfoForm"
                    form={form}
                    labelCol={{span: 6}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    autoComplete="off"
                >
                    <Form.Item
                        label="头像"
                        name="avatar"
                        rules={[{required: true,message: '请选择图片'}]}>
                        <Upload
                            name="avatar"
                            listType="picture-card"
                            className="avatar-uploader"
                            showUploadList={false}
                            action=""
                            beforeUpload={beforeUpload}
                            >
                            {newAvatar ? (
                                <img src={newAvatar} alt="icon" style={{width: '100%',}}/>
                            ) : (
                                uploadButton
                            )}
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        label="用户名"
                        name="username"
                        rules={[{ required: true, message: '请输入用户名'}]}>
                        <div className='user-form-item-content'>
                            <Input placeholder='用户名' onChange={changeUsernameInput} value={newUsername}/>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="真名"
                        name="realname"
                        rules={[{ required: true, message: '请输入真名'}]}
                    >
                        <div className='user-form-item-content'>
                            <Input placeholder='真名' onChange={changeRealnameInput} value={newRealname}/>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="电话"
                        name="phone"
                        rules={[{ required: true, message: '请输入电话'}]}
                    >
                        <div className='user-form-item-content'>
                            <Input placeholder='电话' onChange={changePhoneInput} value={newPhone}/>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="邮箱"
                        name="email"
                        rules={[{ required: true, message: '请输入邮箱'}]}
                    >
                        <div className='user-form-item-content'>
                            <Input placeholder='邮箱' onChange={changeEmailInput} value={newEmail}/>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="重置密码"
                        name="resetPwd"
                    >
                        <div className='user-form-item-content'>
                            <Switch onChange={onChange} checked={resetPwd}/>
                        </div>
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    )
}

export default Admin_StudentManagement