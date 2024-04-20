package org.zoom;

import org.zoom.Utils.AesUtils;
import org.zoom.Utils.ClipboardUtils;
import org.zoom.Utils.Sha256Utils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author $USER
 * @Description: TODO
 * @DateTime $DATE $TIME
 **/

public class Main {

    static JLabel statusLabel = new JLabel("status");

    public static void main(String[] args) {
        // 窗体对象
        JFrame mainFrame = new JFrame("ExamSysHelper");
        mainFrame.setSize(new Dimension(400,190));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Panel对象
        JPanel mainPanel = new JPanel(new FlowLayout());

        // 选项卡对象
        JTabbedPane mainTab = new JTabbedPane();

        // 标签页内容对象
        JComponent panel1 = makeDecryptPanel("解密试卷");
        panel1.setPreferredSize(new Dimension(350,90));
        mainTab.addTab("解密试卷", null, panel1,"Does nothing");

        JComponent panel2 = makeSignPanel("签名答卷");
        mainTab.addTab("签名答卷", null, panel2,"Does nothing");


        // 窗体底部状态栏
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);

        // 组装窗体
        mainPanel.add(mainTab); // 将mainTab添加到mainPanel
        mainFrame.add(mainPanel); // 将mainPanel添加到mainFrame
        mainFrame.add(statusPanel, BorderLayout.SOUTH); // 添加底部状态栏

        mainFrame.setVisible(true);
    }

    // 解密试卷标签页
    protected static JComponent makeDecryptPanel(String text){
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3,1));
        JButton decryptButton = new JButton("点我选择要解密的试卷");
        JTextField aesKeyTextField = new JTextField();
        JButton pasteAesKeyButton = new JButton("粘贴AES密钥");

        // 创建并配置文件选择对话框
        JFileChooser decryptFileChooser = new JFileChooser();
        decryptFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        decryptFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.getName().toLowerCase().endsWith(".pdf")) return true;
                return false;
            }
            @Override
            public String getDescription() { return "PDF文件(*.pdf)"; }
        });

        // 解密按钮事件
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("decrypting...");
                int res = decryptFileChooser.showOpenDialog(null);
                if(res == JFileChooser.APPROVE_OPTION){
                    // 用户点击了确定
                    File currentFile = decryptFileChooser.getSelectedFile();
                    System.out.println(currentFile.getName());
                    System.out.println(currentFile.getPath());
                    System.out.println(currentFile.getParent());
                    String inPath = currentFile.getPath();
                    String newName = currentFile.getName().substring(0, currentFile.getName().lastIndexOf(".")) + "_DECRYPTED" + currentFile.getName().substring(currentFile.getName().lastIndexOf("."));
                    String outPath = currentFile.getParent() + "\\" + newName;
                    String key = aesKeyTextField.getText();
                    try {
                        AesUtils.aesDecryptFile(inPath, outPath, key);
                        System.out.println("试卷解密成功");
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        statusLabel.setText(formatter.format(date) + " 试卷解密成功...");
                    } catch (Exception ex) {
                        System.out.println("试卷解密失败");
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        statusLabel.setText(formatter.format(date) + " 试卷解密失败...");
                        File errFile = new File(outPath);
                        if(errFile.exists()) errFile.delete();
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        // 粘贴AES密钥事件
        pasteAesKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aesKeyTextField.setText(ClipboardUtils.getSysClipboardText());
            }
        });

        panel.add(decryptButton);
        panel.add(aesKeyTextField);
        panel.add(pasteAesKeyButton);
        return panel;
    }

    // 签名答卷标签页
    protected static JComponent makeSignPanel(String text){
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3,1));
        JButton signButton = new JButton("点我选择要签名的答卷");
        JTextField sha256TextField = new JTextField();
        JButton getSha256Button = new JButton("复制SHA-256值");

        // 创建并配置文件选择对话框
        JFileChooser signFileChooser = new JFileChooser();
        signFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        signFileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.getName().toLowerCase().endsWith(".pdf")) return true;
                return false;
            }
            @Override
            public String getDescription() { return "PDF文件(*.pdf)"; }
        });

        // 签名答卷事件
        signButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("signing...");
                int res = signFileChooser.showOpenDialog(null);
                if(res == JFileChooser.APPROVE_OPTION) {
                    // 用户点击了确定
                    File currentFile = signFileChooser.getSelectedFile();
                    System.out.println(currentFile.getName());
                    System.out.println(currentFile.lastModified());
                    System.out.println(currentFile.getPath());

                    try {
                        byte[] sha256 = Sha256Utils.calculateSHA256(currentFile.getPath());
                        String sha256Hex = Sha256Utils.bytesToHex(sha256);
                        System.out.println("SHA256: " + sha256Hex);
                        sha256TextField.setText(sha256Hex);

                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        statusLabel.setText(formatter.format(date) + " 答卷签名已生成...");
                    } catch (IOException | NoSuchAlgorithmException er) {
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        statusLabel.setText(formatter.format(date) + " 答卷签名失败...");

                        er.printStackTrace();
                    }
                }
            }
        });

        // 复制sha256值到剪切板事件
        getSha256Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClipboardUtils.setSysClipboardText(sha256TextField.getText());
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                statusLabel.setText(formatter.format(date) + " SHA-256值已复制到剪切板...");
            }
        });

        panel.add(signButton);
        panel.add(sha256TextField);
        panel.add(getSha256Button);
        return panel;
    }


}