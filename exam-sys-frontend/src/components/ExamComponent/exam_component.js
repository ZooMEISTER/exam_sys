import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { Outlet, useNavigate } from "react-router-dom"

import { Card, Tag, message } from 'antd';
import {
    CheckCircleOutlined,
    ClockCircleOutlined,
    CloseCircleOutlined,
    ExclamationCircleOutlined,
    MinusCircleOutlined,
    SyncOutlined,
  } from '@ant-design/icons';

import "./exam_component.css"

const Exam_Component = (props) => {
    const navigate = useNavigate()
    const permissionLevel = useSelector(state => state.permissionLevel.value)

    const ClickExam = (() => {
        console.log(props.id)
        if(permissionLevel === 2){
            navigate('/home/teacher-operate-class/exam-detail', 
            {
                state: {
                    departmentId: props.departmentId, 
                    departmentName: props.departmentName, 
                    subjectId: props.subjectId, 
                    subjectName: props.subjectName,
                    courseId: props.courseId,
                    courseName: props.courseName,

                    examId: props.id,
                    examName: props.name
                }
            })
        }
        else if(permissionLevel === 1){
            // 先检查学生是否报名了此课程
            if(props.isStudentSigned > 0){
                navigate('/home/student-choose-class/exam-detail', 
                {
                    state: {
                        departmentId: props.departmentId, 
                        departmentName: props.departmentName, 
                        subjectId: props.subjectId, 
                        subjectName: props.subjectName,
                        courseId: props.courseId,
                        courseName: props.courseName,
                        
                        examId: props.id,
                        examName: props.name,
                        examStatus: props.status
                    }
                })
            }
            else{
                message.info("你需要先报名此课程才能参与该课程的考试")
            }
        }
    })

    if(permissionLevel == 2){
        return(
            <div className='comp-root-div'> 
                <div
                    onClick={ClickExam}
                    className='exam-card'
                >
                    <div className='exam-card-content'>
                        <text className='exam-name'>{props.name}</text>
                        <text className='exam-start'>开始：{props.start_time}</text>
                        <text className='exam-end'>结束：{props.end_time}</text>
                    </div>
                </div>
            </div>
        )
    }
    else if(permissionLevel == 1){
        return(
            <div className='comp-root-div'> 
                <div
                    onClick={ClickExam}
                    className='exam-card'
                >
                    <div className='exam-card-content'>
                        <text className='exam-name'>{props.name}</text>

                        {props.showExamStatus == 0 && props.status == 0 ? 
                            <Tag icon={<ClockCircleOutlined />} color='default' className='exam-status-tag-not-start'>未开始</Tag> : <div></div>
                        }
                        {props.showExamStatus == 0 && (props.status == 1 || props.status == 2) ? 
                            <Tag icon={<SyncOutlined spin />} color="processing" className='exam-status-tag'>正在进行</Tag> : <div></div>
                        }
                        {props.showExamStatus == 0 && (props.status == 3 || props.status == 4) ? 
                            <Tag icon={<MinusCircleOutlined />} color="error" className='exam-status-tag'>已结束</Tag> : <div></div>
                        }
                        {props.status == 2 || props.status == 4 ? 
                            <Tag icon={<CheckCircleOutlined />} color="#87d068" className='exam-status-tag latter-exam-status-tag'>已完成</Tag> : <div></div>
                        }
                        {props.status == 1 || props.status == 3 ? 
                            <Tag icon={<CloseCircleOutlined />} color='default' className='exam-status-tag latter-exam-status-tag'>未完成</Tag> : <div></div>
                        }
        
                        <text className='exam-start'>开始：{props.start_time}</text>
                        <text className='exam-end'>结束：{props.end_time}</text>
                    </div>
                </div>
            </div>
        )
    }
    
}

export default Exam_Component