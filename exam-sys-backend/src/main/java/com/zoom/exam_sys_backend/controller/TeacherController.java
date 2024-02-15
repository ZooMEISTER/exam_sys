package com.zoom.exam_sys_backend.controller;

import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/24 22:06
 **/

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    /**
    * @Author: ZooMEISTER
    * @Description: 测试接口
    * @DateTime: 2024/1/24 22:07
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    public String TeacherControllerTest(){
        return "Teacher controller test success";
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师修改个人信息接口
    * @DateTime: 2024/2/14 14:48
    * @Params: [userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @PostMapping("/update-profile")
    public TouristLoginResultVO TeacherUpdateProfile(@RequestParam("userid") Long userid,
                                                     @RequestParam("avatar") String newAvatar,
                                                     @RequestParam("username") String newUsername,
                                                     @RequestParam("realname") String newRealname,
                                                     @RequestParam("phone") String newPhone,
                                                     @RequestParam("email") String newEmail,
                                                     @RequestParam("password") String newPassword){
        return teacherService.TeacherUpdateProfile(userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword);
    }
}
