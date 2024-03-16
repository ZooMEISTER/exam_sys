import { 
    Menu
} from "antd";

import React, { useState } from 'react';
import { Outlet, useNavigate } from "react-router-dom"

import { useSelector } from "react-redux";

import { homePageMenuItems_Teacher, homePageMenuItems_Student } from "../../constants/HomePageMenuItems.js"

//该页的样式文件
import './homePage_index.css'

const HomePage = () => {
    const navigate = useNavigate()
    const [currentSelectedMenuItem, setCurrentSelectedMenuItem] = useState("");
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const onMenuClick = (e) => {
        console.log('click ', e);
        setCurrentSelectedMenuItem(e.key);

        navigate(e.key)
    };


    if(permissionLevel > 0){
        if(permissionLevel === 1){
            // navigate("/home/student-choose-class")
            return (
                <div className="home-page-root">
                    <Menu className="home-page-menu"
                        onClick={onMenuClick} 
                        selectedKeys={[currentSelectedMenuItem]} 
                        mode="inline" 
                        items={homePageMenuItems_Student}
                    />
                    <Outlet/>
                </div>
            )
            
        }
        else if(permissionLevel === 2){
            // navigate("/home/teacher-operate-class")
            return (
                <div className="home-page-root">
                    <Menu className="home-page-menu"
                        onClick={onMenuClick} 
                        selectedKeys={[currentSelectedMenuItem]} 
                        mode="inline" 
                        items={homePageMenuItems_Teacher}
                    />
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