package com.zoom.exam_sys_backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/3/14 20:42
 **/

public class FileUtils {

    // 生成随机长度字符串
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    //保存文件方法
    public static void saveFile(MultipartFile file, String path, String name) throws IOException {
        File dir = new File(path);
        if(!dir.exists()) dir.mkdirs();
        File _file = new File(path + name);
        file.transferTo(_file);
    }
}
