import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'

import Subject_Component from '../../../../../../components/SubjectComponent/subject_component';

import { touristRequest, userRequest } from '../../../../../../utils/request';

import { Breadcrumb } from 'antd';

import "./allSubjectPage_index.css"

const Student_AllSubjectPage_index = () => {
    const state = useLocation()

    const [allSubjectInfo, setAllSubjectInfo] = useState([])


    // 获取数据库中某个学院所有专业的数据
    const getAllSubject = () => {
        userRequest.post("/student/get-all-subject", {
            departmentId: state.state.departmentId
        })
        .then( function(response) {
            console.log(response)
            setAllSubjectInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllSubject()
	}, []);

    return(
        <div>
            {/* 先是一个面包屑 */}
            <Breadcrumb 
                className='navigate-breadcrumb'
                items={[
                    {title: <a href="/home/student-choose-class">所有学院</a>},
                    {title: state.state.departmentName}
                ]}
            />
            {/* 在下面显示所有的学院 */}
            <div className='show-all-subject-div'>
                {allSubjectInfo.map(item => (
                    <Subject_Component
                        key={item.id}
                        id={item.id}
                        icon={item.icon}
                        name={item.name}
                        description={item.description}
                        course_count={item.course_count}
                        
                        departmentId={state.state.departmentId}
                        departmentName={state.state.departmentName}/>
                ))}
                
            </div>
        </div>
    )
}

export default Student_AllSubjectPage_index