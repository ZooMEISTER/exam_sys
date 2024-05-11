package com.zoom.exam_sys_backend.controller;

import com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO;
import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/24 22:08
 **/

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
    * @Author: ZooMEISTER
    * @Description: 测试接口
    * @DateTime: 2024/1/24 22:09
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    public String AdminControllerTest(){
        return "Admin controller test success";
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新个人信息接口
    * @DateTime: 2024/4/21 4:07
    * @Params: [userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @PostMapping("/update-profile")
    public TouristLoginResultVO AdminUpdateProfile(@RequestParam("userid") Long userid,
                                                     @RequestParam("avatar") String newAvatar,
                                                     @RequestParam("username") String newUsername,
                                                     @RequestParam("realname") String newRealname,
                                                     @RequestParam("phone") String newPhone,
                                                     @RequestParam("email") String newEmail,
                                                     @RequestParam("password") String newPassword){
        return adminService.AdminUpdateProfile(userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取所有学院简易信息接口
    * @DateTime: 2024/5/11 17:24
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SimpleObjectInfo>
    */
    @GetMapping("/get-all-department-simple-info")
    public List<SimpleObjectInfo> AdminGetAllSimpleDepartmentInfo(){
        return adminService.AdminGetAllSimpleDepartmentInfo();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取指定学院下所有专业简易信息接口
    * @DateTime: 2024/5/11 17:28
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SimpleObjectInfo>
    */
    @PostMapping("/get-subject-simple-info")
    public List<SimpleObjectInfo> AdminGetSimpleSubjectInfo(@RequestParam("departmentId") Long departmentId){
        return adminService.AdminGetSimpleSubjectInfo(departmentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取所有老师简易信息接口
    * @DateTime: 2024/5/11 17:28
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SimpleObjectInfo>
    */
    @GetMapping("/get-all-teacher-simple-info")
    public List<SimpleObjectInfo> AdminGetAllSimpleTeacherInfo(){
        return adminService.AdminGetAllSimpleTeacherInfo();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取所有添加课程申请的接口
    * @DateTime: 2024/5/8 12:44
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO>
    */
    @GetMapping("/get-all-add-course-application")
    public List<AdminAddCourseApplicationVO> AdminGetAllAddCourseApplication(){
        return adminService.AdminGetAllAddCourseApplication();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员筛选所有添加课程请求接口
    * @DateTime: 2024/5/11 18:01
    * @Params: [departmentId, subjectId, teacherId, applicationStatus]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminAddCourseApplicationVO>
    */
    @PostMapping("/get-add-course-application")
    public List<AdminAddCourseApplicationVO> AdminGetAddCourseApplication(@RequestParam("departmentId") String departmentId,
                                                                          @RequestParam("subjectId") String subjectId,
                                                                          @RequestParam("teacherId") String teacherId,
                                                                          @RequestParam("applicationStatus") int applicationStatus){
        return adminService.AdminGetAddCourseApplication(departmentId, subjectId, teacherId, applicationStatus);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员批准添加课程申请接口
    * @DateTime: 2024/5/8 14:38
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @PostMapping("/add-course-application-approve")
    public AdminApplicationManageResultVO AdminApproveAddCourseApplication(@RequestParam("applicationId") Long applicationId){
        return adminService.AdminApproveAddCourseApplication(applicationId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员拒绝添加课程申请接口
    * @DateTime: 2024/5/8 14:35
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @PostMapping("/add-course-application-decline")
    public AdminApplicationManageResultVO AdminTurnDownAddCourseApplication(@RequestParam("applicationId") Long applicationId){
        return adminService.AdminTurnDownAddCourseApplication(applicationId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取某个状态的成为老师的接口
    * @DateTime: 2024/5/11 20:39
    * @Params: [applicationStatus]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminToTeacherApplicationVO>
    */
    @PostMapping("/get-be-teacher-application")
    public List<AdminToTeacherApplicationVO> AdminGetBeTeacherApplication(@RequestParam("applicationStatus") int applicationStatus){
        return adminService.AdminGetBeTeacherApplication(applicationStatus);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员批准学生成为老师申请接口
    * @DateTime: 2024/5/11 22:09
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @PostMapping("/to-teacher-application-approve")
    public AdminApplicationManageResultVO AdminApproveToTeacherApplication(@RequestParam("applicationId") Long applicationId){
        return adminService.AdminApproveToTeacherApplication(applicationId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员拒绝学生申请成为老师接口
    * @DateTime: 2024/5/11 22:09
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @PostMapping("/to-teacher-application-decline")
    public AdminApplicationManageResultVO AdminTurnDownToTeacherApplication(@RequestParam("applicationId") Long applicationId){
        return adminService.AdminTurnDownToTeacherApplication(applicationId);
    }
}
