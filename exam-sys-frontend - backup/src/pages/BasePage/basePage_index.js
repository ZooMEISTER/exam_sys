import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from "react-router-dom"

import { 
    Menu,
    message,
    Modal
} from "antd";

import { touristRequest } from '../../utils';

import { useDispatch, useSelector } from "react-redux";

import { avatar_setValue } from "../../store/modules/avatarStore";
import { email_setValue } from "../../store/modules/emailStore";
import { permissionLevel_setValue } from "../../store/modules/permissionLevelStore";
import { phone_setValue } from "../../store/modules/phoneStore";
import { realname_setValue } from "../../store/modules/realnameStore";
import { token_setValue } from "../../store/modules/tokenStore";
import { userid_setValue } from "../../store/modules/useridStore";
import { username_setValue } from "../../store/modules/usernameStore";

import storageUtils from '../../utils/storage'
import memoryUtils from '../../utils/memory'

// 顶部菜单栏的信息
import { mainMenuItems_NoLogin, mainMenuItems_Logged } from '../../constants/MainMenuItems';

//该页的样式文件
import './basePage_index.css'
import { floatButtonPrefixCls } from 'antd/es/float-button/FloatButton';

const BasePage = () => {
    const [isLogoutModalOpen, setIsLogoutModalOpen] = useState(false);

    const navigate = useNavigate()
    const dispatch = useDispatch()

    const [currentSelectedMenuItem, setCurrentSelectedMenuItem] = useState("");
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const onClick = (e) => {
        console.log('click ', e);
        setCurrentSelectedMenuItem(e.key);

        if(e.key === "logout"){
            setIsLogoutModalOpen(true)
        }
        else{
            navigate(e.key)
        }
    };

    const handleOk = () => {
        // 退出登录
        localStorage.removeItem("exam-sys-login-token")
        storageUtils.deleteUser()

        dispatch(avatar_setValue(""))
        dispatch(email_setValue(""))
        dispatch(permissionLevel_setValue(0))
        dispatch(phone_setValue(""))
        dispatch(realname_setValue(""))
        dispatch(token_setValue(""))
        dispatch(userid_setValue(0))
        dispatch(username_setValue(""))

        memoryUtils.user = "{}"

        navigate("/")

        setIsLogoutModalOpen(false)
    }
    const handleCancel = () => {
        setIsLogoutModalOpen(false)
    }

    // 自动登录方法
    const autoLogin = () => {
        var token = localStorage.getItem("exam-sys-login-token")
        
        console.log(memoryUtils.user)

        // 当 localStorage 中有用户信息对象且未过期
        if(memoryUtils.user !== "{}" && JSON.parse(memoryUtils.user).expiration > new Date().getTime()){
            console.log("从localstorage获取用户信息")
            dispatch(avatar_setValue(JSON.parse(memoryUtils.user).avatar))
            dispatch(email_setValue(JSON.parse(memoryUtils.user).email))
            dispatch(permissionLevel_setValue(JSON.parse(memoryUtils.user).permissionLevel))
            dispatch(phone_setValue(JSON.parse(memoryUtils.user).phone))
            dispatch(realname_setValue(JSON.parse(memoryUtils.user).realname))
            dispatch(token_setValue(JSON.parse(memoryUtils.user).token))
            dispatch(userid_setValue(JSON.parse(memoryUtils.user).userid))
            dispatch(username_setValue(JSON.parse(memoryUtils.user).username))
        }
        else{
            // 调用后端自动登录接口
            if(localStorage.getItem("exam-sys-login-token") != null && permissionLevel <= 0){
                touristRequest.post('/tourist/autologin', {
                    token: token
                })
                .then(async function (response) {
                    console.log(response);
                    if(response.data.resultCode === 10020){
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
                        memoryUtils.user = JSON.stringify(user)    //保存在内存中
                        storageUtils.saveUser(user) //保存到local中
                    }
                    else if(response.data.resultCode === 10021){
                        // 自动登录失败，无效 token
                        message.warning("无效token")
                    }
                    else if(response.data.resultCode === 10022){
                        // 自动登录失败，用户不存在
                        message.warning("用户不存在")
                    }
                    else if(response.data.resultCode === 10023){
                        // 自动登录失败，无效个人信息版本
                        message.warning("无效个人信息版本")
                    }
                    else{
                        // 自动登录失败，其他原因
                        message.error("自动登录失败: 其他原因")
                    }
                })
                .catch(function (error) {
                    console.log(error);
                })
            }
        }
    }


    // 执行自动登录
    useEffect(() => {
        autoLogin()
	}, []);
    return (
        <div>
            {/* 这是 BasePage,包含了 header（导航栏） 和 footer（信息栏） */}
            <Menu onClick={onClick} 
                selectedKeys={[currentSelectedMenuItem]} 
                theme="light" 
                mode="horizontal" 
                className='base-page-base-menu'
                items={permissionLevel > 0 ? mainMenuItems_Logged : mainMenuItems_NoLogin}
            />

            {/* 二级路由嵌套显示 */}
            <Outlet/>

            <div className='base-page-footer-div'>
                <span className='base-page-footer-div-text'>Made by ZooMEISTER with 💩</span>
            </div>

            <Modal title="退出" open={isLogoutModalOpen} onOk={handleOk} onCancel={handleCancel} okText="确认" cancelText="取消">
                <p>你确定要退出登陆吗</p>
            </Modal>
        </div>
    )
        
}

export default BasePage