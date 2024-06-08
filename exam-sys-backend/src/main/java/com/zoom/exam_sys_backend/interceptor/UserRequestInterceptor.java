package com.zoom.exam_sys_backend.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.constant.ExamSysConstants;
import com.zoom.exam_sys_backend.exception.InvalidProfilevException;
import com.zoom.exam_sys_backend.exception.NoPermissionException;
import com.zoom.exam_sys_backend.exception.UserNotExistException;
import com.zoom.exam_sys_backend.exception.code.InterceptorResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.service.TouristService;
import com.zoom.exam_sys_backend.util.JWTUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author ZooMEISTER
 * @Description: 用户请求拦截器，对用户请求进行鉴权
 * @DateTime 2024/1/23 22:12
 **/

@Component
public class UserRequestInterceptor implements HandlerInterceptor {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TouristService touristService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果请求是浏览器预检，直接放行
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        // 获取请求路径
        String requestPath = request.getServletPath();
        // 期望的权限等级
        int expectPermissionLevel = -1;
        // 实际的权限等级
        int actualPermissionLevel = -1;

        if (requestPath.startsWith("/respondent")) {
            return true;
        }

        if(requestPath.startsWith("/superadmin")){
            // 超级管理员 请求
            expectPermissionLevel = ExamSysConstants.SUPER_ADMIN_PERMISSION_LEVEL;
        }
        else if(requestPath.startsWith("/admin")){
            // 管理员 请求
            expectPermissionLevel = ExamSysConstants.ADMIN_PERMISSION_LEVEL;
        }
        else if(requestPath.startsWith("/teacher") || requestPath.startsWith("/proxy/teacher")){
            // 老师 请求
            expectPermissionLevel = ExamSysConstants.TEACHER_PERMISSION_LEVEL;
        }
        else if(requestPath.startsWith("/student") || requestPath.startsWith("/proxy/student")){
            // 学生 请求
            expectPermissionLevel = ExamSysConstants.STUDENT_PERMISSION_LEVEL;
        }
        else if(requestPath.startsWith("/tourist")){
            // 游客 请求
            expectPermissionLevel = ExamSysConstants.TOURIST_PERMISSION_LEVEL;
        }

        // 判断发送的请求是否需要鉴权
        if(expectPermissionLevel > 0 && request.getHeader("Authorization") != null){
            // 获取请求头中的 token
            //String token = request.getHeader("exam-sys-login-token");
            String token = request.getHeader("Authorization").substring(7);
            try{
                // 解析 token，获得 userid 和 profilev
                Jws<Claims> claimsJws = JWTUtils.parseClaim(token);
                String userid = String.valueOf(claimsJws.getPayload().get("userid"));
                int profilev = Integer.parseInt(String.valueOf(claimsJws.getPayload().get("profilev")));
                // 用 userid 去 Redis 中查询用户的 profilev，并比对是否相同
                TouristLoginResultVO requestSenderVO = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(userid))), TouristLoginResultVO.class);
                if(requestSenderVO != null){
                    // 先判断个人信息版本是否正确
                    if(requestSenderVO.getProfilev() != profilev){
                        // 个人信息版本错误
                        throw new InvalidProfilevException();
                    }
                    else{
                        actualPermissionLevel = requestSenderVO.getPermissionLevel();
                    }
                }
                else{
                    // 若 Redis 中没有，则去 Mysql 中查询
                    requestSenderVO = touristService.SearchUserByUserid(Long.valueOf(userid), token);
                    if(requestSenderVO.getResultCode() == TouristResultCode.TOURIST_LOGIN_FAIL_USER_NOT_EXIST){
                        // 用户不存在
                        throw new UserNotExistException();
                    }
                    else{
                        // 用户存在，将用户信息记录加入 Redis
                        redisTemplate.opsForValue().set(String.valueOf(requestSenderVO.getUserid()), JSONObject.toJSONString(requestSenderVO));
                        // 用户存在，先比对用户信息版本
                        if(requestSenderVO.getProfilev() != profilev){
                            // 个人信息版本错误
                            throw new InvalidProfilevException();
                        }
                        else{
                            // 获取用户的实际权限等级
                            actualPermissionLevel = requestSenderVO.getPermissionLevel();
                        }
                    }
                }

                // 比较期望用户权限等级与实际用户权限等级
                if(actualPermissionLevel < expectPermissionLevel){
                    // 权限不够
                    throw new NoPermissionException();
                }
                // 验证全部通过 放行
                return true;
            }
            catch (Exception e){
                // 返回给前端的 JSON
                JSONObject jsonObject = new JSONObject();

                if(e instanceof SignatureException){
                    // token 无效
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_INVALID_TOKEN);
                    jsonObject.put("msg", "token 无效");
                }
                else if(e instanceof InvalidProfilevException){
                    // 个人信息版本错误
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_INVALID_PROFILEV);
                    jsonObject.put("msg", "个人信息版本错误");
                }
                else if(e instanceof UserNotExistException){
                    // 用户不存在
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_USER_NOT_EXIST);
                    jsonObject.put("msg", "用户不存在");
                }
                else if(e instanceof NoPermissionException){
                    // 没有权限
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_NO_PERMISSION);
                    jsonObject.put("msg", "没有权限");
                }
                else if(e instanceof ExpiredJwtException){
                    // token过期
                    jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_TOKEN_EXPIRED);
                    jsonObject.put("msg", "TOKEN 已过期");
                }

                String jsonObjectStr = JSONObject.toJSONString(jsonObject);
                ReturnJson(response, jsonObjectStr);

                return false;
            }
        }
        else if(expectPermissionLevel == 0){
            // 目前 tourist 开头的请求直接放行
            // 之后可能会做 ip 黑名单，防止某 ip 恶意访问
            return true;
        }
        else{
            // 非法请求，直接拦截
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resultCode", InterceptorResultCode.INTERCEPTED_ILLEGAL_REQUEST);
            jsonObject.put("msg", "非法请求");
            String jsonObjectStr = JSONObject.toJSONString(jsonObject);
            ReturnJson(response, jsonObjectStr);

            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 拦截器的返回JSON数据方法
    * @DateTime: 2024/1/24 22:20
    * @Params:
    * @Return
    */
    private void ReturnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
