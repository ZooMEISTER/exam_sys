package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.po.AdminPO;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/17 19:13
 **/

@Mapper
public interface AdminMapper extends BaseMapper<AdminPO> {
    @Select("SELECT * FROM user_admin WHERE username=#{username} AND deleted='0'")
    AdminPO getAdminPOByUsername(String username);
}
