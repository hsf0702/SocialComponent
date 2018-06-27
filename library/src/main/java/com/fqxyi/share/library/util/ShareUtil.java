package com.fqxyi.share.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.fqxyi.share.library.R;
import com.fqxyi.share.library.dialog.IShareType;

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

    public static int getIcon(int type) {
        switch (type) {
            case IShareType.SHARE_WECHAT: //微信
                return R.drawable.share_icon_wechat;
            case IShareType.SHARE_WECHATMOMENTS: //朋友圈
                return R.drawable.share_icon_wechatmoments;
            case IShareType.SHARE_SHORTMESSAGE: //短信
                return R.drawable.share_icon_shortmessage;
            case IShareType.SHARE_COPY: //复制
                return R.drawable.share_icon_copy;
            case IShareType.SHARE_REFRESH: //刷新
                return R.drawable.share_icon_refresh;
            case IShareType.SHARE_QQ: //QQ
                return R.drawable.share_icon_qq;
            case IShareType.SHARE_SINA: //新浪微博
                return R.drawable.share_icon_sina;
            case IShareType.SHARE_WXMINIPROGRAM: //微信小程序
                return R.drawable.share_icon_wechat;
            case IShareType.SHARE_ALIPAYMINPROGRAM: //支付宝小程序
                return R.drawable.share_icon_alipay;
            case IShareType.SHARE_COLLECTION: //收藏
                return R.drawable.share_icon_collection_normal;
            case IShareType.SHARE_SHOW_ALL: //查看全部
                return R.drawable.share_icon_show_all;
        }
        return R.drawable.share_icon_refresh;
    }

    public static String getName(int type) {
        switch (type) {
            case IShareType.SHARE_WECHAT: //微信好友
                return "微信好友";
            case IShareType.SHARE_WECHATMOMENTS: //朋友圈
                return "朋友圈";
            case IShareType.SHARE_SHORTMESSAGE: //短信
                return "短信";
            case IShareType.SHARE_COPY: //复制
                return "复制链接";
            case IShareType.SHARE_REFRESH: //刷新
                return "刷新";
            case IShareType.SHARE_QQ: //QQ
                return "QQ";
            case IShareType.SHARE_SINA: //新浪微博
                return "微博";
            case IShareType.SHARE_WXMINIPROGRAM: //微信小程序
                return "微信小程序";
            case IShareType.SHARE_ALIPAYMINPROGRAM: //支付宝小程序
                return "支付宝小程序";
            case IShareType.SHARE_COLLECTION: //收藏
                return "收藏";
            case IShareType.SHARE_SHOW_ALL: //查看全部
                return "查看全部";
        }
        return "刷新";
    }

}
