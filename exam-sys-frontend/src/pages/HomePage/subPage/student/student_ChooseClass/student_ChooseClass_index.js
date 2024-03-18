import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from "react-router-dom"

import "./student_ChooseClass_index.css"

const Student_ChooseClass = () => {
    return(
        <div className='root-div'>
            <Outlet/>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Student_ChooseClass