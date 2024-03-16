package com.zoom.exam_sys_backend.constant;

/**
 * @Author ZooMEISTER
 * @Description: 学生考试的几个状态，先将考试分为未开始，正在进行，已结束，再分为学生做没做。
 * @DateTime 2024/3/15 16:46
 **/

public interface ExamStatusStudent {
    public static final int EXAM_STATUS_STUDENT_NOT_START = 0;
    public static final int EXAM_STATUS_STUDENT_STARTED_UNDO = 1;
    public static final int EXAM_STATUS_STUDENT_STARTED_DONE = 2;
    public static final int EXAM_STATUS_STUDENT_ENDED_UNDO = 3;
    public static final int EXAM_STATUS_STUDENT_ENDED_DONE = 4;
}
