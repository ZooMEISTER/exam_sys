package com.zoom.exam_sys_backend.controller;


/**
 *  @Author ZooMEISTER
 *  @Description: TODO
 *  @DateTime 2024/1/1 11:09
 **/

import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristRegisterResultVO;
import com.zoom.exam_sys_backend.service.TouristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tourist")
public class TouristController {

    @Autowired
    private TouristService touristService;


    /**
    * @Author: ZooMEISTER
    * @Description: http://localhost:3001/tourist/test 测试用
    * @DateTime: 2024/1/1 12:49
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    public String TouristControllerTest(){
        return "Tourist controller test success";
    }


    /**
    * @Author: ZooMEISTER
    * @Description: http://localhost:3001/tourist/register 游客注册
    * @DateTime: 2024/1/1 11:50
    * @Params: [avatar, username, password]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristVO
    */
    @PostMapping("/register")
    public TouristRegisterResultVO TouristRegister(@RequestParam("avatar") String avatar,
                                                   @RequestParam("username") String username,
                                                   @RequestParam("password") String password
    ){
        return touristService.InsertRegisterTourist(avatar, username, password);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: http://localhost:3001/tourist/register/login 游客登陆
    * @DateTime: 2024/1/16 20:05
    * @Params: []
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginVO
    */
    @PostMapping("/login")
    public TouristLoginResultVO TouristLogin(@RequestParam("username") String username,
                                             @RequestParam("password") String password
    ){
        return touristService.TouristLogin(username, password);
    }
}
