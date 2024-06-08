// axios的封装处理
import axios from "axios";

// 1.根域名配置
// 2.超时时间
// 3.请求拦截器 / 响应拦截器


// 游客request，仅用于注册和登录
const touristRequest = axios.create({
    baseURL: "http://localhost:3001",
    timeout: 5000,
    headers: { 'content-type': 'application/x-www-form-urlencoded' }
})



// 已登陆用户的request
// 需要带上jwt token
const userRequest = axios.create({
    baseURL: "http://localhost:3001",
    timeout: 5000,
    headers: { 'content-type': 'application/x-www-form-urlencoded' }
})

// 添加请求拦截器
userRequest.interceptors.request.use((config) => {
    // 在请求发送之前会触发这里
    const token = localStorage.getItem("exam-sys-login-token")
    if(token){
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
}, (error) => { 
    return Promise.reject(error)
})

// 添加响应拦截器
userRequest.interceptors.response.use((response) => {
    // 2XX 范围的状态码会触发这里
    // 对响应做点什么
    return response.data
}, (error) => {
    // 超出 2XX 范围的状态码会触发这里
    // 对响应做点什么
    return Promise.reject(error)
})



// 已登陆用户的上传文件的request
// 需要带上jwt token
const userFileUploadRequest = axios.create({
    baseURL: "http://localhost:3001",
    timeout: 20000,
    headers: { 'content-type': 'multipart/form-data' }
})

// 添加请求拦截器
userFileUploadRequest.interceptors.request.use((config) => {
    // 在请求发送之前会触发这里
    const token = localStorage.getItem("exam-sys-login-token")
    if(token){
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
}, (error) => { 
    return Promise.reject(error)
})

// 添加响应拦截器
userFileUploadRequest.interceptors.response.use((response) => {
    // 2XX 范围的状态码会触发这里
    // 对响应做点什么
    return response.data
}, (error) => {
    // 超出 2XX 范围的状态码会触发这里
    // 对响应做点什么
    return Promise.reject(error)
})



// 已登陆用户的下载文件的request
// 需要带上jwt token
const userFileDownloadRequest = axios.create({
    baseURL: "http://localhost:3001",
    timeout: 20000,
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    // responseType: "blob"
})

// 添加请求拦截器
userFileDownloadRequest.interceptors.request.use((config) => {
    // 在请求发送之前会触发这里
    const token = localStorage.getItem("exam-sys-login-token")
    if(token){
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
}, (error) => { 
    return Promise.reject(error)
})

// 添加响应拦截器
userFileDownloadRequest.interceptors.response.use((response) => {
    // 2XX 范围的状态码会触发这里
    // 对响应做点什么
    return response
}, (error) => {
    // 超出 2XX 范围的状态码会触发这里
    // 对响应做点什么
    return Promise.reject(error)
})


export { touristRequest, userRequest, userFileUploadRequest, userFileDownloadRequest }