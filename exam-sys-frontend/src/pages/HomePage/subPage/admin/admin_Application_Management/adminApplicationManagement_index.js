import { Outlet, useNavigate } from "react-router-dom"

const Admin_ApplicationManagement = () => {

    return(
        <div className="root-div">
            <Outlet/>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Admin_ApplicationManagement