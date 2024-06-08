import React, { useState, useEffect } from 'react';

import Department_Component from '../../../../../../components/DepartmentComponnet/department_component';

import { touristRequest, userRequest } from '../../../../../../utils/request';

import { Breadcrumb } from 'antd';

import "./allDepartmentPage_index.css"

const Teacher_AllDepartmentPage_index = () => {
    const [allDepartmentInfo, setAllDepartmentInfo] = useState([])

    // 获取数据库中所有学院的数据
    const getAllDepartment = () => {
        userRequest.get("/teacher/get-all-department")
        .then( function(response) {
            console.log(response)
            setAllDepartmentInfo(response)
        })
        .catch( function (error) {
            console.log(error)
        })
    }

    // 组件加载时自动执行
    useEffect(() => {
        getAllDepartment()
	}, []);

    return(
        <div>
            {/* 先是一个面包屑 */}
            <Breadcrumb 
                className='navigate-breadcrumb'
                items={[
                    {title: "所有学院"}
                ]}
            />
            {/* 在下面显示所有的学院 */}
            <div className='show-all-department-div'>
                {allDepartmentInfo.map(item => (
                    <Department_Component
                        key={item.id}
                        id={item.id}
                        icon={item.icon}
                        name={item.name}
                        description={item.description}
                        subject_count={item.subject_count}/>
                ))}
                
            </div>
        </div>
    )
}

export default Teacher_AllDepartmentPage_index