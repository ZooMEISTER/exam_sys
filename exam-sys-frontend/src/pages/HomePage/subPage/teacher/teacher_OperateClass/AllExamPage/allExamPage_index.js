import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import Exam_Component from '../../../../../../components/ExamComponent/exam_component';
import Student_Component from '../../../../../../components/StudentComponent/student_component_index';
import NumericInput from '../../../../../../components/NumericInput/numeric_input_index';

import { touristRequest, userRequest, userFileUploadRequest } from '../../../../../../utils/request';

import { UploadOutlined } from '@ant-design/icons';
import { Breadcrumb, Button, message, Modal, Form, Input, DatePicker, TimePicker, Upload, Tabs } from 'antd';

import "./allExamPage_index.css"

const Teacher_AllExamPage_index = () =>{
    const state = useLocation()
    const navigate = useNavigate()
    const { TextArea } = Input;

    const [courseInfo, setCourseInfo] = useState({})
    const [showExamStatus, setShowExamStatus] = useState(0)

    const userid = useSelector(state => state.userid.value)
    const [allExamInfo, setAllExamInfo] = useState([])
    const [allSignedStudentInfo, setAllSignedStudentInfo] = useState([])

    const [paperFileName, setPaperFileName] = useState("")
    const [examName, setExamName] = useState("")
    const [examDescription, setExamDescription] = useState("")
    const [examStartDate, setExamStartDate] = useState("")
    const [examStartTime, setExamStartTime] = useState("")
    const [examEndDate, setExamEndDate] = useState("")
    const [examEndTime, setExamEndTime] = useState("")

    const [paperName, setPaperName] = useState("")
    const [paperDescription, setPaperDescription] = useState("")
    const [paperScore, setPaperScore] = useState(0)

    // Upload的自定义request
    const customRequest = async (options) => {
        // 调用api接口进行请求
        await userFileUploadRequest.post("/teacher/upload-paper", {
            examPaper: options.file
        })
        .then( function(response) {
            console.log(response)
            setPaperFileName(response)
            options.onSuccess(response, options.file);
        })
        .catch( function (error) {
            console.log(error)
        })
    }
    // Upload的参数
    const uploadProps = {
        name: 'examPaper', //取值必须和接口参数中的文件参数名相同
        listType: 'picture',
        className: "paper-uploader",
        maxCount: 1,
        //action: "http://localhost:3001/teacher/upload-paper",
        //headers: { Authorization: `Bearer ${localStorage.getItem('exam-sys-login-token')}` },
        customRequest: customRequest
    };

    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleOk = () => {
        var startDateTime = new Date(examStartDate + " " + examStartTime)
        var endDateTime = new Date(examEndDate + " " + examEndTime)
        console.log(startDateTime)
        console.log(endDateTime)
        console.log(startDateTime.getTime())
        console.log(endDateTime.getTime())
        // 添加考试方法
        if(paperFileName == ""){
            message.info("发布考试需要一张试卷")
        }
        else if(examName == ""){
            message.info("发布考试需要考试名称")
        }
        else if(examStartDate == ""){
            message.info("发布考试需要开始日期")
        }
        else if(examStartTime == ""){
            message.info("发布考试需要开始时间")
        }
        else if(examEndDate == ""){
            message.info("发布考试需要结束日期")
        }
        else if(examEndTime == ""){
            message.info("发布考试需要结束时间")
        }
        else if(paperName == ""){
            message.info("请输入试卷名称")
        }
        else if(paperScore == ""){
            message.info("试卷的分数不能为空")
        }
        else if(paperScore <= 0){
            message.info("试卷的分数不能为0分")
        }
        else if(isNaN(paperScore)){
            message.info("试卷的分数必须为数字")
        }
        else if(!(startDateTime.getTime() < endDateTime.getTime())){
            message.info("考试结束时间必须晚于考试开始时间")
        }
        else{
            console.log(examName)
            console.log(examDescription)
            console.log(examStartDate + " " + examStartTime)
            console.log(examEndDate + " " + examEndTime)
            console.log(paperFileName)
            console.log(paperName)
            console.log(paperDescription)
            console.log(paperScore)
            console.log(userid)
            console.log(state.state.courseId)
            userRequest.post("/teacher/add-exam", {
                examName: examName,
                examDescription: examDescription,
                examStartDateTime: examStartDate + " " + examStartTime,
                examEndDateTime: examEndDate + " " + examEndTime,
                paperFileName: paperFileName,
                paperName: paperName,
                paperDescription: paperDescription,
                paperScore: paperScore,
                teachby: userid,
                courseId: state.state.courseId
            })
            .then( function(response) {
                console.log(response)
                if(response.resultCode == 12010){
                    message.success("考试添加成功")
                    setIsModalOpen(false);
                    setPaperFileName("");
                    setExamStartDate("")
                    setExamStartTime("")
                    setExamEndDate("")
                    setExamEndTime("")
                    setPaperName("")
                    setPaperDescription("")
                    setPaperScore(0)

                    getAllExam()
                }
                else if(response.resultCode == 12011){
                    message.error("考试添加失败")
                }
            })
            .catch( function (error) {
                console.log(error)
                message.error("考试添加失败，请查看控制台")
            })
        }
    };
    const handleCancel = () => {
        setIsModalOpen(false);
        setPaperFileName("");
        setExamStartDate("")
        setExamStartTime("")
        setExamEndDate("")
        setExamEndTime("")
        setPaperName("")
        setPaperDescription("")
        setPaperScore(0)
    };

    // 获取某个课程下的所有考试数据
    const getAllExam = () => {
        userRequest.post("/teacher/get-all-exam", {
            courseId: state.state.courseId
        })
        .then( function(response) {
            console.log(response)
            setAllExamInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 获取该课程信息方法
    const getCourseInfo = () =>{
        userRequest.post("/teacher/get-course-info", {
            courseId: state.state.courseId,
        })
        .then( function(response) {
            console.log(response)
            setCourseInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 获取该课程的所有报名学生信息
    const getAllSignedStudentInfo = () => {
        userRequest.post("/teacher/get-all-signed-student-info", {
            courseId: state.state.courseId,
        })
        .then( function(response) {
            console.log(response)
            setAllSignedStudentInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllExam()
        getCourseInfo()
        getAllSignedStudentInfo()
	}, []);

    const jumpBackToSubjects = () =>{
        navigate('/home/teacher-operate-class/subject', 
            {state:    
                {
                    departmentId: state.state.departmentId, 
                    departmentName: state.state.departmentName,
                }
            }
        )
    }

    const jumpBackToCourses = () =>{
        navigate('/home/teacher-operate-class/course', 
            {state:    
                {
                    departmentId: state.state.departmentId, 
                    departmentName: state.state.departmentName,
                    subjectId: state.state.subjectId, 
                    subjectName: state.state.subjectName,
                }
            }
        )
    }

    // 创建新的考试
    const addNewExam = () =>{
        if(courseInfo.teachby != userid){
            message.info("你需要为该课程的授课老师才可创建考试")
        }
        else{
            console.log("创建考试")
            setIsModalOpen(true);
        }
    }

    // 新考试的名称改变
    const examNameInputChange = (event) => {
        setExamName(event.target.value)
    }
    // 新考试的描述改变
    const examDescriptionInputChange = (event) => {
        setExamDescription(event.target.value)
    }

    // 考试开始日期时间改变
    const onStartDateChange = (date, dateString) => {
        //console.log(date, dateString);
        setExamStartDate(dateString)
    };
    const onStartTimeChange = (time, timeString) => {
        //console.log(time, timeString);
        setExamStartTime(timeString)
    };

    // 考试结束日期时间改变
    const onEndDateChange = (date, dateString) => {
        //console.log(date, dateString);
        setExamEndDate(dateString)
    };
    const onEndTimeChange = (time, timeString) => {
        //console.log(time, timeString);
        setExamEndTime(timeString)
    };

    // 新考试的试卷的名称改变
    const paperNameInputChange = (event) => {
        setPaperName(event.target.value)
    }
    // 新考试的试卷的描述改变
    const paperDescriptionInputChange = (event) => {
        setPaperDescription(event.target.value)
    }
    // 新考试的试卷的分数改变
    const paperScoreInputChange = (event) => {
        setPaperScore(event.target.value)
    }

    // 老师更改Tab的显示
    const examTabChange = (tabKey) => {
        console.log(tabKey)
        setShowExamStatus(tabKey)
    }

    return(
        <div>
            {/* 先是一个面包屑 */}
            <Breadcrumb 
                className='navigate-breadcrumb'
                items={[
                    {title: <a href="/home/teacher-operate-class">所有学院</a>},
                    {title: <a onClick={jumpBackToSubjects}>{state.state.departmentName}</a>},
                    {title: <a onClick={jumpBackToCourses}>{state.state.subjectName}</a>},
                    {title: state.state.courseName}
                ]}
            />
            {/* 在下面显示该课程信息 */}
            <div className='teacher-course-info-div'>
                <img className='teacher-course-info-img' src={courseInfo.icon}/> 
                <div className='teacher-course-info-sub-div'>
                    <text className='teacher-course-info-name'>{courseInfo.name}</text>
                    <text className='teacher-course-info-description'>{courseInfo.description}</text>
                    <text className='teacher-course-info-teachby'>由 {courseInfo.teacherRealname} 教学</text>
                    <text className='teacher-course-info-createdtime'>{courseInfo.created_time}</text>
                </div>
                <div className='add-exam-button-div'>
                    <Button className='add-exam-button' type='default' onClick={addNewExam}>创建考试</Button>
                </div>
            </div>
            <Tabs
                className='teacher-exam-tabs'
                defaultActiveKey="0"
                onChange={examTabChange}
                items={[
                    {
                        label: '全部考试',
                        key: '0',
                    },
                    {
                        label: '报名学生',
                        key: '1',
                    },
                ]}    
            ></Tabs>
            {showExamStatus == 0 && 
                /* 在下面显示该课程的所有考试 */
                <div className='show-all-exam-div'>
                    {allExamInfo.map(item => (
                        <Exam_Component
                            className='exam-info-component'

                            key={item.id}
                            id={item.id}
                            name={item.name}
                            description={item.description}
                            start_time={item.start_time}
                            end_time={item.end_time}
                            teachby={item.teachby}
                            type={item.type}
                            published={item.publihed}
                            created_time={item.created_time}
                            status={item.status}
                            finishedStudentCount={item.finishedStudentCount}
                            totalStudentCount={item.totalStudentCount}

                            departmentId={state.state.departmentId}
                            departmentName={state.state.subjectName}
                            subjectId={state.state.subjectId}
                            subjectName={state.state.departmentName}
                            courseId={state.state.courseId}
                            courseName={state.state.courseName}
                        />
                ))}
                </div>
            }
            {showExamStatus == 1 && 
                /* 在下面显示该课程的所有同学 */
                <div className='show-all-course-student-div'>
                    {allSignedStudentInfo.map(item => (
                        <Student_Component
                            className='student-info-component'

                            key={item.id}
                            id={item.id}
                            avatar={item.avatar}
                            username={item.username}
                            realname={item.realname}
                            phone={item.phone}
                            email={item.email}
                        />
                    ))}
                </div>
            }
            
            {/* 添加新考试的弹窗 */}
            <Modal title="添加新考试" 
                open={isModalOpen} 
                destroyOnClose={true}
                onCancel={handleCancel}
                footer={[
                    <Button onClick={handleCancel}>
                        取消
                    </Button>,
                    <Button type="primary" onClick={handleOk}>
                        上传
                    </Button>
                ]}>
                <Form
                    name="添加考试"
                    labelCol={{span: 6,}}
                    wrapperCol={{span: 20}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    autoComplete="off"
                >
                    <Form.Item
                        label="考试名称"
                        name="examName"
                        rules={[
                            {
                            required: true,
                            message: '请输入新的考试名称',
                            },
                        ]}
                        >
                        <Input value={examName} onChange={examNameInputChange}/>
                    </Form.Item>
                    
                    <Form.Item
                        label="考试描述"
                        name="examDescription"
                        >
                        <TextArea rows={4} value={examDescription} onChange={examDescriptionInputChange}/>
                    </Form.Item>

                    <Form.Item
                        label="考试开始时间"
                        name="examStartTime"
                        rules={[
                            {
                            required: true,
                            message: '请输入考试开始时间',
                            },
                        ]}
                        >
                        <DatePicker onChange={onStartDateChange} />
                        <TimePicker className='timepicker' onChange={onStartTimeChange} />
                    </Form.Item>

                    <Form.Item
                        label="考试结束时间"
                        name="examEndTime"
                        rules={[
                            {
                            required: true,
                            message: '请输入考试结束时间',
                            },
                        ]}
                        >
                        <DatePicker onChange={onEndDateChange} />
                        <TimePicker className='timepicker' onChange={onEndTimeChange} />
                    </Form.Item>

                    <Form.Item
                        label="考试试卷"
                        name="examPaperFile"
                        rules={[
                            {
                            required: true,
                            message: '请上传考试试卷',
                            },
                        ]}
                        >
                        <Upload
                            {...uploadProps}
                        >
                            <Button icon={<UploadOutlined />}>Upload</Button>
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        label="试卷名称"
                        name="paperName"
                        rules={[
                            {
                            required: true,
                            message: '请输入试卷名称',
                            },
                        ]}
                        >
                        <Input value={paperName} onChange={paperNameInputChange}/>
                    </Form.Item>
                    
                    <Form.Item
                        label="试卷描述"
                        name="paperDescription"
                        >
                        <TextArea rows={4} value={paperDescription} onChange={paperDescriptionInputChange}/>
                    </Form.Item>

                    <Form.Item
                        label="试卷分数"
                        name="paperScore"
                        rules={[
                            {
                            required: true,
                            message: '请输入试卷分数',
                            },
                        ]}
                        >
                        <Input value={paperScore} onChange={paperScoreInputChange}/>
                    </Form.Item>
                </Form>     
            </Modal>

        </div>
    )


}

export default Teacher_AllExamPage_index