package com.zoom.exam_sys_backend.pojo.vo;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/17 16:18
 **/

public class TeacherExamVO {
    private String id;
    private String name;
    private String description;
    private String start_time;
    private String end_time;
    private String teachby;
    private int type;
    private int published;
    private String created_time;
    private int status;
    private int finishedStudentCount;
    private int totalStudentCount;

    public TeacherExamVO(String id, String name, String description, String start_time, String end_time, String teachby, int type, int published, String created_time, int status, int finishedStudentCount, int totalStudentCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start_time = start_time;
        this.end_time = end_time;
        this.teachby = teachby;
        this.type = type;
        this.published = published;
        this.created_time = created_time;
        this.status = status;
        this.finishedStudentCount = finishedStudentCount;
        this.totalStudentCount = totalStudentCount;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "TeacherExamVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", teachby='" + teachby + '\'' +
                ", type=" + type +
                ", published=" + published +
                ", created_time='" + created_time + '\'' +
                ", status=" + status +
                ", finishedStudentCount=" + finishedStudentCount +
                ", totalStudentCount=" + totalStudentCount +
                '}';
    }
}
