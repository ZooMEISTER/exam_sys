package com.zoom.exam_sys_backend.comparator;

import com.zoom.exam_sys_backend.pojo.vo.ExamVO;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/15 16:09
 **/

public class ExamVOComparator implements Comparator<ExamVO> {
    @Override
    public int compare(ExamVO o1, ExamVO o2) {
        return -o1.getCreated_time().compareTo(o2.getCreated_time());
    }
}
