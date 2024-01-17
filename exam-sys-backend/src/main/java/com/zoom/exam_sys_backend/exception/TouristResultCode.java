package com.zoom.exam_sys_backend.exception;

/*
 *    Created by ZooMEISTER
 *    on 2024/1/1 11:23
 *
 */

public interface TouristResultCode {

    public static final int TOURIST_REGISTER_SUCCESS = 10000;
    public static final int TOURIST_REGISTER_FAIL_USERNAME_ALREADY_EXIST = 10001;
    public static final int TOURIST_REGISTER_FAIL_OTHER_REASON = 10002;

    public static final int TOURIST_LOGIN_SUCCESS = 10010;
    public static final int TOURIST_LOGIN_FAIL_USER_NOT_EXIST = 10011;
    public static final int TOURIST_LOGIN_FAIL_WRONG_PASSWORD = 10012;

}
