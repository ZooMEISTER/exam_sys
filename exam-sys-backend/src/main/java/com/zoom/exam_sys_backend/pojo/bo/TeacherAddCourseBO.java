package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 19:17
 **/

@TableName("application_add_course")
public class TeacherAddCourseBO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long subject_id;
    private String icon;
    private String name;
    private String description;
    private Long teachby;
    private Date created_time;
    private int approve_status;

    public TeacherAddCourseBO(Long id, Long subject_id, String icon, String name, String description, Long teachby, Date created_time, int approve_status) {
        this.id = id;
        this.subject_id = subject_id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.teachby = teachby;
        this.created_time = created_time;
        this.approve_status = approve_status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Long subject_id) {
        this.subject_id = subject_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTeachby() {
        return teachby;
    }

    public void setTeachby(Long teachby) {
        this.teachby = teachby;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public int getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(int approve_status) {
        this.approve_status = approve_status;
    }

    @Override
    public String toString() {
        return "TeacherAddCourseBO{" +
                "id=" + id +
                ", subject_id=" + subject_id +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teachby=" + teachby +
                ", created_time=" + created_time +
                ", approve_status=" + approve_status +
                '}';
    }
}
