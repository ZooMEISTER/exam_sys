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


export { homePageMenuItems_Teacher, homePageMenuItems_Student }