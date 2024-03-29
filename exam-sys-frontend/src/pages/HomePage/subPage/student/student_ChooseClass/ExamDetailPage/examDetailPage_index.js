import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userRequest, userFileUploadRequest } from '../../../../../../utils/request';

import { UploadOutlined } from '@ant-design/icons';
import { Card, Button, Modal, Form, Input, Upload, message } from 'antd';

import "./examDetailPage_index.css"

const Student_ExamDetailPage_index = () => {
    const state = useLocation()
    const navigate = useNavigate()

    const userid = useSelector(state => state.userid.value)

    const [examInfo, setExamInfo] = useState({})
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [sha256Value, setSha256Value] = useState("")
    const [respondentFileName, setRespondentFileName] = useState("")
    const [justAddRespondent, setJustAddRespondent] = useState(false)

    // Upload的自定义request
    const customRequest = async (options) => {
        // 调用api接口进行请求
        await userFileUploadRequest.post("/student/upload-respondent", {
            examRespondent: options.file
        })
        .then( function(response) {
            console.log(response)
            setRespondentFileName(response)
            options.onSuccess(response, options.file);
        })
        .catch( function (error) {
            console.log(error)
        })
    }
    // Upload的参数
    const uploadProps = {
        name: 'examRespondent', //取值必须和接口参数中的文件参数名相同
        listType: 'picture',
        className: "respondent-uploader",
        maxCount: 1,
        customRequest: customRequest
    };

    // 获取该考试的具体信息
    const getExamInfo = () => {
        userRequest.post("/student/get-exam-detail", {
            examId: state.state.examId,
            studentId: userid
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

    // 上传答卷
    const uploadAnswerPaper = () => {
        console.log("上传答卷")
        setIsModalOpen(true)
    }

    const handleOk = () => {
        if(sha256Value == ""){
            message.info("SHA-256 值不能为空")
        }
        else if(respondentFileName == ""){
            message.info("必须上传答卷")
        }
        else{
            userRequest.post("/student/add-respondent-record", {
                examId: examInfo.id,
                studentId: userid,
                respondentFileName: respondentFileName,
                sha256Value: sha256Value
            })
            .then( function(response) {
                if(response.resultCode == 12004){
                    message.success(response.msg)
                    setJustAddRespondent(true)
                    navigate('/home/student-choose-class/exam-detail', 
                    {
                        state: {
                            departmentId: state.state.departmentId, 
                            departmentName: state.state.departmentName, 
                            subjectId: state.state.subjectId, 
                            subjectName: state.state.subjectName,
                            courseId: state.state.courseId,
                            courseName: state.state.courseName,
                            
                            examId: examInfo.id,
                            examName: examInfo.name,
                            examStatus: examInfo.status
                        }
                    })
                }
                else if(response.resultCode == 12005){
                    message.error(response.msg)
                }
            })
            .catch( function (error) {
                console.log(error)
                message.error("unknown error")
            })
            setIsModalOpen(false);
        }
    };
    const handleCancel = () => {
        setSha256Value("")
        setRespondentFileName("")
        setIsModalOpen(false);
    };

    // 返回课程详情页
    const backToAllExam = () => {
        navigate('/home/student-choose-class/exam', 
        {
            state: {
                departmentId: state.state.departmentId, 
                departmentName: state.state.departmentName, 
                subjectId: state.state.subjectId, 
                subjectName: state.state.subjectName,
                courseId: state.state.courseId,
                courseName: state.state.courseName,
            }
        })
    }

    // 学生输入sha256值
    const sha256ValueChange = (event) => {
        setSha256Value(event.target.value)
    }

    return(
        <div className='student-exam-detail-root'>
            <div className='student-back-to-all-exam-div'>
                <Button type='link' onClick={backToAllExam} className='student-back-to-all-exam-button'>返回</Button>
            </div>
            <Card title={examInfo.name} className='student-exam-detail-card'>
                <Form name="basic"
                    labelCol={{span: 4}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}>
                    <Form.Item
                        label="描述"
                        name="exam-description"
                        className='exam-info-form-row'
                    >
                        <label className='student-exam-detail-card-description'>{examInfo.description}</label>
                    </Form.Item>
                    <Form.Item
                        label="信息"
                        name="exam-info"
                        className='exam-info-form-row'
                    >
                        <label className='student-exam-detail-card-start-end'>由 {examInfo.teachby} 于 {examInfo.created_time} 发布</label>
                    </Form.Item>
                    <Form.Item
                        label="开始时间"
                        name="exam-start-time"
                        className='exam-info-form-row'
                    >
                        <label className='student-exam-detail-card-start-end'>{examInfo.start_time}</label>
                    </Form.Item>
                    <Form.Item
                        label="结束时间"
                        name="exam-end-time"
                        className='exam-info-form-row'
                    >
                        <label className='student-exam-detail-card-start-end'>{examInfo.end_time}</label>
                    </Form.Item>
                </Form>
                
                <Card type="inner" title={examInfo.paperName} extra={<a href={examInfo.paperPath} target="_blank">下载试卷</a>} className='student-inner-paper-card'>
                    <Form name="basic"
                        labelCol={{span: 4}}
                        wrapperCol={{span: 16}}
                        style={{maxWidth: 600}}>
                        <Form.Item
                            label="描述"
                            name="paper-description"
                            className='exam-info-form-row'
                        >
                            <label className='student-paper-detail-text'>{examInfo.paperDescription}</label> <br/>
                        </Form.Item>
                        <Form.Item
                            label="总分"
                            name="paper-score"
                            className='exam-info-form-row'
                        >
                            <label className='student-paper-detail-text'>{examInfo.paperScore}</label> <br/>
                        </Form.Item>
                    </Form>
                    {examInfo.status == 0 && 
                        <div className='student-paper-status'>
                            <label>考试未开始</label>
                        </div>
                    }
                    {(examInfo.status == 1 || examInfo.status == 3) && !justAddRespondent &&
                        <div className='student-paper-detail-button-div'>
                            <Button type='default' className='student-upload-respondent' onClick={uploadAnswerPaper}>上传答卷</Button>
                        </div>
                    }
                    {(examInfo.status == 2 || examInfo.status== 4 || justAddRespondent) && examInfo.finalScore < 0 && 
                        <div className='student-paper-status'>
                            已交卷, 等待批阅
                        </div>
                    }
                    {(examInfo.status == 2 || examInfo.status == 4) && examInfo.finalScore >= 0 && 
                        <div className='student-paper-status'>
                            你的得分为：{examInfo.finalScore}
                        </div>
                    }
                    
                </Card>
            </Card>
            
            <Modal title="上传答卷" 
                open={isModalOpen} 
                onCancel={handleCancel}
                destroyOnClose={true}
                footer={[
                    <Button onClick={handleCancel}>
                      取消
                    </Button>,
                    <Button type="primary" onClick={handleOk}>
                      上传
                    </Button>
                  ]}
                >
                <Form
                    name="basic"
                    labelCol={{span: 6}}
                    wrapperCol={{span: 20}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    autoComplete="off"
                >
                    <Form.Item
                        label="SHA-256值"
                        name="sha256_value"
                        rules={[
                            {
                            required: true,
                            message: '请输入SHA-256值',
                            },
                        ]}
                        >
                        <Input value={sha256Value} onChange={sha256ValueChange}/>
                    </Form.Item>

                    <Form.Item
                        label="考试答卷"
                        name="examRespondentFile"

                        rules={[
                            {
                            required: true,
                            message: '请上传考试答卷',
                            },
                        ]}
                        >
                        <Upload
                            {...uploadProps}
                        >
                            <Button icon={<UploadOutlined />}>Upload</Button>
                        </Upload>
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    )
}

export default Student_ExamDetailPage_index