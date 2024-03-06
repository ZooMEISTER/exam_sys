import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import Course_Component from '../../../../../../components/CourseComponent/course_component';

import { touristRequest, userRequest } from '../../../../../../utils/request';

import { Breadcrumb } from 'antd';

import "./allCoursePage_index.css"

const Student_AllCoursePage_index = () => {
    const state = useLocation()
    const navigate = useNavigate()

    const [allCourseInfo, setAllCourseInfo] = useState([])


    // 获取数据库中某个学院所有专业的数据
    const getAllCourse = () => {
        userRequest.post("/student/get-all-course", {
            subjectId: state.state.subjectId
        })
        .then( function(response) {
            console.log(response)
            setAllCourseInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllCourse()
	}, []);

    const jumpBackToSubjects = () =>{
        navigate('/home/student-choose-class/subject', {state: {departmentId: state.state.departmentId, departmentName: state.state.departmentName}})
    }

    return(
        <div>
            {/* 先是一个面包屑 */}
            <Breadcrumb 
                className='navigate-breadcrumb'
                items={[
                    {title: <a href="/home/student-choose-class">所有学院</a>},
                    {title: <a onClick={jumpBackToSubjects}>{state.state.departmentName}</a>},
                    {title: state.state.subjectName}
                ]}
            />
            {/* 在下面显示所有的学院 */}
            <div className='show-all-course-div'>
                {allCourseInfo.map(item => (
                    <Course_Component
                        key={item.id}
                        id={item.id}
                        icon={item.icon}
                        name={item.name}
                        description={item.description}
                        course_count={item.course_count}
                        
                        departmentId={state.state.departmentId}
                        subjectId={state.state.subjectId}/>
                ))}
                
            </div>
        </div>
    )
}

export default Student_AllCoursePage_index