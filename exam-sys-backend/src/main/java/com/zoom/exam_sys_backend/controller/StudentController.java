package com.zoom.exam_sys_backend.controller;

import com.zoom.exam_sys_backend.pojo.vo.CourseVO;
import com.zoom.exam_sys_backend.pojo.vo.DepartmentVO;
import com.zoom.exam_sys_backend.pojo.vo.SubjectVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/24 22:02
 **/

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

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

    /**
    * @Author: ZooMEISTER
    * @Description: 学生更新用户信息接口
    * @DateTime: 2024/2/14 11:46
    * @Params: [userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @PostMapping("/update-profile")
    public TouristLoginResultVO StudentUpdateProfile(@RequestParam("userid") Long userid,
                                                     @RequestParam("avatar") String newAvatar,
                                                     @RequestParam("username") String newUsername,
                                                     @RequestParam("realname") String newRealname,
                                                     @RequestParam("phone") String newPhone,
                                                     @RequestParam("email") String newEmail,
                                                     @RequestParam("password") String newPassword){
        // 调用service接口更新数据库里用户信息
        return studentService.StudentUpdateProfile(userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取所有学院信息接口
    * @DateTime: 2024/3/6 18:13
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.DepartmentVO>
    */
    @GetMapping("/get-all-department")
    public List<DepartmentVO> StudentGetAllDepartment(){
        return studentService.StudentGetAllDepartment();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个学院下所有专业接口
    * @DateTime: 2024/3/6 18:15
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SubjectVO>
    */
    @PostMapping("/get-all-subject")
    public List<SubjectVO> StudentGetAllSubject(@RequestParam("departmentId") Long departmentId){
        return studentService.StudentGetAllSubject(departmentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个专业下所有课程接口
    * @DateTime: 2024/3/6 18:16
    * @Params: [subjectId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.CourseVO>
    */
    @PostMapping("/get-all-course")
    public List<CourseVO> StudentGetAllCourse(@RequestParam("subjectId") Long subjectId){
        return studentService.StudentGetAllCourse(subjectId);
    }
}
