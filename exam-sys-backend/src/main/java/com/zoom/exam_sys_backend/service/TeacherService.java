package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.po.CoursePO;
import com.zoom.exam_sys_backend.pojo.po.DepartmentPO;
import com.zoom.exam_sys_backend.pojo.po.SubjectPO;
import com.zoom.exam_sys_backend.pojo.vo.CourseVO;
import com.zoom.exam_sys_backend.pojo.vo.DepartmentVO;
import com.zoom.exam_sys_backend.pojo.vo.SubjectVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
}
