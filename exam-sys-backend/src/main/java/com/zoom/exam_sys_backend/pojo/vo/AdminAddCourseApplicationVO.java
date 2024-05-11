package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/5/8 12:58
 **/

public class AdminAddCourseApplicationVO {
    private String id;
    private String subject_id;
    private String icon;
    private String name;
    private String description;
    private String teachby;
    private String created_time;
    private int approve_status;

    private String departmentId;
    private String departmentName;
    private String subjectId;
    private String subjectName;

    private String teacherAvatar;
    private String teacherName;
    private String teacherRealname;
    private String teacherPhone;
    private String teacherEmail;

    public AdminAddCourseApplicationVO(String id, String subject_id, String icon, String name, String description, String teachby, String created_time, int approve_status, String departmentId, String departmentName, String subjectId, String subjectName, String teacherAvatar, String teacherName, String teacherRealname, String teacherPhone, String teacherEmail) {
        this.id = id;
        this.subject_id = subject_id;
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.teachby = teachby;
        this.created_time = created_time;
        this.approve_status = approve_status;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.teacherAvatar = teacherAvatar;
        this.teacherName = teacherName;
        this.teacherRealname = teacherRealname;
        this.teacherPhone = teacherPhone;
        this.teacherEmail = teacherEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
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

    public int getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(int approve_status) {
        this.approve_status = approve_status;
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

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherRealname() {
        return teacherRealname;
    }

    public void setTeacherRealname(String teacherRealname) {
        this.teacherRealname = teacherRealname;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    @Override
    public String toString() {
        return "AdminAddCourseApplicationVO{" +
                "id='" + id + '\'' +
                ", subject_id='" + subject_id + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", teachby='" + teachby + '\'' +
                ", created_time='" + created_time + '\'' +
                ", approve_status=" + approve_status +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", teacherAvatar='" + teacherAvatar + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", teacherRealname='" + teacherRealname + '\'' +
                ", teacherPhone='" + teacherPhone + '\'' +
                ", teacherEmail='" + teacherEmail + '\'' +
                '}';
    }
}
