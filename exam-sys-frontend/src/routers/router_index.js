import BasePage from "../pages/BasePage/basePage_index";
import HomePage from "../pages/HomePage/homePage_index";
import LoginPage from "../pages/LoginPage/loginPage_index";
import RegisterPage from "../pages/RegisterPage/registerPage_index";

import { createBrowserRouter } from 'react-router-dom';

// 1.创建router实例对象，并配置路由对应关系
const router = createBrowserRouter([
    {
        path: "/",
        element: <BasePage/>,
        children: [
            {
                index: true,
                element: <HomePage/>
            },
            {
                path: "login",
                element: <LoginPage/>
            },
            {
                path: "register",
                element: <RegisterPage/>
            }
        ]
    },
    
])

export default router