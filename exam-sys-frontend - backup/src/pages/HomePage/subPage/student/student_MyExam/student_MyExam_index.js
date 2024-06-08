import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userRequest } from '../../../../../utils/request';

import { UserOutlined, FileTextOutlined } from '@ant-design/icons';

import {
    CheckCircleOutlined,
    ClockCircleOutlined,
    CloseCircleOutlined,
    InfoCircleOutlined,
    MinusCircleOutlined,
    SyncOutlined,
  } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, Space } from 'antd';

import "./student_MyExam_index.css"

const Student_MyExam = () => {
    const navigate = useNavigate()

    const [finalShowMyExamInfo, setFinalShowMyExamInfo] = useState([])
    const [allMyExamInfo, setAllMyExamInfo] = useState([])
    const [allNotStartMyExamInfo, setAllNotStartMyExamInfo] = useState([])
    const [allOnGoingMyExamInfo, setAllOnGoingMyExamInfo] = useState([])
    const [allEndedMyExamInfo, setAllEndedMyExamInfo] = useState([])
    const [paginationSize, setPaginationSize] = useState(10)

    const [showMyExamStatus, setShowMyExamStatus] = useState(0)

    const userid = useSelector(state => state.userid.value)

    // 获取学生的所有myexam方法
    const getAllMyExamInfo = () => {
        userRequest.post("/student/get-all-my-exam", {
            studentId: userid,
        })
        .then( function(response) {
            console.log(response)
            setAllMyExamInfo(response)
            setFinalShowMyExamInfo(response)
            var notStartMyExamInfo = []
            var onGoingMyExamInfo = []
            var endedMyExamInfo = []
            for(var i = 0;i < response.length;++i){
                if(response[i].status == 0){
                    notStartMyExamInfo.push(response[i])
                }
                else if(response[i].status == 1 || response[i].status == 2){
                    onGoingMyExamInfo.push(response[i])
                }
                else if(response[i].status == 3 || response[i].status == 4){
                    endedMyExamInfo.push(response[i])
                }
            }
            setAllNotStartMyExamInfo(notStartMyExamInfo)
            setAllOnGoingMyExamInfo(onGoingMyExamInfo)
            setAllEndedMyExamInfo(endedMyExamInfo)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllMyExamInfo()
	}, []);


    // 学生切换显示不同状态考试
    const myExamTabChange = (tabKey) => {
        console.log(tabKey)
        setShowMyExamStatus(tabKey)
        if(tabKey == 0){
            setFinalShowMyExamInfo(allMyExamInfo)
        }
        else if(tabKey == 1){
            setFinalShowMyExamInfo(allNotStartMyExamInfo)
        }
        else if(tabKey == 2){
            setFinalShowMyExamInfo(allOnGoingMyExamInfo)
        }
        else if(tabKey == 3){
            setFinalShowMyExamInfo(allEndedMyExamInfo)
        }
    }

    // 查看考试详情
    const seeMyExamDetail = (item) => {
        navigate('/home/student-choose-class/exam-detail', 
            {
                state: {
                    departmentId: item.departmentId, 
                    departmentName: item.departmentName, 
                    subjectId: item.subjectId, 
                    subjectName: item.subjectName,
                    courseId: item.courseId,
                    courseName: item.courseName,
                    
                    examId: item.id,
                    examName: item.name,
                    examStatus: item.status
                }
            })
    }
    const seeCourseDetail = (item) => {
        navigate('/home/student-choose-class/exam', 
        {
            state: {
                departmentId: item.departmentId, 
                departmentName: item.departmentName, 
                subjectId: item.subjectId, 
                subjectName: item.subjectName,
                courseId: item.courseId,
                courseName: item.courseName,
            }
        })
    }
    const seeSubjectDetail = (item) => {
        navigate('/home/student-choose-class/course', 
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
        navigate('/home/student-choose-class/subject', 
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
            <div className='student-myexam-root-div'>
                <div className='student-myexam-label-div'>
                    <label className='student-myexam-label'>我的考试</label>
                    <Segmented className='student-myexam-segmented'
                        options={[
                            { label: '全部', value: '0', icon: <InfoCircleOutlined /> },
                            { label: '未开始', value: '1', icon: <ClockCircleOutlined /> },
                            { label: '正在进行', value: '2', icon: <SyncOutlined /> },
                            { label: '已结束', value: '3', icon: <MinusCircleOutlined /> },
                        ]}
                        onChange={myExamTabChange}
                    />
                </div>
                <div className='student-myexam-list-div'>
                    <Card>
                        <List
                            className='student-myexam-list'
                            pagination={{
                                pageSize: paginationSize,
                            }}
                            dataSource={
                                finalShowMyExamInfo
                            }
                            renderItem={(item, index) => (
                                <List.Item
                                    key={item.id}
                                    actions={[
                                        <div>
                                            <a onClick={(e) => seeMyExamDetail(item)}>查看考试</a>
                                        </div>
                                    ]}
                                >
                                    <Skeleton avatar title={false} loading={item.loading} active>
                                        <List.Item.Meta
                                            title={<div>&nbsp;&nbsp;<a className='student-myexam-course-name' onClick={(e) => seeMyExamDetail(item)}>{item.name}</a></div>}
                                            description={
                                                <div>
                                                    <label>&nbsp;&nbsp;发布：{item.teacherRealname}</label>
                                                    <br/>
                                                    <label>&nbsp;&nbsp;描述：{item.description}</label>
                                                    <br/>
                                                    <label>
                                                        &nbsp;&nbsp;{item.start_time}
                                                        &nbsp;&nbsp;到&nbsp;&nbsp;
                                                        {item.end_time}
                                                    </label>
                                                    <br/><br/>
                                                    &nbsp;&nbsp;
                                                    <a className='student-myexam-course-path-url'  onClick={(e) => seeDepartmentDetail(item)}>{item.departmentName}</a>
                                                    &nbsp;&nbsp;-&nbsp;&nbsp;
                                                    <a className='student-myexam-course-path-url'  onClick={(e) => seeSubjectDetail(item)}>{item.subjectName}</a>
                                                    &nbsp;&nbsp;-&nbsp;&nbsp;
                                                    <a className='student-myexam-course-path-url'  onClick={(e) => seeCourseDetail(item)}>{item.courseName}</a>
                                                </div>}
                                        />
                                        <div className='student-myexam-exam-status-tag-div'>
                                            {item.status == 0 && 
                                                <div><Tag icon={<ClockCircleOutlined />} color='default'>未开始</Tag></div>
                                            }
                                            {(item.status == 1 || item.status == 2) && 
                                                <div><Tag icon={<SyncOutlined spin />} color="processing">正在进行</Tag></div>
                                            }
                                            {(item.status == 3 || item.status == 4) && 
                                                <div><Tag icon={<MinusCircleOutlined />} color="error" >已结束</Tag></div>
                                            }
                                            {(item.status == 2 || item.status == 4) && 
                                                <div><Tag icon={<CheckCircleOutlined />} color="#87d068">已完成</Tag></div>
                                            }
                                            {(item.status == 1 || item.status == 3) && 
                                                <div><Tag icon={<CloseCircleOutlined />} color='default'>未完成</Tag></div>
                                            }
                                            
                                        </div>
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

export default Student_MyExam