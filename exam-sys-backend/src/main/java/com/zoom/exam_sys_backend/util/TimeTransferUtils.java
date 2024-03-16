package com.zoom.exam_sys_backend.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/16 15:13
 **/

public class TimeTransferUtils {
    public static String TransferTime2LocalTime(Date ori){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        //获取当地时区
        Locale locale = Locale.getDefault();
        //利用SimpleDateFormat 进行时间格式的转换
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,locale);

        String aft = sdf.format(ori);
        return aft;
    }
}
