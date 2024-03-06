import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from "react-router-dom"

import "./student_ChooseClass_index.css"

const Student_ChooseClass = () => {
    return(
        <div>
            <Outlet/>
        </div>
    )
}

export default Student_ChooseClass