package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.exception.code.StudentResultCode;
import com.zoom.exam_sys_backend.exception.code.TeacherResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.TeacherMapper;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import com.zoom.exam_sys_backend.pojo.po.TeacherPO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.service.TeacherService;
import com.zoom.exam_sys_backend.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/14 14:38
 **/

@Service
public class TeacherServiceImpl implements TeacherService {

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
                    1,
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
}
