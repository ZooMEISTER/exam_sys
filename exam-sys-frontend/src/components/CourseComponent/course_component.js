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
    })

    return(
        <div className='root-div'>
            <Card
                onClick={ClickCourse}
                className='course-card'
                hoverable
                cover={
                    <img className='cover-img' alt='No Img' src={props.icon}/>
                }
            >
                {props.id} <br/>
                {props.name} <br/>
                {props.description} <br/>
                {props.teachby} <br/>
                {props.created_time} <br/>
            </Card>
        </div>
    )
}

export default Course_Component