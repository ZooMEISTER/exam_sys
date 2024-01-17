package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/16 20:52
 **/

@Mapper
public interface StudentMapper extends BaseMapper<StudentPO> {
    @Select("SELECT * FROM user_student WHERE username=#{username} AND deleted='0'")
    StudentPO getStudentPOByUsername(String username);
}
