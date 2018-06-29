package com.fqxyi.kit.library.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fqxyi.kit.library.R;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;

/**
 * QQ分享帮助类
 */
public class QQShareHelper {

    //分享图文
    public static final int TYPE_IMAGE_TEXT = 0;
    //分享本地图片
    public static final int TYPE_IMAGE = 1;
    //分享音乐
    public static final int TYPE_MUSIC = 2;
    //分享应用
    public static final int TYPE_APP = 3;

    //上下文
    private Activity activity;
    //
    private Tencent tencent;
    //分享结果回调
    private IShareCallback shareCallback;
    //图片缓存的父目录
    private File parentDir;

    /**
     * 初始化QQ
     */
    public QQShareHelper(Activity activity, String appId, File parentDir) {
        this.activity = activity;
        this.parentDir = parentDir;
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }

    /**
     * 具体的分享逻辑
     */
    public void share(ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        //判断数据源是否为空
        if (shareDataBean == null) {
            if (shareCallback != null) {
                shareCallback.onError("shareDataBean == null");
            }
            return;
        }
        //判断是否安装QQ
        if (!tencent.isQQInstalled(activity)) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_qq_uninstall));
            }
            return;
        }
        Bundle bundle = getShareData(shareDataBean);
        //特殊处理
        if (shareDataBean.type == QQShareHelper.TYPE_IMAGE) {
            if (TextUtils.isEmpty(bundle.getString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL))) {
                if (shareCallback != null) {
                    shareCallback.onError(activity.getString(R.string.share_img_not_found));
                }
                return;
            }
        }
        //分享到QQ
        tencent.shareToQQ(activity, bundle, shareListener);
    }

    /**
     * 获得需要传递给QQ的分享数据
     */
    private Bundle getShareData(ShareDataBean shareDataBean) {
        Bundle bundle = new Bundle();
        switch (shareDataBean.type) {
            case QQShareHelper.TYPE_IMAGE_TEXT:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareDataBean.shareTitle);
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDataBean.shareDesc);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareDataBean.shareImage);
                bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareDataBean.shareUrl);
                break;
            case QQShareHelper.TYPE_IMAGE:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, ImageUtil.getLocalImagePath(parentDir, shareDataBean.shareImage));
                break;
            case QQShareHelper.TYPE_MUSIC:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareDataBean.shareTitle);
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDataBean.shareDesc);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareDataBean.shareImage);
                bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareDataBean.shareUrl);
                bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, shareDataBean.shareMusicUrl);
                break;
            case QQShareHelper.TYPE_APP:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareDataBean.shareTitle);
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDataBean.shareDesc);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareDataBean.shareImage);
                break;
        }
        return bundle;
    }

    /**
     * QQ开放平台需要
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
    }

    /**
     * QQ的分享监听器
     */
    private IUiListener shareListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (shareCallback != null) {
                shareCallback.onSuccess();
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (shareCallback != null && uiError != null) {
                shareCallback.onError(uiError.errorMessage);
            }
        }

        @Override
        public void onCancel() {
            if (shareCallback != null) {
                shareCallback.onCancel();
            }
        }
    };

}
