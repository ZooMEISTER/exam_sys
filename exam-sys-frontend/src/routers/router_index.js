
import BasePage from "../pages/BasePage/basePage_index";
    import FrontPage from "../pages/FrontPage/frontPage_index"

    import HomePage from "../pages/HomePage/homePage_index";
        import Default_SubPage from "../pages/HomePage/defaultSubPage/default_sub_page";

        import Student_ChooseClass from "../pages/HomePage/subPage/student/student_ChooseClass/student_ChooseClass_index"
            import Student_AllDepartmentPage_index from "../pages/HomePage/subPage/student/student_ChooseClass/AllDepartmentPage/allDepartmentPage_index";
            import Student_AllSubjectPage_index from "../pages/HomePage/subPage/student/student_ChooseClass/AllSubjectPage/allSubjectPage_index";
            import Student_AllCoursePage_index from "../pages/HomePage/subPage/student/student_ChooseClass/AllCoursePage/allCoursePage_index";
            import Student_AllExamPage_index from "../pages/HomePage/subPage/student/student_ChooseClass/AllExamPage/allExamPage_index";
            import Student_ExamDetailPage_index from "../pages/HomePage/subPage/student/student_ChooseClass/ExamDetailPage/examDetailPage_index";
        import Student_MyClass from "../pages/HomePage/subPage/student/student_MyClass/student_MyClass_index"
        import Student_MyExam from "../pages/HomePage/subPage/student/student_MyExam/student_MyExam_index"

        import Teacher_OperateClass from "../pages/HomePage/subPage/teacher/teacher_OperateClass/teacher_OperateClass_index"
            import Teacher_AllDepartmentPage_index from "../pages/HomePage/subPage/teacher/teacher_OperateClass/AllDepartmentPage/allDepartmentPage_index";
            import Teacher_AllSubjectPage_index from "../pages/HomePage/subPage/teacher/teacher_OperateClass/AllSubjectPage/allSubjectPage_index";
            import Teacher_AllCoursePage_index from "../pages/HomePage/subPage/teacher/teacher_OperateClass/AllCoursePage/allCoursePage_index";
            import Teacher_AllExamPage_index from "../pages/HomePage/subPage/teacher/teacher_OperateClass/AllExamPage/allExamPage_index";
            import Teacher_ExamDetailPage_index from "../pages/HomePage/subPage/teacher/teacher_OperateClass/ExamDetailPage/examDetailPage_index";
        import Teacher_MyClass from "../pages/HomePage/subPage/teacher/teacher_MyClass/teacher_MyClass_index"
        import Teacher_MyExam from "../pages/HomePage/subPage/teacher/teacher_MyExam/teacher_MyExam_index"
        import Teacher_MyApplication from "../pages/HomePage/subPage/teacher/teacher_MyApplication/teacher_MyAppliation_index";

    import TeacherCorrectRespondentPage_index from "../pages/HomePage/subPage/teacher/teacher_CorrectRespondent/teacherCorrectRespondentPage_index";

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
                        index: true,
                        element: <Default_SubPage/>
                    },
                    {
                        path: "teacher-operate-class",
                        element: <Teacher_OperateClass/>,
                        children:[
                            {
                                index: true,
                                element: <Teacher_AllDepartmentPage_index/>
                            },
                            {
                                path: "subject",
                                element: <Teacher_AllSubjectPage_index/>
                            },
                            {
                                path: "course",
                                element: <Teacher_AllCoursePage_index/>
                            },
                            {
                                path: "exam",
                                element: <Teacher_AllExamPage_index/>
                            },
                            {
                                path: "exam-detail",
                                element: <Teacher_ExamDetailPage_index/>
                            }
                        ]
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
                        path: "teacher-my-application",
                        element: <Teacher_MyApplication/>
                    },

                    {   
                        path: "student-choose-class",
                        element: <Student_ChooseClass/>,
                        children:[
                            {
                                index: true,
                                element: <Student_AllDepartmentPage_index/>
                            },
                            {
                                path: "subject",
                                element: <Student_AllSubjectPage_index/>
                            },
                            {
                                path: "course",
                                element: <Student_AllCoursePage_index/>
                            },
                            {
                                path: "exam",
                                element: <Student_AllExamPage_index/>
                            },
                            {
                                path: "exam-detail",
                                element: <Student_ExamDetailPage_index/>
                            }
                        ]
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
                path: "correct-respondent",
                element: <TeacherCorrectRespondentPage_index/>
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