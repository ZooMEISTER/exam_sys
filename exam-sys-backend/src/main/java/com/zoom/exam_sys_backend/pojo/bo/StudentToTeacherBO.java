package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 19:20
 **/

@TableName("application_to_teacher")
public class StudentToTeacherBO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long student_id;
    private String description;
    private int approve_status;
    private Date apply_time;

    public StudentToTeacherBO(Long id, Long student_id, String description, int approve_status, Date apply_time) {
        this.id = id;
        this.student_id = student_id;
        this.description = description;
        this.approve_status = approve_status;
        this.apply_time = apply_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(int approve_status) {
        this.approve_status = approve_status;
    }

    public Date getApply_time() {
        return apply_time;
    }

    public void setApply_time(Date apply_time) {
        this.apply_time = apply_time;
    }

    @Override
    public String toString() {
        return "StudentToTeacherBO{" +
                "id=" + id +
                ", student_id=" + student_id +
                ", description='" + description + '\'' +
                ", approve_status=" + approve_status +
                ", apply_time=" + apply_time +
                '}';
    }
}
