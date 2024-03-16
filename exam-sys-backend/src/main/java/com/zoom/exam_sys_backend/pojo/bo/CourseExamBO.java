package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/14 14:54
 **/

@TableName("relation_course_exam")
public class CourseExamBO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long course_id;
    private Long exam_id;

    public CourseExamBO(Long id, Long course_id, Long exam_id) {
        this.id = id;
        this.course_id = course_id;
        this.exam_id = exam_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public Long getExam_id() {
        return exam_id;
    }

    public void setExam_id(Long exam_id) {
        this.exam_id = exam_id;
    }

    @Override
    public String toString() {
        return "CourseExamBO{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", exam_id=" + exam_id +
                '}';
    }
}
