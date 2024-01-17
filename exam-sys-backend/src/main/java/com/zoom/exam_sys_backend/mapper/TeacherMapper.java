package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import com.zoom.exam_sys_backend.pojo.po.TeacherPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/17 19:13
 **/

@Mapper
public interface TeacherMapper extends BaseMapper<TeacherPO> {
    @Select("SELECT * FROM user_teacher WHERE username=#{username} AND deleted='0'")
    TeacherPO getTeacherPOByUsername(String username);
}
