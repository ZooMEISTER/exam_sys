package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.bo.*;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.CourseVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
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

    @Select("SELECT * FROM sys_department WHERE deleted != 1")
    List<DepartmentPO> studentGetAllDepartment();

    @Select("SELECT * FROM sys_subject WHERE belongto=#{departmentId} AND deleted != 1")
    List<SubjectPO> studentGetAllSubjects(Long departmentId);

    @Select("SELECT * FROM relation_subject_course WHERE subject_id=#{subjectId}")
    List<SubjectCourseBO> studentGetAllSubjectCourseRelation(Long subjectId);

    //select a.*, b.*, c.* from sys_subject a inner join relation_subject_course b on a.id = b.subject_id inner join sys_course c on b.course_id = c.id;
    @Select("SELECT * FROM sys_course WHERE id=#{courseId}")
    CoursePO studentGetSingleCourse(Long courseId);

    @Select("SELECT * FROM relation_course_exam WHERE course_id=#{courseId}")
    List<CourseExamBO> studentGetAllCourseExamRelation(Long courseId);

    @Select("SELECT * FROM sys_exam WHERE id=#{examId}")
    ExamPO studentGetSingleExam(Long examId);

    @Select("SELECT * FROM relation_course_exam WHERE exam_id=#{examId}")
    CourseExamBO studentGetCourseExamRelationByExamId(Long examId);

    @Select("SELECT * FROM relation_exam_paper WHERE exam_id=#{examId}")
    ExamPaperBO studentGetExamPaperRelationByExamId(Long examId);

    @Select("SELECT * FROM sys_paper WHERE id=#{paperId}")
    PaperPO studentGetPaperInfo(Long paperId);

    @Select("SELECT COUNT(*) FROM respondent_exam_student WHERE exam_id=#{examId} AND student_id=#{studentId}")
    int checkIfStudentFinishedExam(Long examId, Long studentId);

    @Select("SELECT COUNT(*) FROM relation_course_student WHERE course_id=#{courseId} AND student_id=#{studentId}")
    int StudentCheckIfSignedCourse(Long studentId, Long courseId);

    @Insert("INSERT INTO relation_course_student(id,course_id,student_id) VALUES(#{relationId},#{courseId},#{studentId})")
    int StudentSignCourse(Long relationId, Long studentId, Long courseId);

    @Select("SELECT * FROM sys_course WHERE id=#{courseId}")
    CoursePO StudentGetCourseInfo(Long courseId);

    @Select("SELECT * FROM respondent_exam_student WHERE exam_id=#{examId} AND student_id=#{studentId}")
    RespondentExamStudentBO StudentGetRespondentInfo(Long examId, Long studentId);

    @Insert("INSERT INTO respondent_exam_student(id,exam_id,student_id,respondent_path,final_score,sha256_code,sign,publickey,is_sign_verify_good,last_modified_time) VALUES(#{respondentId},#{examId},#{studentId},#{respondentFileName},#{finalScore},#{sha256Value},#{sign},#{publicKey},#{is_sign_verify_good},#{last_modified_time})")
    int StudentAddRespondent(Long respondentId, Long examId, Long studentId, String respondentFileName, int finalScore, String sha256Value, String sign, String publicKey, int is_sign_verify_good, Date last_modified_time);

    @Select("SELECT * FROM sys_course WHERE teachby=#{teacherId} AND deleted != 1")
    List<CoursePO> StudentGetAllMyClass(Long teacherId);

    @Select("SELECT * FROM relation_subject_course WHERE course_id=#{courseId}")
    SubjectCourseBO StudentGetSubjectCourseRelationByCourseId(Long courseId);

    @Select("SELECT * FROM sys_subject WHERE id=#{subjectId}")
    SubjectPO StudentGetSubjectPOById(Long subjectId);

    @Select("SELECT * FROM sys_department WHERE id=#{departmentId}")
    DepartmentPO StudentGetDepartmentPOById(Long departmentId);

    @Select("SELECT COUNT(*) FROM relation_course_student WHERE course_id=#{courseId}")
    int StudentGetCourseStudentCount(Long courseId);

    @Select("SELECT * FROM relation_course_student WHERE student_id=#{studentId}")
    List<CourseStudentBO> StudentGetAllCourseStudentRelation(Long studentId);

    @Select("SELECT COUNT(*) FROM relation_course_exam WHERE course_id=#{courseId}")
    int StudentGetCourseExamCountByCourseId(Long courseId);

    @Select("SELECT * FROM user_teacher WHERE id=#{teacherId}")
    TeacherPO StudentGetTeacherPOById(Long teacherId);

    @Select("SELECT * FROM user_student WHERE id=#{studentId}")
    StudentPO StudentGetStudentPOById(Long studentId);

    @Select("SELECT COUNT(*) FROM application_to_teacher WHERE student_id=#{studentId} AND approve_status=#{approveStatus}")
    int StudentGetToTeacherApplicationCount(Long studentId, int approveStatus);

    @Insert("INSERT INTO application_to_teacher(id,student_id,description) VALUES(#{applicationId},#{studentId},#{description})")
    int StudentAddNewToTeacherApplication(Long applicationId, Long studentId, String description);

    @Select("SELECT aes_key FROM sys_paper WHERE id=#{paperId}")
    String StudentGetExamAesKey(Long paperId);

    @Select("SELECT * FROM relation_exam_paper WHERE paper_id=#{paperId}")
    ExamPaperBO StudentGetExamPaperBOByPaperId(Long paperId);
}
