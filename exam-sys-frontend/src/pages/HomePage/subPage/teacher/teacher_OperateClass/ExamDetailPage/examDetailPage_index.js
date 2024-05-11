import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";

import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../../utils/request';

import UserInfo_Component from '../../../../../../components/UserInfoComponent/userinfo_component_index';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message } from 'antd';

import "./examDetailPage_index.css"


const Teacher_ExamDetailPage_index = () => {
    const state = useLocation()
    const navigate = useNavigate()

    const userid = useSelector(state => state.userid.value)

    const [examInfo, setExamInfo] = useState({})
    const [finishedRespondentInfo, setFinishedRespondentInfo] = useState([])
    const [unCheckedFinishedRespondentInfo, setUnCheckedFinishedRespondentInfo] = useState([])
    const [checkedFinishedRespondentInfo, setCheckedFinishedRespondentInfo] = useState([])
    const [unFinishedRespondentInfo, setUnFinishedRespondentInfo] = useState([])

    const [showIfFinishedRespondentStatus, setShowIfFinishedRespondentStatus] = useState(0)
    const [showRespondentStatus, setShowRespondentStatus] = useState(0)
    
    const [paginationSize, setPaginationSize] = useState(10)

    const [userAvatr, setUserAvatar] = useState("")
    const [userUsername, setUserUsername] = useState("")
    const [userRealname, setUserRealname] = useState("")
    const [userPhone, setUserPhone] = useState("")
    const [userEmail, setUserEmail] = useState("")


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

    // 获取该考试所有已交卷的信息
    const getAllFinishedExamInfo = () => {
        userRequest.post("/teacher/get-finished-respondent", {
            examId: state.state.examId
        })
        .then( function(response) {
            console.log(response)
            setFinishedRespondentInfo(response)
            var unCheckedRespondentInfo = []
            var checkedRespondentInfo = []
            for(var i = 0;i < response.length;++i){
                if(response[i].final_score >= 0){
                    checkedRespondentInfo.push(response[i])
                }
                else{
                    unCheckedRespondentInfo.push(response[i])
                }
            }
            setUnCheckedFinishedRespondentInfo(unCheckedRespondentInfo)
            setCheckedFinishedRespondentInfo(checkedRespondentInfo)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 获取该考试所有未交卷的信息
    const getAllUnFinishedExamInfo = () => {
        userRequest.post("/teacher/get-unfinished-respondent", {
            examId: state.state.examId
        })
        .then( function(response) {
            console.log(response)
            setUnFinishedRespondentInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getExamInfo()
        getAllFinishedExamInfo()
        getAllUnFinishedExamInfo()
	}, []);

    // 返回课程详情页
    const backToAllExam = () => {
        navigate('/home/teacher-operate-class/exam', 
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

    // 老师切换显示答卷
    const ifFinishedRespondentTabChange = (tabKey) => {
        console.log(tabKey)
        setShowIfFinishedRespondentStatus(tabKey)
    }
    // 老师切换显示批改和未批改和全部答卷
    const respondentTabChange = (tabKey) => {
        console.log(tabKey)
        setShowRespondentStatus(tabKey)
    }

    const [isModalOpen, setIsModalOpen] = useState(false);
    // 打开显示用户信息的Modal
    const openUserInfoModal = (item, type) => {
        console.log(item)
        console.log(type)
        if(type == 0){
            setUserAvatar(item.studentAvatar)
            setUserUsername(item.studentUsername)
            setUserRealname(item.studentRealname)
            setUserPhone(item.studentPhone)
            setUserEmail(item.studentEmail)
        }
        else if(type == 1){
            setUserAvatar(item.avatar)
            setUserUsername(item.username)
            setUserRealname(item.realname)
            setUserPhone(item.phone)
            setUserEmail(item.email)
        }

        setIsModalOpen(true);
    }
    const handleCancel = () => {
        setIsModalOpen(false);
    };


    // 老师获取AES密钥
    const getAeskey = () => {
        userRequest.post("/teacher/get-exam-aes-key", {
            paperId: examInfo.paperId
        })
        .then( function(response) {
            console.log(response)
            const type = "text/plain";
            const blob = new Blob([response.aesKey], { type });
            const data = [new ClipboardItem({ [type]: blob })];
            navigator.clipboard.write(data).then(
                () => {
                    /* success */
                    message.success("AES密钥已复制到剪切板")
                },
                () => {
                    /* failure */
                    message.error("AES密钥复制失败")
                },
            );
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 老师下载试卷
    const downloadExamPaper = () => {
        message.loading("正在下载试卷...")
        userFileDownloadRequest.post("/teacher/download-exam-paper", {
            paperName: examInfo.paperPath
        })
        .then(function(response) {
            console.log(response)
            var fileNameEncode = response.headers['content-disposition']
                .split('filename=')[1]
                .split(';')[0];
            var fileNameDecode = decodeURI(fileNameEncode);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', fileNameDecode);
            document.body.appendChild(link);
            link.click();

            message.success("下载成功")
        })
        .catch( function (error) {
            console.log(error)

            message.error("下载失败")
        })
    }

    // 老师跳转到试卷批改页面
    const jumpToRespondentCorrectPage = (item) => {
        if(userid == examInfo.teachby){
            navigate('/correct-respondent', 
            {
                state: {
                    departmentId: state.state.departmentId, 
                    departmentName: state.state.departmentName, 
                    subjectId: state.state.subjectId, 
                    subjectName: state.state.subjectName,
                    courseId: state.state.courseId,
                    courseName: state.state.courseName,

                    examId: state.state.examId,
                    examName: examInfo.examName,
                    respondentId: item.id
                }
            })
        }
        else{
            message.error("您不是该课程的授课老师，无法查看学生答卷")
        }
    }

    return(
        <div className='exam-detail-root'>
            <div className='teacher-exam-detail-base'>
                <Button type='link' onClick={backToAllExam} className='teacher-back-to-all-exam-button'>返回</Button>
                <Card title={examInfo.name} className='teacher-exam-detail-card'>
                    <div className='teacher-exam-detail-div'>
                        <label className='teacher-exam-detail-description'>{examInfo.description}</label>
                    </div>
                    <div className='teacher-exam-detail-div'>
                        <label className='teacher-exam-detail-start-end'>由 {examInfo.teacherRealname} 于 {examInfo.created_time} 发布</label>
                        <label className='teacher-exam-detail-start-end'>开始时间：{examInfo.start_time}</label>
                        <label className='teacher-exam-detail-start-end'>结束时间：{examInfo.end_time}</label>
                    </div>

                    <Card className='teacher-paper-card' type="inner" title={examInfo.paperName} extra={
                        <div>
                            <a onClick={getAeskey} target="_blank">复制AES密钥</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a onClick={downloadExamPaper} target="_blank">下载试卷</a>
                            {/* <a href={examInfo.paperPath} target="_blank">下载试卷</a> */}
                        </div>
                    }>
                        <label className='teacher-paper-detail-text'>试卷描述：{examInfo.paperDescription}</label> <br/>
                        <label className='teacher-paper-detail-text'>试卷分数：{examInfo.paperScore}</label> <br/>
                        <div className='teacher-paper-detail-finished-situation-div'>
                            <Tabs
                                className='teacher-respondent-tabs'
                                defaultActiveKey="0"
                                onChange={ifFinishedRespondentTabChange}
                                items={[
                                    {
                                        label: '已交卷',
                                        key: '0',
                                    },
                                    {
                                        label: '未交卷',
                                        key: '1',
                                    },
                                ]}    
                            ></Tabs>

                            {showIfFinishedRespondentStatus == 0 && 
                                <div className='teacher-examdetail-segmented-outer-div'>
                                    <Segmented
                                        className='teacher-examdetail-segmented'
                                        options={[
                                            { label: '全部', value: '0', icon: <MinusCircleOutlined /> },
                                            { label: '未批改', value: '1', icon: <CloseCircleOutlined /> },
                                            { label: '已批改', value: '2', icon: <CheckCircleOutlined /> },
                                        ]}
                                        onChange={respondentTabChange}
                                    />
                                    {showRespondentStatus == 0 && 
                                        <List
                                            pagination={{
                                                pageSize: paginationSize,
                                            }}
                                            dataSource={finishedRespondentInfo}
                                            renderItem={(item, index) => (
                                                <List.Item
                                                    actions={[
                                                        // <a href={item.respondent_path} target='_blank'>查看答卷</a>
                                                        <a onClick={(e) => jumpToRespondentCorrectPage(item)}>查看答卷</a>
                                                    ]}
                                                >
                                                    <Skeleton avatar title={false} loading={item.loading} active>
                                                        <List.Item.Meta
                                                            avatar={<Avatar shape="square" size={48} src={item.studentAvatar} />}
                                                            title={<a onClick={(e) => openUserInfoModal(item, 0)}>{item.studentUsername}</a>}
                                                            description={item.studentRealname}
                                                        />
                                                        {item.final_score >= 0 && (item.final_score >= examInfo.paperScore * 0.6) &&
                                                            <Tag color="blue">得分：{item.final_score}</Tag>
                                                        }
                                                        {item.final_score >= 0 && (item.final_score < examInfo.paperScore * 0.6) &&
                                                            <Tag color="red">得分：{item.final_score}</Tag>
                                                        }
                                                        {item.final_score < 0 && 
                                                            <Tag>未批改</Tag>
                                                        }
                                                    </Skeleton>
                                                </List.Item>
                                            )}
                                        />
                                    }
                                    {showRespondentStatus == 1 && 
                                        <List
                                            pagination={{
                                                pageSize: paginationSize,
                                            }}
                                            dataSource={unCheckedFinishedRespondentInfo}
                                            renderItem={(item, index) => (
                                                <List.Item
                                                    actions={[
                                                        // <a href={item.respondent_path} target='_blank'>查看答卷</a>
                                                        <a onClick={(e) => jumpToRespondentCorrectPage(item)}>查看答卷</a>
                                                    ]}
                                                >
                                                    <List.Item.Meta
                                                        avatar={<Avatar shape="square" size={48} src={item.studentAvatar} />}
                                                        title={<a onClick={(e) => openUserInfoModal(item, 0)}>{item.studentUsername}</a>}
                                                        description={item.studentRealname}
                                                    />
                                                </List.Item>
                                            )}
                                        />
                                    }
                                    {showRespondentStatus == 2 && 
                                        <List
                                            pagination={{
                                                pageSize: paginationSize,
                                            }}
                                            dataSource={checkedFinishedRespondentInfo}
                                            renderItem={(item, index) => (
                                                <List.Item
                                                    actions={[
                                                        // <a href={item.respondent_path} target='_blank'>查看答卷</a>
                                                        <a onClick={(e) => jumpToRespondentCorrectPage(item)}>查看答卷</a>
                                                    ]}
                                                >
                                                    <Skeleton avatar title={false} loading={item.loading} active>
                                                        <List.Item.Meta
                                                            avatar={<Avatar shape="square" size={48} src={item.studentAvatar} />}
                                                            title={<a onClick={(e) => openUserInfoModal(item, 0)}>{item.studentUsername}</a>}
                                                            description={item.studentRealname}
                                                        />
                                                        {(item.final_score >= examInfo.paperScore * 0.6) &&
                                                            <Tag color="blue">得分：{item.final_score}</Tag>
                                                        }
                                                        {(item.final_score < examInfo.paperScore * 0.6) &&
                                                            <Tag color="red">得分：{item.final_score}</Tag>
                                                        }
                                                    </Skeleton>
                                                </List.Item>
                                            )}
                                        />
                                    }
                                </div>
                            }
                            {showIfFinishedRespondentStatus == 1 && 
                                <List
                                    pagination={{
                                        pageSize: paginationSize,
                                    }}
                                    dataSource={unFinishedRespondentInfo}
                                    renderItem={(item, index) => (
                                        <List.Item>
                                            <List.Item.Meta
                                                avatar={<Avatar shape="square" size={48} src={item.avatar} />}
                                                title={<a onClick={(e) => openUserInfoModal(item, 1)}>{item.username}</a>}
                                                description={item.realname}
                                            />
                                        </List.Item>
                                    )}
                                />
                            }
                            
                        </div>
                    </Card>
                </Card>
            </div>

            <UserInfo_Component
                modalName="学生信息"
                isModalOpen={isModalOpen}
                handleCancel={handleCancel}
                avatar={userAvatr}
                username={userUsername}
                realname={userRealname}
                phone={userPhone}
                email={userEmail}
            />
        </div>
    )
}

export default Teacher_ExamDetailPage_index