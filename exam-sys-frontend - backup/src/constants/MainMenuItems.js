// 未登录时的顶部菜单
const mainMenuItems_NoLogin = [
    {
        label: (
            "首页"
        ),
        key: "/"
    },
    {
        label: (
            "考试系统"
        ),
        key: "/home"
    },
    {
        label: "我",
        key: "me",
        children:[{
            label: (
                "登录"
            ),
            key: "/login",
        },
        {
            label: (
                "注册"
            ),
            key: "/register",
        }]
    }
]

// 已登陆后的顶部菜单
const mainMenuItems_Logged = [
    {
        label: (
            "首页"
        ),
        key: "/"
    },
    {
        label: (
            "考试系统"
        ),
        key: "/home"
    },
    {
        label: "我",
        key: "me",
        children:[
            {
                label: (
                    "个人资料"
                ),
                key: "/profile",
            },
            {
            label: (
                "退出登录"
            ),
            key: "logout",
        }]
    }
]


export { mainMenuItems_NoLogin, mainMenuItems_Logged }