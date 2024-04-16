package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/16 18:30
 **/

public class StudentGetExamPaperAesKeyResultVO {
    private int resultCode;
    private String aesKey;
    private String msg;

    public StudentGetExamPaperAesKeyResultVO(int resultCode, String aesKey, String msg) {
        this.resultCode = resultCode;
        this.aesKey = aesKey;
        this.msg = msg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "StudentGetExamPaperAesKeyResultVO{" +
                "resultCode=" + resultCode +
                ", aesKey='" + aesKey + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
