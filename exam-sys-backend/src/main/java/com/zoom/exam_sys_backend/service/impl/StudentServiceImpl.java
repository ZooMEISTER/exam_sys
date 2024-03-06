package com.zoom.exam_sys_backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.exception.code.StudentResultCode;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.mapper.StudentMapper;
import com.zoom.exam_sys_backend.pojo.bo.SubjectCourseBO;
import com.zoom.exam_sys_backend.pojo.po.CoursePO;
import com.zoom.exam_sys_backend.pojo.po.DepartmentPO;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import com.zoom.exam_sys_backend.pojo.po.SubjectPO;
import com.zoom.exam_sys_backend.pojo.vo.CourseVO;
import com.zoom.exam_sys_backend.pojo.vo.DepartmentVO;
import com.zoom.exam_sys_backend.pojo.vo.SubjectVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.service.StudentService;
import com.zoom.exam_sys_backend.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/14 11:46
 **/

@Service
public class StudentServiceImpl implements StudentService {

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
                    coursePO.getCreated_time()
            ));
        }
        return courseVOList;
    }
}
