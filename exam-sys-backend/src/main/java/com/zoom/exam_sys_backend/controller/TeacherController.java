package com.zoom.exam_sys_backend.controller;

import com.zoom.exam_sys_backend.pojo.po.CoursePO;
import com.zoom.exam_sys_backend.pojo.po.DepartmentPO;
import com.zoom.exam_sys_backend.pojo.po.SubjectPO;
import com.zoom.exam_sys_backend.pojo.vo.CourseVO;
import com.zoom.exam_sys_backend.pojo.vo.DepartmentVO;
import com.zoom.exam_sys_backend.pojo.vo.SubjectVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
    * @Author: ZooMEISTER
    * @Description: 老师角色获取所有学院的数据
    * @DateTime: 2024/2/15 17:50
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.DepartmentPO>
    */
    @GetMapping("/get-all-department")
    public List<DepartmentVO> TeacherGetAllDepartment(){
        return teacherService.TeacherGetAllDepartment();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师角色获取某个学院下的所有专业
    * @DateTime: 2024/2/15 17:53
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.SubjectPO>
    */
    @PostMapping("/get-all-subject")
    public List<SubjectVO> TeacherGetAllSubject(@RequestParam("departmentId") Long departmentId){
        return teacherService.TeacherGetAllSubject(departmentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师角色获取某个专业的所有课程
    * @DateTime: 2024/2/15 17:54
    * @Params: [subjectId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.CoursePO>
    */
    @PostMapping("/get-all-course")
    public List<CourseVO> TeacherGetAllCourse(@RequestParam("subjectId") Long subjectId){
        return teacherService.TeacherGetAllCourse(subjectId);
    }

}
