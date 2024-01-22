package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import com.zoom.exam_sys_backend.pojo.vo.TouristRegisterResultVO;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/1 11:52
 **/

@Service
public interface TouristService {
    // 检查用户名是否可用
    Boolean IsUsernameAvailable(String username, String tablename);

    // 游客注册
    TouristRegisterResultVO InsertRegisterTourist(String avatar, String username, String password);

    // 游客登陆 使用用户名和密码
    TouristLoginResultVO TouristLogin(String username, String password);
}
