import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../../utils/request';

import UserInfo_Component from '../../../../../../components/UserInfoComponent/userinfo_component_index';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined, ExclamationCircleFilled } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message, Affix, Modal, Input, InputNumber, Table, Space, Select } from 'antd';

import "./adminAddCourseApplication.css"

const Admin_AddCourse_Application = () => {
    const state = useLocation()
    const navigate = useNavigate()
    const { confirm } = Modal;

    const subjectSelectRef = useRef(null)

    const [showedApplicationData, setShowedApplicationData] = useState([])

    const [curSelectedDepartment, setCurSelectedDepartment] = useState("all")
    const [curSelectedSubject, setCurSelectedSubject] = useState("all")
    const [curSelectedTeacher, setCurSelectedTeacher] = useState("all")
    const [curSelectedStatus, setCurSelectedStatus] = useState(-1)

    const [departmentList, setDepartmentList] = useState([])
    const [subjectList, setSubjectList] = useState([
        {
            value: 'all',
            label: '无'
        }
    ])
    const [teacherList, setTeacherList] = useState([])
    const [statusList, setStatusList] = useState([
        {
            value: -1,
            label: '全部',
        },
        {
            value: 0,
            label: '等待批准',
        },
        {
            value: 1,
            label: '已批准',
        },
        {
            value: 2,
            label: '已拒绝',
        }
    ])

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [userAvatr, setUserAvatar] = useState("")
    const [userUsername, setUserUsername] = useState("")
    const [userRealname, setUserRealname] = useState("")
    const [userPhone, setUserPhone] = useState("")
    const [userEmail, setUserEmail] = useState("")

    const handleCancel = () => {
        setIsModalOpen(false);
    };

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '课程图片',
            dataIndex: 'icon',
            key: 'icon',
            render: (text) => <img className='admin-add-course-application-icon' src={text}/>,
        },
        {
            title: '课程名',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '课程描述',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: '所属学院',
            dataIndex: 'departmentName',
            key: 'departmentName',
        },
        {
            title: '所属专业',
            dataIndex: 'subjectName',
            key: 'subjectName',
        },
        {
            title: '申请人',
            dataIndex: 'teacherRealname',
            key: 'teacherRealname',
            render: (text, record) => (
                <a onClick={(e) => showApplicantInfo(record)}>{text}</a>
            ),
        },
        {
            title: '申请时间',
            dataIndex: 'created_time',
            key: 'created_time',
            sorter: (a, b) =>{
                if(a.created_time > b.created_time) return 1
                else return 0
            }
        },
        {
            title: '申请状态',
            dataIndex: 'approve_status',
            key: 'approve_status',
            render: (text, record) => (
                <div>
                    {text == 0 && <Tag color="blue">等待批准</Tag>}
                    {text == 1 && <Tag color="green">已批准</Tag>}
                    {text == 2 && <Tag color="red">已拒绝</Tag>}
                </div>
            )
        },
        {
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <div>
                    {record.approve_status == 0 && 
                        <Space size="middle">
                            <Button type='primary' onClick={(e) => approveAddCourse(record)}>批准添加</Button>
                            <Button type='primary' onClick={(e) => declineAddCourse(record)} danger>拒绝</Button>
                        </Space>
                    }
                </div>
                
            ),
        },
    ];

    // 筛选下拉框的选择事件
    const handleDepartmentSelectChange = (value) => {
        console.log(value)
        setCurSelectedDepartment(value)
        if(value != curSelectedDepartment){
            setSubjectList([{value: 'all', label: '无'}])
            setCurSelectedSubject('all')
        }
        if(value != 'all'){
            getSubjectList(value)
            
        }
    }
    const handleSubjectSelectChange = (value) => {
        console.log(value)
        setCurSelectedSubject(value)
    }
    const handleTeacherSelectChange = (value) => {
        console.log(value)
        setCurSelectedTeacher(value)
    }
    const handleStatusSelectChange = (value) => {
        console.log(value)
        setCurSelectedStatus(value)
    }

    // 获取所有学院列表
    const getDepartmentList = () => {
        userRequest.get("/admin/get-all-department-simple-info")
        .then(function(response){
            console.log(response)
            var tmp = [{value: 'all', label: '全部'}]
            for(var i = 0;i < response.length;++i){
                tmp.push({
                    value: response[i].id,
                    label: response[i].name
                })
            }
            setDepartmentList(tmp)
        })
        .catch(function(error){
            console.log(error)
        })
    }
    // 获取某个学院的所有专业列表
    const getSubjectList = (departmentId) => {
        userRequest.post("/admin/get-subject-simple-info", {
            departmentId: departmentId
        })
        .then(function(response) {
            console.log(response)
            var tmp = [{value: 'all', label: '无'}]
            for(var i = 0;i < response.length;++i){
                tmp.push({
                    value: response[i].id,
                    label: response[i].name
                })
            }
            setSubjectList(tmp)
        })
        .catch(function (error) {
            console.log(error)
        })
    }
    // 获取所有老师列表
    const getTeacherList = () => {
        userRequest.get("/admin/get-all-teacher-simple-info")
        .then(function(response){
            console.log(response)
            var tmp = [{value: 'all', label: '全部'}]
            for(var i = 0;i < response.length;++i){
                tmp.push({
                    value: response[i].id,
                    label: response[i].name
                })
            }
            setTeacherList(tmp)
        })
        .catch(function(error){
            console.log(error)
        })
    }

    // 筛选事件
    const filterAddCourseApplication = () => {
        if(curSelectedDepartment != 'all' && curSelectedSubject == 'all'){
            message.info("筛选若指定学院，则需同时指定专业")
        }
        else{
            userRequest.post("/admin/get-add-course-application", {
                departmentId: curSelectedDepartment,
                subjectId: curSelectedSubject,
                teacherId: curSelectedTeacher,
                applicationStatus: curSelectedStatus
            })
            .then(function(response) {
                console.log(response)
                setShowedApplicationData(response)
            })
            .catch(function (error) {
                console.log(error)
            })
        }
        
    }


    // 组件加载时自动执行
    useEffect(() => {
        getDepartmentList()
        getTeacherList()
	}, []);

    // 管理员查看申请人信息
    const showApplicantInfo = (record) => {
        setUserAvatar(record.teacherAvatar)
        setUserUsername(record.teacherName)
        setUserRealname(record.teacherRealname)
        setUserPhone(record.teacherPhone)
        setUserEmail(record.teacherEmail)

        setIsModalOpen(true);
    }

    // 管理员批准添加课程
    const approveAddCourse = (record) => {
        console.log(record)
        confirm({
            title: '批准确认',
            icon: <ExclamationCircleFilled />,
            content: '你确定要批准添加该课程的申请吗',
            onOk() {
                console.log('OK');
                userRequest.post("/admin/add-course-application-approve", {
                    applicationId: record.id
                })
                .then(function(response) {
                    console.log(response)
                    if(response.resultCode == 12010){
                        message.success(response.msg)
                    }
                    else if(response.resultCode == 12011){
                        message.error(response.msg)
                    }

                    filterAddCourseApplication()
                })
                .catch(function (error) {
                    console.log(error)
                })
            },
            onCancel() {
                console.log('Cancel');
            },
        });
    }
    // 管理员拒绝添加课程
    const declineAddCourse = (record) => {
        console.log(record)
        confirm({
            title: '拒绝确认',
            icon: <ExclamationCircleFilled />,
            content: '你确定要拒绝添加该课程的申请吗',
            onOk() {
                console.log('OK');
                userRequest.post("/admin/add-course-application-decline", {
                    applicationId: record.id
                })
                .then(function(response) {
                    console.log(response)
                    if(response.resultCode == 12010){
                        message.success(response.msg)
                    }
                    else if(response.resultCode == 12011){
                        message.error(response.msg)
                    }

                    filterAddCourseApplication()
                })
                .catch(function (error) {
                    console.log(error)
                })
            },
            onCancel() {
                console.log('Cancel');
            },
        });
        
    }


    return(
        <div>
            {/* 管理员-添加课程申请管理 */}

            <div className='admin-add-course-application-div'>
                {/*  筛选的下拉框 */}
                <div className='admin-add-course-all-filter-outer-div'>
                    <div className='admin-add-course-all-filter-inner-div'>
                        <label>学院：</label>
                        <Select
                            defaultValue="全部"
                            style={{
                                width: 250,
                            }}
                            onChange={handleDepartmentSelectChange}
                            options={departmentList}
                        />
                    </div>
                    <div className='admin-add-course-all-filter-inner-div'>
                        <label>专业：</label>
                        <Select
                            ref={subjectSelectRef}
                            defaultValue="无"
                            style={{
                                width: 250,
                            }}
                            onChange={handleSubjectSelectChange}
                            options={subjectList}
                        />
                    </div>
                    <div className='admin-add-course-all-filter-inner-div'>
                        <label>申请人：</label>
                        <Select
                            defaultValue="全部"
                            style={{
                                width: 250,
                            }}
                            onChange={handleTeacherSelectChange}
                            options={teacherList}
                        />
                    </div>
                    <div className='admin-add-course-all-filter-inner-div'>
                        <label>申请状态：</label>
                        <Select
                            defaultValue="全部"
                            style={{
                                width: 250,
                            }}
                            onChange={handleStatusSelectChange}
                            options={statusList}
                        />
                    </div>

                    <Button onClick={filterAddCourseApplication} className='admin-add-course-filte-button'>筛选</Button>
                    
                </div>

                <Table className='admin-add-course-application-table' dataSource={showedApplicationData} columns={columns} />

            </div>
            
            
    
            <UserInfo_Component
                modalName="申请人信息"
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

export default Admin_AddCourse_Application