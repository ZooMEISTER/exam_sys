package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.comparator.TeacherExamVOComparator;
import com.zoom.exam_sys_backend.constant.ExamStatusTeacher;
import com.zoom.exam_sys_backend.exception.code.TeacherResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.TeacherMapper;
import com.zoom.exam_sys_backend.pojo.bo.*;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.TeacherService;
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
import java.text.ParseException;
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
                    2,
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
                2,
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
    public String TeacherUploadExamPaperFile(MultipartFile multipartFile) throws IOException {
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
    public TeacherAddExamResultVO TeacherAddExam(String examName, String examDescription, String examStartDateTime, String examEndDateTime, String paperFileName, String paperName, String paperDescription, int paperScore, Long teachby, Long courseId) throws ParseException {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long newPaperId = idWorker.nextId();
        Long newExamId = idWorker.nextId();
        Long relationId = idWorker.nextId();
        Long relationId1 = idWorker.nextId();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date examStartDateTime_Date = formatter.parse(examStartDateTime);
        Date examEndDateTime_Date = formatter.parse(examEndDateTime);

        int res1 = teacherMapper.teacherAddNewPaper(newPaperId, paperName, paperDescription, teachby, paperScore, paperFileName);
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
        int studentCount = teacherMapper.TeacherGetCourseStudentCount(courseId);
        return new TeacherExtendedCourseVO(coursePO.getId().toString(),
                coursePO.getIcon(),
                coursePO.getName(),
                coursePO.getDescription(),
                coursePO.getTeachby().toString(),
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
            myCourseVOList.add(new MyCourseVO(
                    i.getId().toString(),
                    i.getIcon(),
                    i.getName(),
                    i.getDescription(),
                    i.getTeachby().toString(),
                    TimeTransferUtils.TransferTime2LocalTime(i.getCreated_time()),
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
}
