package com.zoom.exam_sys_backend.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/6 14:39
 **/

public class CourseVO {
    private String id;
    private String icon;
    private String name;
    private String description;
    private String teachby;
    private String created_time;

    public CourseVO(String id, String icon, String name, String description, String teachby, String created_time) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.teachby = teachby;
        this.created_time = created_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTeachby() {
        return teachby;
    }

    public void setTeachby(String teachby) {
        this.teachby = teachby;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    @Override
    public String toString() {
        return "CourseVO{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teachby='" + teachby + '\'' +
                ", created_time='" + created_time + '\'' +
                '}';
    }
}
