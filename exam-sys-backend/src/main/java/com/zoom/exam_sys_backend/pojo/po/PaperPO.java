package com.zoom.exam_sys_backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/14 18:38
 **/

@TableName("sys_paper")
public class PaperPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Long teachby;
    private int score;
    private String path;
    private Date created_time;

    public PaperPO(Long id, String name, String description, Long teachby, int score, String path, Date created_time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teachby = teachby;
        this.score = score;
        this.path = path;
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

    public Long getTeachby() {
        return teachby;
    }

    public void setTeachby(Long teachby) {
        this.teachby = teachby;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    @Override
    public String toString() {
        return "PaperPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teachby=" + teachby +
                ", score=" + score +
                ", path='" + path + '\'' +
                ", created_time=" + created_time +
                '}';
    }
}
