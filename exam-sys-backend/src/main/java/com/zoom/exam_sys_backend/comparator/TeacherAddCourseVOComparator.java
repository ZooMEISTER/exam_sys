package com.zoom.exam_sys_backend.comparator;

import com.zoom.exam_sys_backend.pojo.vo.TeacherAddCourseVO;

import java.util.Comparator;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 21:38
 **/

public class TeacherAddCourseVOComparator implements Comparator<TeacherAddCourseVO> {
    @Override
    public int compare(TeacherAddCourseVO o1, TeacherAddCourseVO o2) {
        return -o1.getCreated_time().compareTo(o2.getCreated_time());
    }
}
