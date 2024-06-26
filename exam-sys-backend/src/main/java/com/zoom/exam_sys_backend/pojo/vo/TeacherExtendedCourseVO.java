package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/17 15:07
 **/

public class TeacherExtendedCourseVO {
    private String id;
    private String icon;
    private String name;
    private String description;
    private String teachby;
    private String teacherUsername;
    private String teacherRealname;
    private String created_time;
    private int studentCount;

    public TeacherExtendedCourseVO(String id, String icon, String name, String description, String teachby, String teacherUsername, String teacherRealname, String created_time, int studentCount) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.teachby = teachby;
        this.teacherUsername = teacherUsername;
        this.teacherRealname = teacherRealname;
        this.created_time = created_time;
        this.studentCount = studentCount;
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

    public String getTeacherUsername() {
        return teacherUsername;
    }

    public void setTeacherUsername(String teacherUsername) {
        this.teacherUsername = teacherUsername;
    }

    public String getTeacherRealname() {
        return teacherRealname;
    }

    public void setTeacherRealname(String teacherRealname) {
        this.teacherRealname = teacherRealname;
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

    @Override
    public String toString() {
        return "TeacherExtendedCourseVO{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teachby='" + teachby + '\'' +
                ", teacherUsername='" + teacherUsername + '\'' +
                ", teacherRealname='" + teacherRealname + '\'' +
                ", created_time='" + created_time + '\'' +
                ", studentCount=" + studentCount +
                '}';
    }
}
