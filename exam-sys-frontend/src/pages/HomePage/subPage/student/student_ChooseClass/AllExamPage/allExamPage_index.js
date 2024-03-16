import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import Exam_Component from '../../../../../../components/ExamComponent/exam_component';

import { touristRequest, userRequest } from '../../../../../../utils/request';

import { Breadcrumb, Button, Tabs, message } from 'antd';

import "./allExamPage_index.css"

const Student_AllExamPage_index = () =>{
    const state = useLocation()
    const navigate = useNavigate()

    const userid = useSelector(state => state.userid.value)

    const [allExamInfo, setAllExamInfo] = useState([])
    const [courseInfo, setCourseInfo] = useState({})
    const [showExamStatus, setShowExamStatus] = useState(0) // 0为显示全部 1为显示未开始的 2为显示正在进行 3为显示已结束
    const [isStudentSigned, setIsStudentSigned] = useState(0) // 学生是否报名了此课程

    // 获取某个课程下的所有考试数据
    const getAllExam = () => {
        userRequest.post("/student/get-all-exam", {
            courseId: state.state.courseId,
            studentId: userid
        })
        .then( function(response) {
            console.log(response)
            setAllExamInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 检查学生是否报名了此课程
    const checkIfStudentSigned = () => {
        userRequest.post("/student/check-if-student-signed-course", {
            courseId: state.state.courseId,
            studentId: userid
        })
        .then( function(response) {
            console.log(response)
            setIsStudentSigned(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 获取该课程信息方法
    const getCourseInfo = () =>{
        userRequest.post("/student/get-course-info", {
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

    // 组件加载时自动执行
    useEffect(() => {
        getAllExam()
        getCourseInfo()
        checkIfStudentSigned()
	}, []);

    const jumpBackToSubjects = () =>{
        navigate('/home/student-choose-class/subject', 
            {state:    
                {
                    departmentId: state.state.departmentId, 
                    departmentName: state.state.departmentName,
                }
            }
        )
    }

    const jumpBackToCourses = () =>{
        navigate('/home/student-choose-class/course', 
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

    // 学生更改Tab的显示
    const examTabChange = (tabKey) => {
        console.log(tabKey)
        setShowExamStatus(tabKey)
    }

    //学生报名课程方法
    const studentSignCourse = () => {
        userRequest.post("/student/student-sign-course", {
            courseId: state.state.courseId,
            studentId: userid
        })
        .then( function(response) {
            console.log(response)
            if(response.resultCode == 12002){
                message.success("报名成功")
                setIsStudentSigned(response.recordCount)
            }
            else if(response.resultCode == 12003){
                message.error("报名失败")
            }
        })
        .catch( function (error) {
            console.log(error)
        })
    }


    return(
        <div>
            {/* 先是一个面包屑 */}
            <Breadcrumb 
                className='navigate-breadcrumb'
                items={[
                    {title: <a href="/home/student-choose-class">所有学院</a>},
                    {title: <a onClick={jumpBackToSubjects}>{state.state.departmentName}</a>},
                    {title: <a onClick={jumpBackToCourses}>{state.state.subjectName}</a>},
                    {title: courseInfo.name}
                ]}
            />
            {/* 在下面显示该课程信息 */}
            <div className='student-course-info-div'>
                <img className='student-course-info-img' src={courseInfo.icon}/> 
                <div className='student-course-info-sub-div'>
                    <text className='student-course-info-name'>{courseInfo.name}</text>
                    <text className='student-course-info-description'>{courseInfo.description}</text>
                    <text className='student-course-info-teachby'>由{courseInfo.teachby}教学</text>
                    <text className='student-course-info-createdtime'>创建于{courseInfo.created_time}</text>
                </div>
                <div className='student-sign-course-div'>

                    {isStudentSigned == 0 && 
                        <Button type='primary' className='student-sign-button' onClick={studentSignCourse}>报名课程</Button>
                    }
                    {isStudentSigned > 0 && 
                        <Button type='primary' ghost className='student-sign-button'>已报名</Button>
                    }
                    
                </div>
            </div>
            <div className='info-div'>
                <text className='info-text'>所有考试 ( {allExamInfo.length} ) </text>
            </div>
            {/* 在这里显示选项卡，把不同状态的考试分开 */}
            <Tabs
                className='student-exam-tabs'
                defaultActiveKey="0"
                onChange={examTabChange}
                items={[
                    {
                        label: '全部',
                        key: '0',
                    },
                    {
                        label: '未开始',
                        key: '1',
                    },
                    {
                        label: '正在进行',
                        key: '2',
                    },
                    {
                        label: '已结束',
                        key: '3',
                    },
                ]}    
            ></Tabs>
            {/* 在下面显示该课程的所有考试 */}
            {   // 显示全部考试
                showExamStatus == 0 &&
                <div className='student-show-all-exam-div' >
                    {allExamInfo.map((item) => (
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
                            showExamStatus={showExamStatus}
                            isStudentSigned={isStudentSigned}

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
            {   // 显示未开始考试
                showExamStatus == 1 &&
                <div className='student-show-all-exam-div' >
                    {allExamInfo.map((item) => {
                        if(item.status === 0){ // 显示未开始考试
                            return(
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
                                    showExamStatus={showExamStatus}
                                    isStudentSigned={isStudentSigned}

                                    departmentId={state.state.departmentId}
                                    departmentName={state.state.subjectName}
                                    subjectId={state.state.subjectId}
                                    subjectName={state.state.departmentName}
                                    courseId={state.state.courseId}
                                    courseName={state.state.courseName}
                                />
                            )
                        }
                    })}
                </div>
            }
            {   // 显示正在进行考试
                showExamStatus == 2 &&
                <div className='student-show-all-exam-div' >
                    {allExamInfo.map((item) => {
                        if(item.status === 1 || item.status === 2){ // 显示未开始考试
                            return(
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
                                    showExamStatus={showExamStatus}
                                    isStudentSigned={isStudentSigned}

                                    departmentId={state.state.departmentId}
                                    departmentName={state.state.subjectName}
                                    subjectId={state.state.subjectId}
                                    subjectName={state.state.departmentName}
                                    courseId={state.state.courseId}
                                    courseName={state.state.courseName}
                                />
                            )
                        }
                    })}
                </div>
            }
            {   // 显示已结束考试
                showExamStatus == 3 &&
                <div className='student-show-all-exam-div' >
                    {allExamInfo.map((item) => {
                        if(item.status === 3 || item.status === 4){ // 显示未开始考试
                            return(
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
                                    showExamStatus={showExamStatus}
                                    isStudentSigned={isStudentSigned}

                                    departmentId={state.state.departmentId}
                                    departmentName={state.state.subjectName}
                                    subjectId={state.state.subjectId}
                                    subjectName={state.state.departmentName}
                                    courseId={state.state.courseId}
                                    courseName={state.state.courseName}
                                />
                            )
                        }
                    })}
                </div>
            }
        </div>
    )


}

export default Student_AllExamPage_index