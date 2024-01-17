//import React, { useState } from 'react';
import { 
    Button,
    Checkbox, 
    Form, 
    Input,
    Card,
    message
} from "antd";

import { touristRequest } from '../../utils';
import { useNavigate } from "react-router-dom";


const LoginPage = () => {
    const navigate = useNavigate()

    const onFinish = (values) => {
        console.log('Success:', values);
        // 发送登录请求之前，先对用户输入的密码进行加密
        
    
        // 在这里发送登录请求
        // username: values.username
        // password: values.password
        touristRequest.post('/tourist/login', {
            username: values.username,
            password: values.password
        })
        .then(function (response) {
            console.log(response);
            if(response.data.resultCode === 10010){
                // 登录成功
                message.success("登录成功")
                // 在这里把返回的用户数据存入到redux中
                navigate("/login")
            }
            else if(response.data.resultCode === 10011){
                // 登录失败，用户不存在
                message.warning("用户不存在")
            }
            else if(response.data.resultCode === 10012){
                // 登录失败，密码错误
                message.warning("密码错误")
            }
            else{
                // 登录失败，其他原因
                message.error("登录失败: 其他原因")
            }
        })
        .catch(function (error) {
            console.log(error);
        })
        
    };
    const onFinishFailed = (errorInfo) => {
        console.log('Failed:', errorInfo);
    };

    return (
        <div>
            <Card title="登录" bordered={true} className='card-class'>
                <Form
                    name="loginForm"
                    labelCol={{span: 8}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    onFinish={onFinish}
                    onFinishFailed={onFinishFailed}
                    autoComplete="off"
                >
                    <Form.Item
                        label="用户名"
                        name="username"
                        rules={[{required: true,message: '请输入用户名'}]}>
                        <Input placeholder="用户名"/>
                    </Form.Item>

                    <Form.Item
                        label="密码"
                        name="password"
                        rules={[{required: true,message: '请输入密码'}]}>
                        <Input.Password placeholder="密码"/>
                    </Form.Item>

                    <Form.Item
                        name="remember"
                        valuePropName="checked"
                        wrapperCol={{offset: 8,span: 16}}>
                        <Checkbox>记住我</Checkbox>
                    </Form.Item>

                    <Form.Item
                        wrapperCol={{offset: 8,span: 16}}>
                        <Button type="primary" htmlType="submit">
                            登录
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    )
}

export default LoginPage