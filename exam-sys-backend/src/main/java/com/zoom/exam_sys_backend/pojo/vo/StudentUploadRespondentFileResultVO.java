package com.zoom.exam_sys_backend.pojo.vo;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/20 18:32
 **/

public class StudentUploadRespondentFileResultVO {
    private String respondentFileName;
    private String respondentLastModifiedTime;

    public StudentUploadRespondentFileResultVO(String respondentFileName, String respondentLastModifiedTime) {
        this.respondentFileName = respondentFileName;
        this.respondentLastModifiedTime = respondentLastModifiedTime;
    }

    public String getRespondentFileName() {
        return respondentFileName;
    }

    public void setRespondentFileName(String respondentFileName) {
        this.respondentFileName = respondentFileName;
    }

    public String getRespondentLastModifiedTime() {
        return respondentLastModifiedTime;
    }

    public void setRespondentLastModifiedTime(String respondentLastModifiedTime) {
        this.respondentLastModifiedTime = respondentLastModifiedTime;
    }

    @Override
    public String toString() {
        return "StudentUploadRespondentFileResultVO{" +
                "respondentFileName='" + respondentFileName + '\'' +
                ", respondentLastModifiedTime='" + respondentLastModifiedTime + '\'' +
                '}';
    }
}
