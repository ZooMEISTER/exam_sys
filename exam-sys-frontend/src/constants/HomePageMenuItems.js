const homePageMenuItems_Teacher = [
    {
        label: (
            "课程"
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
        }]
    }
]

const homePageMenuItems_Student = [
    {
        label: (
            "选课"
        ),
        key: "/home/student-choose-class"
    },
    {
        label: "我的",
        key: "me_Student",
        children:[{
            label: (
                "我的课程"
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


export { homePageMenuItems_Teacher, homePageMenuItems_Student }