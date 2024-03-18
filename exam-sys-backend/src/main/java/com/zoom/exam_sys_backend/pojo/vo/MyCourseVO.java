package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/18 19:16
 **/

public class MyCourseVO {
    private String id;
    private String icon;
    private String name;
    private String description;
    private String teachby;
    private String created_time;
    private int studentCount;
    private int examCount;
    private String departmentId;
    private String departmentName;
    private String subjectId;
    private String subjectName;

    public MyCourseVO(String id, String icon, String name, String description, String teachby, String created_time, int studentCount, int examCount, String departmentId, String departmentName, String subjectId, String subjectName) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.teachby = teachby;
        this.created_time = created_time;
        this.studentCount = studentCount;
        this.examCount = examCount;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
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

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getExamCount() {
        return examCount;
    }

    public void setExamCount(int examCount) {
        this.examCount = examCount;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "MyCourseVO{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teachby='" + teachby + '\'' +
                ", created_time='" + created_time + '\'' +
                ", studentCount=" + studentCount +
                ", examCount=" + examCount +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
