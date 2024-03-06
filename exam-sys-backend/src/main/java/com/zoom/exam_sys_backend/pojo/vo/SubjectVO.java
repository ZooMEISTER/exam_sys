package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/6 14:41
 **/

public class SubjectVO {
    private String id;
    private String icon;
    private String name;
    private String description;
    private String belongto;
    private int course_count;

    public SubjectVO(String id, String icon, String name, String description, String belongto, int course_count) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.belongto = belongto;
        this.course_count = course_count;
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

    public String getBelongto() {
        return belongto;
    }

    public void setBelongto(String belongto) {
        this.belongto = belongto;
    }

    public int getCourse_count() {
        return course_count;
    }

    public void setCourse_count(int course_count) {
        this.course_count = course_count;
    }
}
