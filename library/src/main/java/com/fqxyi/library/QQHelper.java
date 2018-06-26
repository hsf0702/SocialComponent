package com.fqxyi.library;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.library.bean.ShareDataBean;
import com.fqxyi.library.bean.ShareQQDataBean;
import com.fqxyi.library.callback.IShareCallback;
import com.fqxyi.library.util.ShareUtil;
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

    public void share(int type, ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        if (!ShareUtil.isQQInstalled(activity)) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_error));
            }
            return;
        }
        switch (type) {
            case ShareDataBean.TYPE_IMAGE_TEXT:
                shareDataBean = ShareQQDataBean.createImageTextData(
                        shareDataBean.appName,
                        shareDataBean.shareTitle,
                        shareDataBean.shareDesc,
                        shareDataBean.shareImage,
                        shareDataBean.shareUrl
                );
            break;
            case ShareDataBean.TYPE_IMAGE:
                shareDataBean = ShareQQDataBean.createImageData(
                        shareDataBean.appName,
                        shareDataBean.shareImage
                );
                break;
        }
        tencent.shareToQQ(activity, shareDataBean.getParams(), iUiListener);
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
