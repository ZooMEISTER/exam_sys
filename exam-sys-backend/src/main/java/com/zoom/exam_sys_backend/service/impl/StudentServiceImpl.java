package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.comparator.ExamVOComparator;
import com.zoom.exam_sys_backend.comparator.StudentExamVOComparator;
import com.zoom.exam_sys_backend.constant.ExamStatusStudent;
import com.zoom.exam_sys_backend.exception.code.StudentResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.StudentMapper;
import com.zoom.exam_sys_backend.pojo.bo.CourseExamBO;
import com.zoom.exam_sys_backend.pojo.bo.ExamPaperBO;
import com.zoom.exam_sys_backend.pojo.bo.RespondentExamStudentBO;
import com.zoom.exam_sys_backend.pojo.bo.SubjectCourseBO;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.StudentService;
import com.zoom.exam_sys_backend.util.FileUtils;
import com.zoom.exam_sys_backend.util.JWTUtils;
import com.zoom.exam_sys_backend.util.SnowflakeIdWorker;
import com.zoom.exam_sys_backend.util.TimeTransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/14 11:46
 **/

@Service
public class StudentServiceImpl implements StudentService {

    @Value("${file.dataFolder}")
    String fileDataFolderPath;

    @Value("${data.examPaperFolder}")
    String examPaperFolderPath;

    @Value("${data.examAnswerPaperFolder}")
    String examAnswerPaperFolderPath;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StudentMapper studentMapper;

    /**
    * @Author: ZooMEISTER
    * @Description: 学生修改个人信息方法
    * @DateTime: 2024/2/14 11:51
    * @Params: [userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @Override
    @Transactional
    public TouristLoginResultVO StudentUpdateProfile(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, String newPassword) {
        TouristLoginResultVO requestSenderVO = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(userid))), TouristLoginResultVO.class);
        if(requestSenderVO == null){
            // redis中没有用户信息，去mysql中找
            StudentPO studentPO = studentMapper.selectById(userid);
            if(studentPO == null){
                // 用户不存在
                return new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_FAIL_USER_NOT_EXIST, 0, "0", null, null, null, null, null, null, 0, 0);
            }
            requestSenderVO = new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_SUCCESS,
                                                        1,
                                                        String.valueOf(userid),
                                                        studentPO.getAvatar(),
                                                        studentPO.getUsername(),
                                                        studentPO.getRealname(),
                                                        studentPO.getPhone(),
                                                        studentPO.getEmail(),
                                                        "",
                                                        studentPO.getDeleted(),
                                                        studentPO.getProfilev());
        }
        int newProfilev = requestSenderVO.getProfilev() + 1;
        int res = 0;

        if(newPassword.equals("NOT_CHANGE")){
            // 不修改密码
            res = studentMapper.updateStudentProfileWithoutPassword(userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newProfilev);
        }
        else{
            // 修改密码
            res = studentMapper.updateById(new StudentPO(userid, newAvatar, newUsername, newRealname, newPassword, newPhone, newEmail, 0, newProfilev));
        }

        // 用户信息更新失败，直接返回
        if(res < 0){
            return new TouristLoginResultVO(StudentResultCode.STUDENT_UPDATE_PROFILE_FAIL, 0, "0", null, null, null, null, null, null, 0, 0);
        }

        // 用户信息更新成功
        // 生成新的token
        String newToken = JWTUtils.genAccessToken(userid, newProfilev);
        // 生成新的对象
        TouristLoginResultVO touristLoginResultVO = new TouristLoginResultVO(StudentResultCode.STUDENT_UPDATE_PROFILE_SUCCESS,
                                                                            1,
                                                                            String.valueOf(userid),
                                                                            newAvatar,
                                                                            newUsername,
                                                                            newRealname,
                                                                            newPhone,
                                                                            newEmail,
                                                                            newToken,
                                                                            0,
                                                                            newProfilev);
        // 更新redis
        redisTemplate.opsForValue().set(String.valueOf(userid), JSONObject.toJSONString(touristLoginResultVO));
        // 返回值
        return touristLoginResultVO;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取所有学院方法
    * @DateTime: 2024/3/6 18:09
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.DepartmentVO>
    */
    @Override
    public List<DepartmentVO> StudentGetAllDepartment() {
        List<DepartmentVO> departmentVOList = new ArrayList<>();
        List<DepartmentPO> departmentPOList = studentMapper.studentGetAllDepartment();
        for(DepartmentPO i : departmentPOList){
            departmentVOList.add(new DepartmentVO(
                    i.getId().toString(),
                    i.getIcon(),
                    i.getName(),
                    i.getDescription(),
                    i.getSubject_count()
            ));
        }
        return departmentVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个学院下所有专业方法
    * @DateTime: 2024/3/6 18:11
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SubjectVO>
    */
    @Override
    public List<SubjectVO> StudentGetAllSubject(Long departmentId) {
        List<SubjectVO> subjectVOList = new ArrayList<>();
        List<SubjectPO> subjectPOList = studentMapper.studentGetAllSubjects(departmentId);
        for(SubjectPO i : subjectPOList){
            subjectVOList.add(new SubjectVO(
                    i.getId().toString(),
                    i.getIcon(),
                    i.getName(),
                    i.getDescription(),
                    i.getBelongto().toString(),
                    i.getCourse_count()
            ));
        }
        return subjectVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个专业下所有课程方法
    * @DateTime: 2024/3/6 18:11
    * @Params: [subjectId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.CourseVO>
    */
    @Override
    public List<CourseVO> StudentGetAllCourse(Long subjectId) {
        // 不推荐使用join，因此在这里用代码逻辑实现多表联合查询
        List<SubjectCourseBO> subjectCoursePOList = studentMapper.studentGetAllSubjectCourseRelation(subjectId);
        List<CourseVO> courseVOList = new ArrayList<>();
        for(SubjectCourseBO i : subjectCoursePOList){
            CoursePO coursePO = studentMapper.studentGetSingleCourse(i.getCourse_id());
            courseVOList.add(new CourseVO(
                    coursePO.getId().toString(),
                    coursePO.getIcon(),
                    coursePO.getName(),
                    coursePO.getDescription(),
                    coursePO.getTeachby().toString(),
                    TimeTransferUtils.TransferTime2LocalTime(coursePO.getCreated_time())
            ));
        }
        return courseVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个课程下所有考试的方法
    * @DateTime: 2024/3/14 17:34
    * @Params: [courseId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.ExamVO>
    */
    @Override
    public List<StudentExamVO> StudentGetAllExam(Long courseId, Long studentId) {
        List<CourseExamBO> courseExamBOList = studentMapper.studentGetAllCourseExamRelation(courseId);
        List<StudentExamVO> studentExamVOList = new ArrayList<>();
        for(CourseExamBO i : courseExamBOList){
            ExamPO examPO = studentMapper.studentGetSingleExam(i.getExam_id());
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDateTime = new Date(System.currentTimeMillis());
            int examStatus = 0; // 对学生来说，考试的状态
            if(currentDateTime.before(examPO.getStart_time())){
                // 该考试未开始
                examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_NOT_START;
            }
            else if(currentDateTime.after(examPO.getEnd_time())){
                // 该考试已结束
                int respondentCount = studentMapper.checkIfStudentFinishedExam(examPO.getId(), studentId);
                if(respondentCount > 0){
                    examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_ENDED_DONE;
                }
                else{
                    examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_ENDED_UNDO;
                }
            }
            else{
                // 该考试正在进行
                int respondentCount = studentMapper.checkIfStudentFinishedExam(examPO.getId(), studentId);
                if(respondentCount > 0){
                    examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_STARTED_DONE;
                }
                else{
                    examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_STARTED_UNDO;
                }
            }

            studentExamVOList.add(new StudentExamVO(
                    examPO.getId().toString(),
                    examPO.getName(),
                    examPO.getDescription(),
                    TimeTransferUtils.TransferTime2LocalTime(examPO.getStart_time()),
                    TimeTransferUtils.TransferTime2LocalTime(examPO.getEnd_time()),
                    examPO.getTeachby().toString(),
                    examPO.getType(),
                    examPO.getPublished(),
                    TimeTransferUtils.TransferTime2LocalTime(examPO.getCreated_time()),
                    examStatus));
        }
        Collections.sort(studentExamVOList, new StudentExamVOComparator());
        return studentExamVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获得某个考试详细信息方法
    * @DateTime: 2024/3/15 16:39
    * @Params: [examId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ExtendedExamVO
    */
    @Override
    public StudentExtendedExamVO StudentGetSingleExamInfo(Long examId, Long studentId) {
        ExamPO examPO = studentMapper.studentGetSingleExam(examId);
        CourseExamBO courseExamBO = studentMapper.studentGetCourseExamRelationByExamId(examId);
        CoursePO coursePO = studentMapper.studentGetSingleCourse(courseExamBO.getCourse_id());
        ExamPaperBO examPaperBO = studentMapper.studentGetExamPaperRelationByExamId(examId);
        PaperPO paperPO = studentMapper.studentGetPaperInfo(examPaperBO.getPaper_id());
        RespondentExamStudentBO respondentExamStudentBO = studentMapper.StudentGetRespondentInfo(examId, studentId);
        int finalScore = -1;
        if(respondentExamStudentBO != null) finalScore = respondentExamStudentBO.getFinal_score();

        return new StudentExtendedExamVO(examPO.getId().toString(),
                examPO.getName(),
                examPO.getDescription(),
                TimeTransferUtils.TransferTime2LocalTime(examPO.getStart_time()),
                TimeTransferUtils.TransferTime2LocalTime(examPO.getEnd_time()),
                examPO.getTeachby().toString(),
                examPO.getType(),
                examPO.getPublished(),
                TimeTransferUtils.TransferTime2LocalTime(examPO.getCreated_time()),
                coursePO.getId().toString(),
                coursePO.getName(),
                paperPO.getId().toString(),
                paperPO.getName(),
                paperPO.getDescription(),
                paperPO.getPath(),
                paperPO.getScore(),
                finalScore
        );
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生查询自己是否报名该课程方法
    * @DateTime: 2024/3/16 15:39
    * @Params: [studentId, courseId]
    * @Return int
    */
    @Override
    public int StudentCheckIfSignedCourse(Long studentId, Long courseId) {
        return studentMapper.StudentCheckIfSignedCourse(studentId, courseId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生报名课程方法
    * @DateTime: 2024/3/16 17:20
    * @Params: [studentId, courseId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.StudentSignCourseResultVO
    */
    @Override
    public StudentSignCourseResultVO StudentSignCourse(Long studentId, Long courseId) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long relationId = idWorker.nextId();
        int res = studentMapper.StudentSignCourse(relationId, studentId, courseId);
        if(res > 0){
            return new StudentSignCourseResultVO(StudentResultCode.STUDENT_SIGN_COURSE_SUCCESS, "报名课程成功", res);
        }
        else {
            return new StudentSignCourseResultVO(StudentResultCode.STUDENT_SIGN_COURSE_FAIL, "报名课程失败", res);
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取某个课程详细信息方法
    * @DateTime: 2024/3/16 18:46
    * @Params: [courseId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.CourseVO
    */
    @Override
    public CourseVO StudentGetCourseInfo(Long courseId) {
        CoursePO coursePO = studentMapper.StudentGetCourseInfo(courseId);
        return new CourseVO(coursePO.getId().toString(),
                            coursePO.getIcon(),
                            coursePO.getName(),
                            coursePO.getDescription(),
                            coursePO.getTeachby().toString(),
                            TimeTransferUtils.TransferTime2LocalTime(coursePO.getCreated_time()));
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生上传答卷方法
    * @DateTime: 2024/3/16 19:46
    * @Params: [multipartFile]
    * @Return java.lang.String
    */
    @Override
    public String StudentUploadRespondentFile(MultipartFile multipartFile) throws IOException {
        // 判断文件是否为空
        if(multipartFile == null){
            return "null respondent file";
        }
        // 获取文件名
        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = multipartFile.getName();
        // 获取文件后缀
        String suffixName = originalFileName.substring(originalFileName.lastIndexOf('.'));
        // 生成随机字符串
        String rndStr = FileUtils.getRandomString(32);

        // 把文件保存到本地
        FileUtils.saveFile(multipartFile, fileDataFolderPath + examAnswerPaperFolderPath, rndStr + originalFileName);

        return rndStr + originalFileName;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生添加答卷记录方法
    * @DateTime: 2024/3/16 20:09
    * @Params: [examId, studentId, respondentFileName, sha256Value]
    * @Return com.zoom.exam_sys_backend.pojo.vo.StudentAddRespondentResultVO
    */
    @Override
    public StudentAddRespondentResultVO StudentAddRespondent(Long examId, Long studentId, String respondentFileName, String sha256Value) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long respondentId = idWorker.nextId();
        int res = studentMapper.StudentAddRespondent(respondentId, examId, studentId, respondentFileName, -1, sha256Value);
        if(res > 0){
            return new StudentAddRespondentResultVO(StudentResultCode.STUDENT_ADD_RESPONDENT_SUCCESS, "交卷成功");
        }
        else{
            return new StudentAddRespondentResultVO(StudentResultCode.STUDENT_ADD_RESPONDENT_FAIL, "交卷失败");
        }
    }
}
