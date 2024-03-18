package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.bo.*;
import com.zoom.exam_sys_backend.pojo.po.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/17 19:13
 **/

@Mapper
public interface TeacherMapper extends BaseMapper<TeacherPO> {
    @Select("SELECT * FROM user_teacher WHERE username=#{username} AND deleted='0'")
    TeacherPO getTeacherPOByUsername(String username);

    @Update("UPDATE user_teacher SET avatar=#{newAvatar}, username=#{newUsername}, realname=#{newRealname}, phone=#{newPhone}, email=#{newEmail}, profilev=#{newProfilev} WHERE id=#{userid}")
    int updateTeacherProfileWithoutPassword(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, int newProfilev);

    @Select("SELECT * FROM sys_department")
    List<DepartmentPO> teacherGetAllDepartment();

    @Select("SELECT * FROM sys_subject WHERE belongto=#{departmentId}")
    List<SubjectPO> teacherGetAllSubjects(Long departmentId);

    @Select("SELECT * FROM relation_subject_course WHERE subject_id=#{subjectId}")
    List<SubjectCourseBO> teacherGetAllSubjectCourseRelation(Long subjectId);

    //select a.*, b.*, c.* from sys_subject a inner join relation_subject_course b on a.id = b.subject_id inner join sys_course c on b.course_id = c.id;
    @Select("SELECT * FROM sys_course WHERE id=#{courseId}")
    CoursePO teacherGetSingleCourse(Long courseId);

    @Select("SELECT * FROM relation_course_exam WHERE course_id=#{courseId}")
    List<CourseExamBO> teacherGetAllCourseExamRelation(Long courseId);

    @Select("SELECT * FROM sys_exam WHERE id=#{examId}")
    ExamPO teacherGetSingleExam(Long examId);

    @Select("SELECT * FROM relation_course_exam WHERE exam_id=#{examId}")
    CourseExamBO teacherGetCourseExamRelationByExamId(Long examId);

    @Select("SELECT * FROM relation_exam_paper WHERE exam_id=#{examId}")
    ExamPaperBO teacherGetExamPaperRelationByExamId(Long examId);

    @Select("SELECT * FROM sys_paper WHERE id=#{paperId}")
    PaperPO teacherGetPaperInfo(Long paperId);

    @Insert("INSERT INTO sys_paper(id,name,description,teachby,score,path) VALUES(#{newPaperId},#{newPaperName},#{newPaperDescription},#{teachby},#{newPaperScore},#{newPaperPath})")
    int teacherAddNewPaper(Long newPaperId, String newPaperName, String newPaperDescription, Long teachby, int newPaperScore, String newPaperPath);

    @Insert("INSERT INTO sys_exam(id,name,description,start_time,end_time,teachby,type,published) VALUES(#{newExamId},#{newExamName},#{newExamDescription},#{newExamStartDateTime},#{newExamEndDateTime},#{teachby},#{type},#{published})")
    int teacherAddNewExam(Long newExamId, String newExamName, String newExamDescription, Date newExamStartDateTime, Date newExamEndDateTime, Long teachby, int type, int published);

    @Insert("INSERT INTO relation_exam_paper(id,exam_id,paper_id) VALUES(#{relationId},#{newExamId},#{newPaperId})")
    int teacherAddNewExamPaperRelation(Long relationId, Long newExamId, Long newPaperId);

    @Insert("INSERT INTO relation_course_exam(id,course_id,exam_id) VALUES(#{relationId},#{courseId},#{newExamId})")
    int teacherAddNewCourseExamRelation(Long relationId, Long courseId, Long newExamId);

    @Select("SELECT * FROM sys_course WHERE id=#{courseId}")
    CoursePO TeacherGetCourseInfo(Long courseId);

    @Select("SELECT COUNT(*) FROM relation_course_student WHERE course_id=#{courseId}")
    int TeacherGetCourseStudentCount(Long courseId);

    @Select("SELECT COUNT(*) FROM respondent_exam_student WHERE exam_id=#{examId}")
    int TeacherGetExamFinishedStudentCount(Long examId);

    @Select("SELECT * FROM relation_course_student WHERE course_id=#{courseId}")
    List<CourseStudentBO> TeacherGetAllCourseStudentRelationByCourseId(Long courseId);

    @Select("SELECT * FROM user_student WHERE id=#{studentId}")
    StudentPO TeacherGetSingleStudent(Long studentId);

    @Select("SELECT * FROM respondent_exam_student WHERE exam_id=#{examId}")
    List<RespondentExamStudentBO> TeacherGetAllFinishedRespondentInfo(Long examId);

    @Select("SELECT course_id FROM relation_course_exam WHERE exam_id=#{examId}")
    Long TeacherGetExamCourseIdByExamId(Long examId);

    @Select("SELECT COUNT(*) FROM respondent_exam_student WHERE exam_id=#{examId} AND student_id=#{studentId}")
    int TeacherGetStudentRespondentCount(Long examId, Long studentId);

    @Select("SELECT * FROM sys_course WHERE teachby=#{teacherId}")
    List<CoursePO> TeacherGetAllMyClass(Long teacherId);

    @Select("SELECT * FROM relation_subject_course WHERE course_id=#{courseId}")
    SubjectCourseBO TeacherGetSubjectCourseRelationByCourseId(Long courseId);

    @Select("SELECT * FROM sys_subject WHERE id=#{subjectId}")
    SubjectPO TeacherGetSubjectPOById(Long subjectId);

    @Select("SELECT * FROM sys_department WHERE id=#{departmentId}")
    DepartmentPO TeacherGetDepartmentPOById(Long departmentId);

    @Select("SELECT COUNT(*) FROM relation_course_exam WHERE course_id=#{courseId}")
    int TeacherGetCourseExamCountByCourseId(Long courseId);
}
