import { 
    Menu
} from "antd";

import React, { useEffect, useState } from 'react';
import { Outlet, useNavigate } from "react-router-dom"

import { useSelector } from "react-redux";

import { homePageMenuItems_Admin, homePageMenuItems_Teacher, homePageMenuItems_Student } from "../../constants/HomePageMenuItems.js"

//该页的样式文件
import './homePage_index.css'
import userEvent from "@testing-library/user-event";

const HomePage = () => {
    const navigate = useNavigate()
    const [currentSelectedMenuItem, setCurrentSelectedMenuItem] = useState("");

    const userid = useSelector(state => state.userid.value)
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const onMenuClick = (e) => {
        console.log('click ', e);
        setCurrentSelectedMenuItem(e.key);

        navigate(e.key)
    };


    // 组件加载时自动执行
    useEffect(() => {
        // if(userid == 1){
        //     navigate('/home/student-choose-class')
        // }
        // else if(userid == 2){
        //     navigate('/home/teacher-operate-class')
        // }
	}, []);


    if(permissionLevel > 0){
        if(permissionLevel === 1){
            // navigate("/home/student-choose-class")
            return (
                <div className="home-page-root">
                    <div className="home-page-menu-outer-div">
                        <Menu className="home-page-menu"
                            onClick={onMenuClick} 
                            selectedKeys={[currentSelectedMenuItem]} 
                            mode="inline" 
                            theme="light"
                            items={homePageMenuItems_Student}
                        />
                    </div>
                    <Outlet/>
                </div>
            )
            
        }
        else if(permissionLevel === 2){
            // navigate("/home/teacher-operate-class")
            return (
                <div className="home-page-root">
                    <div className="home-page-menu-outer-div">
                        <Menu className="home-page-menu"
                            onClick={onMenuClick} 
                            selectedKeys={[currentSelectedMenuItem]} 
                            mode="inline" 
                            theme="light"
                            items={homePageMenuItems_Teacher}
                        />
                    </div>
                    <Outlet/>
                </div>
            )
            
        }
        else if(permissionLevel === 3){
            // navigate("/home/teacher-operate-class")
            return (
                <div className="home-page-root">
                    <div className="home-page-menu-outer-div">
                        <Menu className="home-page-menu"
                            onClick={onMenuClick} 
                            selectedKeys={[currentSelectedMenuItem]} 
                            mode="inline" 
                            theme="light"
                            items={homePageMenuItems_Admin}
                        />
                    </div>
                    <Outlet/>
                </div>
            )
            
        }
        
    }
    else{
        return (
            <div className="home-page-root">
                <div>
                    请先登录
                </div>
            </div>
        )
    }
    
}

export default HomePage