package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.constant.ExamSysConstants;
import com.zoom.exam_sys_backend.exception.code.AdminResultCode;
import com.zoom.exam_sys_backend.exception.code.TeacherResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.AdminMapper;
import com.zoom.exam_sys_backend.pojo.bo.StudentToTeacherBO;
import com.zoom.exam_sys_backend.pojo.bo.SubjectCourseBO;
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

    RedisTemplate redisTemplate;
    AdminMapper adminMapper;

    public AdminServiceImpl(RedisTemplate redisTemplate, AdminMapper adminMapper) {
        this.redisTemplate = redisTemplate;
        this.adminMapper = adminMapper;
    }

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
            if(departmentPO.getDeleted() > 0) continue;  // 排除已经逻辑删除的学院
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
    public ResultVO AdminApproveAddCourseApplication(Long applicationId) {
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
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "课程添加成功");
        }
        else {
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "课程添加失败");
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
    public ResultVO AdminTurnDownAddCourseApplication(Long applicationId) {
        int res = adminMapper.AdminUpdateApplicationStatus("application_add_course", applicationId, 2);
        if(res > 0){
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "已拒绝申请");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "拒绝申请失败");
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
    public ResultVO AdminApproveToTeacherApplication(Long applicationId) {
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
            return  new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "批准申请成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "批准申请失败");
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
    public ResultVO AdminTurnDownToTeacherApplication(Long applicationId) {
        int res = adminMapper.AdminUpdateApplicationStatus("application_to_teacher", applicationId, 2);
        if(res > 0){
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS, "已拒绝该申请");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_APPLICATION_STATUS_FAIL, "拒绝申请失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员搜索名称符合要求的学院方法
    * @DateTime: 2024/5/13 15:06
    * @Params: [searchStr]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.DepartmentVO>
    */
    @Override
    public List<DepartmentVO> AdminSearchDepartment(String searchStr) {
        List<DepartmentVO> departmentVOList = new ArrayList<>();
        List<DepartmentPO> departmentPOList = adminMapper.AdminSearchDepartment(searchStr);
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
    * @Description: 管理员添加新学院方法
    * @DateTime: 2024/5/13 17:56
    * @Params: [newDepartmentName, newDepartmentIcon, newDepartmentDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    @Transactional
    public ResultVO AdminAddNewDepartment(String newDepartmentName, String newDepartmentIcon, String newDepartmentDescription) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long newDepartmentId = idWorker.nextId();
        int res = adminMapper.AdminAddNewDepartment(newDepartmentId, newDepartmentName, newDepartmentIcon, newDepartmentDescription);
        if(res > 0){
            return new ResultVO(AdminResultCode.ADMIN_INSERT_DEPARTMENT_SUCCESS, "添加学院成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_INSERT_DEPARTMENT_FAIL, "添加学院失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新学院信息方法
    * @DateTime: 2024/5/13 17:09
    * @Params: [newDepartmentName, newDepartmentIcon, newDepartmentDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminUpdateDepartmentInfo(Long departmentId, String newDepartmentName, String newDepartmentIcon, String newDepartmentDescription) {
        int res = adminMapper.AdminUpdateDepartmentInfo(departmentId, newDepartmentName, newDepartmentIcon, newDepartmentDescription);
        if(res > 0){
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_DEPARTMENT_INFO_SUCCESS, "学院信息更新成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_DEPARTMENT_INFO_FAIL, "学院信息更新失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员删除学院方法
    * @DateTime: 2024/5/13 18:24
    * @Params: [departmentId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminDeleteDepartment(Long departmentId) {
        int res = adminMapper.AdminDeleteDepartment(departmentId);
        if(res > 0){
            return new ResultVO(AdminResultCode.ADMIN_DELETE_DEPARTMENT_SUCCESS, "删除学院成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_DELETE_DEPARTMENT_FAIL, "删除学院失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取专业管理所有专业方法
    * @DateTime: 2024/5/14 0:15
    * @Params: [departmentId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminSubjectManagementVO>
    */
    @Override
    public List<AdminSubjectManagementVO> AdminGetSubjectManagement(String departmentId) {
        List<AdminSubjectManagementVO> adminSubjectManagementVOList = new ArrayList<>();
        String d = departmentId.equals("all") ? "" : departmentId;
        List<SubjectPO> subjectPOList = adminMapper.AdminGetSubjectManagement(d);
        for(SubjectPO i : subjectPOList){
            DepartmentPO departmentPO = adminMapper.AdminGetDepartmentPOById(i.getBelongto());
            if(departmentPO.getDeleted() > 0) continue; // 跳过隶属于已逻辑删除的学院的专业
            adminSubjectManagementVOList.add(new AdminSubjectManagementVO(
                    i.getId().toString(),
                    i.getIcon(),
                    i.getName(),
                    i.getDescription(),
                    i.getBelongto().toString(),
                    i.getCourse_count(),
                    departmentPO.getName()
            ));
        }
        return adminSubjectManagementVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新专业信息方法
    * @DateTime: 2024/5/14 0:50
    * @Params: [subjectId, newSubjectIcon, newSubjectName, newSubjectDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminUpdateSubjectInfo(Long subjectId, String newSubjectIcon, String newSubjectName, String newSubjectDescription) {
        int res = adminMapper.AdminUpdateSubjectInfo(subjectId, newSubjectIcon, newSubjectName, newSubjectDescription);
        if(res > 0){
            return  new ResultVO(AdminResultCode.ADMIN_UPDATE_SUBJECT_INFO_SUCCESS, "更新专业信息成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_SUBJECT_INFO_FAIL, "更新专业信息失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员添加新专业方法
    * @DateTime: 2024/5/14 1:11
    * @Params: [belongto, newSubjectIcon, newSubjectName, newSubjectDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    @Transactional
    public ResultVO AdminAddNewSubject(Long belongto, String newSubjectIcon, String newSubjectName, String newSubjectDescription) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long newSubjectId = idWorker.nextId();
        int res1 = adminMapper.AdminAddNewSubject(newSubjectId, newSubjectIcon, newSubjectName, newSubjectDescription, belongto);
        int res2 = adminMapper.AdminDepartmentSubjectCountIncrement(belongto);
        if(res1 > 0 && res2 > 0){
            return  new ResultVO(AdminResultCode.ADMIN_INSERT_SUBJECT_SUCCESS, "添加专业成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_INSERT_SUBJECT_FAIL, "添加专业失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员删除专业方法
    * @DateTime: 2024/5/14 1:25
    * @Params: [subjectId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminDeleteSubject(Long subjectId) {
        int res1 = adminMapper.AdminDeletedSubject(subjectId);
        SubjectPO subjectPO = adminMapper.AdminGetSubjectPOById(subjectId);
        int res2 = adminMapper.AdminDepartmentSubjectCountDecrement(subjectPO.getBelongto());
        if(res1 > 0 && res2 > 0){
            return  new ResultVO(AdminResultCode.ADMIN_DELETE_SUBJECT_SUCCESS, "删除专业成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_DELETE_SUBJECT_FAIL, "删除专业失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员根据条件筛选课程方法
    * @DateTime: 2024/5/14 21:35
    * @Params: [departmentId, subjectId, teacherId]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.AdminCourseManagementVO>
    */
    @Override
    public List<AdminCourseManagementVO> AdminGetCourseByConditions(String departmentId, String subjectId, String teacherId) {
        List<AdminCourseManagementVO> adminCourseManagementVOList = new ArrayList<>();
        String d = departmentId.equals("all") ? "" : departmentId;
        String s = subjectId.equals("all") ? "" : subjectId;
        String t = teacherId.equals("all") ? "" : teacherId;
        List<CoursePO> coursePOList = adminMapper.AdminGetCourseByTeachby(t);
        for(CoursePO i : coursePOList){
            SubjectCourseBO subjectCourseBO = adminMapper.AdminGetSubjectCourseBOByCourseId(i.getId());
            SubjectPO subjectPO = adminMapper.AdminGetSubjectPOById(subjectCourseBO.getSubject_id());
            DepartmentPO departmentPO = adminMapper.AdminGetDepartmentPOById(subjectPO.getBelongto());
            TeacherPO teacherPO = adminMapper.AdminGetTeacherPOById(i.getTeachby());
            if(!s.equals("") && !String.valueOf(subjectCourseBO.getSubject_id()).equals(s)) continue;
            adminCourseManagementVOList.add(new AdminCourseManagementVO(
                    i.getId().toString(),
                    i.getIcon(),
                    i.getName(),
                    i.getDescription(),
                    i.getTeachby().toString(),
                    TimeTransferUtils.TransferTime2LocalTime(i.getCreated_time()),
                    departmentPO.getId().toString(),
                    departmentPO.getName().toString(),
                    subjectPO.getId().toString(),
                    subjectPO.getName(),
                    teacherPO.getRealname()
            ));
        }

        return adminCourseManagementVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新课程信息方法
    * @DateTime: 2024/5/15 17:30
    * @Params: [courseId, newCourseIcon, newCourseName, newCourseDescription]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminUpdateCourseInfo(Long courseId, String newCourseIcon, String newCourseName, String newCourseDescription) {
        int res = adminMapper.AdminUpdateCourseInfo(courseId, newCourseIcon, newCourseName, newCourseDescription);
        if(res > 0){
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_COURSE_INFO_SUCCESS, "更新课程信息成功");
        }
        else {
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_COURSE_INFO_FAIL, "更新课程信息失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员添加课程方法
    * @DateTime: 2024/5/15 17:31
    * @Params: [belongto, newCourseIcon, newCourseName, newCourseDescription, teachby]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    @Transactional
    public ResultVO AdminAddNewCourse(Long belongto, String newCourseIcon, String newCourseName, String newCourseDescription, Long teachby) {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        Long newCourseId = idWorker.nextId();
        Long newRelationId = idWorker.nextId();
        int res1 = adminMapper.AdminAddNewCourse(newCourseId, newCourseIcon, newCourseName, newCourseDescription, teachby);
        int res2 = adminMapper.AdminAddSubjectCourseRelation(newRelationId, belongto, newCourseId);
        int res3 = adminMapper.AdminPlusOne2SubjectCourseCount(belongto);

        if(res1 > 0 && res2 > 0 && res3 > 0){
            return new ResultVO(AdminResultCode.ADMIN_INSERT_COURSE_SUCCESS, "添加课程成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_INSERT_COURSE_FAIL, "添加课程失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员删除课程方法
    * @DateTime: 2024/5/16 12:07
    * @Params: [courseId]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminDeleteCourse(Long courseId) {
        int res1 = adminMapper.AdminDeletedCourse(courseId);
        SubjectCourseBO subjectCourseBO = adminMapper.AdminGetSubjectCourseBOByCourseId(courseId);
        int res2 = adminMapper.AdminMinusOne2SubjectCourseCount(subjectCourseBO.getSubject_id());
        if(res1 > 0 && res2 > 0){
            return new ResultVO(AdminResultCode.ADMIN_DELETE_COURSE_SUCCESS, "删除课程成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_DELETE_COURSE_FAIL, "删除课程失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取老师用户信息方法
    * @DateTime: 2024/5/18 16:36
    * @Params: [searchStr]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.TeacherVO>
    */
    @Override
    public List<TeacherVO> AdminGetTeacherInfo(String searchStr) {
        List<TeacherVO> teacherVOList = new ArrayList<>();
        List<TeacherPO> teacherPOList = adminMapper.AdminGetTeacherInfo(searchStr);
        for(TeacherPO i : teacherPOList){
            teacherVOList.add(new TeacherVO(
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
        return teacherVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新老师用户信息方法
    * @DateTime: 2024/5/18 17:33
    * @Params: [curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail, resetPwd]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminUpdateTeacherInfo(Long curUserId, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, Boolean resetPwd) {
        if(redisTemplate.opsForValue().get(curUserId.toString()) != null){
            redisTemplate.delete(curUserId.toString());
        }
        int res1 = adminMapper.AdminUpdateTeacherProfile(curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail);
        int res2 = 1;
        if(resetPwd){
            res2 = 0;
            res2 = adminMapper.AdminResetTeacherPassword(curUserId, ExamSysConstants.RESET_PASSWORD_DEFAULT_PASSWORD);
        }

        if(res1 > 0 && res2 > 0){
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_TEACHER_PROFILE_SUCCESS, "更新老师用户信息成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_TEACHER_PROFILE_FAIL, "更新老师用户信息失败");
        }
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员获取学生用户信息方法
    * @DateTime: 2024/5/18 17:11
    * @Params: [searchStr]
    * @Return java.util.List<com.zoom.exam_sys_backend.pojo.vo.StudentVO>
    */
    @Override
    public List<StudentVO> AdminGetStudentInfo(String searchStr) {
        List<StudentVO> studentVOList = new ArrayList<>();
        List<StudentPO> studentPOList = adminMapper.AdminGetStudentInfo(searchStr);
        for(StudentPO i : studentPOList){
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
        return studentVOList;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 管理员更新学生用户信息方法
    * @DateTime: 2024/5/18 17:34
    * @Params: [curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail, resetPwd]
    * @Return com.zoom.exam_sys_backend.pojo.vo.ResultVO
    */
    @Override
    public ResultVO AdminUpdateStudentInfo(Long curUserId, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, Boolean resetPwd) {
        if(redisTemplate.opsForValue().get(curUserId.toString()) != null){
            redisTemplate.delete(curUserId.toString());
        }
        int res1 = adminMapper.AdminUpdateStudentProfile(curUserId, newAvatar, newUsername, newRealname, newPhone, newEmail);
        int res2 = 1;
        if(resetPwd){
            res2 = 0;
            res2 = adminMapper.AdminResetStudentPassword(curUserId, ExamSysConstants.RESET_PASSWORD_DEFAULT_PASSWORD);
        }

        if(res1 > 0 && res2 > 0){
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_STUDENT_PROFILE_SUCCESS, "更新学生用户信息成功");
        }
        else{
            return new ResultVO(AdminResultCode.ADMIN_UPDATE_STUDENT_PROFILE_FAIL, "更新学生用户信息失败");
        }
    }
}
