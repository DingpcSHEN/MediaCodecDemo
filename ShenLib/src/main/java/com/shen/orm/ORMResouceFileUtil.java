package com.shen.orm;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.content.Context;
/**
 * assert资源的读取
 * Created by Administrator on 2017/4/28 0028.
 */
public class ORMResouceFileUtil {

    /**
     * 从assert文件夹下读取文本资源
     * @param context 上下文
     * @param fileName 文件名
     * @return 文件内容字符串
     */
    public static String readStringFromAssert(Context context, String fileName) {
        String result = null;
        byte[] buffer = readBytesFromAssert(context, fileName);
        try {
            result = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从raw文件夹下读取文本资源
     *
     * @param context 上下文
     * @param rawId raw资源id
     * @return 文件内容字符串
     */
    public static String readStringFromRaw(Context context, int rawId) {
        String result = null;
        byte[] buffer = readBytesFromRaw(context, rawId);
        try {
            result = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从assert文件夹下读取文件到字节数组
     *
     * @param context 上下文
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public static byte[] readBytesFromAssert(Context context, String fileName) {
        InputStream is = null;
        byte[] buffer = null;
        try {
            is = context.getAssets().open(fileName);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return buffer;
    }

    /**
     * 从raw文件夹下读取文件到字节数组
     *
     * @param context 上下文
     * @param rawId raw资源id
     * @return 文件字节数组
     */
    public static byte[] readBytesFromRaw(Context context, int rawId) {
        InputStream is = null;
        byte[] buffer = null;
        try {
            is = context.getResources().openRawResource(rawId);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }


}
