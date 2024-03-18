package com.zoom.exam_sys_backend.comparator;

import com.zoom.exam_sys_backend.pojo.vo.TeacherExamVO;

import java.util.Comparator;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/17 16:22
 **/

public class TeacherExamVOComparator implements Comparator<TeacherExamVO> {
    @Override
    public int compare(TeacherExamVO o1, TeacherExamVO o2) {
        return -o1.getCreated_time().compareTo(o2.getCreated_time());
    }
}
