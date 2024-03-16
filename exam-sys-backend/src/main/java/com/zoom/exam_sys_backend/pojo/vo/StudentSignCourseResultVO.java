package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/16 17:13
 **/

public class StudentSignCourseResultVO {
    int resultCode;
    String msg;
    int recordCount;

    public StudentSignCourseResultVO(int resultCode, String msg, int recordCount) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.recordCount = recordCount;
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

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    @Override
    public String toString() {
        return "StudentSignCourseResultVO{" +
                "resultCode=" + resultCode +
                ", msg='" + msg + '\'' +
                ", recordCount=" + recordCount +
                '}';
    }
}
