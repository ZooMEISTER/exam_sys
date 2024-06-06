package com.zoom.exam_sys_backend.exception.code;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/21 4:12
 **/

public interface AdminResultCode {
    public static final int ADMIN_UPDATE_PROFILE_SUCCESS = 12000;
    public static final int ADMIN_UPDATE_PROFILE_FAIL = 12001;
    public static final int ADMIN_UPDATE_APPLICATION_STATUS_SUCCESS = 12010;
    public static final int ADMIN_UPDATE_APPLICATION_STATUS_FAIL = 12011;

    public static final int ADMIN_UPDATE_DEPARTMENT_INFO_SUCCESS = 12020;
    public static final int ADMIN_UPDATE_DEPARTMENT_INFO_FAIL = 12021;
    public static final int ADMIN_INSERT_DEPARTMENT_SUCCESS = 12022;
    public static final int ADMIN_INSERT_DEPARTMENT_FAIL = 12023;
    public static final int ADMIN_DELETE_DEPARTMENT_SUCCESS = 12024;
    public static final int ADMIN_DELETE_DEPARTMENT_FAIL = 12025;

    public static final int ADMIN_UPDATE_SUBJECT_INFO_SUCCESS = 12030;
    public static final int ADMIN_UPDATE_SUBJECT_INFO_FAIL = 12031;
    public static final int ADMIN_INSERT_SUBJECT_SUCCESS = 12032;
    public static final int ADMIN_INSERT_SUBJECT_FAIL = 12033;
    public static final int ADMIN_DELETE_SUBJECT_SUCCESS = 12034;
    public static final int ADMIN_DELETE_SUBJECT_FAIL = 12035;

    public static final int ADMIN_UPDATE_COURSE_INFO_SUCCESS = 12040;
    public static final int ADMIN_UPDATE_COURSE_INFO_FAIL = 12041;
    public static final int ADMIN_INSERT_COURSE_SUCCESS = 12042;
    public static final int ADMIN_INSERT_COURSE_FAIL = 12043;
    public static final int ADMIN_DELETE_COURSE_SUCCESS = 12044;
    public static final int ADMIN_DELETE_COURSE_FAIL = 12045;

    public static final int ADMIN_UPDATE_EXAM_INFO_SUCCESS = 12050;
    public static final int ADMIN_UPDATE_EXAM_INFO_FAIL = 12051;
    public static final int ADMIN_INSERT_EXAM_SUCCESS = 12052;
    public static final int ADMIN_INSERT_EXAM_FAIL = 12053;
    public static final int ADMIN_DELETE_EXAM_SUCCESS = 12054;
    public static final int ADMIN_DELETE_EXAM_FAIL = 12055;

    public static final int ADMIN_UPDATE_TEACHER_PROFILE_SUCCESS = 12060;
    public static final int ADMIN_UPDATE_TEACHER_PROFILE_FAIL = 12061;

    public static final int ADMIN_UPDATE_STUDENT_PROFILE_SUCCESS = 12070;
    public static final int ADMIN_UPDATE_STUDENT_PROFILE_FAIL = 12071;
}
