package org.zoom;

import org.zoom.Utils.*;

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
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
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
        mainFrame.setSize(new Dimension(500,175));
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
        panel1.setSize(new Dimension(500,90));
        mainTab.addTab("解密试卷", null, panel1,"Does nothing");

        JComponent panel2 = makeSignPanel("签名答卷");
        panel2.setSize(new Dimension(500,90));
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
        JTextField signTextField = new JTextField(20);
        JButton getSignButton = new JButton("复制签名");
        JTextField publicKeyTextField = new JTextField(20);
        JButton getPublicKeyButton = new JButton("复制公钥");


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
                        // 对文件生成sha256hash值
                        byte[] sha256 = Sha256Utils.calculateSHA256(currentFile.getPath());

                        // 生成密钥对
                        KeyPair keyPair = ECDsaUtils.generateKeyPair_ECDSA();
                        byte[] privateKey = keyPair.getPrivate().getEncoded();
                        byte[] publicKey = (keyPair.getPublic().getEncoded());
                        publicKeyTextField.setText(HexUtils.bytesToHexString(publicKey));
                        System.out.println("PrivateKey: " + HexUtils.bytesToHexString(privateKey));
                        System.out.println("PublicKey: " + HexUtils.bytesToHexString(publicKey));

                        // 对文件的sha256生成签名
                        byte[] sign = ECDsaUtils.getSign_ECDSA(privateKey, sha256);
                        signTextField.setText(HexUtils.bytesToHexString(sign));
                        System.out.println("Sign: " + HexUtils.bytesToHexString(sign));

                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        statusLabel.setText(formatter.format(date) + " 答卷签名已生成...");
                    } catch (IOException | NoSuchAlgorithmException er) {
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        statusLabel.setText(formatter.format(date) + " 答卷签名失败...");

                        er.printStackTrace();
                    } catch (InvalidKeySpecException ex) {
                        throw new RuntimeException(ex);
                    } catch (SignatureException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvalidKeyException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // 复制签名到剪切板事件
        getSignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClipboardUtils.setSysClipboardText(signTextField.getText());
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                statusLabel.setText(formatter.format(date) + " 签名已复制到剪切板...");
            }
        });

        // 复制公钥到剪切板事件
        getPublicKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClipboardUtils.setSysClipboardText(publicKeyTextField.getText());
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                statusLabel.setText(formatter.format(date) + " 公钥已复制到剪切板...");
            }
        });

        panel.add(signButton);

        JPanel jp1 = new JPanel(false);
        jp1.setLayout(new GridLayout(1, 2));
        jp1.add(signTextField);
        jp1.add(getSignButton);
        JPanel jp2 = new JPanel(false);
        jp2.setLayout(new GridLayout(1, 2));
        jp2.add(publicKeyTextField);
        jp2.add(getPublicKeyButton);

        panel.add(jp1);
        panel.add(jp2);

        return panel;
    }


}