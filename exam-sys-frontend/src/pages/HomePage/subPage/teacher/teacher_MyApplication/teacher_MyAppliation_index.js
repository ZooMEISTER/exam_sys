import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userRequest, userFileUploadRequest } from '../../../../../utils/request';

import { MehOutlined, ClockCircleOutlined, StopOutlined, CheckCircleOutlined } from '@ant-design/icons';
import { Breadcrumb, Button, message, Modal, Form, Input, DatePicker, TimePicker, Upload, Tabs, Segmented, List, Card, Skeleton, Tag } from 'antd';

import "./teacher_MyApplication_index.css"

const Teacher_MyApplication = () => {
    const navigate = useNavigate()

    const userid = useSelector(state => state.userid.value)

    const [paginationSize, setPaginationSize] = useState(10)

    const [showApplicationType, setShowApplicationType] = useState(0)
    const [showApplicationStatus, setShowApplicationStatus] = useState(0)

    const [finalListShowApplication, setFinalListShowApplication] = useState([])

    const [allMyAddCourseApplication, setAllMyAddCourseApplication] = useState([])
    const [allPendingMyAddCourseApplication, setAllPendingMyAddCourseApplication] = useState([])
    const [allApprovedMyAddCourseApplication, setAllApprovedMyAddCourseApplication] = useState([])
    const [allDeclinedMyAddCourseApplication, setAllDeclinedMyAddCourseApplication] = useState([])

    // 老师更改Tab的显示
    const applicationTabChange = (tabKey) => {
        console.log(tabKey)
        setShowApplicationType(tabKey)
    }
    //老师更改Segment的显示
    const applicationSegmentChange = (tabKey) => {
        console.log(tabKey)
        if(showApplicationType == 0){
            if(tabKey == 0){
                setFinalListShowApplication(allMyAddCourseApplication)
            }
            else if(tabKey == 1){
                setFinalListShowApplication(allPendingMyAddCourseApplication)
            }
            else if(tabKey == 2){
                setFinalListShowApplication(allDeclinedMyAddCourseApplication)
            }
            else if(tabKey == 3){
                setFinalListShowApplication(allApprovedMyAddCourseApplication)
            }
        }
        setShowApplicationStatus(tabKey)
    }


    // 获取老师的所有添加课程申请方法
    const getAllMyAddCourseApplication = () => {
        userRequest.post("/teacher/get-all-my-addcourse-application", {
            teacherId: userid,
        })
        .then( function(response) {
            console.log(response)

            setFinalListShowApplication(response)
            setAllMyAddCourseApplication(response)

            var pendingMyAddCourseAppliation = []
            var approvedMyAddCourseAppliation = []
            var declinedMyAddCourseAppliation = []
            for(var i = 0;i < response.length;++i){
                if(response[i].approve_status == 0){
                    pendingMyAddCourseAppliation.push(response[i])
                }
                else if(response[i].approve_status == 1){
                    approvedMyAddCourseAppliation.push(response[i])
                }
                else if(response[i].approve_status == 2){
                    declinedMyAddCourseAppliation.push(response[i])
                }
            }
            setAllPendingMyAddCourseApplication(pendingMyAddCourseAppliation)
            setAllApprovedMyAddCourseApplication(approvedMyAddCourseAppliation)
            setAllDeclinedMyAddCourseApplication(declinedMyAddCourseAppliation)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllMyAddCourseApplication()
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
        <div className="root-div">
            <div className="teacher-myapplication-root-div">
                <label className='teacher-myapplication-label'>我的申请</label>
                <Tabs
                    className='teacher-myapplication-tabs'
                    defaultActiveKey="0"
                    onChange={applicationTabChange}
                    items={[
                        {
                            label: '添加课程',
                            key: '0',
                        },
                        {
                            label: '测试',
                            key: '1',
                        },
                    ]}    
                ></Tabs>
                <Segmented className='teacher-myapplication-segmented'
                    options={[
                        { label: '全部', value: '0', icon: <MehOutlined /> },
                        { label: '等待', value: '1', icon: <ClockCircleOutlined /> },
                        { label: '已否决', value: '2', icon: <StopOutlined /> },
                        { label: '已通过', value: '3', icon: <CheckCircleOutlined /> },
                    ]}
                    onChange={applicationSegmentChange}
                />

                {showApplicationType == 0 && 
                    <Card className='teacher-myapplication-list-card'>
                        <List
                            className='teacher-myapplication-list'
                            itemLayout="vertical"
                            pagination={{
                                pageSize: paginationSize,
                            }}
                            dataSource={finalListShowApplication}
                            renderItem={(item, index) => (
                                <List.Item
                                    key={item.id}
                                    actions={[
                                        <div>
                                            &nbsp;&nbsp;
                                            {item.approve_status == 0 && <Tag>等待批准</Tag>}
                                            {item.approve_status == 1 && <Tag color='green'>已批准</Tag>}
                                            {item.approve_status == 2 && <Tag color='red'>已拒绝</Tag>}
                                        </div>
                                    ]}
                                    extra={
                                        <img
                                            className='teacher-myapplication-list-item-icon'
                                            alt="logo"
                                            src={item.icon}
                                        />
                                    }
                                >
                                    <Skeleton avatar title={false} loading={item.loading} active>
                                        <List.Item.Meta
                                            title={<div>&nbsp;&nbsp;{item.name}</div>}
                                            description={<div>&nbsp;&nbsp;<a className='teacher-myclass-course-path-url'  onClick={(e) => seeDepartmentDetail(item)}>{item.departmentName}</a> - <a className='teacher-myclass-course-path-url'  onClick={(e) => seeSubjectDetail(item)}>{item.subjectName}</a></div>}
                                        />
                                        <label><div>&nbsp;&nbsp;{item.description}</div></label>
                                    </Skeleton>
                                </List.Item>
                            )}
                        />
                    </Card>
                    
                }


            </div>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Teacher_MyApplication