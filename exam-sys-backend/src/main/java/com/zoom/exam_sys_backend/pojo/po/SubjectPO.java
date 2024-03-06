package com.zoom.exam_sys_backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/2/15 17:31
 **/

@TableName("sys_subject")
public class SubjectPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String icon;
    private String name;
    private String description;
    private Long belongto;
    private int course_count;
    @TableLogic(value = "0", delval = "1")
    private int deleted;

    public SubjectPO(Long id, String icon, String name, String description, Long belongto, int course_count, int deleted) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.belongto = belongto;
        this.course_count = course_count;
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

    public Long getBelongto() {
        return belongto;
    }

    public void setBelongto(Long belongto) {
        this.belongto = belongto;
    }

    public int getCourse_count() {
        return course_count;
    }

    public void setCourse_count(int course_count) {
        this.course_count = course_count;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "SubjectPO{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", belongto=" + belongto +
                ", course_count=" + course_count +
                ", deleted=" + deleted +
                '}';
    }
}
