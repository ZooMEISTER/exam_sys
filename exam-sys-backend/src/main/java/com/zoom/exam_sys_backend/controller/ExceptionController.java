package com.zoom.exam_sys_backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZooMEISTER
 * @Description: 全局异常处理
 * @DateTime 2024/3/16 20:56
 **/

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map globalException(HttpServletRequest request, Exception e) {
        Map<String,Object> map = new HashMap<>();
        map.put("code",500);
        map.put("message",e.getMessage());
        return map;
    }
}
