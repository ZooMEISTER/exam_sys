package com.zoom.exam_sys_backend.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/17 17:24
 **/

public class StudentVO {
    private String id;
    private String avatar;
    private String username;
    private String realname;
    private String phone;
    private String email;
    private int deleted;
    private int profilev;

    public StudentVO(String id, String avatar, String username, String realname, String phone, String email, int deleted, int profilev) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.realname = realname;
        this.phone = phone;
        this.email = email;
        this.deleted = deleted;
        this.profilev = profilev;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getProfilev() {
        return profilev;
    }

    public void setProfilev(int profilev) {
        this.profilev = profilev;
    }

    @Override
    public String toString() {
        return "StudentVO{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", deleted=" + deleted +
                ", profilev=" + profilev +
                '}';
    }
}
