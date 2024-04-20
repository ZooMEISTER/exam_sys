package com.zoom.exam_sys_backend.util;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

/**
 * @Author ZooMEISTER
 * @Description: AES 加密相关工具类
 * @DateTime 2024/4/16 16:05
 **/

public class AESUtils {

    public static void aesEncryptFile(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFile(sourceFilePath, destFilePath, key, Cipher.ENCRYPT_MODE);
    }
    public static void aesDecryptFile(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFile(sourceFilePath, destFilePath, key, Cipher.DECRYPT_MODE);
    }

    public static void aesEncryptFileForInput(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFileForInput(sourceFilePath, destFilePath, key, Cipher.ENCRYPT_MODE);
    }
    public static void aesDecryptFileForInput(String sourceFilePath, String destFilePath, String key) throws Exception {
        aesFileForInput(sourceFilePath, destFilePath, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 通过文件输入流加密文件并输出到指定路径
     * CipherOutputStream进行加密数据
     */
    public static void aesFile(String sourceFilePath, String destFilePath, String key, int mode) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);
        if (!(sourceFile.exists() && sourceFile.isFile())) {
            throw new IllegalArgumentException("加密源文件不存在");
        }
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(destFile);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKeySpec);
        // 对输出流包装
        CipherOutputStream cout = new CipherOutputStream(out, cipher);
        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            cout.write(cache, 0, nRead);
            cout.flush();
        }
        cout.close();
        out.close();
        in.close();
    }

    /**
     * 通过文件输入流加密文件并输出到指定路径
     * CipherInputStream进行加密数据
     */
    public static void aesFileForInput(String sourceFilePath, String destFilePath, String key, int mode) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);
        if (sourceFile.exists() && sourceFile.isFile()) {
            throw new IllegalArgumentException("加密源文件不存在");
        }
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(destFile);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES/ECB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKeySpec);
        // 对输入流包装
        CipherInputStream cin = new CipherInputStream(in, cipher);

        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = cin.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        cin.close();
        in.close();
    }

    /**
     * AES加密方法，此处使用AES-128-ECB加密模式，key需要为16位
     * @param sKey
     * @return
     */
    public static void encryptStream(ByteArrayOutputStream source, OutputStream out, String sKey) {
        long start = System.currentTimeMillis();
        try {
            // 判断Key是否正确
            if (sKey == null || sKey.length() != 16) {
                throw new RuntimeException("Key为空或长度不为16位");
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            CipherOutputStream cout = new CipherOutputStream(out, cipher);
            cout.write(source.toByteArray());
            cout.close();
            source.close();
        } catch (Exception e) {
            System.out.println("AES解密失败" + e.toString());
        }
    }

    /**
     * AES解密方法，此处使用AES-128-ECB加密模式，key需要为16位
     * @param sKey
     * @return
     */
    public static void decryptStream(InputStream in, OutputStream out, String sKey) {

        long start = System.currentTimeMillis();
        try {
            // 判断Key是否正确
            if (sKey == null || sKey.length() != 16) {
                throw new RuntimeException("Key为空或长度不为16位");
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // 先用base64解密
            System.out.println("AES解密耗时：" + (System.currentTimeMillis() - start) + "ms");

            CipherOutputStream cout = new CipherOutputStream(out, cipher);
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                cout.write(cache, 0, nRead);
                cout.flush();
            }
            cout.close();
            in.close();
        } catch (Exception e) {
            System.out.println("AES解密失败" + e.toString());
        }
    }
}
