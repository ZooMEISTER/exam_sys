package com.zoom.exam_sys_backend.controller;

import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
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
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.DepartmentVO>
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
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.SubjectVO>
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
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.CourseVO>
    */
    @PostMapping("/get-all-course")
    public List<CourseVO> TeacherGetAllCourse(@RequestParam("subjectId") Long subjectId){
        return teacherService.TeacherGetAllCourse(subjectId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个课程下的所有考试接口
    * @DateTime: 2024/3/14 14:50
    * @Params: [courseId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.ExamVO>
    */
    @PostMapping("/get-all-exam")
    public List<ExamVO> TeacherGetAllExam(@RequestParam("courseId") Long courseId){
        return teacherService.TeacherGetAllExam(courseId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个特定考试信息接口
    * @DateTime: 2024/3/14 18:09
    * @Params: [examId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ExamVO
    */
    @PostMapping("/get-exam-detail")
    public TeacherExtendedExamVO TeacherGetSingleExamInfo(@RequestParam("examId") Long examId){
        return teacherService.TeacherGetSingleExamInfo(examId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师上传考试试卷文件接口
    * @DateTime: 2024/3/15 13:57
    * @Params: [multipartFile]
    * @Return java.lang.String
    */
    @PostMapping("/upload-paper")
    public String TeacherUploadExamPaperFile(@RequestParam("examPaper") MultipartFile multipartFile) throws IOException {
        return teacherService.TeacherUploadExamPaperFile(multipartFile);
    }


    /**
    * @Author: ZooMEISTER
    * @Description: 老师添加考试接口
    * @DateTime: 2024/3/15 15:08
    * @Params: [examName, examDescription, examStartDateTime, examEndDateTime, paperFileName, paperName, paperDescription, paperScore, teachby]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TeacherAddExamResultVO
    */
    @PostMapping("/add-exam")
    public TeacherAddExamResultVO TeacherAddExam(@RequestParam("examName") String examName,
                                                 @RequestParam("examDescription") String examDescription,
                                                 @RequestParam("examStartDateTime") String examStartDateTime,
                                                 @RequestParam("examEndDateTime") String examEndDateTime,
                                                 @RequestParam("paperFileName") String paperFileName,
                                                 @RequestParam("paperName") String paperName,
                                                 @RequestParam("paperDescription") String paperDescription,
                                                 @RequestParam("paperScore") int paperScore,
                                                 @RequestParam("teachby") Long teachby,
                                                 @RequestParam("courseId") Long courseId) throws ParseException {
        return teacherService.TeacherAddExam(examName, examDescription, examStartDateTime, examEndDateTime, paperFileName, paperName, paperDescription, paperScore, teachby, courseId);
    }
}
