import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../utils/request';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message, Affix, Modal, Input, InputNumber } from 'antd';

import "./teacherCorrectRespondentPage_index.css"

const TeacherCorrectRespondentPage_index = () => {
    const state = useLocation()
    const navigate = useNavigate()

    const [examInfo, setExamInfo] = useState([])
    const [respondentInfo, setRespondentInfo] = useState([])

    const [modalTitle, setModalTitle] = useState("打分")
    const [finalScore, setFinalScore] = useState(0)

    // 获取考试信息
    const getExamInfo = () => {
        userRequest.post("/teacher/get-exam-detail", {
            examId: state.state.examId
        })
        .then( function(response) {
            console.log(response)
            setExamInfo(response)
            setModalTitle("打分 " + "[0, " + response.paperScore + "]")
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 获取答卷信息
    const getRespondentInfo = () => {
        userRequest.post("/teacher/get-respondent-info", {
            respondentId: state.state.respondentId
        })
        .then( function(response) {
            console.log(response)
            setRespondentInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getExamInfo()
        getRespondentInfo()
	}, []);

    // 跳转回考试详情页
    const backToExamDetail = () => {
        navigate('/home/teacher-operate-class/exam-detail', 
            {
                state: {
                    departmentId: state.state.departmentId, 
                    departmentName: state.state.departmentName, 
                    subjectId: state.state.subjectId, 
                    subjectName: state.state.subjectName,
                    courseId: state.state.courseId,
                    courseName: state.state.courseName,

                    examId: state.state.examId,
                    examName: state.state.examName
                }
            })
    }

    // 老师输入考试分数
    const finalScoreInputChange = (event) => {
        console.log(event)
        setFinalScore(event)
    }

    // 老师打分
    const correctTheRespondent = () => {
        setIsModalOpen(true)
    }
    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleOk = () => {
        userRequest.post("/teacher/correct-respondent", {
            respondentId: state.state.respondentId,
            finalScore: finalScore
        })
        .then( function(response) {
            console.log(response)
            if(response.resultCode == 12020){
                message.success(response.msg)
                setIsModalOpen(false);
                setFinalScore(0)
                backToExamDetail()
            }
            else if(response.resultCode == 12021){
                message.error(response.msg)
            }
        })
        .catch( function (error) {
            console.log(error)
        })
        
    };
    const handleCancel = () => {
        setIsModalOpen(false);
        setFinalScore(0)
    };

    return(
        <div>
            <div className='teacher-correct-respondent-root-div'>
                <div className='teacher-correct-respondent-title-div'>
                    <Button onClick={backToExamDetail}>返回考试详情页</Button>
                </div>
                
                {/* "http://view.officeapps.live.com/op/view.aspx?src=" +  */}
                {/* 'http://localhost:3001/respondent/' + respondentInfo.respondent_path */}
                <iframe 
                    className='teacher-correct-respondent-iframe'
                    height='700'
                    allowFullScreen='true'
                    src={'http://localhost:3001/respondent/' + respondentInfo.respondent_path}
                >
                </iframe>
                
            </div>
            
            <div className='teacher-correct-respondent-info-card-div'>
                <div className='teacher-correct-respondent-info-card'>
                    {/* <label className='teacher-correct-respondent-info-title'>考试信息</label> */}
                    <label className='teacher-correct-respondent-info-sub-title'>学院</label>
                    <label className='teacher-correct-respondent-info-content'>{state.state.departmentName}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>专业</label>
                    <label className='teacher-correct-respondent-info-content'>{state.state.subjectName}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>课程</label>
                    <label className='teacher-correct-respondent-info-content'>{state.state.courseName}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>考试名称</label>
                    <label className='teacher-correct-respondent-info-content'>{examInfo.name}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>考试描述</label>
                    <label className='teacher-correct-respondent-info-content'>{examInfo.description}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>开始时间</label>
                    <label className='teacher-correct-respondent-info-content'>{examInfo.start_time}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>结束时间</label>
                    <label className='teacher-correct-respondent-info-content'>{examInfo.end_time}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>试卷名称</label>
                    <label className='teacher-correct-respondent-info-content'>{examInfo.paperName}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>试卷描述</label>
                    <label className='teacher-correct-respondent-info-content'>{examInfo.paperDescription}</label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>总分</label>
                    <label className='teacher-correct-respondent-info-content'>{examInfo.paperScore}</label>

                    <label className='teacher-correct-respondent-info-sub-title'>答卷最后修改时间</label>
                    <label className='teacher-correct-respondent-info-content'>
                        {respondentInfo.lastModifiedTime}
                        {new Date(respondentInfo.lastModifiedTime) > new Date(examInfo.end_time) && 
                            <Tag color="red">超时</Tag>
                        }
                        {new Date(respondentInfo.lastModifiedTime) <= new Date(examInfo.end_time) && new Date(respondentInfo.lastModifiedTime) >= new Date(examInfo.start_time) &&
                            <Tag color="green">未超时</Tag>
                        }
                    </label>
                    
                    <label className='teacher-correct-respondent-info-sub-title'>答卷提交时间</label>
                    <label className='teacher-correct-respondent-info-content'>{respondentInfo.created_time}</label>

                    <label className='teacher-correct-respondent-info-sub-title'>ECDSA验证</label>
                    <label className='teacher-correct-respondent-info-content'>
                        {respondentInfo.is_sign_verify_good == 0 &&
                            <Tag color="red">不通过</Tag>
                        }
                        {respondentInfo.is_sign_verify_good == 1 &&
                            <Tag color="green">通过</Tag>
                        }
                    </label>
                    
                    {respondentInfo.final_score == -1 &&
                        <Button onClick={correctTheRespondent} className='teacher-correct-respondent-button' type='primary'>打分</Button>
                    }
                    {respondentInfo.final_score != -1 &&
                        <div className='column-div'>
                            <label className='teacher-correct-respondent-info-sub-title'>最终得分</label>
                            <label className='teacher-correct-respondent-info-content'>{respondentInfo.final_score}</label>
                        </div>
                    }
                    
                </div>
            </div>

            <Modal title={modalTitle}
                open={isModalOpen} 
                destroyOnClose={true}
                onCancel={handleCancel}
                footer={[
                    <Button onClick={handleCancel}>
                        取消
                    </Button>,
                    <Button type="primary" onClick={handleOk}>
                        确定
                    </Button>
                ]}>
                    <InputNumber min={0} max={examInfo.paperScore} defaultValue={0} onChange={finalScoreInputChange}/>
            </Modal>
        </div>
    )
}

export default TeacherCorrectRespondentPage_index