import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../../utils/request';

import UserInfo_Component from '../../../../../../components/UserInfoComponent/userinfo_component_index';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined, ExclamationCircleFilled } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message, Affix, Modal, Input, InputNumber, Table, Space, Select } from 'antd';

import "./adminBeTeacherApplication.css"

const Admin_BeTeacher_Application = () => {
    const state = useLocation()
    const navigate = useNavigate()
    const { confirm } = Modal;

    const [allToTeacherApplicationList, setAllToTeacherApplicationList] = useState([])

    const [curSelectedStatus, setCurSelectedStatus] = useState(-1)
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
            title: '申请ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '申请人ID',
            dataIndex: 'student_id',
        },
        {
            title: '头像',
            dataIndex: 'avatar',
            key: 'avatar',
            render: (text) => <img className='admin-to-teacher-application-icon' src={text}/>,
        },
        {
            title: '申请人',
            dataIndex: 'realname',
            key: 'realname',
            render: (text, record) => (
                <a onClick={(e) => showApplicantInfo(record)}>{text}</a>
            ),
        },
        {
            title: '申请描述',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: '申请时间',
            dataIndex: 'apply_time',
            key: 'apply_time',
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
                            <Button onClick={(e) => approveToTeacher(record)} type='primary'>批准</Button>
                            <Button onClick={(e) => declineToTeacher(record)} type='primary' danger>拒绝</Button>
                        </Space>
                    }
                </div>
                
            ),
        },
    ];

    // 变更筛选条件下拉框事件
    const handleStatusSelectChange = (value) => {
        console.log(value)
        setCurSelectedStatus(value)
    }

    // 筛选成为老师申请
    const filterBeTeacherApplication = () => {
        userRequest.post("/admin/get-be-teacher-application", {
            applicationStatus: curSelectedStatus
        })
        .then(function(response) {
            console.log(response)
            setAllToTeacherApplicationList(response)
        })
        .catch(function (error) {
            console.log(error)
        })
    }


    // 管理员批准成为老师申请
    const approveToTeacher = (record) => {
        console.log(record)
        confirm({
            title: '批准确认',
            icon: <ExclamationCircleFilled />,
            content: '你确定要批准该学生成为老师的申请吗',
            onOk() {
                console.log('OK');
                userRequest.post("/admin/to-teacher-application-approve", {
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

                    filterBeTeacherApplication()
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
    // 管理员拒绝成为老师申请
    const declineToTeacher = (record) => {
        console.log(record)
        confirm({
            title: '拒绝确认',
            icon: <ExclamationCircleFilled />,
            content: '你确定要拒绝该学生成为老师的申请吗',
            onOk() {
                console.log('OK');
                userRequest.post("/admin/to-teacher-application-decline", {
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

                    filterBeTeacherApplication()
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

    // 管理员查看申请人信息
    const showApplicantInfo = (record) => {
        setUserAvatar(record.avatar)
        setUserUsername(record.username)
        setUserRealname(record.realname)
        setUserPhone(record.phone)
        setUserEmail(record.email)

        setIsModalOpen(true);
    }

    return(
        <div>
            {/* 管理员-成为老师申请管理 */}

            <div className="admin-be-teacher-application-div">
                <div className='admin-be-teacher-all-filter-outer-div'>
                    <div className='admin-be-teacher-all-filter-inner-div'>
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

                    <Button onClick={filterBeTeacherApplication} className='admin-be-teacher-filte-button'>筛选</Button>
                    
                </div>

                <Table className='admin-be-teacher-application-table' dataSource={allToTeacherApplicationList} columns={columns} />

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

export default Admin_BeTeacher_Application