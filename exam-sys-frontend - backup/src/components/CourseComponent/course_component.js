import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { Outlet, useNavigate } from "react-router-dom"

import { Card } from 'antd';

import "./course_component.css"

const Course_Component = (props) => {
    const navigate = useNavigate()
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const ClickCourse = (() => {
        console.log(props.id)
        if(permissionLevel === 2){
            navigate('/home/teacher-operate-class/exam', 
            {
                state: {
                    departmentId: props.departmentId, 
                    departmentName: props.departmentName, 
                    subjectId: props.subjectId, 
                    subjectName: props.subjectName,
                    courseId: props.id,
                    courseIcon: props.icon,
                    courseName: props.name,
                    courseDescription: props.description,
                    courseTeachby: props.teachby,
                    courseCreated_time: props.created_time
                }
            })
        }
        else if(permissionLevel === 1){
            navigate('/home/student-choose-class/exam', 
            {
                state: {
                    departmentId: props.departmentId, 
                    departmentName: props.departmentName, 
                    subjectId: props.subjectId, 
                    subjectName: props.subjectName,
                    courseId: props.id,
                    courseIcon: props.icon,
                    courseName: props.name,
                    courseDescription: props.description,
                    courseTeachby: props.teachby,
                    courseCreated_time: props.created_time
                }
            })
        }
    })

    return(
        <div className='comp-root-div'>
            <Card
                onClick={ClickCourse}
                className='course-card'
                hoverable
                bordered={false}
                cover={
                    <img className='course-cover-img' alt='No Img' src={props.icon}/>
                }
            >
                {/* {props.id} <br/>
                {props.name} <br/>
                {props.description} <br/>
                {props.teachby} <br/>
                {props.created_time} <br/> */}
                <div className='course-card-info-div'>
                    <label className='course-card-title'>{props.name}</label>
                    <label className='course-card-description'>{props.description}</label>
                    <label className='course-card-teachby'>由 {props.teacherRealname} 教学</label>
                    <label className='course-card-created-time'>创建：{props.created_time}</label>
                </div>
            </Card>
        </div>
    )
}

export default Course_Component