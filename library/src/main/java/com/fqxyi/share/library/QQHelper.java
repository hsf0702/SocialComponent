package com.fqxyi.share.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fqxyi.share.library.bean.ShareDataBean;
import com.fqxyi.share.library.bean.ShareQQDataBean;
import com.fqxyi.share.library.callback.IShareCallback;
import com.fqxyi.share.library.util.ShareUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQHelper {

    private Activity activity;
    private Tencent tencent;
    //
    private IShareCallback shareCallback;

    public QQHelper(Activity activity, String appId) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }

    public void share(ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        if (!ShareUtil.isQQInstalled(activity)) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_error));
            }
            return;
        }
        tencent.shareToQQ(activity, getShareData(shareDataBean), iUiListener);
    }

    private Bundle getShareData(ShareDataBean shareDataBean) {
        switch (shareDataBean.type) {
            case ShareQQDataBean.TYPE_IMAGE_TEXT:
                return ShareQQDataBean.createImageTextData(
                        shareDataBean.appName,
                        shareDataBean.shareTitle,
                        shareDataBean.shareDesc,
                        shareDataBean.shareImage,
                        shareDataBean.shareUrl
                ).getParams();
            case ShareQQDataBean.TYPE_IMAGE:
                return ShareQQDataBean.createImageData(
                        shareDataBean.appName,
                        shareDataBean.shareImage
                ).getParams();
        }
        return ShareQQDataBean.createImageTextData(
                shareDataBean.appName,
                shareDataBean.shareTitle,
                shareDataBean.shareDesc,
                shareDataBean.shareImage,
                shareDataBean.shareUrl
        ).getParams();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, iUiListener);
    }

    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
    }

    private IUiListener iUiListener = new IUiListener() {
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
