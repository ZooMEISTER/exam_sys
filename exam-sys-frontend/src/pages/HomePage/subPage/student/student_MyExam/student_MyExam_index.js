import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userRequest } from '../../../../../utils/request';

import { UserOutlined, FileTextOutlined } from '@ant-design/icons';

import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, Space } from 'antd';

import "./student_MyExam_index.css"


const Student_MyExam = () => {
    const navigate = useNavigate()

    const [allMyExamInfo, setAllMyExamInfo] = useState([])
    const [paginationSize, setPaginationSize] = useState(10)


    const userid = useSelector(state => state.userid.value)

    
    return(
        <div>
            <div>
                学生的考试
            </div>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Student_MyExam