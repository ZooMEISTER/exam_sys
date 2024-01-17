package com.zoom.exam_sys_backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/17 19:09
 **/

@TableName("user_teacher")
public class TeacherPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String avatar;
    private String username;
    private String realname;
    private String password;
    private String phone;
    private String email;
    @TableLogic(value = "0", delval = "1")
    private int deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public TeacherPO(Long id, String avatar, String username, String realname, String password, String phone, String email, int deleted) {
        this.id = id;
        this.avatar = avatar;
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "TeacherPO{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
