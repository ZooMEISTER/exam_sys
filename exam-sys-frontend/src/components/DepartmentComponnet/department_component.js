import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { Outlet, useNavigate } from "react-router-dom"

import { Card } from 'antd';

import "./department_component.css"

const Department_Component = (props) => {
    const navigate = useNavigate()
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const ClickDepartment = (() => {
        console.log(props.id)
        if(permissionLevel === 2){
            navigate('/home/teacher-operate-class/subject', {state: {departmentId: props.id, departmentName: props.name}})
        }
        else if(permissionLevel === 1){
            navigate('/home/student-choose-class/subject', {state: {departmentId: props.id, departmentName: props.name}})
        }
    })

    return(
        <div className='comp-root-div'>
            <Card 
                onClick={ClickDepartment}
                className='department-card' 
                hoverable
                bordered={false}
                cover={
                    <img className='department-cover-img' alt='No Img' src={props.icon}/>
                }
            >
                {/* {props.id} <br/>
                {props.name} <br/>
                {props.description} <br/>
                {props.subject_count} <br/> */}
                <div className='department-card-info-div'>
                    <label className='department-card-title'>{props.name}</label>
                    <label className='department-card-description'>{props.description}</label>
                    <label className='department-card-subject-count'>专业数：{props.subject_count}</label>
                </div>
                
            </Card>
        </div>
    )
}

export default Department_Component