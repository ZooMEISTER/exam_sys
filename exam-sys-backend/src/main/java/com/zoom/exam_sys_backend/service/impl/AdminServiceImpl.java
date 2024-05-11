package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.constant.ExamSysConstants;
import com.zoom.exam_sys_backend.exception.code.AdminResultCode;
import com.zoom.exam_sys_backend.exception.code.TeacherResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.AdminMapper;
import com.zoom.exam_sys_backend.pojo.bo.StudentToTeacherBO;
import com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO;
import com.zoom.exam_sys_backend.pojo.po.*;
import com.zoom.exam_sys_backend.pojo.vo.*;
import com.zoom.exam_sys_backend.service.AdminService;
import com.zoom.exam_sys_backend.util.JWTUtils;
import com.zoom.exam_sys_backend.util.SnowflakeIdWorker;
import com.zoom.exam_sys_backend.util.TimeTransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 19:23
 **/

@Service
public class AdminServiceImpl implements AdminService {

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
    AdminMapper adminMapper;


    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新个人信息方法
    * @DateTime: 2024/4/21 4:07
    * @Params: [userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newPassword]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @Override
    public TouristLoginResultVO AdminUpdateProfile(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, String newPassword) {
        TouristLoginResultVO requestSenderVO = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(userid))), TouristLoginResultVO.class);
        if(requestSenderVO == null){
            // redis中没有用户信息，去mysql中找
            AdminPO adminPO = adminMapper.selectById(userid);
            if(adminPO == null){
                // 用户不存在
                return new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_FAIL_USER_NOT_EXIST, 0, "0", null, null, null, null, null, null, 0, 0);
            }
            requestSenderVO = new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_SUCCESS,
                    ExamSysConstants.ADMIN_PERMISSION_LEVEL,
                    String.valueOf(userid),
                    adminPO.getAvatar(),
                    adminPO.getUsername(),
                    adminPO.getRealname(),
                    adminPO.getPhone(),
                    adminPO.getEmail(),
                    "",
                    adminPO.getDeleted(),
                    adminPO.getProfilev());
        }
        int newProfilev = requestSenderVO.getProfilev() + 1;
        int res = 0;

        if(newPassword.equals("NOT_CHANGE")){
            // 不修改密码
            res = adminMapper.updateAdminProfileWithoutPassword(userid, newAvatar, newUsername, newRealname, newPhone, newEmail, newProfilev);
        }
        else{
            // 修改密码
            res = adminMapper.updateById(new AdminPO(userid, newAvatar, newUsername, newRealname, newPassword, newPhone, newEmail, 0, newProfilev));
        }

        // 用户信息更新失败，直接返回
        if(res < 0){
            return new TouristLoginResultVO(AdminResultCode.ADMIN_UPDATE_PROFILE_FAIL, 0, "0", null, null, null, null, null, null, 0, 0);
        }

        // 用户信息更新成功
        // 生成新的token
        String newToken = JWTUtils.genAccessToken(userid, newProfilev);
        // 生成新的对象
        TouristLoginResultVO touristLoginResultVO = new TouristLoginResultVO(AdminResultCode.ADMIN_UPDATE_PROFILE_SUCCESS,
                ExamSysConstants.ADMIN_PERMISSION_LEVEL,
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
    * @Description: 管理员获取所有学院简易信息方法
    * @DateTime: 2024/5/11 17:25
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SimpleObjectInfo>
    */
    @Override
    public List<SimpleObjectInfo> AdminGetAllSimpleDepartmentInfo() {
        return adminMapper.AdminGetAllDepartmentSimpleInfo();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取指定学院下所有专业简易信息方法
    * @DateTime: 2024/5/11 17:28
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SimpleObjectInfo>
    */
    @Override
    public List<SimpleObjectInfo> AdminGetSimpleSubjectInfo(Long departmentId) {
        return adminMapper.AdminGetSubjectSimpleInfo(departmentId);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取所有添加课程申请的方法
    * @DateTime: 2024/5/11 17:29
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.SimpleObjectInfo>
    */
    @Override
    public List<SimpleObjectInfo> AdminGetAllSimpleTeacherInfo() {
        return adminMapper.AdminGetAllTeacherSimpleInfo();
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员筛选所有添加课程请求方法
    * @DateTime: 2024/5/11 18:02
    * @Params: [departmentId, subjectId, teacherId, applicationStatus]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminAddCourseApplicationVO>
    */
    @Override
    public List<AdminAddCourseApplicationVO> AdminGetAddCourseApplication(String departmentId, String subjectId, String teacherId, int applicationStatus) {
        List<AdminAddCourseApplicationVO> adminAddCourseApplicationVOList = new ArrayList<>();
        String d = departmentId.equals("all") ? "" : departmentId.toString();
        String s = subjectId.equals(("all")) ? "" : subjectId.toString();
        String t = teacherId.equals(("all")) ? "" : teacherId.toString();
        String a = applicationStatus == -1 ? "" : String.valueOf(applicationStatus);
        List<TeacherAddCourseBO> teacherAddCourseBOList = adminMapper.AdminGetAddCourseApplication(d, s, t, a);

        for(TeacherAddCourseBO i : teacherAddCourseBOList){
            SubjectPO subjectPO = adminMapper.AdminGetSubjectPOById(i.getSubject_id());
            DepartmentPO departmentPO = adminMapper.AdminGetDepartmentPOById(subjectPO.getBelongto());
            TeacherPO teacherPO = adminMapper.AdminGetTeacherPOById(i.getTeachby());
            adminAddCourseApplicationVOList.add(new AdminAddCourseApplicationVO(
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
                    subjectPO.getName(),
                    teacherPO.getAvatar(),
                    teacherPO.getUsername(),
                    teacherPO.getRealname(),
                    teacherPO.getPhone(),
                    teacherPO.getEmail()
            ));
        }

        return adminAddCourseApplicationVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取所有添加课程申请的方法
    * @DateTime: 2024/5/8 12:44
    * @Params: []
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.bo.TeacherAddCourseBO>
    */
    @Override
    public List<AdminAddCourseApplicationVO> AdminGetAllAddCourseApplication() {
        List<AdminAddCourseApplicationVO> adminAddCourseApplicationVOList = new ArrayList<>();
        List<TeacherAddCourseBO> teacherAddCourseBOList = adminMapper.AdminGetAllAddCourseApplication();

        for(TeacherAddCourseBO i : teacherAddCourseBOList){
            SubjectPO subjectPO = adminMapper.AdminGetSubjectPOById(i.getSubject_id());
            DepartmentPO departmentPO = adminMapper.AdminGetDepartmentPOById(subjectPO.getBelongto());
            TeacherPO teacherPO = adminMapper.AdminGetTeacherPOById(i.getTeachby());
            adminAddCourseApplicationVOList.add(new AdminAddCourseApplicationVO(
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
                    subjectPO.getName(),
                    teacherPO.getAvatar(),
                    teacherPO.getUsername(),
                    teacherPO.getRealname(),
                    teacherPO.getPhone(),
                    teacherPO.getEmail()
            ));
        }

        return adminAddCourseApplicationVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员批准添加课程申请方法
    * @DateTime: 2024/5/8 14:38
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @Override
    @Transactional
    public AdminApplicationManageResultVO AdminApproveAddCourseApplication(Long applicationId) {
        TeacherAddCourseBO teacherAddCourseBO = adminMapper.AdminGetAddCourseApplicationBOById(applicationId);
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long newCourseId = idWorker.nextId();
        Long newRelationId = idWorker.nextId();
        int res1 = adminMapper.AdminInsertNewCourse(
                newCourseId,
                teacherAddCourseBO.getIcon(),
                teacherAddCourseBO.getName(),
                teacherAddCourseBO.getDescription(),
                teacherAddCourseBO.getTeachby()
        );
        int res2 = adminMapper.AdminInsertSubjectCourseBO(
                newRelationId,
                teacherAddCourseBO.getSubject_id(),
                newCourseId
        );
        int res3 = adminMapper.AdminPlusOne2SubjectCourseCount(teacherAddCourseBO.getSubject_id());
        int res4 = adminMapper.AdminUpdateApplicationStatus("application_add_course", teacherAddCourseBO.getId(), 1);
        if(res1 > 0 && res2 > 0 && res3 > 0 && res4 > 0){
            return new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "课程添加成功");
        }
        else {
            return new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "课程添加失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员拒绝添加课程申请方方法
    * @DateTime: 2024/5/8 14:36
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @Override
    @Transactional
    public AdminApplicationManageResultVO AdminTurnDownAddCourseApplication(Long applicationId) {
        int res = adminMapper.AdminUpdateApplicationStatus("application_add_course", applicationId, 2);
        if(res > 0){
            return new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "已拒绝申请");
        }
        else{
            return new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "拒绝申请失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取某个状态的成为老师申请方法
    * @DateTime: 2024/5/11 20:39
    * @Params: [applicationStatus]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminToTeacherApplicationVO>
    */
    @Override
    public List<AdminToTeacherApplicationVO> AdminGetBeTeacherApplication(int applicationStatus) {
        List<AdminToTeacherApplicationVO> adminToTeacherApplicationVOList = new ArrayList<>();
        String a = applicationStatus == -1 ? "" : String.valueOf(applicationStatus);
        List<StudentToTeacherBO> studentToTeacherBOList = adminMapper.AdminGetToTeacherApplication(a);
        for(StudentToTeacherBO i : studentToTeacherBOList){
            StudentPO studentPO = adminMapper.AdminGetStudentPOById(i.getStudent_id());
            adminToTeacherApplicationVOList.add(new AdminToTeacherApplicationVO(
                    i.getId().toString(),
                    i.getStudent_id().toString(),
                    i.getDescription(),
                    i.getApprove_status(),
                    TimeTransferUtils.TransferTime2LocalTime(i.getApply_time()),
                    studentPO.getAvatar(),
                    studentPO.getUsername(),
                    studentPO.getRealname(),
                    studentPO.getPhone(),
                    studentPO.getEmail()
            ));
        }
        return adminToTeacherApplicationVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员批准成为老师申请方法
    * @DateTime: 2024/5/11 22:10
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @Override
    @Transactional
    public AdminApplicationManageResultVO AdminApproveToTeacherApplication(Long applicationId) {
        StudentToTeacherBO studentToTeacherBO = adminMapper.AdminGetToTeacherBOById(applicationId);
        StudentPO studentPO = adminMapper.AdminGetStudentPOById(studentToTeacherBO.getStudent_id());
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long newTeacherId = idWorker.nextId();
        int res1 = adminMapper.AdminInsertNewTeacher(
                newTeacherId,
                studentPO.getAvatar(),
                studentPO.getUsername(),
                studentPO.getRealname(),
                studentPO.getPassword(),
                studentPO.getPhone(),
                studentPO.getEmail()
        );
        int res2 = adminMapper.AdminDeleteStudent(studentToTeacherBO.getStudent_id());
        int res3 = adminMapper.AdminUpdateApplicationStatus("application_to_teacher", applicationId, 1);
        redisTemplate.opsForValue().getAndDelete(studentPO.getId().toString());
        redisTemplate.opsForValue().set(newTeacherId.toString(), new TouristLoginResultVO(
                12000,
                2,
                newTeacherId.toString(),
                studentPO.getAvatar(),
                studentPO.getUsername(),
                studentPO.getRealname(),
                studentPO.getPhone(),
                studentPO.getEmail(),
                "token",
                0,
                studentPO.getProfilev()
        ));
        if(res1 > 0 && res2 > 0 && res3 > 0){
            return  new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "批准申请成功");
        }
        else{
            return new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "批准申请失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员拒绝学生成为老师申请方法
    * @DateTime: 2024/5/11 22:10
    * @Params: [applicationId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.AdminApplicationManageResultVO
    */
    @Override
    @Transactional
    public AdminApplicationManageResultVO AdminTurnDownToTeacherApplication(Long applicationId) {
        int res = adminMapper.AdminUpdateApplicationStatus("application_to_teacher", applicationId, 2);
        if(res > 0){
            return new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "已拒绝该申请");
        }
        else{
            return new AdminApplicationManageResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "拒绝申请失败");
        }
    }
}
