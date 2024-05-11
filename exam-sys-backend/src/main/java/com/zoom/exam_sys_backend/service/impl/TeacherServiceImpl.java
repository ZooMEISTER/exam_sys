package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.comparator.MyExamVOComparator;
import com.zoom.exam_sys_backend.comparator.TeacherAddCourseVOComparator;
import com.zoom.exam_sys_backend.comparator.TeacherExamVOComparator;
import com.zoom.exam_sys_backend.constant.ExamStatusTeacher;
import com.zoom.exam_sys_backend.constant.ExamSysConstants;
import com.zoom.exam_sys_backend.exception.code.TeacherResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.TeacherMapper;
import com.zoom.exam_sys_backend.pojo.bo.*;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.TeacherService;
import com.zoom.exam_sys_backend.util.*;
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
 * @DateTime 2024/2/14 14:38
 **/

@Service
public class TeacherServiceImpl implements TeacherService {

    @Value("${file.dataFolder}")
    String fileDataFolderPath;

    @Value("${data.examPaperFolder}")
    String examPaperFolderPath;

    @Value("${data.examAnswerPaperFolder}")
    String examAnswerPaperFolderPath;

    @Value("${data.respondentMappingPath}")
    String respondentMappingPath;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TeacherMapper teacherMapper;

    /**
    * @Author: ZooMEISTER
    * @Description: 老师修改个人信息方法
    * @DateTime: 2024/2/14 14:40
    * @Params: [userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @Override
    @Transactional
    public TouristLoginResultVO TeacherUpdateProfile(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, String newPassword) {
        TouristLoginResultVO requestSenderVO = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(userid))), TouristLoginResultVO.class);
        if(requestSenderVO == null){
            // redis中没有用户信息，去mysql中找
            TeacherPO teacherPO = teacherMapper.selectById(userid);
            if(teacherPO == null){
                // 用户不存在
                return new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_FAIL_USER_NOT_EXIST, 0, "0", null, null, null, null, null, null, 0, 0);
            }
            requestSenderVO = new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_SUCCESS,
                    ExamSysConstants.TEACHER_PERMISSION_LEVEL,
                    String.valueOf(userid),
                    teacherPO.getAvatar(),
                    teacherPO.getUsername(),
                    teacherPO.getRealname(),
                    teacherPO.getPhone(),
                    teacherPO.getEmail(),
                    "",
                    teacherPO.getDeleted(),
                    teacherPO.getProfilev());
        }
        int newProfilev = requestSenderVO.getProfilev() + 1;
        int res = 0;

        if(newPassword.equals("NOT_CHANGE")){
            // 不修改密码
            res = teacherMapper.updateTeacherProfileWithoutPassword(userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newProfilev);
        }
        else{
            // 修改密码
            res = teacherMapper.updateById(new TeacherPO(userid, newAvatar, newUsername, newRealname, newPassword, newPhone, newEmail, 0, newProfilev));
        }

        // 用户信息更新失败，直接返回
        if(res < 0){
            return new TouristLoginResultVO(TeacherResultCode.TEACHER_UPDATE_PROFILE_FAIL, 0, "0", null, null, null, null, null, null, 0, 0);
        }

        // 用户信息更新成功
        // 生成新的token
        String newToken = JWTUtils.genAccessToken(userid, newProfilev);
        // 生成新的对象
        TouristLoginResultVO touristLoginResultVO = new TouristLoginResultVO(TeacherResultCode.TEACHER_UPDATE_PROFILE_SUCCESS,
                ExamSysConstants.TEACHER_PERMISSION_LEVEL,
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
    * @Description: 老师获取所有学院信息方法
    * @DateTime: 2024/2/16 18:08
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.DepartmentPO>
    */
    @Override
    public List<DepartmentVO> TeacherGetAllDepartment() {
        List<DepartmentVO> departmentVOList = new ArrayList<>();
        List<DepartmentPO> departmentPOList = teacherMapper.teacherGetAllDepartment();
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
    * @Description: 老师获取某个学院下所有专业方法
    * @DateTime: 2024/2/16 18:09
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.SubjectPO>
    */
    @Override
    public List<SubjectVO> TeacherGetAllSubject(Long departmentId) {
        List<SubjectVO> subjectVOList = new ArrayList<>();
        List<SubjectPO> subjectPOList = teacherMapper.teacherGetAllSubjects(departmentId);
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
    * @Description: 老师获取某个专业下所有课程方法
    * @DateTime: 2024/2/16 18:09
    * @Params: [subjectId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.po.CoursePO>
    */
    @Override
    public List<CourseVO> TeacherGetAllCourse(Long subjectId) {
        // 不推荐使用join，因此在这里用代码逻辑实现多表联合查询
        List<SubjectCourseBO> subjectCoursePOList = teacherMapper.teacherGetAllSubjectCourseRelation(subjectId);
        List<CourseVO> courseVOList = new ArrayList<>();
        for(SubjectCourseBO i : subjectCoursePOList){
            CoursePO coursePO = teacherMapper.teacherGetSingleCourse(i.getCourse_id());
            TeacherPO teacherPO = teacherMapper.TeacherGetTeacherPOById(coursePO.getTeachby());
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
    * @Description: 老师获取某个课程下所有考试方法
    * @DateTime: 2024/3/14 14:52
    * @Params: [courseId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.ExamVO>
    */
    @Override
    public List<TeacherExamVO> TeacherGetAllExam(Long courseId) {
        List<CourseExamBO> courseExamBOList = teacherMapper.teacherGetAllCourseExamRelation(courseId);
        List<TeacherExamVO> teacherExamVOList = new ArrayList<>();

        for(CourseExamBO i : courseExamBOList){
            ExamPO examPO = teacherMapper.teacherGetSingleExam(i.getExam_id());
            int finishedStudentCount = teacherMapper.TeacherGetExamFinishedStudentCount(i.getExam_id());
            int totalStudentCount = teacherMapper.TeacherGetCourseStudentCount(courseId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDateTime = new Date(System.currentTimeMillis());
            int examStatus = -1; // 对老师来说，考试的状态
            if(currentDateTime.before(examPO.getStart_time())){
                examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_NOT_START;
            }
            else if(currentDateTime.after(examPO.getStart_time()) && currentDateTime.before(examPO.getEnd_time())){
                examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_ON_GOING;
            }
            else if(currentDateTime.after(examPO.getEnd_time())){
                examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_ENDED;
            }
            teacherExamVOList.add(new TeacherExamVO(
                    examPO.getId().toString(),
                    examPO.getName(),
                    examPO.getDescription(),
                    TimeTransferUtils.TransferTime2LocalTime(examPO.getStart_time()),
                    TimeTransferUtils.TransferTime2LocalTime(examPO.getEnd_time()),
                    examPO.getTeachby().toString(),
                    examPO.getType(),
                    examPO.getPublished(),
                    TimeTransferUtils.TransferTime2LocalTime(examPO.getCreated_time()),
                    examStatus,
                    finishedStudentCount,
                    totalStudentCount
            ));
        }
        Collections.sort(teacherExamVOList, new TeacherExamVOComparator());
        return teacherExamVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个特定考试的信息方法
    * @DateTime: 2024/3/14 18:07
    * @Params: [examId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ExtendedExamVO
    */
    @Override
    public TeacherExtendedExamVO TeacherGetSingleExamInfo(Long examId) {
        ExamPO examPO = teacherMapper.teacherGetSingleExam(examId);
        CourseExamBO courseExamBO = teacherMapper.teacherGetCourseExamRelationByExamId(examId);
        CoursePO coursePO = teacherMapper.teacherGetSingleCourse(courseExamBO.getCourse_id());
        ExamPaperBO examPaperBO = teacherMapper.teacherGetExamPaperRelationByExamId(examId);
        PaperPO paperPO = teacherMapper.teacherGetPaperInfo(examPaperBO.getPaper_id());
        TeacherPO teacherPO = teacherMapper.TeacherGetTeacherPOById(coursePO.getTeachby());
        Date currentDateTime = new Date(System.currentTimeMillis());
        int examStatus = -1; // 对老师来说，考试的状态
        if(currentDateTime.before(examPO.getStart_time())){
            examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_NOT_START;
        }
        else if(currentDateTime.after(examPO.getStart_time()) && currentDateTime.before(examPO.getEnd_time())){
            examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_ON_GOING;
        }
        else if(currentDateTime.after(examPO.getEnd_time())){
            examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_ENDED;
        }
        return new TeacherExtendedExamVO(examPO.getId().toString(),
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
                examStatus
        );
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师上传考试试卷文件方法
    * @DateTime: 2024/3/15 13:58
    * @Params: [multipartFile]
    * @Return java.lang.String
    */
    @Override
    public String TeacherUploadExamPaperFile(MultipartFile multipartFile) throws Exception {
        // 判断文件是否为空
        if(multipartFile == null){
            return "null paper file";
        }
        // 获取文件名
        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = multipartFile.getName();
        // 获取文件后缀
        String suffixName = originalFileName.substring(originalFileName.lastIndexOf('.'));
        // 生成随机字符串
        String rndStr = FileUtils.getRandomString(32);

        // 把文件保存到本地
        FileUtils.saveFile(multipartFile, fileDataFolderPath + examPaperFolderPath, rndStr + originalFileName);

        // 这个返回的是未加密的试卷文件名
        return rndStr + originalFileName;
    }


    /**
    * @Author: ZooMEISTER
    * @Description: 老师添加考试方法
    * @DateTime: 2024/3/15 15:08
    * @Params: [examName, examDescription, examStartDateTime, examEndDateTime, paperFileName, paperName, paperDescription, paperScore, teachby]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TeacherAddExamResultVO
    */
    @Override
    @Transactional
    public TeacherAddExamResultVO TeacherAddExam(String examName, String examDescription, String examStartDateTime, String examEndDateTime, String paperFileName, String paperName, String paperDescription, int paperScore, Long teachby, Long courseId) throws Exception {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long newPaperId = idWorker.nextId();
        Long newExamId = idWorker.nextId();
        Long relationId = idWorker.nextId();
        Long relationId1 = idWorker.nextId();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date examStartDateTime_Date = formatter.parse(examStartDateTime);
        Date examEndDateTime_Date = formatter.parse(examEndDateTime);

        // 对试卷进行加密
        String aeskey = FileUtils.getRandomString(16);
        AESUtils.aesEncryptFile(
                fileDataFolderPath + examPaperFolderPath + paperFileName,
                fileDataFolderPath + examPaperFolderPath + "e/" + paperFileName,
                aeskey);

        int res1 = teacherMapper.teacherAddNewPaper(newPaperId, paperName, paperDescription, teachby, paperScore, paperFileName, aeskey);
        int res2 = teacherMapper.teacherAddNewExam(newExamId, examName, examDescription, examStartDateTime_Date, examEndDateTime_Date, teachby, 1, 0);
        int res3 = teacherMapper.teacherAddNewExamPaperRelation(relationId, newExamId, newPaperId);
        int res4 = teacherMapper.teacherAddNewCourseExamRelation(relationId1, courseId, newExamId);
        if(res1 > 0 && res2 > 0 && res3 > 0 && res4 > 0){
            return new TeacherAddExamResultVO(TeacherResultCode.TEACHER_ADD_NEW_EXAM_SUCCESS, "考试添加成功");
        }
        else{
            return new TeacherAddExamResultVO(TeacherResultCode.TEACHER_ADD_NEW_EXAM_FAIL, "考试添加失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个课程详细信息方法
    * @DateTime: 2024/3/17 14:43
    * @Params: [courseId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.CourseVO
    */
    @Override
    public TeacherExtendedCourseVO TeacherGetCourseInfo(Long courseId) {
        CoursePO coursePO = teacherMapper.TeacherGetCourseInfo(courseId);
        TeacherPO teacherPO = teacherMapper.TeacherGetTeacherPOById(coursePO.getTeachby());
        int studentCount = teacherMapper.TeacherGetCourseStudentCount(courseId);
        return new TeacherExtendedCourseVO(coursePO.getId().toString(),
                coursePO.getIcon(),
                coursePO.getName(),
                coursePO.getDescription(),
                coursePO.getTeachby().toString(),
                teacherPO.getUsername(),
                teacherPO.getRealname(),
                TimeTransferUtils.TransferTime2LocalTime(coursePO.getCreated_time()),
                studentCount);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个课程下所有报名学生信息的方法
    * @DateTime: 2024/3/17 17:56
    * @Params: [courseId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.StudentVO>
    */
    @Override
    public List<StudentVO> TeacherGetAllCourseSignedStudent(Long courseId) {
        List<CourseStudentBO> courseStudentBOList = teacherMapper.TeacherGetAllCourseStudentRelationByCourseId(courseId);
        List<StudentVO> studentVOList = new ArrayList<>();
        for(CourseStudentBO i : courseStudentBOList){
            StudentPO studentPO = teacherMapper.TeacherGetSingleStudent(i.getStudent_id());
            studentVOList.add(new StudentVO(
                    studentPO.getId().toString(),
                    studentPO.getAvatar(),
                    studentPO.getUsername(),
                    studentPO.getRealname(),
                    studentPO.getPhone(),
                    studentPO.getEmail(),
                    studentPO.getDeleted(),
                    studentPO.getProfilev()));
        }
        return studentVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个考试所有已交卷的答卷，学生信息方法
    * @DateTime: 2024/3/17 21:44
    * @Params: [examId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.FinishedRespondentExamStudentVO>
    */
    @Override
    public List<FinishedRespondentExamStudentVO> TeacherGetAllFinishedRespondentExamStudentInfo(Long examId) {
        List<RespondentExamStudentBO> respondentExamStudentBOList = teacherMapper.TeacherGetAllFinishedRespondentInfo(examId);
        List<FinishedRespondentExamStudentVO> finishedRespondentExamStudentVOList = new ArrayList<>();
        for(RespondentExamStudentBO i : respondentExamStudentBOList){
            StudentPO studentPO = teacherMapper.TeacherGetSingleStudent(i.getStudent_id());
            finishedRespondentExamStudentVOList.add(new FinishedRespondentExamStudentVO(
                    i.getId().toString(),
                    i.getExam_id().toString(),
                    i.getStudent_id().toString(),
                    i.getRespondent_path(),
                    i.getFinal_score(),
                    i.getSha256_code(),
                    i.getCreated_time(),
                    studentPO.getId().toString(),
                    studentPO.getAvatar(),
                    studentPO.getUsername(),
                    studentPO.getRealname(),
                    studentPO.getPhone(),
                    studentPO.getEmail()
            ));
        }
        return finishedRespondentExamStudentVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个考试所有未交卷的答卷，学生信息方法
    * @DateTime: 2024/3/17 21:52
    * @Params: [examId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.StudentVO>
    */
    @Override
    public List<StudentVO> TeacherGetAllUnFinishedRespondentExamStudentInfo(Long examId) {
        Long courseId = teacherMapper.TeacherGetExamCourseIdByExamId(examId);
        List<StudentPO> studentPOList = new ArrayList<>();
        List<CourseStudentBO> courseStudentBOList = teacherMapper.TeacherGetAllCourseStudentRelationByCourseId(courseId);
        for(CourseStudentBO i : courseStudentBOList){
            studentPOList.add(teacherMapper.TeacherGetSingleStudent(i.getStudent_id()));
        }
        List<StudentVO> studentVOList = new ArrayList<>();
        for(StudentPO i : studentPOList){
            if(teacherMapper.TeacherGetStudentRespondentCount(examId, i.getId()) <= 0){
                studentVOList.add(new StudentVO(
                        i.getId().toString(),
                        i.getAvatar(),
                        i.getUsername(),
                        i.getRealname(),
                        i.getPhone(),
                        i.getEmail(),
                        i.getDeleted(),
                        i.getProfilev()
                ));
            }
        }
        return studentVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获得所有我的课程方法
    * @DateTime: 2024/3/18 19:20
    * @Params: [teacherId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.TeacherMyCourseVO>
    */
    @Override
    public List<MyCourseVO> TeacherGetAllMyCourse(Long teacherId) {
        List<CoursePO> coursePOList = teacherMapper.TeacherGetAllMyClass(teacherId);
        List<MyCourseVO> myCourseVOList = new ArrayList<>();
        for(CoursePO i : coursePOList){
            int totalStudentCount = teacherMapper.TeacherGetCourseStudentCount(i.getId());
            int examCount = teacherMapper.TeacherGetCourseExamCountByCourseId(i.getId());
            SubjectCourseBO subjectCourseBO = teacherMapper.TeacherGetSubjectCourseRelationByCourseId(i.getId());
            SubjectPO subjectPO = teacherMapper.TeacherGetSubjectPOById(subjectCourseBO.getSubject_id());
            DepartmentPO departmentPO = teacherMapper.TeacherGetDepartmentPOById(subjectPO.getBelongto());
            TeacherPO teacherPO = teacherMapper.TeacherGetTeacherPOById(teacherId);
            myCourseVOList.add(new MyCourseVO(
                    i.getId().toString(),
                    i.getIcon(),
                    i.getName(),
                    i.getDescription(),
                    i.getTeachby().toString(),
                    TimeTransferUtils.TransferTime2LocalTime(i.getCreated_time()),
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
    * @Description: 老师获取自己的所有考试方法
    * @DateTime: 2024/3/19 15:23
    * @Params: [teacherId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.MyExamVO>
    */
    @Override
    public List<MyExamVO> TeacherGetAllMyExam(Long teacherId) {
        List<MyExamVO> myExamVOList = new ArrayList<>();
        List<CoursePO> coursePOList = teacherMapper.TeacherGetAllMyClass(teacherId);
        for(CoursePO i : coursePOList){
            SubjectCourseBO subjectCourseBO = teacherMapper.TeacherGetSubjectCourseRelationByCourseId(i.getId());
            SubjectPO subjectPO = teacherMapper.TeacherGetSubjectPOById(subjectCourseBO.getSubject_id());
            DepartmentPO departmentPO = teacherMapper.TeacherGetDepartmentPOById(subjectPO.getBelongto());
            List<CourseExamBO> courseExamBOList = teacherMapper.TeacherGetAllCourseExamRelation(i.getId());
            TeacherPO teacherPO = teacherMapper.TeacherGetTeacherPOById(teacherId);
            for(CourseExamBO m : courseExamBOList){
                ExamPO examPO = teacherMapper.teacherGetSingleExam(m.getExam_id());
                int finishedStudentCount = teacherMapper.TeacherGetExamFinishedStudentCount(examPO.getId());
                int totalStudentCount = teacherMapper.TeacherGetCourseStudentCount(i.getId());
                Date currentDateTime = new Date(System.currentTimeMillis());
                int examStatus = 0; // 对学生来说，考试的状态
                if(currentDateTime.before(examPO.getStart_time())){
                    // 该考试未开始
                    examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_NOT_START;
                }
                else if(currentDateTime.after(examPO.getEnd_time())){
                    // 该考试已结束
                    examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_ENDED;
                }
                else{
                    // 该考试正在进行
                    examStatus = ExamStatusTeacher.EXAM_STATUS_TEACHER_ON_GOING;
                }
                int final_score = -1;
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
                        finishedStudentCount,
                        totalStudentCount,
                        departmentPO.getId().toString(),
                        departmentPO.getName(),
                        subjectPO.getId().toString(),
                        subjectPO.getName(),
                        i.getId().toString(),
                        i.getName()
                ));
            }
        }

        Collections.sort(myExamVOList, new MyExamVOComparator());
        return myExamVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师添加新课程申请方法
    * @DateTime: 2024/3/19 20:03
    * @Params: [newCourseIcon, newCourseName, newCourseDescription, teachby]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TeacherAddNewCourseApplyResultVO
    */
    @Override
    @Transactional
    public TeacherAddNewCourseApplyResultVO TeacherAddNewCourse(String newCourseIcon, String newCourseName, String newCourseDescription, Long teachby, Long subjectId) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long applicationId = idWorker.nextId();
        int res = teacherMapper.TeacherAddNewCourse(applicationId, newCourseIcon, newCourseName, newCourseDescription, teachby, subjectId);
        if(res > 0){
            return new TeacherAddNewCourseApplyResultVO(TeacherResultCode.TEACHER_ADD_NEW_COURSE_SUCESS, "添加课程申请成功");
        }
        else{
            return new TeacherAddNewCourseApplyResultVO(TeacherResultCode.TEACHER_ADD_NEW_COURSE_FAIL, "添加课程申请失败,请查看控制台");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取所有自己申请添加课程的申请方法
    * @DateTime: 2024/3/19 21:13
    * @Params: [teacherId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO>
    */
    @Override
    public List<TeacherAddCourseVO> TeacherGetAllMyAddCourseApplication(Long teacherId) {
        List<TeacherAddCourseVO> teacherAddCourseVOList = new ArrayList<>();
        List<TeacherAddCourseBO> teacherAddCourseBOList = teacherMapper.TeacherGetAllMyAddCourseApplication(teacherId);
        for(TeacherAddCourseBO i : teacherAddCourseBOList){
            SubjectPO subjectPO = teacherMapper.TeacherGetSubjectPOById(i.getSubject_id());
            DepartmentPO departmentPO = teacherMapper.TeacherGetDepartmentPOById(subjectPO.getBelongto());
            teacherAddCourseVOList.add(new TeacherAddCourseVO(
                    i.getId().toString(),
                    i.getSubject_id().toString(),
                    i.getIcon(),
                    i.getName(),
                    i.getDescription(),
                    i.getTeachby().toString(),
                    TimeTransferUtils.TransferTime2LocalTime(i.getCreated_time()),
                    i.getApprove_status(),
                    departmentPO.getId().toString(),
                    departmentPO.getName(),
                    subjectPO.getId().toString(),
                    subjectPO.getName()
            ));
        }
        Collections.sort(teacherAddCourseVOList, new TeacherAddCourseVOComparator());
        return teacherAddCourseVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师获取某个考试试卷的aes密钥
    * @DateTime: 2024/4/16 16:37
    * @Params: [paperId]
    * @Return java.lang.String
    */
    @Override
    public String TeacherGetExamAesKey(Long paperId) {
        return teacherMapper.TeacherGetExamAesKey(paperId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师下载考试试卷方法
    * @DateTime: 2024/4/16 18:28
    * @Params: [paperName, response]
    * @Return void
    */
    @Override
    public void TeacherDownloadExamPaper(String paperName, HttpServletResponse response) {
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

    /**
    * @Author: ZooMEISTER
    * @Description: 老师批卷时获取答卷信息方法
    * @DateTime: 2024/4/20 17:26
    * @Params: [respondentId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.RespondentTeacherVO
    */
    @Override
    public RespondentTeacherVO TeacherGetRespondentInfo(Long respondentId) {
        // 获取答卷信息
        RespondentExamStudentBO respondentExamStudentBO = teacherMapper.TeacherGetRespondentInfo(respondentId);
        return new RespondentTeacherVO(respondentExamStudentBO.getId().toString(),
                respondentExamStudentBO.getExam_id().toString(),
                respondentExamStudentBO.getStudent_id().toString(),
                respondentExamStudentBO.getRespondent_path(),
                respondentExamStudentBO.getFinal_score(),
                respondentExamStudentBO.getSha256_code(),
                TimeTransferUtils.TransferTime2LocalTime(respondentExamStudentBO.getCreated_time()),
                respondentExamStudentBO.getIs_sha256_good(),
                TimeTransferUtils.TransferTime2LocalTime(respondentExamStudentBO.getLast_modified_time()));
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 老师批改试卷方法
    * @DateTime: 2024/4/20 20:33
    * @Params: [respondentId, finalScore]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TeacherCorrectRespondentResultVO
    */
    @Override
    public TeacherCorrectRespondentResultVO TeacherCorrectRespondent(Long respondentId, int finalScore) {
        int res = teacherMapper.TeacherCorrectRespondentScore(respondentId, finalScore);
        if(res > 0){
            return new TeacherCorrectRespondentResultVO(TeacherResultCode.TEACHER_CORRECT_RESPONDENT_SUCCESS, "批改成功");
        }
        else{
            return new TeacherCorrectRespondentResultVO(TeacherResultCode.TEACHER_CORRECT_RESPONDENT_FAIL, "批改失败");
        }
    }
}
