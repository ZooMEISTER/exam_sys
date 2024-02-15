package com.zoom.exam_sys_backend.service;

import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/14 11:46
 **/

@Service
public interface StudentService {
    TouristLoginResultVO StudentUpdateProfile(Long userid, String newAvatar, String newUsername, String newRealname, String newPhone, String newEmail, String newPassword);
}
