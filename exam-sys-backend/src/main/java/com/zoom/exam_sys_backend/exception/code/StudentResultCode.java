package com.zoom.exam_sys_backend.exception.code;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/14 12:14
 **/

public interface StudentResultCode {
    public static final int STUDENT_UPDATE_PROFILE_SUCCESS = 12000;
    public static final int STUDENT_UPDATE_PROFILE_FAIL = 12001;
    public static final int STUDENT_SIGN_COURSE_SUCCESS = 12002;
    public static final int STUDENT_SIGN_COURSE_FAIL = 12003;
    public static final int STUDENT_ADD_RESPONDENT_SUCCESS = 12004;
    public static final int STUDENT_ADD_RESPONDENT_FAIL = 12005;
    public static final int STUDENT_TO_TEACHER_APPLICATION_ALREADY_EXIST = 12006;
    public static final int STUDENT_TO_TEACHER_APPLICATION_ADDED = 12007;
    public static final int STUDENT_TO_TEACHER_APPLICATION_ADD_FAIL = 12008;
    public static final int STUDENT_GET_AES_KEY_SUCCESS = 12010;
    public static final int STUDENT_GET_AES_KEY_FAIL = 12011;

}
