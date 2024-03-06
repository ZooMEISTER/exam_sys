package com.zoom.exam_sys_backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/15 17:31
 **/

@TableName("sys_course")
public class CoursePO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String icon;
    private String name;
    private String description;
    private Long teachby;
    private Date created_time;
    @TableLogic(value = "0", delval = "1")
    private int deleted;

    public CoursePO(Long id, String icon, String name, String description, Long teachby, Date created_time, int deleted) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.teachby = teachby;
        this.created_time = created_time;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "CoursePO{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teachby=" + teachby +
                ", created_time=" + created_time +
                ", deleted=" + deleted +
                '}';
    }
}
