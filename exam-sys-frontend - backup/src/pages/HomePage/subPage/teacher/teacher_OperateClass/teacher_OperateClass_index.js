import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from "react-router-dom"

import { touristRequest, userRequest } from '../../../../../utils/request';

import "./teacher_OperateClass_index.css"

const Teacher_OperateClass = () => {

    
    return(
        <div className='root-div'>
            <Outlet/>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Teacher_OperateClass