package com.fans.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.Map;

/**
 * ftp上传下载工具类
 *
 * @author Fan
 * @date 2018年8月02日下午8:11:51
 */
public class FtpUtil {
    /**
     * Description: 向FTP服务器上传文件
     * host FTP服务器hostname
     * port FTP服务器端口
     * userName FTP登录账号
     * passWord FTP登录密码
     * basePath FTP服务器基础目录
     * filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * fileName 上传到FTP服务器上的文件名
     * input 输入流
     *
     * @return 成功返回true，否则返回false
     */
    public static Boolean uploadFile(Map<String, Object> inParam) {
        //参数提取
        String host = inParam.get("host").toString();
        int port = Integer.parseInt(inParam.get("port").toString());
        String userName = inParam.get("userName").toString();
        String passWord = inParam.get("passWord").toString();
        String basePath = inParam.get("basePath").toString();
        String filePath = inParam.get("filePath").toString();
        String fileName = inParam.get("fileName").toString();
        InputStream input = (InputStream) inParam.get("input");
        Boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            int reply;
            ftpClient.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftpClient.login(userName, passWord);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return result;
            }
            // 切换到上传目录
            if (!ftpClient.changeWorkingDirectory(basePath + filePath)) {
                // 如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftpClient.changeWorkingDirectory(tempPath)) {
                        if (!ftpClient.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftpClient.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //这个方法是每次数据连接之前，ftp client告诉 ftp server开通一个端口来传输数据 主动或者被动
            ftpClient.enterLocalPassiveMode();
            //上传文件
            if (!ftpClient.storeFile(fileName, input)) {
                return result;
            }
            input.close();
            ftpClient.logout();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * Description: 从FTP服务器下载文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
                                       String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }
}
