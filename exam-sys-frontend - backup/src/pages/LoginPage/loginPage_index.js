//import React, { useState } from 'react';
import { 
    Button,
    Checkbox, 
    Form, 
    Input,
    Card,
    message
} from "antd";

import { useDispatch } from "react-redux";
import { avatar_setValue } from "../../store/modules/avatarStore";
import { email_setValue } from "../../store/modules/emailStore";
import { permissionLevel_setValue } from "../../store/modules/permissionLevelStore";
import { phone_setValue } from "../../store/modules/phoneStore";
import { realname_setValue } from "../../store/modules/realnameStore";
import { token_setValue } from "../../store/modules/tokenStore";
import { userid_setValue } from "../../store/modules/useridStore";
import { username_setValue } from "../../store/modules/usernameStore";

import { touristRequest } from '../../utils/request';
import storageUtils from '../../utils/storage'
import memoryUtils from '../../utils/memory'
import { useNavigate } from "react-router-dom";
import { useState } from "react";


const LoginPage = () => {
    const navigate = useNavigate()
    const dispatch = useDispatch()

    const [ rememberCheck, setRememberCheck ] = useState(true)

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
        .then(async function (response) {
            console.log(response);
            if(response.data.resultCode === 10010){
                // 登录成功
                message.success("登录成功")
                // 在这里把返回的用户数据存入到redux中
                dispatch(avatar_setValue(response.data.avatar))
                dispatch(email_setValue(response.data.email))
                dispatch(permissionLevel_setValue(response.data.permissionLevel))
                dispatch(phone_setValue(response.data.phone))
                dispatch(realname_setValue(response.data.realname))
                dispatch(token_setValue(response.data.token))
                dispatch(userid_setValue(response.data.userid))
                dispatch(username_setValue(response.data.username))

                const user = response.data
                user.expiration = new Date().getTime() + (24 * 60 * 60 * 1000);
                //如何显示用户信息呢？需要储存起来
                memoryUtils.user = JSON.stringify(user)     //保存在内存中
                storageUtils.saveUser(user) //保存到local中


                // 这里判断用户是否选择了"记住我"
                // 选了就将 token 存到 cookie 中
                if(rememberCheck === true){
                    console.log("Remember me")
                    localStorage.setItem("exam-sys-login-token", response.data.token)
                }
                else{
                    console.log("not Remember me")
                    localStorage.removeItem("exam-sys-login-token")
                }
                
                navigate("/")
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
    // 记住我 Checkbox 变化事件
    const onCheckboxChange = (e) => {
        console.log(`checked = ${e.target.checked}`);
        setRememberCheck(`${e.target.checked}`)
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
                        rules={[{required: true, message: '请输入用户名'}]}>
                        <Input placeholder="用户名"/>
                    </Form.Item>

                    <Form.Item
                        label="密码"
                        name="password"
                        rules={[{required: true, message: '请输入密码'}]}>
                        <Input.Password placeholder="密码"/>
                    </Form.Item>

                    <Form.Item
                        name="remember"
                        valuePropName="checked"
                        wrapperCol={{offset: 8, span: 16}}>
                        <Checkbox 
                            onChange={onCheckboxChange}>
                                记住我
                        </Checkbox>
                    </Form.Item>

                    <Form.Item
                        wrapperCol={{offset: 8, span: 16}}>
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