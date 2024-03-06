package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/6 14:40
 **/

public class DepartmentVO {
    private String id;
    private String icon;
    private String name;
    private String description;
    private int subject_count;

    public DepartmentVO(String id, String icon, String name, String description, int subject_count) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.subject_count = subject_count;
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

    public int getSubject_count() {
        return subject_count;
    }

    public void setSubject_count(int subject_count) {
        this.subject_count = subject_count;
    }
}
