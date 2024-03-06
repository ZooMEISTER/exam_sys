package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.vo.CourseVO;
import com.zoom.exam_sys_backend.pojo.vo.DepartmentVO;
import com.zoom.exam_sys_backend.pojo.vo.SubjectVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import org.springframework.stereotype.Service;

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
}
