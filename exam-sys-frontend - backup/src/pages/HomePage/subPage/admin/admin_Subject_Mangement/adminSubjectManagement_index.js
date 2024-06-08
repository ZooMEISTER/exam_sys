import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../utils/request';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined, ExclamationCircleFilled, PlusOutlined } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message, Affix, Modal, Input, InputNumber, Table, Space, Select, Form, Upload } from 'antd';

import './adminSubjectManagement_index.css'

const Admin_SubjectManagement = () => {
    const state = useLocation()
    const navigate = useNavigate()
    const { confirm } = Modal;
    const { TextArea } = Input;
    const [form] = Form.useForm(); // 对表单的引用

    const [curSelectedDepartment, setCurSelectedDepartment] = useState("all")

    const [departmentList, setDepartmentList] = useState([])
    const [departmentList_NoAll, setDepartmentList_NoAll] = useState([])
    const [subjectList, setSubjectList] = useState([])


    const [isOpenModal, setIsOpenMadal] = useState(false)
    const [modalTitle, setModalTitle] = useState("")
    const [curSubjectId, setCurSubjectId] = useState("")
    const [newSubjectName, setNewSubjectName] = useState("")
    const [newSubjectIcon, setNewSubjectIcon] = useState("")
    const [newSubjectDescription, setNewSubjectDescription] = useState("")

    const [curNewSubjectBelong, setCurNewSubjectBelong] = useState("none");

    const columns = [
        {
            title: '专业ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '专业图片',
            dataIndex: 'icon',
            key: 'icon',
            render: (text) => <img className='admin-department-management-icon' src={text}/>,
        },
        {
            title: '专业名称',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '专业描述',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: '隶属学院',
            dataIndex: 'belong_department',
            key: 'belong_department',
        },
        {
            title: '课程数',
            dataIndex: 'course_count',
            key: 'course_count',
        },
        {
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <Space>
                    <Button onClick={(e) => openModal(record)} type='primary'>编辑</Button>
                    <Button onClick={(e) => delSubject(record)} type='primary' danger>删除</Button>
                </Space>
            ),
        },
    ];



    // 学院筛选下拉框的选择事件
    const handleDepartmentSelectChange = (value) => {
        console.log(value)
        setCurSelectedDepartment(value)
    }

    // 获取所有学院列表
    const getDepartmentList = () => {
        userRequest.get("/admin/get-all-department-simple-info")
        .then(function(response){
            console.log(response)
            var tmp = [{value: 'all', label: '全部'}]
            var tmp_ = [{value: 'none', label: '无'}]
            for(var i = 0;i < response.length;++i){
                tmp.push({
                    value: response[i].id,
                    label: response[i].name
                })
                tmp_.push({
                    value: response[i].id,
                    label: response[i].name
                })
            }
            setDepartmentList(tmp)
            setDepartmentList_NoAll(tmp_)
        })
        .catch(function(error){
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getDepartmentList()
    }, []);

    // 根据选择的学院筛选专业
    const filterSubjectManagement = () => {
        userRequest.post("/admin/get-subject-management", {
            departmentId: curSelectedDepartment,
        })
        .then(function(response) {
            console.log(response)
            setSubjectList(response)
        })
        .catch(function (error) {
            console.log(error)
        })
    }


    const openModal = (record) => {
        setModalTitle("专业信息")
        setNewSubjectName(record.name)
        setNewSubjectIcon(record.icon)
        setNewSubjectDescription(record.description)

        setCurSubjectId(record.id)
        setIsOpenMadal(true)
    }
    const handleOk = () => {
        console.log(newSubjectIcon)
        console.log(newSubjectName)
        console.log(newSubjectDescription)

        if(newSubjectIcon == ""){
            message.info("专业图片不能为空")
        }
        else if(newSubjectName == ""){
            message.info("专业名不能为空")
        }
        else{
            if(modalTitle == "添加新专业"){
                if(curNewSubjectBelong == 'none'){
                    message.info("添加新专业需要选择隶属学院")
                }
                else{
                    userRequest.post("/admin/add-subject", {
                        belongto: curNewSubjectBelong,
                        newSubjectName: newSubjectName,
                        newSubjectIcon: newSubjectIcon,
                        newSubjectDescription: newSubjectDescription
                    })
                    .then(function(response) {
                        console.log(response)
                        
                        if(response.resultCode == 12032){
                            message.success(response.msg)
                            setIsOpenMadal(false)
    
                            filterSubjectManagement()
                        }
                        else if(response.resultCode == 12033){
                            message.error(response.msg)
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
                }
            }
            else if(modalTitle == "专业信息"){
                userRequest.post("/admin/update-subject-info", {
                    subjectId: curSubjectId,
                    newSubjectName: newSubjectName,
                    newSubjectIcon: newSubjectIcon,
                    newSubjectDescription: newSubjectDescription
                })
                .then(function(response) {
                    console.log(response)
                    
                    if(response.resultCode == 12030){
                        message.success(response.msg)
                        setIsOpenMadal(false)

                        filterSubjectManagement()
                    }
                    else if(response.resultCode == 12031){
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

    // 管理员删除专业
    const delSubject = (record) => {
        confirm({
            title: '删除确认',
            icon: <ExclamationCircleFilled />,
            content: <div>你确定要删除 {record.name} 吗? </div>,
            onOk() {
                console.log('OK');
                userRequest.post("/admin/delete-subject", {
                    subjectId: record.id
                })
                .then(function(response) {
                    console.log(response)
                    if(response.resultCode == 12034){
                        message.success(response.msg)
                    }
                    else if(response.resultCode == 12035){
                        message.error(response.msg)
                    }

                    filterSubjectManagement()
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
                setNewSubjectIcon(url);
                console.log(url)
            });
        }
        return false;
    };


    const changeSubjectNameInput = (e) => {
        setNewSubjectName(e.target.value)
    }
    const changeSubjectDescriptionInput = (e) => {
        setNewSubjectDescription(e.target.value)
    }

    const addNewSubject = () => {
        setModalTitle("添加新专业")
        setNewSubjectName("")
        setNewSubjectIcon("")
        setNewSubjectDescription("")

        setIsOpenMadal(true)
    }

    const handleNewSubjectBelongSelectChange = (value) => {
        console.log(value)
        setCurNewSubjectBelong(value)
    }
 
    return(
        <div className='root-div'>
            {/* 管理员-专业管理 */}

            <div className='admin-subject-management-div'>
                <div className='admin-subject-management-filter-outer-div'>
                    <div className='admin-subject-management-filter-inner-div'>
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
                    
                    <Button type='primary' onClick={filterSubjectManagement} className='admin-subject-management-filte-button'>筛选</Button>
                    <Button onClick={addNewSubject} className='admin-subject-management-add-subject-button'>添加新专业</Button>

                </div>

                <Table className='admin-subject-management-table' dataSource={subjectList} columns={columns} />
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
                    name="subjectInfoForm"
                    form={form}
                    labelCol={{span: 6}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}
                    initialValues={{remember: true}}
                    autoComplete="off"
                >
                    <Form.Item
                        label="专业图片"
                        name="subjectIcon"
                        rules={[{required: true,message: '请选择图片'}]}>
                        <Upload
                            name="avatar"
                            listType="picture-card"
                            className="avatar-uploader"
                            showUploadList={false}
                            action=""
                            beforeUpload={beforeUpload}
                            >
                            {newSubjectIcon ? (
                                <img src={newSubjectIcon} alt="icon" style={{width: '100%',}}/>
                            ) : (
                                uploadButton
                            )}
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        label="专业名"
                        name="subjectName"
                        rules={[{ required: true, message: '请输入专业名'}]}>
                        <div className='department-form-item-content'>
                            <Input placeholder='专业名' onChange={changeSubjectNameInput} value={newSubjectName}/>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="专业描述"
                        name="subjectDescription"
                    >
                        <div className='department-form-item-content'>
                            <TextArea placeholder='专业描述' onChange={changeSubjectDescriptionInput} value={newSubjectDescription}/>
                        </div>
                    </Form.Item>

                    {modalTitle == '添加新专业' && 
                        <Form.Item
                            label="隶属学院"
                            name="belongto"
                            rules={[{ required: true, message: '请选择隶属学院'}]}>
                            <div className='department-form-item-content'>
                                <Select
                                    defaultValue="无"
                                    style={{
                                        width: 250,
                                    }}
                                    onChange={handleNewSubjectBelongSelectChange}
                                    options={departmentList_NoAll}
                                />
                            </div>
                        </Form.Item>
                    }
                    
                </Form>
            </Modal>
        </div>
    )
}

export default Admin_SubjectManagement