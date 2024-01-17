import React, { useState } from 'react';
import { Outlet } from "react-router-dom"

import { 
    Menu,
} from "antd";

// 顶部菜单栏的信息
import mainMenuItems_NoLogin from '../../constants/MainMenuItems';

//该页的样式文件
import './basePage_index.css'

const BasePage = () => {
    const [currentSelectedMenuItem, setCurrentSelectedMenuItem] = useState("");
    const onClick = (e) => {
        console.log('click ', e);
        setCurrentSelectedMenuItem(e.key);
    };
    return (
        <div>
            {/* 这是 BasePage,包含了 header（导航栏） 和 footer（信息栏） */}
            <Menu onClick={onClick} selectedKeys={[currentSelectedMenuItem]} mode="horizontal" items={mainMenuItems_NoLogin}/>

            {/* 二级路由嵌套显示 */}
            <Outlet/>

            <div className='base-page-footer-div'>
                <span className='base-page-footer-div-text'>Made by ZooMEISTER with 💩</span>
            </div>
        </div>
    )
        
}

export default BasePage