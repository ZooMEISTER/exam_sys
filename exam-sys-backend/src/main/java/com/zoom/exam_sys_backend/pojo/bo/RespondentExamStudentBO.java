package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

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
    private String sign;
    private String publickey;
    private int is_sign_verify_good;
    private Date created_time;
    private Date last_modified_time;

    public RespondentExamStudentBO(Long id, Long exam_id, Long student_id, String respondent_path, int final_score, String sha256_code, String sign, String publickey, int is_sign_verify_good, Date created_time, Date last_modified_time) {
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
        this.last_modified_time = last_modified_time;
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

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public Date getLast_modified_time() {
        return last_modified_time;
    }

    public void setLast_modified_time(Date last_modified_time) {
        this.last_modified_time = last_modified_time;
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
                ", sign='" + sign + '\'' +
                ", publickey='" + publickey + '\'' +
                ", is_sign_verify_good=" + is_sign_verify_good +
                ", created_time=" + created_time +
                ", last_modified_time=" + last_modified_time +
                '}';
    }
}
