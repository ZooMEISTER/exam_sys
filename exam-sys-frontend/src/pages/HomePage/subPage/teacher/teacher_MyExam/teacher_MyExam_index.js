import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { touristRequest, userRequest } from '../../../../../utils/request';

import { UserOutlined, FileTextOutlined } from '@ant-design/icons';

import { Card, Button, Tabs, List, Avatar, Radio, Segmented, Skeleton, Tag, Space } from 'antd';

import "./teacher_MyExam_index.css"

const Teacher_MyExam = () => {
    const navigate = useNavigate()

    const [allMyExamInfo, setAllMyExamInfo] = useState([])
    const [paginationSize, setPaginationSize] = useState(10)

    const userid = useSelector(state => state.userid.value)

    return(
        <div>
            <div>
                老师的考试
            </div>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Teacher_MyExam