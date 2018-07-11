package com.reptile.tianya.common;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

public class FtpUtils {

    private FTPClient ftp;

    public FtpUtils(String ip,String user,String pwd) throws Exception {
        ftp = new FTPClient();
        int reply;
        ftp.connect(ip, 21);
        ftp.login(user, pwd);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        /** 被动上传模式 */
        ftp.enterLocalPassiveMode();
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
        } else {
            ftp.changeWorkingDirectory("");
        }
    }

    /**
     *
     * @param file
     *            上传的文件或文件夹
     * @throws Exception
     */
    public void upload(String path,String filename, File file) throws Exception {
        if (!file.isDirectory()) {
            ftp.makeDirectory(path);
            ftp.changeWorkingDirectory(path);
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);
            ftp.storeFile(filename, input);
            input.close();
        }
    }

    public void upload(String path, File file) throws Exception {
        if (file.isDirectory()) {
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getPath() + "\\" + files[i]);
                if (file1.isDirectory()) {
                    upload(path, file1);
                    ftp.changeToParentDirectory();
                } else {
                    File file2 = new File(file.getPath() + "\\" + files[i]);
                    FileInputStream input = new FileInputStream(file2);
                    ftp.storeFile(file2.getName(), input);
                    input.close();
                }
            }
        } else {
            ftp.makeDirectory(path);
            ftp.changeWorkingDirectory(path);
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);
            ftp.storeFile(file2.getName(), input);
            input.close();
        }
    }


    public static void main(String[] args) throws Exception {
//        MyConstant.FTP_IP = "119.90.36.152";
//        MyConstant.FTP_USER = "hxy";
//        MyConstant.FTP_PASSWORD = "123456";
    	String ip = "119.90.36.152";
    	String user="hxy";
    	String pwd="123456";
        FtpUtils t = new FtpUtils(ip,user,pwd);
        File file = new File("e:\\jd.txt");
        t.upload("ctrip\\323", file);
        System.out.println("success.");
    }
}