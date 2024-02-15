package com.zoom.exam_sys_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoom.exam_sys_backend.pojo.po.StudentPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/16 20:52
 **/

@Mapper
public interface StudentMapper extends BaseMapper<StudentPO> {
    @Select("SELECT * FROM user_student WHERE username=#{username} AND deleted='0'")
    StudentPO getStudentPOByUsername(String username);

    @Update("UPDATE user_student SET avatar=#{newAvatar}, username=#{newUsername}, realname=#{newRealname}, phone=#{newPhone}, email=#{newEmail}, profilev=#{newProfilev} WHERE id=#{userid}")
    int updateStudentProfileWithoutPassword(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, int newProfilev);
}
