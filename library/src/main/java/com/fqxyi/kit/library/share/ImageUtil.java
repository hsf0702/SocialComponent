package com.fqxyi.kit.library.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.fqxyi.kit.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片操作工具类
 */
public class ImageUtil {

    /**
     * 下载图片
     * @param file 图片存放位置
     * @param imageUrl 图片地址
     * @return true 下载成功 false 下载失败
     */
    public static boolean downloadImage(File file, String imageUrl) {
        if (file == null || TextUtils.isEmpty(imageUrl)) {
            return false;
        }
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(imageUrl);
            // 打开连接
            conn = (HttpURLConnection) url.openConnection();
            // 输入流
            is = conn.getInputStream();
            // 1K的数据缓冲
            byte[] b = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            return true;
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            // 完毕，关闭所有链接
            if (null != conn) {
                conn.disconnect();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
        }
        return false;
    }

    /**
     * 将图片地址转换为Bitmap
     * @param imageUrl 图片地址
     * @return Bitmap
     */
    public static Bitmap getImageBitmap(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        if (!imageUrl.startsWith("http")) {
            return BitmapFactory.decodeFile(imageUrl);
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            // 构造URL
            URL url = new URL(imageUrl);
            // 打开连接
            conn = (HttpURLConnection) url.openConnection();
            // 输入流
            is = conn.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            // 完毕，关闭所有链接
            if (null != conn) {
                conn.disconnect();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
        }
        return null;
    }

    /**
     * 将图片地址转换为byte[]
     * @param imageUrl 图片地址
     * @return byte[]
     */
    public static byte[] getImageByte(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(imageUrl);
            // 打开连接
            conn = (HttpURLConnection) url.openConnection();
            // 输入流
            is = conn.getInputStream();
            // 1K的数据缓冲
            byte[] b = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出流
            os = new ByteArrayOutputStream();
            // 开始读取
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            // 完毕，关闭所有链接
            if (null != conn) {
                conn.disconnect();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
        }
        return null;
    }

}
