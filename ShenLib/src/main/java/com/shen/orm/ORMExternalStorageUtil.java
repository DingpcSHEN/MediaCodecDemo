package com.shen.orm;

import android.os.Environment;

import com.shen.tools.ToolDebug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/**
 * 外部存儲卡工具类
 * 需要添加权限
 * android.permission.WRITE_EXTERNAL_STORAGE
 * android.permission.MOUNT_UNMOUNT_FILESYSTEMS
 * Created by Administrator on 2017/4/28 0028.
 */
public class ORMExternalStorageUtil {

    /**
     * 获取绝对路径
     * @param fileName
     * @return
     */
    public static String getAbsolutePath(String fileName){
        return getExternalStoragePath() + "/" + fileName;
    }
    public static String getAbsolutePath(String path,String fileName){
        return getExternalStoragePath() + path + fileName;
    }
    /**
     * 是否可写
     * @return 可写性
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 是否可读
     * @return 可读性
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 获得根路径
     * @return 外置内存卡根路径
     */
    public static String getExternalStoragePath() {
        if (isExternalStorageWritable())
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        else
            return null;
    }

    /**
     * 获得下载目录路径
     * @return 外置内存卡下载路径
     */
    public static String getExternalDownloadPath() {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    /**
     * 向根路径写文件
     * @param fileName 文件名
     * @param content 上下文
     * @return 是否写入成功
     */
    public static boolean write(String fileName, String content) {
        return write("/", fileName, content);
    }

    /**
     * 向根目录写字节
     * @param fileName 文件名
     * @param bytes 文件字节数组
     * @return 是否写入成功
     */
    public static boolean writeBytes(String fileName, byte[] bytes) {
        return writeBytes("/", fileName, bytes);
    }

    /**
     * 向指定目录的文件中写入字符串,路径以/开始/结尾
     * @param path 相对于根路径的路径，路径以/开始，以/结尾
     * @param fileName 文件名
     * @param content 文件内容
     * @return 是否写入成功
     */
    public static boolean write(String path, String fileName, String content) {
        return writeBytes(path, fileName, content.getBytes());
    }

    /**
     * 向指定目录的文件写入字节数组,路径以/开始/结尾
     * @param path 相对于根路径的路径，路径以/开始，以/结尾
     * @param fileName 文件名
     * @param bytes 字节数组
     * @return
     */
    public static boolean writeBytes(String path, String fileName, byte bytes[]) {
        boolean flag = false;
        if (!path.equals("/")) {
            File dir = new File(getExternalStoragePath() + path);
            if (!dir.exists()) {
                if (!(dir.mkdir() || dir.isDirectory())) {
                    // 文件目录创建失败或者不是一个目录
                    return false;
                }
            }
        }
        File file = new File(getExternalStoragePath() + path + fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            fos.write(bytes);
            flag = true;
        } catch (FileNotFoundException e) {
            ToolDebug.printLogE("ORM","writeBytes-FileNotFoundException "+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            ToolDebug.printLogE("ORM","writeBytes-IOException "+e.toString());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ToolDebug.printLogE("ORM","writeBytes-flag="+flag);
        return flag;
    }

    /**
     * 向指定目录的文件追加数据
     * @param path
     * @param fileName
     * @param bytes
     * @return
     */
    public static boolean appendBytes(String path, String fileName, byte bytes[]) {
        boolean flag = false;
        if (!path.equals("/")) {
            File dir = new File(getExternalStoragePath() + path);
            if (!dir.exists()) {
                if (!(dir.mkdir() || dir.isDirectory())) {
                    // 文件目录创建失败或者不是一个目录
                    return false;
                }
            }
        }
        // 打开一个随机访问文件流 按读写方式
        try {
            RandomAccessFile randomFile = new RandomAccessFile(getExternalStoragePath() + path + fileName, "rw");
            // 文件长度 字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾
            randomFile.seek(fileLength);
            randomFile.write(bytes);
            randomFile.close();
            flag=true;
        }catch (Exception e){
            ToolDebug.printLogE("ORM","appendBytes-e="+e.toString());
        }
        ToolDebug.printLogE("ORM","appendBytes-flag="+flag);
        return flag;
    }
    public static boolean appendBytes( String fileName, byte bytes[]) {
        return appendBytes("/",fileName,bytes);
    }

    /**
     * 从根路径读字节
     * @param fileName 文件名
     * @return 字节数组
     */
    public static byte[] readBytes(String fileName) {
        return readBytes("/", fileName);
    }

    /**
     * 从指定目录读字节,路径以/开始/结尾
     * @param path 相对于根路径的路径，路径以/开始，以/结尾
     * @param fileName 文件名
     * @return 字节数组
     */
    public static byte[] readBytes(String path, String fileName) {
        File file = new File(getExternalStoragePath() + path + fileName);
        if (!file.isFile()) {
            return null;
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                int length = fis.available();
                byte[] buffer = new byte[length];
                fis.read(buffer);
                return buffer;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 从根目录读文本
     * @param fileName 文件名
     * @return 字符串
     */
    public static String read(String fileName) {
        return read("/", fileName);
    }

    /**
     * 从指定目录读文本,路径以/开始/结尾
     * @param path 相对于根路径的路径，路径以/开始，以/结尾
     * @param fileName 文件名
     * @return 字符串
     */
    public static String read(String path, String fileName) {
        try {
            byte[] readBytes = readBytes(path, fileName);
            if (readBytes == null) {
                return null;
            }
            return new String(readBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从根目录删除
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public static boolean delete(String fileName) {
        return delete("/", fileName);
    }

    /**
     * 从指定目录删除,路径以/开始/结尾
     * @param path 相对于根路径的路径，路径以/开始，以/结尾
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public static boolean delete(String path, String fileName) {
        File file = new File(getExternalStoragePath() + path + fileName);
        if (file.exists())
            return file.delete();
        else
            return true;
    }




}
