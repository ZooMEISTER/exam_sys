package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.bo.StudentToTeacherBO;
import com.zoom.exam_sys_backend.pojo.bo.SubjectCourseBO;
import com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.ResultVO;
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

    @Update("UPDATE sys_subject SET course_count=course_count+1 WHERE id=#{subject_id}")
    int AdminPlusOne2SubjectCourseCount(Long subject_id);

    @Update("UPDATE sys_subject SET course_count=course_count-1 WHERE id=#{subject_id}")
    int AdminMinusOne2SubjectCourseCount(Long subject_id);

    @Select("SELECT * FROM application_add_course")
    List<TeacherAddCourseBO> AdminGetAllAddCourseApplication();

    @Select("SELECT * FROM application_add_course WHERE id=#{applicationId}")
    TeacherAddCourseBO AdminGetAddCourseApplicationBOById(Long applicationId);

    @Update("UPDATE ${tableName} SET approve_status=#{newStatus} WHERE id=#{applicationId}")
    int AdminUpdateApplicationStatus(String tableName, Long applicationId, int newStatus);

    @Select("SELECT id, name FROM sys_department WHERE deleted != 1")
    List<SimpleObjectInfo> AdminGetAllDepartmentSimpleInfo();

    @Select("SELECT id, name FROM sys_subject WHERE belongto=#{departmentId} AND deleted != 1")
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

    @Select("SELECT * FROM sys_department WHERE name LIKE '%${searchStr}%' AND deleted != 1")
    List<DepartmentPO> AdminSearchDepartment(String searchStr);

    @Insert("INSERT INTO sys_department (id, icon, name, description) VALUES (#{newDepartmentId}, #{newDepartmentIcon}, #{newDepartmentName}, #{newDepartmentDescription})")
    int AdminAddNewDepartment(Long newDepartmentId, String newDepartmentName, String newDepartmentIcon, String newDepartmentDescription);

    @Update("UPDATE sys_department SET name=#{newDepartmentName}, icon=#{newDepartmentIcon}, description=#{newDepartmentDescription} WHERE id=#{departmentId}")
    int AdminUpdateDepartmentInfo(Long departmentId, String newDepartmentName, String newDepartmentIcon, String newDepartmentDescription);

    @Update("UPDATE sys_department SET deleted=1 WHERE id=#{departmentId}")
    int AdminDeleteDepartment(Long departmentId);

    @Select("SELECT * FROM sys_subject WHERE belongto LIKE '%${belongto}%' AND deleted != 1")
    List<SubjectPO> AdminGetSubjectManagement(String belongto);

    @Update("UPDATE sys_subject SET icon=#{newSubjectIcon}, name=#{newSubjectName}, description=#{newSubjectDescription} WHERE id=#{subjectId}")
    int AdminUpdateSubjectInfo(Long subjectId, String newSubjectIcon, String newSubjectName, String newSubjectDescription);

    @Insert("INSERT INTO sys_subject (id, icon, name, description, belongto) VALUES (#{id}, #{icon}, #{name}, #{description}, #{belongto})")
    int AdminAddNewSubject(Long id, String icon, String name, String description, Long belongto);

    @Update("UPDATE sys_department SET subject_count=subject_count+1 WHERE id=#{departmentId}")
    int AdminDepartmentSubjectCountIncrement(Long departmentId);

    @Update("UPDATE sys_department SET subject_count=subject_count-1 WHERE id=#{departmentId}")
    int AdminDepartmentSubjectCountDecrement(Long departmentId);

    @Update("UPDATE sys_subject SET deleted = 1 WHERE id=#{subjectId}")
    int AdminDeletedSubject(Long subjectId);

    @Select("SELECT * FROM sys_course WHERE teachby LIKE '%${teacherId}%' AND deleted != 1")
    List<CoursePO> AdminGetCourseByTeachby(String teacherId);

    @Select("SELECT * FROM relation_subject_course WHERE course_id=#{courseId}")
    SubjectCourseBO AdminGetSubjectCourseBOByCourseId(Long courseId);

    @Update("UPDATE sys_course SET icon=#{newCourseIcon}, name=#{newCourseName}, description=#{newCourseDescription} WHERE id=#{courseId}")
    int AdminUpdateCourseInfo(Long courseId, String newCourseIcon, String newCourseName, String newCourseDescription);

    @Insert("INSERT INTO sys_course (id, icon, name, description, teachby) VALUES(#{id}, #{icon}, #{name}, #{description}, #{teachby})")
    int AdminAddNewCourse(Long id, String icon, String name, String description, Long teachby);

    @Insert("INSERT INTO relation_subject_course(id, subject_id, course_id) VALUES(#{id}, #{subject_id}, #{course_id})")
    int AdminAddSubjectCourseRelation(Long id, Long subject_id, Long course_id);

    @Update("UPDATE sys_course SET deleted = 1 WHERE id=#{courseId}")
    int AdminDeletedCourse(Long courseId);

    @Select("SELECT * FROM user_teacher WHERE realname LIKE '%${searchStr}%'")
    List<TeacherPO> AdminGetTeacherInfo(String searchStr);

    @Select("SELECT * FROM user_student WHERE realname LIKE '%${searchStr}%'")
    List<StudentPO> AdminGetStudentInfo(String searchStr);

    @Update("UPDATE user_teacher SET avatar=#{newAvatar}, username=#{newUsername}, realname=#{newRealname}, phone=#{newPhone}, email=#{newEmail} WHERE id=#{curUserId}")
    int AdminUpdateTeacherProfile(Long curUserId, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail);

    @Update("UPDATE user_teacher SET password=#{defaultPassword}")
    int AdminResetTeacherPassword(Long curUserid, String defaultPassword);

    @Update("UPDATE user_student SET avatar=#{newAvatar}, username=#{newUsername}, realname=#{newRealname}, phone=#{newPhone}, email=#{newEmail} WHERE id=#{curUserId}")
    int AdminUpdateStudentProfile(Long curUserId, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail);

    @Update("UPDATE user_student SET password=#{defaultPassword}")
    int AdminResetStudentPassword(Long curUserid, String defaultPassword);
}
