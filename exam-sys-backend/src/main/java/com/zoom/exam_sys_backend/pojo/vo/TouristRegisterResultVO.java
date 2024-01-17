package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: 游客注册时传给前端的对象
 * @DateTime 2024/1/1 11:49
 **/

public class TouristRegisterResultVO {
    int resultCode;
    String info;

    public int getResultCode() {
        return resultCode;
    }

    public String getInfo() {
        return info;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public TouristRegisterResultVO(int resultCode, String info) {
        this.resultCode = resultCode;
        this.info = info;
    }

    @Override
    public String toString() {
        return "TouristVO{" +
                "resultCode=" + resultCode +
                ", info='" + info + '\'' +
                '}';
    }
}
