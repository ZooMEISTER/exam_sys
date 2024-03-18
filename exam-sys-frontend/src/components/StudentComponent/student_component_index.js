import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { Outlet, useNavigate } from "react-router-dom"

import { Card, Tag, message, Modal, Form } from 'antd';

import UserInfo_Component from '../UserInfoComponent/userinfo_component_index';

import "./student_component_index.css"

const Student_Component = (props) => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const showStudentInfo = () => {
        setIsModalOpen(true);
    };
    const handleCancel = () => {
        setIsModalOpen(false);
    };

    return(
        <div>
            <div className='student-component-root-div'>
                <Card 
                    className='teacher-student-card'
                    onClick={showStudentInfo}

                    hoverable
                    bordered={false}
                    size="small"
                    cover={
                        <img className='teacher-student-card-avatar' alt='No Img' src={props.avatar}/>
                    }>
                    <div className='teacher-student-card-info-div'>
                        <label className='teacher-student-card-username'>{props.username}</label>
                        <label className='teacher-student-card-realname'>{props.realname}</label>
                    </div>
                </Card>
            </div>

            <UserInfo_Component
                modalName="学生信息"
                isModalOpen={isModalOpen}
                handleCancel={handleCancel}
                avatar={props.avatar}
                username={props.username}
                realname={props.realname}
                phone={props.phone}
                email={props.email}
            />
        </div>
    )
}

export default Student_Component