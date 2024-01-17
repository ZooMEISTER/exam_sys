const mainMenuItems_NoLogin = [{
    label: (
        <a href="/">
            考试系统
        </a>
    ),
    key: "front-page"
},
{
    label: "我",
    key: "me",
    children:[{
        label: (
            <a href="/login">
                登录
            </a>
        ),
        key: "login",
    },
    {
        label: (
            <a href="/register">
                注册
            </a>
        ),
        key: "register",
    }]
}
]

export default mainMenuItems_NoLogin