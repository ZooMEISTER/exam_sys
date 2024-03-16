import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userRequest } from '../../../../../../utils/request';

import { Card, Button } from 'antd';

import "./examDetailPage_index.css"

const Teacher_ExamDetailPage_index = () => {
    const state = useLocation()
    const navigate = useNavigate()

    const [examInfo, setExamInfo] = useState({})

    // 获取该考试的具体信息
    const getExamInfo = () => {
        userRequest.post("/teacher/get-exam-detail", {
            examId: state.state.examId
        })
        .then( function(response) {
            console.log(response)
            setExamInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getExamInfo()
	}, []);

    // 下载试卷
    const downloadPaper = () => {
        console.log("下载试卷")
        window.open(examInfo.paperPath, "_blank", "resizable,scrollbars,status");
    }

    // 上传答卷
    const uploadAnswerPaper = () => {
        console.log("上传答卷")

    }

    return(
        <div className='exam-detail-root'>
            <div className='exam-detail-base'>
                <div className='exam-detail-div'>
                    <label className='exam-detail-name'>{examInfo.name}</label>
                </div>
                <div className='exam-detail-div'>
                    <label className='exam-detail-description'>{examInfo.description}</label>
                </div>
                <div className='exam-detail-div'>
                    <label className='exam-detail-start-end'>由 {examInfo.teachby} 于 {examInfo.created_time} 发布</label>
                    <label className='exam-detail-start-end'>开始时间：{examInfo.start_time}</label>
                    <label className='exam-detail-start-end'>结束时间：{examInfo.end_time}</label>
                </div>

                <Card className='paper-card'>
                    <label className='paper-detail-text'>试卷名称：{examInfo.paperName}</label> <br/>
                    <label className='paper-detail-text'>试卷描述：{examInfo.paperDescription}</label> <br/>
                    <label className='paper-detail-text'>试卷分数：{examInfo.paperScore}</label> <br/>
                    <div className='paper-detail-button-div'>
                        <Button type='primary' className='paper-detail-button' onClick={downloadPaper}>下载试卷</Button>
                        <Button type='default' className='paper-detail-button' onClick={uploadAnswerPaper}>上传答卷</Button>
                    </div>
                </Card>
                
            </div>
        </div>
    )
}

export default Teacher_ExamDetailPage_index