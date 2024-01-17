package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/1 11:51
 **/

@Mapper
public interface TouristMapper extends BaseMapper<StudentPO> {
    @Select("SELECT COUNT(*) FROM ${tablename} WHERE username=#{username} AND deleted='0'")
    int IsUsernameAvailable(String username, String tablename);
}
