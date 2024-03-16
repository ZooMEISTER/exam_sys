package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.vo.*;
import org.springframework.stereotype.Service;
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
    List<ExamVO> TeacherGetAllExam(Long courseId);
    TeacherExtendedExamVO TeacherGetSingleExamInfo(Long examId);
    String TeacherUploadExamPaperFile(MultipartFile multipartFile) throws IOException;
    TeacherAddExamResultVO TeacherAddExam(String examName, String examDescription, String examStartDateTime, String examEndDateTime, String paperFileName, String paperName, String paperDescription, int paperScore, Long teachby, Long courseId) throws ParseException;
}
