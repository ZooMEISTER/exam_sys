import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import Course_Component from '../../../../../../components/CourseComponent/course_component';

import { touristRequest, userRequest } from '../../../../../../utils/request';

import { PlusOutlined } from '@ant-design/icons';
import { Breadcrumb, Button, Card, Modal, message, Form, Input, Upload } from 'antd';

import "./allCoursePage_index.css"

const Teacher_AllCoursePage_index = () => {
    const state = useLocation()
    const navigate = useNavigate()
    const { TextArea } = Input;

    const userid = useSelector(state => state.userid.value)

    const [allCourseInfo, setAllCourseInfo] = useState([])

    const [newCourseIcon, setNewCourseIcon] = useState("")
    const [newCourseName, setNewCourseName] = useState("")
    const [newCourseDescription, setNewCourseDescription] = useState("")


    // 获取数据库中某个学院所有专业的数据
    const getAllCourse = () => {
        userRequest.post("/teacher/get-all-course", {
            subjectId: state.state.subjectId
        })
        .then( function(response) {
            console.log(response)
            setAllCourseInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllCourse()
	}, []);

    const jumpBackToSubjects = () =>{
        navigate('/home/teacher-operate-class/subject', 
            {state:    
                {
                    departmentId: state.state.departmentId, 
                    departmentName: state.state.departmentName
                }
            }
        )
    }

    // 老师输入新课程名称
    const newCourseNameInputChange = (event) => {
        setNewCourseName(event.target.value)
    }
    // 老师输入新课程描述
    const newCourseDescriptionInputChange = (event) => {
        setNewCourseDescription(event.target.value)
    }

    // 上传头像占位按钮
    const uploadButton = (
        <div>
            <PlusOutlined />
            <div style={{marginTop: 8}}>
                上传
            </div>
        </div>
    );

    // 图片转成base64
    const getBase64 = (img, callback) => {
        const reader = new FileReader();
        reader.addEventListener('load', () => callback(reader.result));
        reader.readAsDataURL(img);
    };
    // 选择图片后，进行基础判断，并将图片显示出来
    const beforeUpload = (file) => {
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            message.error('您只能上传 JPG / PNG 格式的图片 !');
        }
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isLt2M) {
            message.error('头像文件大小需小于 2MB !');
        }
        if(isJpgOrPng && isLt2M){
            getBase64(file, (url) => {
                setNewCourseIcon(url);
                console.log(url)
            });
        }
        
        return false;
    };

    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleOk = () => {
        // 添加考试方法
        if(newCourseIcon == ""){
            message.info("发布课程需要一张图片")
        }
        else if(newCourseName == ""){
            message.info("发布课程需要课程名称")
        }
        else{
            console.log(newCourseIcon)
            console.log(newCourseName)
            console.log(newCourseDescription)
            
            userRequest.post("/teacher/add-course", {
                icon: newCourseIcon,
                name: newCourseName,
                description: newCourseDescription,
                teachby: userid,
                subjectId: state.state.subjectId
            })
            .then( function(response) {
                console.log(response)
                if(response.resultCode == 12012){
                    message.success(response.msg)
                }
                else if(response.resultCode == 12013){
                    message.error(response.msg)
                }
                
                setIsModalOpen(false);
                setNewCourseIcon("")
                setNewCourseName("")
                setNewCourseDescription("")
            })
            .catch( function (error) {
                console.log(error)
                message.error("课程添加失败，请查看控制台")
            })
        }
    };
    const handleCancel = () => {
        setIsModalOpen(false);
        setNewCourseIcon("")
        setNewCourseName("")
        setNewCourseDescription("")
    };

    //添加课程方法
    const ClickAddCourse = () => {
        setIsModalOpen(true)
    }

    return(
        <div>
            {/* 先是一个面包屑 */}
            <div className='teacher-all-course-page-title-div'>
                <Breadcrumb 
                    className='teacher-all-course-page-navigate-breadcrumb'
                    items={[
                        {title: <a href="/home/teacher-operate-class">所有学院</a>},
                        {title: <a onClick={jumpBackToSubjects}>{state.state.departmentName}</a>},
                        {title: state.state.subjectName}
                    ]}
                />
                {/* <Button className='teacher-all-course-page-add-course-button'>添加课程</Button> */}
            </div>
            
            {/* 在下面显示所有的学院 */}
            <div className='show-all-course-div'>
                {allCourseInfo.map(item => (
                    <Course_Component
                        key={item.id}
                        id={item.id}
                        icon={item.icon}
                        name={item.name}
                        description={item.description}
                        teachby={item.teachby}
                        teacherRealname={item.teacherRealname}
                        created_time={item.created_time}
                        
                        departmentId={state.state.departmentId}
                        departmentName={state.state.departmentName}
                        subjectId={state.state.subjectId}
                        subjectName={state.state.subjectName}/>
                ))}
                <Card
                    onClick={ClickAddCourse}
                    className='course-card teacher-add-new-course-card'
                    hoverable
                    bordered={false}
                >
                    <div className='teacher-add-new-course-icon-outer-div'>
                        <PlusOutlined style={{ fontSize: '50px'}} className='teacher-add-new-course-icon'/>
                    </div>
                </Card>
            </div>

            {/* 添加新课程的弹窗 */}
            <Modal title="添加新课程" 
                open={isModalOpen} 
                destroyOnClose={true}
                onCancel={handleCancel}
                footer={[
                    <Button onClick={handleCancel}>
                        取消
                    </Button>,
                    <Button type="primary" onClick={handleOk}>
                        添加
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
                        label="课程图片"
                        name="newCourseIcon"
                        rules={[
                            {
                            required: true,
                            message: '请上传新的课程图片',
                            },
                        ]}
                        >
                        <Upload
                            name="avatar"
                            listType="picture-card"
                            className="avatar-uploader"
                            showUploadList={false}
                            action=""
                            beforeUpload={beforeUpload}
                            >
                            {newCourseIcon ? (
                                <img src={newCourseIcon} alt="avatar" style={{width: '100%',}}/>
                            ) : (
                                uploadButton
                            )}
                        </Upload>
                    </Form.Item>
                    
                    <Form.Item
                        label="课程名称"
                        name="newCourseName"
                        rules={[
                            {
                            required: true,
                            message: '请输入新的课程名称',
                            },
                        ]}
                        >
                        <Input value={newCourseName} onChange={newCourseNameInputChange}/>
                    </Form.Item>

                    <Form.Item
                        label="课程描述"
                        name="newCourseDescription"
                        >
                        <TextArea rows={4} value={newCourseDescription} onChange={newCourseDescriptionInputChange}/>
                    </Form.Item>

                </Form>     
            </Modal>
        </div>
    )
}

export default Teacher_AllCoursePage_index