import { Outlet, useNavigate } from "react-router-dom"

const Admin_UserManagement = () => {

    return(
        <div className="root-div">
            <Outlet/>
            <div className='btm-spc-div'></div>
        </div>
    )
}

export default Admin_UserManagement