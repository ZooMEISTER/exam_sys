package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.vo.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/14 11:46
 **/

@Service
public interface StudentService {
    TouristLoginResultVO StudentUpdateProfile(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, String newPassword);
    List<DepartmentVO> StudentGetAllDepartment();
    List<SubjectVO> StudentGetAllSubject(Long departmentId);
    List<CourseVO> StudentGetAllCourse(Long subjectId);
    List<StudentExamVO> StudentGetAllExam(Long courseId, Long studentId);
    StudentExtendedExamVO StudentGetSingleExamInfo(Long examId, Long studentId);
    int StudentCheckIfSignedCourse(Long studentId, Long courseId);
    StudentSignCourseResultVO StudentSignCourse(Long studentId, Long courseId);
    CourseVO StudentGetCourseInfo(Long courseId);
    String StudentUploadRespondentFile(MultipartFile multipartFile) throws IOException;
    StudentAddRespondentResultVO StudentAddRespondent(Long examId, Long studentId, String respondentFileName, String sha256Value);
    List<MyCourseVO> StudentGetAllMyCourse(Long studentId);
    List<MyExamVO> StudentGetAllMyExam(Long studentId);
    StudentToTeacherResultVO StudentApplyTobeTeacher(Long studentId, String description);
    StudentGetExamPaperAesKeyResultVO StudentGetExamAesKey(Long paperId);
    void StudentDownloadExamPaper(String paperName, HttpServletResponse response);

}
