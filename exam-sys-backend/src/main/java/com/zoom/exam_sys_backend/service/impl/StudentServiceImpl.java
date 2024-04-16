package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.comparator.ExamVOComparator;
import com.zoom.exam_sys_backend.comparator.MyExamVOComparator;
import com.zoom.exam_sys_backend.comparator.StudentExamVOComparator;
import com.zoom.exam_sys_backend.constant.ExamStatusStudent;
import com.zoom.exam_sys_backend.constant.ExamSysConstants;
import com.zoom.exam_sys_backend.exception.code.StudentResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.StudentMapper;
import com.zoom.exam_sys_backend.pojo.bo.*;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.StudentService;
import com.zoom.exam_sys_backend.util.FileUtils;
import com.zoom.exam_sys_backend.util.JWTUtils;
import com.zoom.exam_sys_backend.util.SnowflakeIdWorker;
import com.zoom.exam_sys_backend.util.TimeTransferUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
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
            TeacherPO teacherPO = studentMapper.StudentGetTeacherPOById(coursePO.getTeachby());
            courseVOList.add(new CourseVO(
                    coursePO.getId().toString(),
                    coursePO.getIcon(),
                    coursePO.getName(),
                    coursePO.getDescription(),
                    coursePO.getTeachby().toString(),
                    teacherPO.getUsername(),
                    teacherPO.getRealname(),
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
        TeacherPO teacherPO = studentMapper.StudentGetTeacherPOById(coursePO.getTeachby());
        int finalScore = -1;
        if(respondentExamStudentBO != null) finalScore = respondentExamStudentBO.getFinal_score();
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

        return new StudentExtendedExamVO(examPO.getId().toString(),
                examPO.getName(),
                examPO.getDescription(),
                TimeTransferUtils.TransferTime2LocalTime(examPO.getStart_time()),
                TimeTransferUtils.TransferTime2LocalTime(examPO.getEnd_time()),
                examPO.getTeachby().toString(),
                examPO.getType(),
                examPO.getPublished(),
                TimeTransferUtils.TransferTime2LocalTime(examPO.getCreated_time()),
                teacherPO.getUsername(),
                teacherPO.getRealname(),
                coursePO.getId().toString(),
                coursePO.getName(),
                paperPO.getId().toString(),
                paperPO.getName(),
                paperPO.getDescription(),
                paperPO.getPath(),
                paperPO.getScore(),
                finalScore,
                examStatus
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
        TeacherPO teacherPO = studentMapper.StudentGetTeacherPOById(coursePO.getTeachby());
        return new CourseVO(
                coursePO.getId().toString(),
                coursePO.getIcon(),
                coursePO.getName(),
                coursePO.getDescription(),
                coursePO.getTeachby().toString(),
                teacherPO.getUsername(),
                teacherPO.getRealname(),
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

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取所有自己参加的课的方法
    * @DateTime: 2024/3/18 21:54
    * @Params: [studentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.MyCourseVO>
    */
    @Override
    public List<MyCourseVO> StudentGetAllMyCourse(Long studentId) {
        List<MyCourseVO> myCourseVOList = new ArrayList<>();
        List<CourseStudentBO> courseStudentBOList = studentMapper.StudentGetAllCourseStudentRelation(studentId);
        for(CourseStudentBO i : courseStudentBOList){
            int totalStudentCount = studentMapper.StudentGetCourseStudentCount(i.getCourse_id());
            int examCount = studentMapper.StudentGetCourseExamCountByCourseId(i.getCourse_id());
            CoursePO coursePO = studentMapper.StudentGetCourseInfo(i.getCourse_id());
            SubjectCourseBO subjectCourseBO = studentMapper.StudentGetSubjectCourseRelationByCourseId(i.getCourse_id());
            SubjectPO subjectPO = studentMapper.StudentGetSubjectPOById(subjectCourseBO.getSubject_id());
            DepartmentPO departmentPO = studentMapper.StudentGetDepartmentPOById(subjectPO.getBelongto());
            TeacherPO teacherPO = studentMapper.StudentGetTeacherPOById(coursePO.getTeachby());
            myCourseVOList.add(new MyCourseVO(
                    coursePO.getId().toString(),
                    coursePO.getIcon(),
                    coursePO.getName(),
                    coursePO.getDescription(),
                    coursePO.getTeachby().toString(),
                    TimeTransferUtils.TransferTime2LocalTime(coursePO.getCreated_time()),
                    teacherPO.getUsername(),
                    teacherPO.getRealname(),
                    totalStudentCount,
                    examCount,
                    departmentPO.getId().toString(),
                    departmentPO.getName(),
                    subjectPO.getId().toString(),
                    subjectPO.getName()
            ));
        }
        return myCourseVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取所有我参加的考试方法
    * @DateTime: 2024/3/19 12:56
    * @Params: [studentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.MyExamVO>
    */
    @Override
    public List<MyExamVO> StudentGetAllMyExam(Long studentId) {
        List<MyExamVO> myExamVOList = new ArrayList<>();
        List<CourseStudentBO> courseStudentBOList = studentMapper.StudentGetAllCourseStudentRelation(studentId);
        for(CourseStudentBO i : courseStudentBOList){
            CoursePO coursePO = studentMapper.StudentGetCourseInfo(i.getCourse_id());
            SubjectCourseBO subjectCourseBO = studentMapper.StudentGetSubjectCourseRelationByCourseId(i.getCourse_id());
            SubjectPO subjectPO = studentMapper.StudentGetSubjectPOById(subjectCourseBO.getSubject_id());
            DepartmentPO departmentPO = studentMapper.StudentGetDepartmentPOById(subjectPO.getBelongto());
            List<CourseExamBO> courseExamBOList = studentMapper.studentGetAllCourseExamRelation(i.getCourse_id());
            TeacherPO teacherPO = studentMapper.StudentGetTeacherPOById(coursePO.getTeachby());
            for(CourseExamBO m : courseExamBOList){
                ExamPO examPO = studentMapper.studentGetSingleExam(m.getExam_id());
                Date currentDateTime = new Date(System.currentTimeMillis());
                int examStatus = 0; // 对学生来说，考试的状态
                int respondentCount = studentMapper.checkIfStudentFinishedExam(examPO.getId(), studentId);
                if(currentDateTime.before(examPO.getStart_time())){
                    // 该考试未开始
                    examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_NOT_START;
                }
                else if(currentDateTime.after(examPO.getEnd_time())){
                    // 该考试已结束
                    if(respondentCount > 0){
                        examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_ENDED_DONE;
                    }
                    else{
                        examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_ENDED_UNDO;
                    }
                }
                else{
                    // 该考试正在进行
                    if(respondentCount > 0){
                        examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_STARTED_DONE;
                    }
                    else{
                        examStatus = ExamStatusStudent.EXAM_STATUS_STUDENT_STARTED_UNDO;
                    }
                }
                int final_score = -1;
                if(respondentCount > 0){
                    final_score = studentMapper.StudentGetRespondentInfo(examPO.getId(), studentId).getFinal_score();
                }
                myExamVOList.add(new MyExamVO(
                        examPO.getId().toString(),
                        examPO.getName(),
                        examPO.getDescription(),
                        TimeTransferUtils.TransferTime2LocalTime(examPO.getStart_time()),
                        TimeTransferUtils.TransferTime2LocalTime(examPO.getEnd_time()),
                        examPO.getTeachby().toString(),
                        examPO.getType(),
                        examPO.getPublished(),
                        TimeTransferUtils.TransferTime2LocalTime(examPO.getCreated_time()),
                        teacherPO.getUsername(),
                        teacherPO.getRealname(),
                        examStatus,
                        final_score,
                        -1,
                        -1,
                        departmentPO.getId().toString(),
                        departmentPO.getName(),
                        subjectPO.getId().toString(),
                        subjectPO.getName(),
                        coursePO.getId().toString(),
                        coursePO.getName()
                ));
            }
        }

        Collections.sort(myExamVOList, new MyExamVOComparator());
        return myExamVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生申请成为老师的方法
    * @DateTime: 2024/3/26 16:01
    * @Params: [studentId, description]
    * @Return com.zoom.exam_sys_backend.pojo.vo.StudentToTeacherResultVO
    */
    @Override
    @Transactional
    public StudentToTeacherResultVO StudentApplyTobeTeacher(Long studentId, String description) {
        int applicationCount = studentMapper.StudentGetToTeacherApplicationCount(studentId, 0);
        if(applicationCount > 0){
            // 已有pending的申请
            return new StudentToTeacherResultVO(StudentResultCode.STUDENT_TO_TEACHER_APPLICATION_ALREADY_EXIST, "已存在等待批准的申请");
        }
        else{
            // 添加申请
            SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
            Long applicationId = idWorker.nextId();
            int res = studentMapper.StudentAddNewToTeacherApplication(applicationId, studentId, description);
            if(res > 0){
                return new StudentToTeacherResultVO(StudentResultCode.STUDENT_TO_TEACHER_APPLICATION_ADDED, "已发送申请,等待管理员批准");
            }
            else{
                return new StudentToTeacherResultVO(StudentResultCode.STUDENT_TO_TEACHER_APPLICATION_ADD_FAIL, "添加申请失败");
            }
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生获取考试试卷aes密钥的方法
    * @DateTime: 2024/4/16 18:33
    * @Params: [paperId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.StudentGetExamPaperAesKeyResultVO
    */
    @Override
    public StudentGetExamPaperAesKeyResultVO StudentGetExamAesKey(Long paperId) {
        ExamPaperBO examPaperBO = studentMapper.StudentGetExamPaperBOByPaperId(paperId);
        ExamPO examPO = studentMapper.studentGetSingleExam(examPaperBO.getExam_id());

        if (examPO.getStart_time().getTime() - System.currentTimeMillis() > ExamSysConstants.AES_KEY_AHEAD_AVAILABLE_TIME) {
            return new StudentGetExamPaperAesKeyResultVO(StudentResultCode.STUDENT_GET_AES_KEY_FAIL, "err", "获取aesKey失败：未到考试时间");
        }
        else {
            String aesKey = studentMapper.StudentGetExamAesKey(paperId);
            return new StudentGetExamPaperAesKeyResultVO(StudentResultCode.STUDENT_GET_AES_KEY_SUCCESS, aesKey, "aesKey获取成功");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 学生下载试卷方法
    * @DateTime: 2024/4/16 18:29
    * @Params: [paperName, response]
    * @Return void
    */
    @Override
    public void StudentDownloadExamPaper(String paperName, HttpServletResponse response) {
        String filePath = fileDataFolderPath + examPaperFolderPath + "e/" + paperName;

        try {
            // path是指想要下载的文件的路径
            File file = new File(filePath);
            // 获取文件名
            String filename = file.getName();
            // 获取文件后缀名
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
