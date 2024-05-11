
const homePageMenuItems_Admin = [
    {
        label: (
            "学院管理"
        ),
        key: "/home/admin-department-management"
    },
    {
        label: (
            "专业管理"
        ),
        key: "/home/admin-subject-management"
    },
    {
        label: (
            "课程管理"
        ),
        key: "/home/admin-course-management"
    },
    {
        label: (
            "考试管理"
        ),
        key: "/home/admin-exam-management"
    },
    {
        label: (
            "申请管理"
        ),
        key: "/home/admin-application-management",
        children: [
            {
                label: (
                    "添加课程申请"
                ),
                key: "/home/admin-application-management/add-course-application"
            },
            {
                label: (
                    "成为老师申请"
                ),
                key: "/home/admin-application-management/be-teacher-application"
            }
        ]
    }
]

const homePageMenuItems_Teacher = [
    {
        label: (
            "所有学院"
        ),
        key: "/home/teacher-operate-class"
    },
    {
        label: "我的",
        key: "me_Teacher",
        children:[{
            label: (
                "我的课程"
            ),
            key: "/home/teacher-my-class",
        },
        {
            label: (
                "我的考试"
            ),
            key: "/home/teacher-my-exam",
        },
        {
            label: (
                "我的申请"
            ),
            key: "/home/teacher-my-application",
        }]
    }
]

const homePageMenuItems_Student = [
    {
        label: (
            "所有学院"
        ),
        key: "/home/student-choose-class"
    },
    {
        label: "我的",
        key: "me_Student",
        children:[{
            label: (
                "我参加的课程"
            ),
            key: "/home/student-my-class",
        },
        {
            label: (
                "我的考试"
            ),
            key: "/home/student-my-exam",
        }]
    }
]


export { homePageMenuItems_Admin, homePageMenuItems_Teacher, homePageMenuItems_Student }