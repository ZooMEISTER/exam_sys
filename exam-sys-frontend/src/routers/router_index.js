import FrontPage from "../pages/FrontPage/frontPage_index"

import BasePage from "../pages/BasePage/basePage_index";

import HomePage from "../pages/HomePage/homePage_index";
    import Student_ChooseClass from "../pages/HomePage/subPage/student/student_ChooseClass/student_ChooseClass_index"
    import Student_MyClass from "../pages/HomePage/subPage/student/student_MyClass/student_MyClass_index"
    import Student_MyExam from "../pages/HomePage/subPage/student/student_MyExam/student_MyExam_index"
    import Teacher_OperateClass from "../pages/HomePage/subPage/teacher/teacher_OperateClass/teacher_OperateClass_index"
    import Teacher_MyClass from "../pages/HomePage/subPage/teacher/teacher_MyClass/teacher_MyClass_index"
    import Teacher_MyExam from "../pages/HomePage/subPage/teacher/teacher_MyExam/teacher_MyExam_index"

import LoginPage from "../pages/LoginPage/loginPage_index";

import RegisterPage from "../pages/RegisterPage/registerPage_index";

import ProfilePage from "../pages/ProfilePage/profilePage_index";

import { createBrowserRouter } from 'react-router-dom';

// 1.创建router实例对象，并配置路由对应关系
const router = createBrowserRouter([
    {
        path: "/",
        element: <BasePage/>,
        children: [
            {
                index: true,
                element: <FrontPage/>
            },
            {   
                path: "home",
                element: <HomePage/>,
                children: [
                    {
                        path: "teacher-operate-class",
                        element: <Teacher_OperateClass/>
                    },
                    {
                        path: "teacher-my-class",
                        element: <Teacher_MyClass/>
                    },
                    {
                        path: "teacher-my-exam",
                        element: <Teacher_MyExam/>
                    },

                    {   
                        path: "student-choose-class",
                        element: <Student_ChooseClass/>
                    },
                    {   
                        path: "student-my-class",
                        element: <Student_MyClass/>
                    },
                    {   
                        path: "student-my-exam",
                        element: <Student_MyExam/>
                    }
                ]
            },
            {
                path: "login",
                element: <LoginPage/>
            },
            {
                path: "register",
                element: <RegisterPage/>
            },
            {
                path: "profile",
                element: <ProfilePage/>
            }
        ]
    },
    
])

export default router