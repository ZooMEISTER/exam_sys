package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/17 17:38
 **/

@TableName("relation_course_student")
public class CourseStudentBO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long course_id;
    private Long student_id;

    public CourseStudentBO(Long id, Long course_id, Long student_id) {
        this.id = id;
        this.course_id = course_id;
        this.student_id = student_id;
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

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    @Override
    public String toString() {
        return "CourseStudentBO{" +
                "id=" + id +
                ", course_id=" + course_id +
                ", student_id=" + student_id +
                '}';
    }
}
