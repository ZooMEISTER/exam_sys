package com.zoom.exam_sys_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/24 22:10
 **/

@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

    /**
    * @Author: ZooMEISTER
    * @Description: 测试接口
    * @DateTime: 2024/1/24 22:11
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    public String SuperAdminControllerTest(){
        return "SuperAdmin controller test success";
    }
}
