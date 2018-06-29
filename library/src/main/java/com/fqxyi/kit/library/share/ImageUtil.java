package com.fqxyi.kit.library.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.fqxyi.kit.util.LogUtil;

import java.io.ByteArrayInputStream;
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
            return compressBitmapGetBitmap(imageUrl, null);
        }
        try {
            return compressBitmapGetBitmap(null, new URL(imageUrl));
        } catch (IOException e) {
            LogUtil.e(e);
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
        if (!imageUrl.startsWith("http")) {
            return compressBitmapGetByte(imageUrl, null);
        }
        try {
            return compressBitmapGetByte(null, new URL(imageUrl));
        } catch (IOException e) {
            LogUtil.e(e);
        }
        return null;
    }

    /**
     * 图片压缩
     * @param url 图片地址
     * @return Bitmap
     */
    public static Bitmap compressBitmapGetBitmap(String localFilePath, URL url) {
        return compressBitmapQualityGetBitmap(compressBitmapSize(localFilePath, url), 32);
    }

    /**
     * 图片压缩
     * @param url 图片地址
     * @return byte[]
     */
    public static byte[] compressBitmapGetByte(String localFilePath, URL url) {
        return compressBitmapQualityGetByte(compressBitmapSize(localFilePath, url), 32);
    }

    /**
     * 尺寸压缩 限制图片的最大边长为200px
     * @param url 图片地址
     * @return Bitmap
     */
    public static Bitmap compressBitmapSize(String localFilePath, URL url) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        options.inJustDecodeBounds = true;
        if (!TextUtils.isEmpty(localFilePath)) {
            BitmapFactory.decodeFile(localFilePath);
        } else {
            try {
                //打开连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                //关闭连接
                conn.disconnect();
            } catch (IOException e) {
                LogUtil.e(e);
                return null;
            }
        }
        // 获取图片宽高
        int height = options.outHeight;
        int width = options.outWidth;
        // 默认像素压缩比例，压缩为原图的1/1
        int inSampleSize = 1;
        // 获取图片的最大边长
        int maxLen = Math.max(height, width);
        if (maxLen > 200) {
            //向上取整
            inSampleSize = (int) Math.ceil(maxLen / 200f);
        }
        // 计算好压缩比例后，这次可以去加载原图了
        options.inJustDecodeBounds = false;
        // 设置为刚才计算的压缩比例
        options.inSampleSize = inSampleSize;
        //
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (!TextUtils.isEmpty(localFilePath)) {
            return BitmapFactory.decodeFile(localFilePath);
        } else {
            try {
                //重新打开连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                //重新关闭连接
                conn.disconnect();
                return bitmap;
            } catch (IOException e) {
                LogUtil.e(e);
                return null;
            }
        }
    }

    public static Bitmap compressBitmapQualityGetBitmap(Bitmap bitmap, long maxSize) {
        return BitmapFactory.decodeStream(new ByteArrayInputStream(compressBitmapQuality(bitmap, maxSize).toByteArray()));
    }

    public static byte[] compressBitmapQualityGetByte(Bitmap bitmap, long maxSize) {
        return compressBitmapQuality(bitmap, maxSize).toByteArray();
    }

    /**
     * 质量压缩 压缩Bitmap到指定的大小范围内
     * @param maxSize Bitmap被允许占有的最大大小，单位为KB
     * @return Bitmap
     */
    private static ByteArrayOutputStream compressBitmapQuality(Bitmap bitmap, long maxSize) {
        // Bitmap默认质量为100，表示从未被压缩过
        int quality = 100;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length / 1024 > maxSize && quality > 0) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }
        bitmap.recycle();

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos;
    }

}
