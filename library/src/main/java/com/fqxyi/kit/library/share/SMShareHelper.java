package com.fqxyi.kit.library.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.fqxyi.kit.util.DownloadUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 短信分享帮助类
 */
public class SMShareHelper {

    //上下文
    private Activity activity;
    //分享结果回调
    private IShareCallback shareCallback;
    //图片缓存的父目录
    private File parentDir;

    /**
     * 初始化短信
     */
    public SMShareHelper(Activity activity) {
        this.activity = activity;
        parentDir = new File(Environment.getExternalStorageDirectory(), "kit" + File.separator + "share");
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    /**
     * 具体的分享逻辑
     */
    public void share(final ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        //判断数据源是否为空
        if (shareDataBean == null) {
            if (shareCallback != null) {
                shareCallback.onError("shareDataBean == null");
            }
            return;
        }
        //分享到短信或彩信
        if (TextUtils.isEmpty(shareDataBean.shareImage)) {
            shareSms(shareDataBean);
        } else {
            if (shareDataBean.shareImage.startsWith("http")) {
                String fileName = shareDataBean.shareImage.substring(shareDataBean.shareImage.lastIndexOf("/") + 1);
                final File file = new File(parentDir, fileName);
                if (file.exists()) {
                    shareMms(shareDataBean, "file://" + file.getAbsolutePath());
                } else {
                    new DownloadUtil(activity, parentDir, shareDataBean.shareImage, new DownloadUtil.DownloadStateListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFailure(List<Integer> failureIndexs) {
                            shareSms(shareDataBean);
                        }

                        @Override
                        public void onAllSucceed(String fileDownloadDir) {
                            shareMms(shareDataBean, "file://" + file.getAbsolutePath());
                        }

                        @Override
                        public void onError(IOException e) {
                            shareSms(shareDataBean);
                        }
                    }).startDownload();
                }
            } else {
                shareMms(shareDataBean, shareDataBean.shareImage);
            }
        }
    }

    /**
     * 分享短信
     */
    private void shareSms(ShareDataBean shareDataBean) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
        intent.putExtra("sms_body", shareDataBean.shareTitle + "【" + shareDataBean.shareDesc + "】 链接 " + shareDataBean.shareUrl);
        activity.startActivity(intent);
    }

    /**
     * 分享彩信
     */
    private void shareMms(ShareDataBean shareDataBean, String localImageUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, shareDataBean.shareTitle);
        intent.putExtra("subject", shareDataBean.shareTitle);
        intent.putExtra("sms_body",shareDataBean.shareTitle + "【" + shareDataBean.shareDesc + "】 链接 " + shareDataBean.shareUrl);
        intent.putExtra(Intent.EXTRA_TEXT, shareDataBean.shareTitle + "【" + shareDataBean.shareDesc + "】 链接 " + shareDataBean.shareUrl);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(localImageUrl));
        intent.setType("image/*");
        activity.startActivity(intent);
    }

}
