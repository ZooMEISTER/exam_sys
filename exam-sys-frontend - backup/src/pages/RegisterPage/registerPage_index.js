import React, { useState } from 'react';
import { 
    Button,
    Card, 
    Form, 
    Input,
    Space,
    Upload,
    message
} from "antd";
import { PlusOutlined } from '@ant-design/icons';

import { touristRequest } from '../../utils';
import { useNavigate } from "react-router-dom";

import './registerPage_index.css'

const RegisterPage = () => {
    const [form] = Form.useForm(); // 对表单的引用
    const [imageUrl, setImageUrl] = useState(); // 头像的url（base64）
    // const [messageApi] = message.useMessage();
    const navigate = useNavigate()

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


    // 确认密码的校验器
    const passwordConfValidator = (rule, val, callback) => {
        //console.log(val)
        //console.log(form.getFieldValue("password"))
        if(!(val === form.getFieldValue("password"))){
            callback("两次输入的密码不同")
        }
        callback()
    }


    // 表单验证成功后调用
    const onFinish = (values) => {
        console.log('Success:', values);
        // 发送注册请求之前，先对用户输入的密码进行加密


        // 在这里向后端发送注册请求
        // avatar: imageUrl
        // username: values.username
        // password: values.password
        touristRequest.post('/tourist/register', {
            avatar: imageUrl,
            username: values.username,
            password: values.password
        })
        .then(function (response) {
            console.log(response);
            if(response.data.resultCode === 10000){
                // 注册成功，跳转登录页
                message.success("注册成功")
                navigate("/login")
            }
            else if(response.data.resultCode === 10001){
                // 注册失败，用户名已存在
                message.warning("用户名已存在")
            }
            else{
                // 注册失败，其他原因
                message.error("注册失败: 其他原因")
            }
        })
        .catch(function (error) {
            console.log(error);
        })
    };
    const onFinishFailed = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };
    
    // 清空输入框
    const onReset = () => {
        form.resetFields();
    };

    return (
        <div>
            <Card title="注册" bordered={true} className='card-class'>
                <Form
                    name="registerForm"
                    form={form}
                    labelCol={{span: 8}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
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
                        <Input placeholder='用户名'/>
                    </Form.Item>

                    <Form.Item
                        label="密码"
                        name="password"
                        rules={[
                            {required: true, message: '请输入密码'},
                            {pattern: "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$", message: '密码仅能且须由字母和数字组成,且长度为 8-16 位'}
                        ]}>
                        <Input.Password placeholder='密码'/>
                    </Form.Item>

                    <Form.Item
                        label="确认密码"
                        name="password_confirm"
                        rules={[
                                {required: true, message: '请确认密码'},
                                {validator: passwordConfValidator}
                            ]}>
                        <Input.Password placeholder='确认密码' />
                    </Form.Item>

                    <Form.Item
                        wrapperCol={{offset: 8,span: 16}}>
                        <Space>
                            <Button type="primary" htmlType="submit">
                                注册
                            </Button>
                            <Button type="default" onClick={onReset}>
                                清空
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    )
}

export default RegisterPage