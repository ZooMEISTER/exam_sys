package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/6 12:50
 **/

@TableName("relation_subject_course")
public class SubjectCourseBO {
    @TableId(type = IdType.AUTO)
    private long id;
    private long subject_id;
    private long course_id;

    public SubjectCourseBO(long id, long subject_id, long course_id) {
        this.id = id;
        this.subject_id = subject_id;
        this.course_id = course_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(long subject_id) {
        this.subject_id = subject_id;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }
}
