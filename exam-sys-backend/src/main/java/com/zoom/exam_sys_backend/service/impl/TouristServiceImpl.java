package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.constant.ExamSysConstants;
import com.zoom.exam_sys_backend.exception.InvalidProfilevException;
import com.zoom.exam_sys_backend.exception.NoPermissionException;
import com.zoom.exam_sys_backend.exception.UserNotExistException;
import com.zoom.exam_sys_backend.exception.code.InterceptorResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.*;
import com.zoom.exam_sys_backend.pojo.po.AdminPO;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import com.zoom.exam_sys_backend.pojo.po.SuperAdminPO;
import com.zoom.exam_sys_backend.pojo.po.TeacherPO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristRegisterResultVO;
import com.zoom.exam_sys_backend.service.TouristService;
import com.zoom.exam_sys_backend.util.JWTUtils;
import com.zoom.exam_sys_backend.util.SnowflakeIdWorker;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/1 11:52
 **/

@Service
public class TouristServiceImpl implements TouristService {


    private RedisTemplate<String, Object> redisTemplate;
    private TouristMapper touristMapper;
    private SuperAdminMapper superAdminMapper;
    private AdminMapper adminMapper;
    private TeacherMapper teacherMapper;
    private StudentMapper studentMapper;

    public TouristServiceImpl(RedisTemplate<String, Object> redisTemplate, TouristMapper touristMapper, SuperAdminMapper superAdminMapper, AdminMapper adminMapper, TeacherMapper teacherMapper, StudentMapper studentMapper) {
        this.redisTemplate = redisTemplate;
        this.touristMapper = touristMapper;
        this.superAdminMapper = superAdminMapper;
        this.adminMapper = adminMapper;
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 检查该用户名是否可用
    * @DateTime: 2024/1/1 12:37
    * @Params: [username]
    * @Return java.lang.Boolean
    */
    @Override
    public Boolean IsUsernameAvailable(String username, String tablename) {
        int count = touristMapper.IsUsernameAvailable(username, tablename);
        return (count == 0);
    }


    /**
    * @Author: ZooMEISTER
    * @Description: 通过 userid 查找用户信息
    * @DateTime: 2024/1/24 21:29
    * @Params: [userid]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @Override
    public TouristLoginResultVO SearchUserByUserid(Long userid, String token) {
        SuperAdminPO superAdminPO = superAdminMapper.selectById(userid);
        AdminPO adminPO = adminMapper.selectById(userid);
        TeacherPO teacherPO = teacherMapper.selectById(userid);
        StudentPO studentPO = studentMapper.selectById(userid);

        if(superAdminPO != null){
            // 超级管理员表中有对应记录
            return new TouristLoginResultVO(
                    TouristResultCode.TOURIST_LOGIN_SUCCESS,
                    ExamSysConstants.SUPER_ADMIN_PERMISSION_LEVEL,
                    String.valueOf(superAdminPO.getId()),
                    superAdminPO.getAvatar(),
                    superAdminPO.getUsername(),
                    superAdminPO.getRealname(),
                    superAdminPO.getPhone(),
                    superAdminPO.getEmail(),
                    token,
                    superAdminPO.getDeleted(),
                    superAdminPO.getProfilev()
            );
        }
        else if(adminPO != null){
            // 管理员表中有对应记录
            return new TouristLoginResultVO(
                    TouristResultCode.TOURIST_LOGIN_SUCCESS,
                    ExamSysConstants.ADMIN_PERMISSION_LEVEL,
                    String.valueOf(adminPO.getId()),
                    adminPO.getAvatar(),
                    adminPO.getUsername(),
                    adminPO.getRealname(),
                    adminPO.getPhone(),
                    adminPO.getEmail(),
                    token,
                    adminPO.getDeleted(),
                    adminPO.getProfilev()
            );
        }
        else if(teacherPO != null){
            // 老师表中有对应记录
            return new TouristLoginResultVO(
                    TouristResultCode.TOURIST_LOGIN_SUCCESS,
                    ExamSysConstants.TEACHER_PERMISSION_LEVEL,
                    String.valueOf(teacherPO.getId()),
                    teacherPO.getAvatar(),
                    teacherPO.getUsername(),
                    teacherPO.getRealname(),
                    teacherPO.getPhone(),
                    teacherPO.getEmail(),
                    token,
                    teacherPO.getDeleted(),
                    teacherPO.getProfilev()
            );
        }
        else if(studentPO != null){
            // 学生表中有对应记录
            return new TouristLoginResultVO(
                    TouristResultCode.TOURIST_LOGIN_SUCCESS,
                    ExamSysConstants.STUDENT_PERMISSION_LEVEL,
                    String.valueOf(studentPO.getId()),
                    studentPO.getAvatar(),
                    studentPO.getUsername(),
                    studentPO.getRealname(),
                    studentPO.getPhone(),
                    studentPO.getEmail(),
                    token,
                    studentPO.getDeleted(),
                    studentPO.getProfilev()
            );
        }

        return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_USER_NOT_EXIST, 0, "0", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", 0, 0);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客注册 新用户注册均为学生角色，需要申请以升级角色
    * @DateTime: 2024/1/1 12:14
    * @Params: [avatar, username, password]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristVO
    */
    @Override
    public TouristRegisterResultVO InsertRegisterTourist(String avatar, String username, String password) {
        // 检查用户名是否可用 三个角色的表都要查
        if(!IsUsernameAvailable(username, "user_student") || !IsUsernameAvailable(username, "user_teacher") || !IsUsernameAvailable(username, "user_admin")){
            return new TouristRegisterResultVO(TouristResultCode.TOURIST_REGISTER_FAIL_USERNAME_ALREADY_EXIST , "用户名已存在");
        }
        // 雪花算法生成id
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        long id = idWorker.nextId();
        // 插入数据库
        int insertResult = touristMapper.insert(new StudentPO(
                id, avatar, username, "default", password, "default", "default", 0, 0
        ));
        //返回 VO
        return new TouristRegisterResultVO((insertResult > 0) ? TouristResultCode.TOURIST_REGISTER_SUCCESS : TouristResultCode.TOURIST_REGISTER_FAIL_OTHER_REASON , String.valueOf(id));
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 游客登陆
    * @DateTime: 2024/1/16 20:42
    * @Params: [username, password]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginVO
    */
    @Override
    public TouristLoginResultVO TouristLogin(String username, String password) {
        // 获取到登录用户名，需要先去数据库的每一张表中查找
        // 如果在管理员表中找到，那么就是管理员账户
        // 如果在老师表中找到，那么就是老师账户
        // 如果在学生表中找到，那么就是学生账户
        // 实际上返回的对象中只有permissionLevel有区别

        // 这个是用户登录后服务端生成的 JWT token
        // 调用 JWTUtil 来处理这个
        String token = "";
        TouristLoginResultVO touristLoginResultVO = null;
        boolean correctPassword = false;

        if(!IsUsernameAvailable(username, "user_super_admin")){
            // 超级管理员表中有对应记录
            // 获取表中记录
            SuperAdminPO superAdminPO = superAdminMapper.getSuperAdminPOByUsername(username);
            // 比对密码
            if(password.equals(superAdminPO.getPassword())){
                token = JWTUtils.genAccessToken(superAdminPO.getId(), superAdminPO.getProfilev());
                correctPassword = true;
                touristLoginResultVO = new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        ExamSysConstants.SUPER_ADMIN_PERMISSION_LEVEL,
                        String.valueOf(superAdminPO.getId()),
                        superAdminPO.getAvatar(),
                        superAdminPO.getUsername(),
                        superAdminPO.getRealname(),
                        superAdminPO.getPhone(),
                        superAdminPO.getEmail(),
                        token,
                        superAdminPO.getDeleted(),
                        superAdminPO.getProfilev()
                );
            }
        }
        else if(!IsUsernameAvailable(username, "user_admin")){
            // 管理员表中有对应记录
            // 获取表中记录
            AdminPO adminPO = adminMapper.getAdminPOByUsername(username);
            // 比对密码
            if(password.equals(adminPO.getPassword())){
                token = JWTUtils.genAccessToken(adminPO.getId(), adminPO.getProfilev());
                correctPassword = true;
                touristLoginResultVO = new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        ExamSysConstants.ADMIN_PERMISSION_LEVEL,
                        String.valueOf(adminPO.getId()),
                        adminPO.getAvatar(),
                        adminPO.getUsername(),
                        adminPO.getRealname(),
                        adminPO.getPhone(),
                        adminPO.getEmail(),
                        token,
                        adminPO.getDeleted(),
                        adminPO.getProfilev()
                );
            }
        }
        else if(!IsUsernameAvailable(username, "user_teacher")){
            // 老师表中有对应记录
            // 获取表中记录
            TeacherPO teacherPO = teacherMapper.getTeacherPOByUsername(username);
            // 比对密码
            if(password.equals(teacherPO.getPassword())){
                token = JWTUtils.genAccessToken(teacherPO.getId(), teacherPO.getProfilev());
                correctPassword = true;
                touristLoginResultVO = new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        ExamSysConstants.TEACHER_PERMISSION_LEVEL,
                        String.valueOf(teacherPO.getId()),
                        teacherPO.getAvatar(),
                        teacherPO.getUsername(),
                        teacherPO.getRealname(),
                        teacherPO.getPhone(),
                        teacherPO.getEmail(),
                        token,
                        teacherPO.getDeleted(),
                        teacherPO.getProfilev()
                );
            }
        }
        else if(!IsUsernameAvailable(username, "user_student")){
            // 学生表中有对应记录
            // 获取表中记录
            StudentPO studentPO = studentMapper.getStudentPOByUsername(username);
            // 比对密码
            if(password.equals(studentPO.getPassword())){
                token = JWTUtils.genAccessToken(studentPO.getId(), studentPO.getProfilev());
                correctPassword = true;
                touristLoginResultVO = new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        ExamSysConstants.STUDENT_PERMISSION_LEVEL,
                        String.valueOf(studentPO.getId()),
                        studentPO.getAvatar(),
                        studentPO.getUsername(),
                        studentPO.getRealname(),
                        studentPO.getPhone(),
                        studentPO.getEmail(),
                        token,
                        studentPO.getDeleted(),
                        studentPO.getProfilev()
                );
            }
        }

        // 如果返回对象不为null，且密码正确
        if(touristLoginResultVO != null && correctPassword){
            // 对redis进行操作，将用户id和用户对象存入
            redisTemplate.opsForValue().set(String.valueOf(touristLoginResultVO.getUserid()), JSONObject.toJSONString(touristLoginResultVO));

            return touristLoginResultVO;
        }
        else if(touristLoginResultVO == null && !correctPassword){
            return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_WRONG_PASSWORD, 0, "0", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", 0, 0);
        }

        // 数据库中没找到 用户不存在
        return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_USER_NOT_EXIST, 0, "0", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", 0, 0);
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 用户自动登录方法
    * @DateTime: 2024/1/25 13:32
    * @Params: [token]
    * @Return com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO
    */
    @Override
    public TouristLoginResultVO TouristAutoLogin(String token) {
        try{
            // 解析 token，获得 userid 和 profilev
            Jws<Claims> claimsJws = JWTUtils.parseClaim(token);
            String userid = String.valueOf(claimsJws.getPayload().get("userid"));
            int profilev = Integer.parseInt(String.valueOf(claimsJws.getPayload().get("profilev")));
            // 用 userid 去 Redis 中查询用户的 profilev，并比对是否相同
            TouristLoginResultVO requestSenderVO = JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(userid))), TouristLoginResultVO.class);
            if(requestSenderVO != null){
                // 先判断个人信息版本是否正确
                if(requestSenderVO.getProfilev() != profilev){
                    // 个人信息版本错误
                    throw new InvalidProfilevException();
                }
            }
            else{
                // 若 Redis 中没有，则去 Mysql 中查询
                requestSenderVO = SearchUserByUserid(Long.valueOf(userid), token);
                if(requestSenderVO.getResultCode() == TouristResultCode.TOURIST_LOGIN_FAIL_USER_NOT_EXIST){
                    // 用户不存在
                    throw new UserNotExistException();
                }
                else{
                    // 用户存在，将用户信息记录加入 Redis
                    redisTemplate.opsForValue().set(String.valueOf(requestSenderVO.getUserid()), JSONObject.toJSONString(requestSenderVO));
                    // 用户存在，先比对用户信息版本
                    if(requestSenderVO.getProfilev() != profilev){
                        // 个人信息版本错误
                        throw new InvalidProfilevException();
                    }
                }
            }

            requestSenderVO.setResultCode(TouristResultCode.TOURIST_AUTOLOGIN_SUCCESS);

            return requestSenderVO;
        }
        catch (Exception e){
            if(e instanceof SignatureException){
                // token 无效
                return new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_FAIL_INVALID_TOKEN, 0, "0", null, null, null, null, null, null, 0, 0);
            }
            else if(e instanceof InvalidProfilevException){
                // 个人信息版本错误
                return new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_FAIL_INVALID_PROFILEV, 0, "0", null, null, null, null, null, null, 0, 0);
            }
            else if(e instanceof UserNotExistException){
                // 用户不存在
                return new TouristLoginResultVO(TouristResultCode.TOURIST_AUTOLOGIN_FAIL_USER_NOT_EXIST, 0, "0", null, null, null, null, null, null, 0, 0);
            }
        }
        return new TouristLoginResultVO(TouristResultCode.TOURIST_RESULT_CODE_UNKNOWN_ERROR, 0, "0", null, null, null, null, null, null, 0, 0);
    }
}
