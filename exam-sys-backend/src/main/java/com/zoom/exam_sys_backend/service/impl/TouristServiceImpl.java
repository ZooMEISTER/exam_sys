package com.zoom.exam_sys_backend.service.impl;

import com.zoom.exam_sys_backend.exception.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.*;
import com.zoom.exam_sys_backend.pojo.po.AdminPO;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import com.zoom.exam_sys_backend.pojo.po.SuperAdminPO;
import com.zoom.exam_sys_backend.pojo.po.TeacherPO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristRegisterResultVO;
import com.zoom.exam_sys_backend.service.TouristService;
import com.zoom.exam_sys_backend.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/1 11:52
 **/

@Service
public class TouristServiceImpl implements TouristService {

    @Autowired
    private TouristMapper touristMapper;

    @Autowired
    private SuperAdminMapper superAdminMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

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
                id, avatar, username, "default", password, "default", "default", 0
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
        String token = "";

        if(!IsUsernameAvailable(username, "user_super_admin")){
            // 超级管理员表中有对应记录
            // 获取表中记录
            SuperAdminPO superAdminPO = superAdminMapper.getSuperAdminPOByUsername(username);
            // 比对密码
            if(password.equals(superAdminPO.getPassword())){
                return new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        4,
                        superAdminPO.getId(),
                        superAdminPO.getAvatar(),
                        superAdminPO.getUsername(),
                        superAdminPO.getRealname(),
                        superAdminPO.getPhone(),
                        superAdminPO.getEmail(),
                        token
                );
            }
            else{
                return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_WRONG_PASSWORD, 0, 0, "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");
            }
        }
        else if(!IsUsernameAvailable(username, "user_admin")){
            // 管理员表中有对应记录
            // 获取表中记录
            AdminPO adminPO = adminMapper.getAdminPOByUsername(username);
            // 比对密码
            if(password.equals(adminPO.getPassword())){
                return new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        3,
                        adminPO.getId(),
                        adminPO.getAvatar(),
                        adminPO.getUsername(),
                        adminPO.getRealname(),
                        adminPO.getPhone(),
                        adminPO.getEmail(),
                        token
                );
            }
            else{
                return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_WRONG_PASSWORD, 0, 0, "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");
            }
        }
        else if(!IsUsernameAvailable(username, "user_teacher")){
            // 老师表中有对应记录
            // 获取表中记录
            TeacherPO teacherPO = teacherMapper.getTeacherPOByUsername(username);
            // 比对密码
            if(password.equals(teacherPO.getPassword())){
                return new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        2,
                        teacherPO.getId(),
                        teacherPO.getAvatar(),
                        teacherPO.getUsername(),
                        teacherPO.getRealname(),
                        teacherPO.getPhone(),
                        teacherPO.getEmail(),
                        token
                );
            }
            else{
                return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_WRONG_PASSWORD, 0, 0, "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");
            }
        }
        else if(!IsUsernameAvailable(username, "user_student")){
            // 学生表中有对应记录
            // 获取表中记录
            StudentPO studentPO = studentMapper.getStudentPOByUsername(username);
            // 比对密码
            if(password.equals(studentPO.getPassword())){
                return new TouristLoginResultVO(
                        TouristResultCode.TOURIST_LOGIN_SUCCESS,
                        1,
                        studentPO.getId(),
                        studentPO.getAvatar(),
                        studentPO.getUsername(),
                        studentPO.getRealname(),
                        studentPO.getPhone(),
                        studentPO.getEmail(),
                        token
                );
            }
            else{
                return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_WRONG_PASSWORD, 0, 0, "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");
            }
        }

        // 数据库中没找到 用户不存在
        return new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_USER_NOT_EXIST, 0, 0, "NULL", "NULL", "NULL", "NULL", "NULL", "NULL");
    }
}
