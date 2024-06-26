package com.zoom.exam_sys_backend.pojo.vo;

import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: 包含考试的所有信息，相比于ExamVO，额外包含了考试所在的课程的信息，和考卷路径等信息
 * @DateTime 2024/3/14 18:25
 **/

public class TeacherExtendedExamVO {
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

    private String courseId;
    private String courseName;

    private String paperId;
    private String paperName;
    private String paperDescription;
    private String paperPath;
    private int paperScore;

    private int status;

    public TeacherExtendedExamVO(String id, String name, String description, String start_time, String end_time, String teachby, int type, int published, String created_time, String teacherUsername, String teacherRealname, String courseId, String courseName, String paperId, String paperName, String paperDescription, String paperPath, int paperScore, int status) {
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
        this.courseId = courseId;
        this.courseName = courseName;
        this.paperId = paperId;
        this.paperName = paperName;
        this.paperDescription = paperDescription;
        this.paperPath = paperPath;
        this.paperScore = paperScore;
        this.status = status;
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

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperDescription() {
        return paperDescription;
    }

    public void setPaperDescription(String paperDescription) {
        this.paperDescription = paperDescription;
    }

    public String getPaperPath() {
        return paperPath;
    }

    public void setPaperPath(String paperPath) {
        this.paperPath = paperPath;
    }

    public int getPaperScore() {
        return paperScore;
    }

    public void setPaperScore(int paperScore) {
        this.paperScore = paperScore;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TeacherExtendedExamVO{" +
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
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", paperId='" + paperId + '\'' +
                ", paperName='" + paperName + '\'' +
                ", paperDescription='" + paperDescription + '\'' +
                ", paperPath='" + paperPath + '\'' +
                ", paperScore=" + paperScore +
                ", status=" + status +
                '}';
    }
}
