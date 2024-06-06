import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userFileDownloadRequest, userRequest } from '../../../../../utils/request';

import UserInfo_Component from '../../../../../components/UserInfoComponent/userinfo_component_index';

import { MinusCircleOutlined, CloseCircleOutlined, CheckCircleOutlined, ExclamationCircleFilled, PlusOutlined } from '@ant-design/icons';
import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, message, Affix, Modal, Input, InputNumber, Table, Space, Select, Form, Upload } from 'antd';

import './adminCourseManagement_index.css'


const Admin_CourseManagement = () => {
    const state = useLocation()
    const navigate = useNavigate()
    const { confirm } = Modal;
    const { TextArea } = Input;
    const [form] = Form.useForm(); // 对表单的引用


    const [curSelectedDepartment, setCurSelectedDepartment] = useState("all")
    const [curSelectedSubject, setCurSelectedSubject] = useState("all")
    const [curSelectedTeacher, setCurSelectedTeacher] = useState("all")

    const [isOpenModal, setIsOpenMadal] = useState(false)
    const [modalTitle, setModalTitle] = useState("")
    const [newCourseIcon, setNewCourseIcon] = useState("")
    const [newCourseName, setNewCourseName] = useState("")
    const [newCourseDescription, setNewCourseDescription] = useState("")
    const [newCourseTeacher, setNewCourseTeacher] = useState("")
    const [curNewCourseBelongedSubject, setCurNewCourseBelongedSubject] = useState("")

    const [curCourseId, setCurCourseId] = useState("")


    const [departmentList_, setdepartmentList_] = useState([])
    const [subjectList_, setSubjectList_] = useState([])

    const [departmentList, setDepartmentList] = useState([])
    const [subjectList, setSubjectList] = useState(
        [{
            value: 'all',
            label: '无'
        }]
    )
    const [courseList, setCourseList] = useState([])

    const [teacherList, setTeacherList] = useState([])

    const columns = [
        {
            title: '课程ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: '课程图片',
            dataIndex: 'icon',
            key: 'icon',
            render: (text) => <img className='admin-department-management-icon' src={text}/>,
        },
        {
            title: '课程名称',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: '课程描述',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: '隶属学院',
            dataIndex: 'departmentName',
            key: 'departmentName',
        },
        {
            title: '隶属专业',
            dataIndex: 'subjectName',
            key: 'subjectName',
        },
        {
            title: '教学老师',
            dataIndex: 'teacherRealname',
            key: 'teacherRealname',
        },
        {
            title: '创建时间',
            dataIndex: 'created_time',
            key: 'created_time',
            sorter: (a, b) =>{
                if(a.created_time > b.created_time) return 1
                else return 0
            }
        },
        {
            title: '操作',
            key: 'action',
            render: (text, record) => (
                <Space>
                    <Button onClick={(e) => openModal(record)} type='primary'>编辑</Button>
                    <Button onClick={(e) => delCourse(record)} type='primary' danger>删除</Button>
                </Space>
            ),
        },
    ];


    // 组件加载时自动执行
    useEffect(() => {
        getDepartmentList()
        getTeacherList()
    }, []);

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
            setdepartmentList_(tmp_)
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


    // 学院下拉框选择事件
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
    // 专业下拉框选择事件
    const handleSubjectSelectChange = (value) => {
        setCurSelectedSubject(value)
    }
    // 老师下拉框选择事件
    const handleTeacherSelectChange = (value) => {
        setCurSelectedTeacher(value)
    }
    // 筛选课程事件
    const filterCourseManagement = () => {
        console.log(curSelectedDepartment)
        console.log(curSelectedSubject)
        console.log(curSelectedTeacher)

        if(curSelectedDepartment != 'all' && curSelectedSubject == 'all'){
            message.info("筛选若指定学院，则需同时指定专业")
        }
        else{
            userRequest.post("/admin/get-courses", {
                departmentId: curSelectedDepartment,
                subjectId: curSelectedSubject,
                teacherId: curSelectedTeacher,
            })
            .then(function(response) {
                console.log(response)
                setCourseList(response)
            })
            .catch(function (error) {
                console.log(error)
            })
        }
    }

    const openModal = (record) => {
        setModalTitle("课程信息")
        setNewCourseName(record.name)
        setNewCourseIcon(record.icon)
        setNewCourseDescription(record.description)

        setCurCourseId(record.id)
        setIsOpenMadal(true)
    }
    const handleOk = () => {
        console.log(newCourseIcon)
        console.log(newCourseName)
        console.log(newCourseDescription)

        if(newCourseIcon == ""){
            message.info("课程图片不能为空")
        }
        else if(newCourseName == ""){
            message.info("课程名不能为空")
        }
        else{
            if(modalTitle == "添加新课程"){
                if(curNewCourseBelongedSubject == 'none'){
                    message.info("添加新课程需要选择隶属专业")
                }
                else if(newCourseTeacher == '' || newCourseTeacher == 'all' || newCourseTeacher == 'none'){
                    message.info("添加课程需要选择教学老师")
                }
                else{
                    userRequest.post("/admin/add-course", {
                        belongto: curNewCourseBelongedSubject,
                        newCourseName: newCourseName,
                        newCourseIcon: newCourseIcon,
                        newCourseDescription: newCourseDescription,
                        teacherId: newCourseTeacher
                    })
                    .then(function(response) {
                        console.log(response)
                        
                        if(response.resultCode == 12042){
                            message.success(response.msg)
                            setIsOpenMadal(false)
    
                            filterCourseManagement()
                        }
                        else if(response.resultCode == 12043){
                            message.error(response.msg)
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
                    
                }
            }
            else if(modalTitle == "课程信息"){
                userRequest.post("/admin/update-course-info", {
                    courseId: curCourseId,
                    newCourseName: newCourseName,
                    newCourseIcon: newCourseIcon,
                    newCourseDescription: newCourseDescription
                })
                .then(function(response) {
                    console.log(response)

                    if(response.resultCode == 12040){
                        message.success(response.msg)
                        setIsOpenMadal(false)

                        filterCourseManagement()
                    }
                    else if(response.resultCode == 12041){
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

    // 添加课程事件
    const addNewCourse = () => {
        setModalTitle("添加新课程")
        setNewCourseName("")
        setNewCourseIcon("")
        setNewCourseDescription("")

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
                setNewCourseIcon(url);
                console.log(url)
            });
        }
        return false;
    };

    const changeCourseNameInput = (e) => {
        setNewCourseName(e.target.value)
    }
    const changeCourseDescriptionInput = (e) => {
        setNewCourseDescription(e.target.value)
    }


    const handleNewCourseBelongDepartmentSelectChange = (departmentId) => {
        setCurNewCourseBelongedSubject('none')
        setSubjectList_([])
        if(departmentId != 'none'){
            userRequest.post("/admin/get-subject-simple-info", {
                departmentId: departmentId
            })
            .then(function(response) {
                console.log(response)
                var tmp = [{value: 'none', label: '无'}]
                for(var i = 0;i < response.length;++i){
                    tmp.push({
                        value: response[i].id,
                        label: response[i].name
                    })
                }
                setSubjectList_(tmp)
            })
            .catch(function (error) {
                console.log(error)
            })
        }
        
    }

    const handleNewCourseBelongSubjectSelectChange = (value) => {
        setCurNewCourseBelongedSubject(value)
    }

    const handleNewCourseTeacherSelectChange = (value) => {
        console.log(value)
        setNewCourseTeacher(value)
    }


    const delCourse = (record) => {
        confirm({
            title: '删除确认',
            icon: <ExclamationCircleFilled />,
            content: <div>你确定要删除 {record.name} 吗? </div>,
            onOk() {
                console.log('OK');
                userRequest.post("/admin/delete-course", {
                    courseId: record.id
                })
                .then(function(response) {
                    console.log(response)
                    if(response.resultCode == 12044){
                        message.success(response.msg)
                    }
                    else if(response.resultCode == 12045){
                        message.error(response.msg)
                    }

                    filterCourseManagement()
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
            {/* 管理员-课程管理 */}

            <div className='admin-course-management-div'>
                <div className='admin-course-management-filter-outer-div'>
                    <div className='admin-course-management-filter-inner-div'>
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

                    <div className='admin-course-management-filter-inner-div'>
                        <label>专业：</label>
                        <Select
                            defaultValue="全部"
                            style={{
                                width: 250,
                            }}
                            onChange={handleSubjectSelectChange}
                            options={subjectList}
                        />
                    </div>

                    <div className='admin-course-management-filter-inner-div'>
                        <label>老师：</label>
                        <Select
                            defaultValue="全部"
                            style={{
                                width: 250,
                            }}
                            onChange={handleTeacherSelectChange}
                            options={teacherList}
                        />
                    </div>
                    
                    <Button type='primary' onClick={filterCourseManagement} className='admin-course-management-filte-button'>筛选</Button>
                    <Button onClick={addNewCourse} className='admin-course-management-add-course-button'>添加新课程</Button>

                </div>

                <Table className='admin-course-management-table' dataSource={courseList} columns={columns} />
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
                        label="课程图片"
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
                            {newCourseIcon ? (
                                <img className='form-icon' src={newCourseIcon} alt="icon" style={{width: '100%',}}/>
                            ) : (
                                uploadButton
                            )}
                        </Upload>
                    </Form.Item>

                    <Form.Item
                        label="课程名"
                        name="subjectName"
                        rules={[{ required: true, message: '请输入课程名'}]}>
                        <div className='department-form-item-content'>
                            <Input placeholder='课程名' onChange={changeCourseNameInput} value={newCourseName}/>
                        </div>
                    </Form.Item>

                    <Form.Item
                        label="课程描述"
                        name="subjectDescription"
                    >
                        <div className='department-form-item-content'>
                            <TextArea placeholder='课程描述' onChange={changeCourseDescriptionInput} value={newCourseDescription}/>
                        </div>
                    </Form.Item>

                    {modalTitle == '添加新课程' && 
                    <div>
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
                                    onChange={handleNewCourseBelongDepartmentSelectChange}
                                    options={departmentList_}
                                />
                            </div>
                        </Form.Item>

                        <Form.Item
                            label="隶属学院"
                            name="belongto"
                            rules={[{ required: true, message: '请选择隶属专业'}]}>
                            <div className='department-form-item-content'>
                                <Select
                                    defaultValue="无"
                                    style={{
                                        width: 250,
                                    }}
                                    onChange={handleNewCourseBelongSubjectSelectChange}
                                    options={subjectList_}
                                />
                            </div>
                        </Form.Item>

                        <Form.Item
                            label="教学老师"
                            name="teachby"
                            rules={[{ required: true, message: '请选择教学老师'}]}>
                            <div className='department-form-item-content'>
                                <Select
                                    defaultValue="无"
                                    style={{
                                        width: 250,
                                    }}
                                    onChange={handleNewCourseTeacherSelectChange}
                                    options={teacherList}
                                />
                            </div>
                        </Form.Item>
                    </div>
                    }
                    
                </Form>
            </Modal>

        </div>
    )
}

export default Admin_CourseManagement