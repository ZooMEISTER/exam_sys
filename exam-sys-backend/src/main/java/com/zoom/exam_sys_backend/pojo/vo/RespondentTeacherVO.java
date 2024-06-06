package com.zoom.exam_sys_backend.pojo.vo;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: 老师查看答卷是返回给前端的对象
 * @DateTime 2024/4/20 17:21
 **/

public class RespondentTeacherVO {
    private String id;
    private String exam_id;
    private String student_id;
    private String respondent_path;
    private int final_score;
    private String sha256_code;
    private String sign;
    private String publickey;
    private int is_sign_verify_good;
    private String created_time;
    private String lastModifiedTime;

    public RespondentTeacherVO(String id, String exam_id, String student_id, String respondent_path, int final_score, String sha256_code, String sign, String publickey, int is_sign_verify_good, String created_time, String lastModifiedTime) {
        this.id = id;
        this.exam_id = exam_id;
        this.student_id = student_id;
        this.respondent_path = respondent_path;
        this.final_score = final_score;
        this.sha256_code = sha256_code;
        this.sign = sign;
        this.publickey = publickey;
        this.is_sign_verify_good = is_sign_verify_good;
        this.created_time = created_time;
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getRespondent_path() {
        return respondent_path;
    }

    public void setRespondent_path(String respondent_path) {
        this.respondent_path = respondent_path;
    }

    public int getFinal_score() {
        return final_score;
    }

    public void setFinal_score(int final_score) {
        this.final_score = final_score;
    }

    public String getSha256_code() {
        return sha256_code;
    }

    public void setSha256_code(String sha256_code) {
        this.sha256_code = sha256_code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public int getIs_sign_verify_good() {
        return is_sign_verify_good;
    }

    public void setIs_sign_verify_good(int is_sign_verify_good) {
        this.is_sign_verify_good = is_sign_verify_good;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public String toString() {
        return "RespondentTeacherVO{" +
                "id='" + id + '\'' +
                ", exam_id='" + exam_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", respondent_path='" + respondent_path + '\'' +
                ", final_score=" + final_score +
                ", sha256_code='" + sha256_code + '\'' +
                ", sign='" + sign + '\'' +
                ", publickey='" + publickey + '\'' +
                ", is_sign_verify_good=" + is_sign_verify_good +
                ", created_time='" + created_time + '\'' +
                ", lastModifiedTime='" + lastModifiedTime + '\'' +
                '}';
    }
}
