package com.fans.utils;

import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.joda.time.DateTime;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * ftp上传下载工具类
 *
 * @author Fan
 * @date 2018年8月02日下午8:11:51
 */
@Slf4j
@NoArgsConstructor
@ToString
public class FtpUtils {
    private static final String HOST = PropertiesUtils.loadProperties("ftp", "host");
    private static final String PORT = PropertiesUtils.loadProperties("ftp", "port");
    private static final String USERNAME = PropertiesUtils.loadProperties("ftp", "userName");
    private static final String PASSWORD = PropertiesUtils.loadProperties("ftp", "passWord");
    private static final String BASE_PATH = PropertiesUtils.loadProperties("ftp", "basePath");
    private static final String URL = PropertiesUtils.loadProperties("ftp", "url");
    private static String FILE_PATH = new DateTime().toString("/yyyy/MM/dd");
    private static FTPClient ftpClient = new FTPClient();

    /**
     * Description: 向FTP服务器上传文件
     * host FTP服务器hostname
     * port FTP服务器端口
     * username FTP登录账号
     * password FTP登录密码
     * basePath FTP服务器基础目录
     * filePath FTP服务器文件存放路径。例如分日期存放：/2019/01/01。文件的路径为basePath+filePath
     * fileName 上传到FTP服务器上的文件名
     * input 输入流
     *
     * @return 成功返回true，否则返回false
     */
    public static String uploadFile(List<File> fileList) throws IOException {
        connectServer();
        StringBuilder remotePath = new StringBuilder();
        FileInputStream fis = null;
        try {
            //  切换到上传目录
            if (!ftpClient.changeWorkingDirectory(BASE_PATH + FILE_PATH)) {
                //  如果目录不存在创建目录
                mkdir();
            }
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding(StandardCharsets.UTF_8.name());
            //设置上传文件的类型为二进制类型
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //这个方法是每次数据连接之前，ftp client告诉 ftp server开通一个端口来传输数据 主动或者被动
            ftpClient.enterLocalPassiveMode();
            //上传文件
            String fileName;
            for (File fileItem : fileList) {
                fis = new FileInputStream(fileItem);
                fileName = fileItem.getName();
                ftpClient.storeFile(fileName, fis);
                remotePath.append(URL).append(FILE_PATH).append("/").append(fileName).append(",");
            }
            Objects.requireNonNull(fis).close();
            ftpClient.logout();
        } catch (Exception e) {
            log.error("【文件上传异常】", e);
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
        remotePath = new StringBuilder(remotePath.substring(0, remotePath.length() - 1));
        return remotePath.toString();
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return 是否成功
     */
    public static boolean downloadFile(String remotePath, String fileName, String localPath) {
        connectServer();
        boolean result = false;
        try {
            // 转移到FTP服务器目录
            ftpClient.changeWorkingDirectory(remotePath);
            FTPFile[] fs = ftpClient.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftpClient.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    log.error("--> 下载文件失败!!! 原因:{}", ioe.getMessage(), ioe);
                }
            }
        }
        return result;
    }

    private static void mkdir() throws Exception {
        String[] dirs = FILE_PATH.split("/");
        String tempPath = BASE_PATH;
        for (String dir : dirs) {
            if (null == dir || "".equals(dir)) {
                continue;
            }
            tempPath += "/" + dir;
            if (!ftpClient.changeWorkingDirectory(tempPath)) {
                if (!ftpClient.makeDirectory(tempPath)) {
                    throw new Exception("--> 【文件目录创建失败】path : " + tempPath + "");
                } else {
                    ftpClient.changeWorkingDirectory(tempPath);
                }
            }
        }
    }

    private static void connectServer() {
        try {
            ftpClient.connect(HOST, Integer.parseInt(Objects.requireNonNull(PORT)));
            ftpClient.login(USERNAME, PASSWORD);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new Exception(String.format("【FTPUtil连接服务失败】 replyCode : %s", reply));
            }
        } catch (Exception e) {
            log.error("--> 【FTPUtil连接服务异常】", e);
        }
    }
}
