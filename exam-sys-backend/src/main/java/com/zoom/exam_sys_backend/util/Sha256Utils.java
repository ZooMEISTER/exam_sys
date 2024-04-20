package com.zoom.exam_sys_backend.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/18 15:31
 **/

public class Sha256Utils {
    /**
     * 计算SHA256哈希值
     * @param filePath 文件路径
     * @return 字节数组
     * @throws IOException IO异常
     * @throws NoSuchAlgorithmException NoSearch算法异常
     */
    public static byte[] calculateSHA256(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (
                FileInputStream fis = new FileInputStream(filePath);
                FileChannel channel = fis.getChannel();
                DigestInputStream dis = new DigestInputStream(fis, digest)) {
            ByteBuffer buffer = ByteBuffer.allocate(8192); // 8 KB buffer
            while (channel.read(buffer) != -1) {
                buffer.flip();
                digest.update(buffer);
                buffer.clear();
            }
            return digest.digest();
        }
    }

    /**
     * 将字节数组转换为String类型哈希值
     * @param bytes 字节数组
     * @return 哈希值
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
