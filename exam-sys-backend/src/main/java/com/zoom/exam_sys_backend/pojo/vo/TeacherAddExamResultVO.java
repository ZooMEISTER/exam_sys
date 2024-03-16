package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/15 14:42
 **/

public class TeacherAddExamResultVO {
    private int resultCode;
    private String msg;

    public TeacherAddExamResultVO(int resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TeacherAddExamResultVO{" +
                "resultCode=" + resultCode +
                ", msg='" + msg + '\'' +
                '}';
    }
}
