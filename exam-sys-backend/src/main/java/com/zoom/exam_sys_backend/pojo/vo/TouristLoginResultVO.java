package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: 游客登录时返回给前端的对象
 * @DateTime 2024/1/16 20:01
 **/

public class TouristLoginResultVO {
    int resultCode;
    int permissionLevel;
    long userid;
    String avatar;
    String username;
    String realname;
    String phone;
    String email;
    String token;

    public TouristLoginResultVO(int resultCode, int permissionLevel, long userid, String avatar, String username, String realname, String phone, String email, String token) {
        this.resultCode = resultCode;
        this.permissionLevel = permissionLevel;
        this.userid = userid;
        this.avatar = avatar;
        this.username = username;
        this.realname = realname;
        this.phone = phone;
        this.email = email;
        this.token = token;
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

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
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
}
