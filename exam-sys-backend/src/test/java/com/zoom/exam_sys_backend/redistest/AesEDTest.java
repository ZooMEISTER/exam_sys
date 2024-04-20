package com.zoom.exam_sys_backend.redistest;

import com.zoom.exam_sys_backend.util.AESUtils;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/18 15:29
 **/

public class AesEDTest {
    public static void main(String[] args) throws Exception {
        String sfp = "C:\\Users\\ZooMEISTER\\Downloads\\智能基座昇腾OpenLab高校开发者体验项目v2.0.pdf";
        String edfp = "C:\\Users\\ZooMEISTER\\Downloads\\智能基座昇腾OpenLab高校开发者体验项目v2.0_e.pdf";
        String ddfp = "C:\\Users\\ZooMEISTER\\Downloads\\9pgUpbG5FIZH3dhWrHePFCCk3qXm9O1R智能基座昇腾OpenLab高校开发者体验项目v2.0_d.pdf";
        String key = "RiyN05VVOf4rFGKz";
        AESUtils.aesEncryptFile(sfp, edfp, key);
        AESUtils.aesDecryptFile(edfp, ddfp, key);
    }
}
