import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { Outlet, useNavigate } from "react-router-dom"

import { Card } from 'antd';

import "./subject_component.css"

const Subject_Component = (props) => {
    const navigate = useNavigate()
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const ClickSubject = (() => {
        console.log(props.id)
        if(permissionLevel === 2){
            navigate('/home/teacher-operate-class/course', 
            {
                state: {
                    departmentId: props.departmentId, 
                    departmentName: props.departmentName, 
                    subjectId: props.id, 
                    subjectName: props.name
                }
            })
        }
        else if(permissionLevel === 1){
            navigate('/home/student-choose-class/course', 
            {
                state: {
                    departmentId: props.departmentId, 
                    departmentName: props.departmentName, 
                    subjectId: props.id, 
                    subjectName: props.name
                }
            })
        }
    })

    return(
        <div className='comp-root-div'>
            <Card
                onClick={ClickSubject}
                className='subject-card'
                hoverable
                bordered={false}
                cover={
                    <img className='subject-cover-img' alt='No Img' src={props.icon}/>
                }
            >
                {/* {props.id} <br/>
                {props.name} <br/>
                {props.description} <br/>
                {props.course_count} <br/> */}
                
                <div className='subject-card-info-div'>
                    <label className='subject-card-title'>{props.name}</label>
                    <label className='subject-card-description'>{props.description}</label>
                    <label className='subject-card-subject-count'>课程数：{props.course_count}</label>
                </div>
            </Card>
        </div>
    )
}

export default Subject_Component