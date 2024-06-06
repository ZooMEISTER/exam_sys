import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../utils/request';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined, ExclamationCircleFilled, PlusOutlined } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message, Affix, Modal, Input, InputNumber, Table, Space, Select, Form, Upload } from 'antd';

import './adminDepartmentManagement_index.css'

const Admin_DepartmentManagement = () => {
    const state = useLocation()
    const navigate = useNavigate()
    const { confirm } = Modal;
    const { TextArea } = Input;
    const [form] = Form.useForm(); // 对表单的引用

    const [searchStr, setSearchStr] = useState("")
    const [departmentList, setDepartmentList] = useState([])

    const [isOpenModal, setIsOpenMadal] = useState(false)
    const [modalTitle, setModalTitle] = useState("")
    const [curDepartmentId, setCurDepartmentId] = useState("")
    const [newDepartmentName, setNewDeartmentName] = useState("")
    const [newDepartmentIcon, setNewDepartmentIcon] = useState("")
    const [newDepartmentDescription, setNewDepartmentDescription] = useState("")

    const columns = [
        {
            title: '学院ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '学院图片',
            dataIndex: 'icon',
            key: 'icon',
            render: (text) => <img className='admin-department-management-icon' src={text}/>,
        },
        {
            title: '学院名称',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '学院描述',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: '专业数',
            dataIndex: 'subject_count',
            key: 'subject_count',
        },
        {
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <Space>
                    <Button onClick={(e) => openModal(record)} type='primary'>编辑</Button>
                    <Button onClick={(e) => delDepartment(record)} type='primary' danger>删除</Button>
                </Space>
            ),
        },
    ];


    const changeSearchStr = (e) => {
        console.log(e.target.value)
        setSearchStr(e.target.value)
    }

    const search = () => {
        userRequest.post("/admin/search-department", {
            searchStr: searchStr
        })
        .then(function(response) {
            console.log(response)
            
            setDepartmentList(response)
        })
        .catch(function (error) {
            console.log(error)
        })
    }

    const openModal = (record) => {
        setModalTitle("学院信息")
        setNewDeartmentName(record.name)
        setNewDepartmentIcon(record.icon)
        setNewDepartmentDescription(record.description)

        setCurDepartmentId(record.id)
        setIsOpenMadal(true)
    }
    const handleOk = () => {
        console.log(newDepartmentIcon)
        console.log(newDepartmentName)
        console.log(newDepartmentDescription)

        if(newDepartmentIcon == ""){
            message.info("学院图片不能为空")
        }
        else if(newDepartmentName == ""){
            message.info("学院名不能为空")
        }
        else{
            if(modalTitle == "添加新学院"){
                userRequest.post("/admin/add-department", {
                    newDepartmentName: newDepartmentName,
                    newDepartmentIcon: newDepartmentIcon,
                    newDepartmentDescription: newDepartmentDescription
                })
                .then(function(response) {
                    console.log(response)
                    
                    if(response.resultCode == 12022){
                        message.success(response.msg)
                        setIsOpenMadal(false)

                        search()
                    }
                    else if(response.resultCode == 12023){
                        message.error(response.msg)
                    }
                })
                .catch(function (error) {
                    console.log(error)
                })
            }
            else if(modalTitle == "学院信息"){
                userRequest.post("/admin/update-department-info", {
                    departmentId: curDepartmentId,
                    newDepartmentName: newDepartmentName,
                    newDepartmentIcon: newDepartmentIcon,
                    newDepartmentDescription: newDepartmentDescription
                })
                .then(function(response) {
                    console.log(response)
                    
                    if(response.resultCode == 12020){
                        message.success(response.msg)
                        setIsOpenMadal(false)

                        search()
                    }
                    else if(response.resultCode == 12021){
                        message.error(response.msg)
                    }
                })
                .catch(function (error) {
                    console.log(error)
                })
            }
        }

        
    }
    const handleCancel = () => {
        setIsOpenMadal(false)
    }


    const addDepartment = () => {
        setModalTitle("添加新学院")
        setNewDeartmentName("")
        setNewDepartmentIcon("")
        setNewDepartmentDescription("")

        setIsOpenMadal(true)
    }


    // 上传图片占位按钮
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
                setNewDepartmentIcon(url);
                console.log(url)
            });
        }
        return false;
    };

    const changeDepartmentNameInput = (e) => {
        setNewDeartmentName(e.target.value)
    }
    const changeDepartmentDescriptionInput = (e) => {
        setNewDepartmentDescription(e.target.value)
    }


    const delDepartment = (record) => {
        confirm({
            title: '删除确认',
            icon: <ExclamationCircleFilled />,
            content: <div>你确定要删除 {record.name} 吗? </div>,
            onOk() {
                console.log('OK');
                userRequest.post("/admin/delete-department", {
                    departmentId: record.id
                })
                .then(function(response) {
                    console.log(response)
                    if(response.resultCode == 12024){
                        message.success(response.msg)
                    }
                    else if(response.resultCode == 12025){
                        message.error(response.msg)
                    }

                    search()
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
        <div className='root-div'>
            {/* 管理员-学院管理 */}

            <div className="admin-department-management-div">

                <div className='admin-department-management-filter-outer-div'>
                    <Input className='admin-department-management-search-input' onChange={changeSearchStr}></Input>
                    <Button type='primary' onClick={search} className='admin-department-management-search-button'>搜索</Button>
                    <Button onClick={addDepartment} className='admin-department-management-search-button'>添加学院</Button>
                </div>

                <Table className='admin-department-management-table' dataSource={departmentList} columns={columns} />

            </div>

            <Modal
                title={modalTitle}
                open={isOpenModal}
                onOk={handleOk}
                onCancel={handleCancel}
                okText="确认"
                cancelText="取消"
            >
                <Form
                    name="departmentInfoForm"
                    form={form}
                    labelCol={{span: 6}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    autoComplete="off"
                >
                    <Form.Item
                        label="学院图片"
                        name="departmentIcon"
                        rules={[{required: true,message: '请选择图片'}]}>
                        <Upload
                            name="avatar"
                            listType="picture-card"
                            className="avatar-uploader"
                            showUploadList={false}
                            action=""
                            beforeUpload={beforeUpload}
                            >
                            {newDepartmentIcon ? (
                                <img src={newDepartmentIcon} alt="icon" style={{width: '100%',}}/>
                            ) : (
                                uploadButton
                            )}
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        label="学院名"
                        name="departmentName"
                        rules={[{ required: true, message: '请输入学院名'}]}>
                        <div className='department-form-item-content'>
                            <Input placeholder='学院名' onChange={changeDepartmentNameInput} value={newDepartmentName}/>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="学院描述"
                        name="departmentDescription"
                    >
                        <div className='department-form-item-content'>
                            <TextArea placeholder='学院描述' onChange={changeDepartmentDescriptionInput} value={newDepartmentDescription}/>
                        </div>
                    </Form.Item>
                </Form>
            </Modal>

        </div>
    )
}

export default Admin_DepartmentManagement