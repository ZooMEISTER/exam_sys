package com.zoom.exam_sys_backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/15 17:30
 **/

@TableName("sys_department")
public class DepartmentPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String icon;
    private String name;
    private String description;
    private int subject_count;
    @TableLogic(value = "0", delval = "1")
    private int deleted;

    public DepartmentPO(Long id, String icon, String name, String description, int subject_count, int deleted) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.subject_count = subject_count;
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

    public int getSubject_count() {
        return subject_count;
    }

    public void setSubject_count(int subject_count) {
        this.subject_count = subject_count;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "DepartmentPO{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subject_count=" + subject_count +
                ", deleted=" + deleted +
                '}';
    }
}
