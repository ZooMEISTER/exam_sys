package com.zoom.exam_sys_backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/14 14:45
 **/

@TableName("sys_exam")
public class ExamPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Date start_time;
    private Date end_time;
    private Long teachby;
    private int type;
    private int published;
    private Date created_time;

    public ExamPO(Long id, String name, String description, Date start_time, Date end_time, Long teachby, int type, int published, Date created_time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start_time = start_time;
        this.end_time = end_time;
        this.teachby = teachby;
        this.type = type;
        this.published = published;
        this.created_time = created_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Long getTeachby() {
        return teachby;
    }

    public void setTeachby(Long teachby) {
        this.teachby = teachby;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    @Override
    public String toString() {
        return "ExamPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", teachby=" + teachby +
                ", type=" + type +
                ", published=" + published +
                ", created_time=" + created_time +
                '}';
    }
}
