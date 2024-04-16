package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO;
import com.zoom.exam_sys_backend.pojo.vo.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/14 14:37
 **/

@Service
public interface TeacherService {
    TouristLoginResultVO TeacherUpdateProfile(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, String newPassword);
    List<DepartmentVO> TeacherGetAllDepartment();
    List<SubjectVO> TeacherGetAllSubject(Long departmentId);
    List<CourseVO> TeacherGetAllCourse(Long subjectId);
    List<TeacherExamVO> TeacherGetAllExam(Long courseId);
    TeacherExtendedExamVO TeacherGetSingleExamInfo(Long examId);
    String TeacherUploadExamPaperFile(MultipartFile multipartFile) throws Exception;
    TeacherAddExamResultVO TeacherAddExam(String examName, String examDescription, String examStartDateTime, String examEndDateTime, String paperFileName, String paperName, String paperDescription, int paperScore, Long teachby, Long courseId) throws Exception;
    TeacherExtendedCourseVO TeacherGetCourseInfo(Long courseId);
    List<StudentVO> TeacherGetAllCourseSignedStudent(Long courseId);
    List<FinishedRespondentExamStudentVO> TeacherGetAllFinishedRespondentExamStudentInfo(Long examId);
    List<StudentVO> TeacherGetAllUnFinishedRespondentExamStudentInfo(Long examId);
    List<MyCourseVO> TeacherGetAllMyCourse(Long teacherId);
    List<MyExamVO> TeacherGetAllMyExam(Long teacherId);
    TeacherAddNewCourseApplyResultVO TeacherAddNewCourse(String newCourseIcon, String newCourseName, String newCourseDescription, Long teachby, Long subjectId);
    List<TeacherAddCourseVO> TeacherGetAllMyAddCourseApplication(Long teacherId);
    String TeacherGetExamAesKey(Long paperId);
    void TeacherDownloadExamPaper(String paperName, HttpServletResponse response);
}
