package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/5/14 0:12
 **/

public class AdminSubjectManagementVO {
    private String id;
    private String icon;
    private String name;
    private String description;
    private String belongto;
    private int course_count;

    private String belong_department;

    public AdminSubjectManagementVO(String id, String icon, String name, String description, String belongto, int course_count, String belong_department) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.belongto = belongto;
        this.course_count = course_count;
        this.belong_department = belong_department;
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

    public String getBelong_department() {
        return belong_department;
    }

    public void setBelong_department(String belong_department) {
        this.belong_department = belong_department;
    }

    @Override
    public String toString() {
        return "AdminSubjectManagementVO{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", belongto='" + belongto + '\'' +
                ", course_count=" + course_count +
                ", belong_department='" + belong_department + '\'' +
                '}';
    }
}
