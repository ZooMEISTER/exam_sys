package com.zoom.exam_sys_backend.pojo.vo;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/5/11 20:37
 **/

public class AdminToTeacherApplicationVO {
    private String id;
    private String student_id;
    private String description;
    private int approve_status;
    private String apply_time;

    private String avatar;
    private String username;
    private String realname;
    private String phone;
    private String email;

    public AdminToTeacherApplicationVO(String id, String student_id, String description, int approve_status, String apply_time, String avatar, String username, String realname, String phone, String email) {
        this.id = id;
        this.student_id = student_id;
        this.description = description;
        this.approve_status = approve_status;
        this.apply_time = apply_time;
        this.avatar = avatar;
        this.username = username;
        this.realname = realname;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
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

    public String getApply_time() {
        return apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AdminToTeacherApplicationVO{" +
                "id='" + id + '\'' +
                ", student_id='" + student_id + '\'' +
                ", description='" + description + '\'' +
                ", approve_status=" + approve_status +
                ", apply_time='" + apply_time + '\'' +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
