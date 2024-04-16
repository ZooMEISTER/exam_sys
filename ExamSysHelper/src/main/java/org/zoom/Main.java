package org.zoom;

import org.zoom.Utils.AesUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author $USER
 * @Description: TODO
 * @DateTime $DATE $TIME
 **/

public class Main {

    public static void main(String[] args) throws Exception {
        Frame mFrame = new Frame("ExamSysHelper");
        mFrame.setLocation(500,500);
        mFrame.setSize(300,200);
        mFrame.setBackground(Color.darkGray);
        mFrame.setVisible(true);

    }
}