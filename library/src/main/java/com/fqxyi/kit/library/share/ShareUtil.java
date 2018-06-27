package com.fqxyi.kit.library.share;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

/**
 * 分享工具类
 */
public class ShareUtil {

    /**
     * 判断是网络图片还是本地图片，true为网络图片
     */
    public boolean isNetImage(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return false;
        }
        if (imageUrl.startsWith("http")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将图片（网络图片或本地图片）转换为Bitmap，需要在子线程中工作，否则会抛出Crash
     */
    public static Bitmap getImageBitmap(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        if (imageUrl.startsWith("http")) {
            Bitmap bitmap = null;
            InputStream in = null;
            BufferedOutputStream out = null;
            try {
                in = new BufferedInputStream(new URL(imageUrl).openStream(), 1024);
                final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                out = new BufferedOutputStream(dataStream, 1024);
                copy(in, out);
                out.flush();
                byte[] data = dataStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                data = null;
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return BitmapFactory.decodeFile(imageUrl);
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    public static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 将bitmap转换为byte[]
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, boolean needThumb) {
        Bitmap newBmp;
        if (needThumb) {
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            if (width > height) {
                height = height * 150 / width;
                width = 150;
            } else {
                width = width * 150 / height;
                height = 150;
            }
            newBmp = Bitmap.createScaledBitmap(bmp, width, height, true);
            bmp.recycle();
        } else {
            newBmp = bmp;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        newBmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        newBmp.recycle();

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 是否安装qq
     */
    public static boolean isQQInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfo = packageManager.getInstalledPackages(0);
        if (packageInfo != null) {
            for (int i = 0; i < packageInfo.size(); i++) {
                String pn = packageInfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}
