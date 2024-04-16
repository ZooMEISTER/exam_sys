package com.zoom.exam_sys_backend.service.impl;

import com.zoom.exam_sys_backend.mapper.AdminMapper;
import com.zoom.exam_sys_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 19:23
 **/

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;


}
