package com.zoom.exam_sys_backend.pojo.vo;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/17 21:41
 **/

public class FinishedRespondentExamStudentVO {
    private String id;
    private String exam_id;
    private String student_id;
    private String respondent_path;
    private int final_score;
    private String sha256_code;
    private Date created_time;

    private String studentId;
    private String studentAvatar;
    private String studentUsername;
    private String studentRealname;
    private String studentPhone;
    private String studentEmail;

    public FinishedRespondentExamStudentVO(String id, String exam_id, String student_id, String respondent_path, int final_score, String sha256_code, Date created_time, String studentId, String studentAvatar, String studentUsername, String studentRealname, String studentPhone, String studentEmail) {
        this.id = id;
        this.exam_id = exam_id;
        this.student_id = student_id;
        this.respondent_path = respondent_path;
        this.final_score = final_score;
        this.sha256_code = sha256_code;
        this.created_time = created_time;
        this.studentId = studentId;
        this.studentAvatar = studentAvatar;
        this.studentUsername = studentUsername;
        this.studentRealname = studentRealname;
        this.studentPhone = studentPhone;
        this.studentEmail = studentEmail;
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

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentRealname() {
        return studentRealname;
    }

    public void setStudentRealname(String studentRealname) {
        this.studentRealname = studentRealname;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    @Override
    public String toString() {
        return "FinishedRespondentExamStudentVO{" +
                "id='" + id + '\'' +
                ", exam_id='" + exam_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", respondent_path='" + respondent_path + '\'' +
                ", final_score=" + final_score +
                ", sha256_code='" + sha256_code + '\'' +
                ", created_time=" + created_time +
                ", studentId='" + studentId + '\'' +
                ", studentAvatar='" + studentAvatar + '\'' +
                ", studentUsername='" + studentUsername + '\'' +
                ", studentRealname='" + studentRealname + '\'' +
                ", studentPhone='" + studentPhone + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                '}';
    }
}
