package com.zoom.exam_sys_backend.exception.code;

/*
 *    Created by ZooMEISTER
 *    on 2024/1/1 11:23
 *
 */

public interface TouristResultCode {

    public static final int TOURIST_RESULT_CODE_UNKNOWN_ERROR = 10099;

    public static final int TOURIST_REGISTER_SUCCESS = 10000;
    public static final int TOURIST_REGISTER_FAIL_USERNAME_ALREADY_EXIST = 10001;
    public static final int TOURIST_REGISTER_FAIL_OTHER_REASON = 10002;

    public static final int TOURIST_LOGIN_SUCCESS = 10010;
    public static final int TOURIST_LOGIN_FAIL_USER_NOT_EXIST = 10011;
    public static final int TOURIST_LOGIN_FAIL_WRONG_PASSWORD = 10012;

    public static final int TOURIST_AUTOLOGIN_SUCCESS = 10020;
    public static final int TOURIST_AUTOLOGIN_FAIL_INVALID_TOKEN = 10021;
    public static final int TOURIST_AUTOLOGIN_FAIL_USER_NOT_EXIST = 10022;
    public static final int TOURIST_AUTOLOGIN_FAIL_INVALID_PROFILEV = 10023;

}
