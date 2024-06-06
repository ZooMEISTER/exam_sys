package com.zoom.exam_sys_backend.controller;

import com.zoom.exam_sys_backend.annotation.LogAnnotation;
import com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO;
import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
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

    private AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 测试接口
    * @DateTime: 2024/1/24 22:09
    * @Params: []
    * @Return java.lang.String
    */
    @GetMapping("/test")
    @LogAnnotation(description = "管理员测试接口")
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
    @LogAnnotation(description = "管理员更新个人信息接口")
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
    @LogAnnotation(description = "管理员获取所有学院简易信息接口")
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
    @LogAnnotation(description = "管理员获取指定学院下所有专业简易信息接口")
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
    @LogAnnotation(description = "管理员获取所有老师简易信息接口")
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
    @LogAnnotation(description = "管理员获取所有添加课程申请的接口")
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
    @LogAnnotation(description = "管理员筛选所有添加课程请求接口")
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
    @LogAnnotation(description = "管理员批准添加课程申请接口")
    public ResultVO AdminApproveAddCourseApplication(@RequestParam("applicationId") Long applicationId){
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
    @LogAnnotation(description = "管理员拒绝添加课程申请接口")
    public ResultVO AdminTurnDownAddCourseApplication(@RequestParam("applicationId") Long applicationId){
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
    @LogAnnotation(description = "管理员获取某个状态的成为老师的接口")
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
    @LogAnnotation(description = "管理员批准学生成为老师申请接口")
    public ResultVO AdminApproveToTeacherApplication(@RequestParam("applicationId") Long applicationId){
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
    @LogAnnotation(description = "管理员拒绝学生申请成为老师接口")
    public ResultVO AdminTurnDownToTeacherApplication(@RequestParam("applicationId") Long applicationId){
        return adminService.AdminTurnDownToTeacherApplication(applicationId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员搜索名称符合要求的学院接口
    * @DateTime: 2024/5/13 15:06
    * @Params: [searchStr]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.DepartmentVO>
    */
    @PostMapping("/search-department")
    @LogAnnotation(description = "管理员搜索名称符合要求的学院接口")
    public List<DepartmentVO> AdminSearchDepartment(@RequestParam("searchStr") String searchStr){
        return adminService.AdminSearchDepartment(searchStr);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员添加新学员接口
    * @DateTime: 2024/5/13 17:55
    * @Params: [newDepartmentName, newDepartmentIcon, newDepartmentDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/add-department")
    @LogAnnotation(description = "管理员添加新学员接口")
    public ResultVO AdminAddNewDepartment(@RequestParam("newDepartmentName") String newDepartmentName,
                                          @RequestParam("newDepartmentIcon") String newDepartmentIcon,
                                          @RequestParam("newDepartmentDescription") String newDepartmentDescription){
        return adminService.AdminAddNewDepartment(newDepartmentName, newDepartmentIcon, newDepartmentDescription);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新学院信息接口
    * @DateTime: 2024/5/13 17:09
    * @Params: [newDepartmentName, newDepartmentIcon, newDepartmentDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/update-department-info")
    @LogAnnotation(description = "管理员更新学院信息接口")
    public ResultVO AdminUpdateDepartmentInfo(@RequestParam("departmentId") Long departmentId,
                                              @RequestParam("newDepartmentName") String newDepartmentName,
                                              @RequestParam("newDepartmentIcon") String newDepartmentIcon,
                                              @RequestParam("newDepartmentDescription") String newDepartmentDescription){
        return adminService.AdminUpdateDepartmentInfo(departmentId, newDepartmentName, newDepartmentIcon, newDepartmentDescription);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员删除学院接口
    * @DateTime: 2024/5/13 18:24
    * @Params: [departmentId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/delete-department")
    @LogAnnotation(description = "管理员删除学院接口")
    public ResultVO AdminDeleteDepartment(@RequestParam("departmentId") Long departmentId){
        return adminService.AdminDeleteDepartment(departmentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取专业管理所有专业接口
    * @DateTime: 2024/5/14 0:15
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminSubjectManagementVO>
    */
    @PostMapping("/get-subject-management")
    @LogAnnotation(description = "管理员获取专业管理所有专业接口")
    public List<AdminSubjectManagementVO> AdminGetSubjectManagement(@RequestParam("departmentId") String departmentId){
        return adminService.AdminGetSubjectManagement(departmentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新专业信息接口
    * @DateTime: 2024/5/14 0:50
    * @Params: [subjectId, newSubjectIcon, newSubjectName, newSubjectDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/update-subject-info")
    @LogAnnotation(description = "管理员更新专业信息接口")
    public ResultVO AdminUpdateSubjectInfo(@RequestParam("subjectId") Long subjectId,
                                           @RequestParam("newSubjectIcon") String newSubjectIcon,
                                           @RequestParam("newSubjectName") String newSubjectName,
                                           @RequestParam("newSubjectDescription") String newSubjectDescription){
        return adminService.AdminUpdateSubjectInfo(subjectId, newSubjectIcon, newSubjectName, newSubjectDescription);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员添加新专业接口
    * @DateTime: 2024/5/14 1:11
    * @Params: [belongto, newSubjectIcon, newSubjectName, newSubjectDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/add-subject")
    @LogAnnotation(description = "管理员添加新专业接口")
    public ResultVO AdminAddNewSubject(@RequestParam("belongto") Long belongto,
                                       @RequestParam("newSubjectIcon") String newSubjectIcon,
                                       @RequestParam("newSubjectName") String newSubjectName,
                                       @RequestParam("newSubjectDescription") String newSubjectDescription){
        return adminService.AdminAddNewSubject(belongto, newSubjectIcon, newSubjectName, newSubjectDescription);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员删除专业接口
    * @DateTime: 2024/5/14 1:25
    * @Params: [subjectId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/delete-subject")
    @LogAnnotation(description = "管理员删除专业接口")
    public ResultVO AdminDeleteSubject(@RequestParam("subjectId") Long subjectId){
        return adminService.AdminDeleteSubject(subjectId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员根据条件筛选课程接口
    * @DateTime: 2024/5/14 21:35
    * @Params: [departmentId, subjectId, teacherId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminCourseManagementVO>
    */
    @PostMapping("/get-courses")
    @LogAnnotation(description = "管理员根据条件筛选课程接口")
    public List<AdminCourseManagementVO> AdminGetCourseByConditions(@RequestParam("departmentId") String departmentId,
                                                                    @RequestParam("subjectId") String subjectId,
                                                                    @RequestParam("teacherId") String teacherId){
        return adminService.AdminGetCourseByConditions(departmentId, subjectId, teacherId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新课程信息接口
    * @DateTime: 2024/5/15 17:30
    * @Params: [courseId, newCourseIcon, newCourseName, newCourseDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/update-course-info")
    @LogAnnotation(description = "管理员更新课程信息接口")
    public ResultVO AdminUpdateCourseInfo(@RequestParam("courseId") Long courseId,
                                          @RequestParam("newCourseIcon") String newCourseIcon,
                                          @RequestParam("newCourseName") String newCourseName,
                                          @RequestParam("newCourseDescription") String newCourseDescription){
    return adminService.AdminUpdateCourseInfo(courseId, newCourseIcon, newCourseName, newCourseDescription);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员添加课程接口
    * @DateTime: 2024/5/15 17:30
    * @Params: [belongto, newCourseIcon, newCourseName, newCourseDescription, teachby]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/add-course")
    @LogAnnotation(description = "管理员添加课程接口")
    public ResultVO AdminAddNewCourse(@RequestParam("belongto") Long belongto,
                                      @RequestParam("newCourseIcon") String newCourseIcon,
                                      @RequestParam("newCourseName") String newCourseName,
                                      @RequestParam("newCourseDescription") String newCourseDescription,
                                      @RequestParam("teacherId") Long teachby){
        return adminService.AdminAddNewCourse(belongto, newCourseIcon, newCourseName, newCourseDescription, teachby);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员删除课程方法
    * @DateTime: 2024/5/16 12:07
    * @Params: [courseId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/delete-course")
    @LogAnnotation(description = "管理员删除课程方法")
    public ResultVO AdminDeleteCourse(@RequestParam("courseId") Long courseId){
        return adminService.AdminDeleteCourse(courseId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取老师用户信息接口
    * @DateTime: 2024/5/18 16:36
    * @Params: [searchStr]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.TeacherVO>
    */
    @PostMapping("/get-teacher")
    @LogAnnotation(description = "管理员获取老师用户信息接口")
    public List<TeacherVO> AdminGetTeacherInfo(@RequestParam("searchStr") String searchStr){
        return adminService.AdminGetTeacherInfo(searchStr);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新教师用户信息接口
    * @DateTime: 2024/5/18 17:28
    * @Params: [curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail, resetPwd]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/update-teacher-info")
    @LogAnnotation(description = "管理员更新教师用户信息接口")
    public ResultVO AdminUpdateTeacherInfo(@RequestParam("curUserId") Long curUserId,
                                           @RequestParam("newAvatar") String newAvatar,
                                           @RequestParam("newUsername") String newUsername,
                                           @RequestParam("newRealname") String newRealname,
                                           @RequestParam("newPhone") String newPhone,
                                           @RequestParam("newEmail") String newEmail,
                                           @RequestParam("resetPwd") Boolean resetPwd){
        return adminService.AdminUpdateTeacherInfo(curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail, resetPwd);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取学生用户信息接口
    * @DateTime: 2024/5/18 17:11
    * @Params: [searchStr]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.StudentVO>
    */
    @PostMapping("/get-student")
    @LogAnnotation(description = "管理员获取学生用户信息接口")
    public List<StudentVO> AdminGetStudentInfo(@RequestParam("searchStr") String searchStr){
        return adminService.AdminGetStudentInfo(searchStr);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新学生用户信息接口
    * @DateTime: 2024/5/18 17:29
    * @Params: [curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail, resetPwd]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @PostMapping("/update-student-info")
    @LogAnnotation(description = "管理员更新学生用户信息接口")
    public ResultVO AdminUpdateStudentInfo(@RequestParam("curUserId") Long curUserId,
                                           @RequestParam("newAvatar") String newAvatar,
                                           @RequestParam("newUsername") String newUsername,
                                           @RequestParam("newRealname") String newRealname,
                                           @RequestParam("newPhone") String newPhone,
                                           @RequestParam("newEmail") String newEmail,
                                           @RequestParam("resetPwd") Boolean resetPwd){
        return adminService.AdminUpdateStudentInfo(curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail, resetPwd);
    }
}
