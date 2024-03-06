package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.bo.SubjectCourseBO;
import com.zoom.exam_sys_backend.pojo.po.CoursePO;
import com.zoom.exam_sys_backend.pojo.po.DepartmentPO;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import com.zoom.exam_sys_backend.pojo.po.SubjectPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/16 20:52
 **/

@Mapper
public interface StudentMapper extends BaseMapper<StudentPO> {
    @Select("SELECT * FROM user_student WHERE username=#{username} AND deleted='0'")
    StudentPO getStudentPOByUsername(String username);

    @Update("UPDATE user_student SET avatar=#{newAvatar}, username=#{newUsername}, realname=#{newRealname}, phone=#{newPhone}, email=#{newEmail}, profilev=#{newProfilev} WHERE id=#{userid}")
    int updateStudentProfileWithoutPassword(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, int newProfilev);

    @Select("SELECT * FROM sys_department")
    List<DepartmentPO> studentGetAllDepartment();

    @Select("SELECT * FROM sys_subject WHERE belongto=#{departmentId}")
    List<SubjectPO> studentGetAllSubjects(Long departmentId);

    @Select("SELECT * FROM relation_subject_course WHERE subject_id=#{subjectId}")
    List<SubjectCourseBO> studentGetAllSubjectCourseRelation(Long subjectId);

    //select a.*, b.*, c.* from sys_subject a inner join relation_subject_course b on a.id = b.subject_id inner join sys_course c on b.course_id = c.id;
    @Select("SELECT * FROM sys_course WHERE id=#{courseId}")
    CoursePO studentGetSingleCourse(Long courseId);
}
