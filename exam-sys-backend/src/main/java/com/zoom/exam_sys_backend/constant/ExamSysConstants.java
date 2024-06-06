package com.zoom.exam_sys_backend.constant;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/19 21:10
 **/

public interface ExamSysConstants {
    public static final int SUPER_ADMIN_PERMISSION_LEVEL = 4;
    public static final int ADMIN_PERMISSION_LEVEL = 3;
    public static final int TEACHER_PERMISSION_LEVEL = 2;
    public static final int STUDENT_PERMISSION_LEVEL = 1;
    public static final int TOURIST_PERMISSION_LEVEL = 0;

    // 学生可以在考试开始之前提前拿到aeskey的时间（ms）
    public static final long AES_KEY_AHEAD_AVAILABLE_TIME = 60000;

    // 重置密码 默认密码
    public static final String RESET_PASSWORD_DEFAULT_PASSWORD = "abc123456";
}
