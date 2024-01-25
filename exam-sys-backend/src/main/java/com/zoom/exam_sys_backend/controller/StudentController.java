package com.zoom.exam_sys_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/24 22:02
 **/

@RestController
@RequestMapping("/student")
public class StudentController {

    /**
    * @Author: ZooMEISTER
    * @Description: 测试接口
    * @DateTime: 2024/1/24 22:06
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    public String StudentControllerTest(){
        return "Student controller test success";
    }
}
