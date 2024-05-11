package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.bo.StudentToTeacherBO;
import com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.SimpleObjectInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/17 19:13
 **/

@Mapper
public interface AdminMapper extends BaseMapper<AdminPO> {
    @Select("SELECT * FROM user_admin WHERE username=#{username} AND deleted='0'")
    AdminPO getAdminPOByUsername(String username);

    @Update("UPDATE user_admin SET avatar=#{newAvatar}, username=#{newUsername}, realname=#{newRealname}, phone=#{newPhone}, email=#{newEmail}, profilev=#{newProfilev} WHERE id=#{userid}")
    int updateAdminProfileWithoutPassword(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, int newProfilev);

    @Select("SELECT * FROM user_teacher WHERE id=#{teacherId}")
    TeacherPO AdminGetTeacherPOById(Long teacherId);

    @Select("SELECT * FROM user_student WHERE id=#{studentId}")
    StudentPO AdminGetStudentPOById(Long studentId);

    @Select("SELECT * FROM sys_subject WHERE id=#{subjectId}")
    SubjectPO AdminGetSubjectPOById(Long subjectId);

    @Select("SELECT * FROM sys_department WHERE id=#{departmentId}")
    DepartmentPO AdminGetDepartmentPOById(Long departmentId);

    @Insert("INSERT INTO sys_course (id, icon, name, description, teachby) VALUES (#{id}, #{icon}, #{name}, #{description}, #{teachby})")
    int AdminInsertNewCourse(Long id, String icon, String name, String description, Long teachby);

    @Insert("INSERT INTO relation_subject_course (id, subject_id, course_id) VALUES (#{id}, #{subject_id}, #{course_id})")
    int AdminInsertSubjectCourseBO(Long id, Long subject_id, Long course_id);

    @Update("UPDATE sys_subject SET course_count=course_count+1")
    int AdminPlusOne2SubjectCourseCount(Long subject_id);

    @Select("SELECT * FROM application_add_course")
    List<TeacherAddCourseBO> AdminGetAllAddCourseApplication();

    @Select("SELECT * FROM application_add_course WHERE id=#{applicationId}")
    TeacherAddCourseBO AdminGetAddCourseApplicationBOById(Long applicationId);

    @Update("UPDATE ${tableName} SET approve_status=#{newStatus} WHERE id=#{applicationId}")
    int AdminUpdateApplicationStatus(String tableName, Long applicationId, int newStatus);

    @Select("SELECT id, name FROM sys_department")
    List<SimpleObjectInfo> AdminGetAllDepartmentSimpleInfo();

    @Select("SELECT id, name FROM sys_subject WHERE belongto=#{departmentId}")
    List<SimpleObjectInfo> AdminGetSubjectSimpleInfo(Long departmentId);

    @Select("SELECT id, realname AS name FROM user_teacher")
    List<SimpleObjectInfo> AdminGetAllTeacherSimpleInfo();

    @Select("SELECT * FROM application_add_course WHERE (subject_id LIKE '%${subjectId}%') AND (teachby LIKE '%${teacherId}%') AND (approve_status LIKE '%${applicationStatus}%')")
    List<TeacherAddCourseBO> AdminGetAddCourseApplication(String departmentId, String subjectId, String teacherId, String applicationStatus);

    @Select("SELECT * FROM application_to_teacher WHERE approve_status LIKE '%${applicationStatus}%'")
    List<StudentToTeacherBO> AdminGetToTeacherApplication(String applicationStatus);

    @Select("SELECT * FROM application_to_teacher WHERE id=#{applicationId}")
    StudentToTeacherBO AdminGetToTeacherBOById(Long applicationId);

    @Insert("INSERT INTO user_teacher (id, avatar, username, realname, password, phone, email) VALUES (#{id}, #{avatar}, #{username}, #{realname}, #{password}, #{phone}, #{email})")
    int AdminInsertNewTeacher(Long id, String avatar, String username, String realname, String password, String phone, String email);

    @Update("UPDATE user_student SET deleted=1 WHERE id=#{studentId}")
    int AdminDeleteStudent(Long studentId);
}
