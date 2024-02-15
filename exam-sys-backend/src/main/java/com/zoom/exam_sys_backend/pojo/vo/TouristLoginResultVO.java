package com.zoom.exam_sys_backend.pojo.vo;

import java.io.Serializable;

/**
 * @Author ZooMEISTER
 * @Description: 游客登录时返回给前端的对象
 * @DateTime 2024/1/16 20:01
 **/

public class TouristLoginResultVO {
    int resultCode;
    int permissionLevel;
    String userid;
    String avatar;
    String username;
    String realname;
    String phone;
    String email;
    String token;
    private int deleted;
    private int profilev;

    public TouristLoginResultVO(int resultCode, int permissionLevel, String userid, String avatar, String username, String realname, String phone, String email, String token, int deleted, int profilev) {
        this.resultCode = resultCode;
        this.permissionLevel = permissionLevel;
        this.userid = userid;
        this.avatar = avatar;
        this.username = username;
        this.realname = realname;
        this.phone = phone;
        this.email = email;
        this.token = token;
        this.deleted = deleted;
        this.profilev = profilev;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        return "TouristLoginResultVO{" +
                "resultCode=" + resultCode +
                ", permissionLevel=" + permissionLevel +
                ", userid=" + userid +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", deleted=" + deleted +
                ", profilev=" + profilev +
                '}';
    }
}
