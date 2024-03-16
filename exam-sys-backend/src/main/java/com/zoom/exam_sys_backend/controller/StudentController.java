package com.zoom.exam_sys_backend.controller;

import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个课程下所有考试的接口
    * @DateTime: 2024/3/14 17:35
    * @Params: [courseId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.ExamVO>
    */
    @PostMapping("/get-all-exam")
    public List<StudentExamVO> StudentGetAllExam(@RequestParam("courseId") Long courseId,
                                                 @RequestParam("studentId") Long studentId){
        return studentService.StudentGetAllExam(courseId, studentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个考试详细信息的接口
    * @DateTime: 2024/3/15 16:39
    * @Params: [examId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.StudentExtendedExamVO
    */
    @PostMapping("/get-exam-detail")
    public StudentExtendedExamVO StudentGetSingleExamInfo(@RequestParam("examId") Long examId,
                                                          @RequestParam("studentId") Long studentId){
        return studentService.StudentGetSingleExamInfo(examId, studentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生查询自己是否报名该课程接口
    * @DateTime: 2024/3/16 15:38
    * @Params: [studentId, courseId]
    * @Return int
    */
    @PostMapping("/check-if-student-signed-course")
    public int StudentCheckIfSignedCourse(@RequestParam("studentId") Long studentId,
                                          @RequestParam("courseId") Long courseId){
        return studentService.StudentCheckIfSignedCourse(studentId, courseId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生报名课程接口
    * @DateTime: 2024/3/16 17:20
    * @Params: [studentId, courseId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.StudentSignCourseResultVO
    */
    @PostMapping("/student-sign-course")
    public StudentSignCourseResultVO StudentSignCourse(@RequestParam("studentId") Long studentId,
                                                       @RequestParam("courseId") Long courseId){
        return studentService.StudentSignCourse(studentId, courseId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个课程信息的接口
    * @DateTime: 2024/3/16 18:47
    * @Params: [courseId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.CourseVO
    */
    @PostMapping("/get-course-info")
    public CourseVO StudentGetCourseInfo(@RequestParam("courseId") Long courseId){
        return studentService.StudentGetCourseInfo(courseId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生上传答卷接口
    * @DateTime: 2024/3/16 19:46
    * @Params: [multipartFile]
    * @Return java.lang.String
    */
    @PostMapping("/upload-respondent")
    public String StudentUploadRespondentFile(@RequestParam("examRespondent") MultipartFile multipartFile) throws IOException {
        return studentService.StudentUploadRespondentFile(multipartFile);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生添加答卷记录接口
    * @DateTime: 2024/3/16 20:09
    * @Params: [examId, studentId, respondentFileName, sha256Value]
    * @Return com.zoom.exam_sys_backend.pojo.vo.StudentAddRespondentResultVO
    */
    @PostMapping("/add-respondent-record")
    public StudentAddRespondentResultVO StudentAddRespondent(@RequestParam("examId") Long examId,
                                                            @RequestParam("studentId") Long studentId,
                                                            @RequestParam("respondentFileName") String respondentFileName,
                                                            @RequestParam("sha256Value") String sha256Value){
        return studentService.StudentAddRespondent(examId, studentId, respondentFileName, sha256Value);
    }
}
