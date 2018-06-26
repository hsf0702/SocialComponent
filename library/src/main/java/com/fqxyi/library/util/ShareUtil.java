package com.fqxyi.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.fqxyi.library.dialog.IShareType;
import com.fqxyi.library.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * 分享工具类
 */
public class ShareUtil {

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
