package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO;
import com.zoom.exam_sys_backend.pojo.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 19:22
 **/

@Service
public interface AdminService {
    TouristLoginResultVO AdminUpdateProfile(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, String newPassword);
    List<SimpleObjectInfo> AdminGetAllSimpleDepartmentInfo();
    List<SimpleObjectInfo> AdminGetSimpleSubjectInfo(Long departmentId);
    List<SimpleObjectInfo> AdminGetAllSimpleTeacherInfo();
    List<AdminAddCourseApplicationVO> AdminGetAddCourseApplication(String departmentId, String subjectId, String teacherId, int applicationStatus);
    List<AdminAddCourseApplicationVO> AdminGetAllAddCourseApplication();
    ResultVO AdminApproveAddCourseApplication(Long applicationId);
    ResultVO AdminTurnDownAddCourseApplication(Long applicationId);
    List<AdminToTeacherApplicationVO> AdminGetBeTeacherApplication(int applicationStatus);
    ResultVO AdminApproveToTeacherApplication(Long applicationId);
    ResultVO AdminTurnDownToTeacherApplication(Long applicationId);
    List<DepartmentVO> AdminSearchDepartment(String searchStr);
    ResultVO AdminAddNewDepartment(String newDepartmentName, String newDepartmentIcon, String newDepartmentDescription);
    ResultVO AdminUpdateDepartmentInfo(Long departmentId, String newDepartmentName, String newDepartmentIcon, String newDepartmentDescription);
    ResultVO AdminDeleteDepartment(Long departmentId);
    List<AdminSubjectManagementVO> AdminGetSubjectManagement(String departmentId);
    ResultVO AdminUpdateSubjectInfo(Long subjectId, String newSubjectIcon, String newSubjectName, String newSubjectDescription);
    ResultVO AdminAddNewSubject(Long belongto, String newSubjectIcon, String newSubjectName, String newSubjectDescription);
    ResultVO AdminDeleteSubject(Long subjectId);
    List<AdminCourseManagementVO> AdminGetCourseByConditions(String departmentId, String subjectId, String teacherId);
    ResultVO AdminUpdateCourseInfo(Long courseId, String newCourseIcon, String newCourseName, String newCourseDescription);
    ResultVO AdminAddNewCourse(Long belongto, String newCourseIcon, String newCourseName, String newCourseDescription, Long teachby);
    ResultVO AdminDeleteCourse(Long courseId);
    List<TeacherVO> AdminGetTeacherInfo(String searchStr);
    ResultVO AdminUpdateTeacherInfo(Long curUserId, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, Boolean resetPwd);
    List<StudentVO> AdminGetStudentInfo(String searchStr);
    ResultVO AdminUpdateStudentInfo(Long curUserId, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, Boolean resetPwd);
}
