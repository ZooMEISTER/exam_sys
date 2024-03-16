package com.zoom.exam_sys_backend.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/14 18:44
 **/

@TableName("relation_exam_paper")
public class ExamPaperBO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long exam_id;
    private Long paper_id;

    public ExamPaperBO(Long id, Long exam_id, Long paper_id) {
        this.id = id;
        this.exam_id = exam_id;
        this.paper_id = paper_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExam_id() {
        return exam_id;
    }

    public void setExam_id(Long exam_id) {
        this.exam_id = exam_id;
    }

    public Long getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(Long paper_id) {
        this.paper_id = paper_id;
    }

    @Override
    public String toString() {
        return "ExamPaperBO{" +
                "id=" + id +
                ", exam_id=" + exam_id +
                ", paper_id=" + paper_id +
                '}';
    }
}
