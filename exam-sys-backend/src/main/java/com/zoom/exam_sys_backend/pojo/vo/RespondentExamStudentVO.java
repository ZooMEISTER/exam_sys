package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/16 19:08
 **/

public class RespondentExamStudentVO {
    private String id;
    private String exam_id;
    private String student_id;
    private String respondent_path;
    private int final_score;
    private String sha256_code;

    public RespondentExamStudentVO(String id, String exam_id, String student_id, String respondent_path, int final_score, String sha256_code) {
        this.id = id;
        this.exam_id = exam_id;
        this.student_id = student_id;
        this.respondent_path = respondent_path;
        this.final_score = final_score;
        this.sha256_code = sha256_code;
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

    @Override
    public String toString() {
        return "RespondentExamStudentVO{" +
                "id='" + id + '\'' +
                ", exam_id='" + exam_id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", respondent_path='" + respondent_path + '\'' +
                ", final_score=" + final_score +
                ", sha256_code='" + sha256_code + '\'' +
                '}';
    }
}
