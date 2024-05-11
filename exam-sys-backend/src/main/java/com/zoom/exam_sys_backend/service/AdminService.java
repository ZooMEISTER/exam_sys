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
    AdminApplicationManageResultVO AdminApproveAddCourseApplication(Long applicationId);
    AdminApplicationManageResultVO AdminTurnDownAddCourseApplication(Long applicationId);
    List<AdminToTeacherApplicationVO> AdminGetBeTeacherApplication(int applicationStatus);
    AdminApplicationManageResultVO AdminApproveToTeacherApplication(Long applicationId);
    AdminApplicationManageResultVO AdminTurnDownToTeacherApplication(Long applicationId);
}
