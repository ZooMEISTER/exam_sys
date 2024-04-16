package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 19:20
 **/

@TableName("application_to_teacher")
public class StudentToTeacherBO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long student_id;
    private String description;

    public StudentToTeacherBO(Long id, Long student_id, String description) {
        this.id = id;
        this.student_id = student_id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "StudentToTeacherBO{" +
                "id=" + id +
                ", student_id=" + student_id +
                ", description='" + description + '\'' +
                '}';
    }
}
