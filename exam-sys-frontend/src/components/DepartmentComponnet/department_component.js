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
        <div className='root-div'>
            <Card 
                onClick={ClickDepartment}
                className='department-card' 
                hoverable
                cover={
                    <img className='cover-img' alt='No Img' src={props.icon}/>
                }
            >
                {props.id} <br/>
                {props.name} <br/>
                {props.description} <br/>
                {props.subject_count} <br/>
            </Card>
        </div>
    )
}

export default Department_Component