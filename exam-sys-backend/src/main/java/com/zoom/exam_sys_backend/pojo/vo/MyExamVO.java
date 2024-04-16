package com.zoom.exam_sys_backend.pojo.vo;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 12:54
 **/

public class MyExamVO {
    private String id;
    private String name;
    private String description;
    private String start_time;
    private String end_time;
    private String teachby;
    private int type;
    private int published;
    private String created_time;

    private String teacherUsername;
    private String teacherRealname;

    private int status;
    private int final_score;
    private int finishedStudentCount;
    private int totalStudentCount;

    private String departmentId;
    private String departmentName;
    private String subjectId;
    private String subjectName;
    private String courseId;
    private String courseName;

    public MyExamVO(String id, String name, String description, String start_time, String end_time, String teachby, int type, int published, String created_time, String teacherUsername, String teacherRealname, int status, int final_score, int finishedStudentCount, int totalStudentCount, String departmentId, String departmentName, String subjectId, String subjectName, String courseId, String courseName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start_time = start_time;
        this.end_time = end_time;
        this.teachby = teachby;
        this.type = type;
        this.published = published;
        this.created_time = created_time;
        this.teacherUsername = teacherUsername;
        this.teacherRealname = teacherRealname;
        this.status = status;
        this.final_score = final_score;
        this.finishedStudentCount = finishedStudentCount;
        this.totalStudentCount = totalStudentCount;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTeachby() {
        return teachby;
    }

    public void setTeachby(String teachby) {
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

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFinal_score() {
        return final_score;
    }

    public void setFinal_score(int final_score) {
        this.final_score = final_score;
    }

    public int getFinishedStudentCount() {
        return finishedStudentCount;
    }

    public void setFinishedStudentCount(int finishedStudentCount) {
        this.finishedStudentCount = finishedStudentCount;
    }

    public int getTotalStudentCount() {
        return totalStudentCount;
    }

    public void setTotalStudentCount(int totalStudentCount) {
        this.totalStudentCount = totalStudentCount;
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "MyExamVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", teachby='" + teachby + '\'' +
                ", type=" + type +
                ", published=" + published +
                ", created_time='" + created_time + '\'' +
                ", teacherUsername='" + teacherUsername + '\'' +
                ", teacherRealname='" + teacherRealname + '\'' +
                ", status=" + status +
                ", final_score=" + final_score +
                ", finishedStudentCount=" + finishedStudentCount +
                ", totalStudentCount=" + totalStudentCount +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
