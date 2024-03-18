import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userRequest } from '../../../../../utils/request';

import { UserOutlined, FileTextOutlined } from '@ant-design/icons';

import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, Space } from 'antd';

import "./teacher_MyClass_index.css"


const Teacher_MyClass = () => {
    const navigate = useNavigate()

    const [allMyClassInfo, setAllMyClassInfo] = useState([])
    const [paginationSize, setPaginationSize] = useState(10)


    const userid = useSelector(state => state.userid.value)

    // 获取老师的所有myclass方法
    const getAllMyClassInfo = () => {
        userRequest.post("/teacher/get-all-my-course", {
            teacherId: userid,
        })
        .then( function(response) {
            console.log(response)
            setAllMyClassInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllMyClassInfo()
	}, []);

    // 跳转查看课程详情
    const seeMyClassDetail = (item) => {
        console.log(item)
        navigate('/home/teacher-operate-class/exam', 
            {
                state: {
                    departmentId: item.departmentId, 
                    departmentName: item.departmentName, 
                    subjectId: item.subjectId, 
                    subjectName: item.subjectName,
                    courseId: item.id,
                    courseIcon: item.icon,
                    courseName: item.name,
                    courseDescription: item.description,
                    courseTeachby: item.teachby,
                    courseCreated_time: item.created_time
                }
            })
    }
    const seeSubjectDetail = (item) => {
        console.log(item)
        navigate('/home/teacher-operate-class/course', 
            {
                state: {
                    departmentId: item.departmentId, 
                    departmentName: item.departmentName, 
                    subjectId: item.subjectId, 
                    subjectName: item.subjectName
                }
            })
    }
    const seeDepartmentDetail = (item) => {
        console.log(item)
        navigate('/home/teacher-operate-class/subject', 
        {
            state: 
            {
                departmentId: item.departmentId, 
                departmentName: item.departmentName
            }
        })
    }

    return(
        <div className='root-div'>
            <div className='teacher-myclass-root-div'>
                <div className='teacher-myclass-label-div'>
                    <label className='teacher-myclass-label'>我的课程</label>
                </div>
                <div className='teacher-myclass-list-div'>
                    <Card>
                        <List
                            className='teacher-myclass-list'
                            itemLayout="vertical"
                            pagination={{
                                pageSize: paginationSize,
                            }}
                            dataSource={allMyClassInfo}
                            renderItem={(item, index) => (
                                <List.Item
                                    key={item.id}
                                    actions={[
                                        <div>
                                            &nbsp;&nbsp;
                                            <UserOutlined /> {item.studentCount}
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <FileTextOutlined /> {item.examCount}
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a onClick={(e) => seeMyClassDetail(item)}>查看课程</a>
                                        </div>
                                    ]}
                                    extra={
                                        <img
                                            className='teacher-myclass-list-item-icon'
                                            alt="logo"
                                            src={item.icon}
                                        />
                                    }
                                >
                                    <Skeleton avatar title={false} loading={item.loading} active>
                                        <List.Item.Meta
                                            title={<div>&nbsp;&nbsp;<a className='teacher-myclass-course-name' onClick={(e) => seeMyClassDetail(item)}>{item.name}</a></div>}
                                            description={<div>&nbsp;&nbsp;<a className='teacher-myclass-course-path-url'  onClick={(e) => seeDepartmentDetail(item)}>{item.departmentName}</a> - <a className='teacher-myclass-course-path-url'  onClick={(e) => seeSubjectDetail(item)}>{item.subjectName}</a></div>}
                                        />
                                        <label><div>&nbsp;&nbsp;{item.description}</div></label>
                                    </Skeleton>
                                </List.Item>
                            )}
                        />
                    </Card>
                    
                </div>
            </div>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Teacher_MyClass