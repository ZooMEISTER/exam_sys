package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: 游客登录时返回给前端的对象
 * @DateTime 2024/1/16 20:01
 **/

public class TouristLoginResultVO {
    int resultCode;
    int permissionLevel;
    long userId;
    String avatar;
    String userName;
    String realName;
    String phone;
    String email;
    String token;

    public TouristLoginResultVO(int resultCode, int permissionLevel, long userId, String avatar, String userName, String realName, String phone, String email, String token) {
        this.resultCode = resultCode;
        this.permissionLevel = permissionLevel;
        this.userId = userId;
        this.avatar = avatar;
        this.userName = userName;
        this.realName = realName;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
