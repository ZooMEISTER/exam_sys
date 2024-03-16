package com.zoom.exam_sys_backend.comparator;

import com.zoom.exam_sys_backend.pojo.vo.StudentExamVO;

import java.util.Comparator;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/16 15:06
 **/

public class StudentExamVOComparator implements Comparator<StudentExamVO> {
    @Override
    public int compare(StudentExamVO o1, StudentExamVO o2) {
        return -o1.getCreated_time().compareTo(o2.getCreated_time());
    }
}
