package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/16 19:04
 **/

@TableName("respondent_exam_student")
public class RespondentExamStudentBO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long exam_id;
    private Long student_id;
    private String respondent_path;
    private int final_score;
    private String sha256_code;

    public RespondentExamStudentBO(Long id, Long exam_id, Long student_id, String respondent_path, int final_score, String sha256_code) {
        this.id = id;
        this.exam_id = exam_id;
        this.student_id = student_id;
        this.respondent_path = respondent_path;
        this.final_score = final_score;
        this.sha256_code = sha256_code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExam_id() {
        return exam_id;
    }

    public void setExam_id(Long exam_id) {
        this.exam_id = exam_id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
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
        return "RespondentExamStudentBO{" +
                "id=" + id +
                ", exam_id=" + exam_id +
                ", student_id=" + student_id +
                ", respondent_path='" + respondent_path + '\'' +
                ", final_score=" + final_score +
                ", sha256_code='" + sha256_code + '\'' +
                '}';
    }
}
