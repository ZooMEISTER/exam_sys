package com.zoom.exam_sys_backend.comparator;

import com.zoom.exam_sys_backend.pojo.vo.MyExamVO;

import java.util.Comparator;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/19 14:32
 **/

public class MyExamVOComparator implements Comparator<MyExamVO> {
    @Override
    public int compare(MyExamVO o1, MyExamVO o2) {
        return -o1.getCreated_time().compareTo(o2.getCreated_time());
    }
}
