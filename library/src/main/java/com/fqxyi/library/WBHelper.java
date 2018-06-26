package com.fqxyi.library;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.fqxyi.library.bean.ShareDataBean;
import com.fqxyi.library.bean.ShareWBDataBean;
import com.fqxyi.library.callback.IShareCallback;
import com.fqxyi.library.util.ShareUtil;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

public class WBHelper {

    private Activity activity;

    private IShareCallback shareCallback;

    private WbShareHandler wbShareHandler;

    public WBHelper(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, ""));
    }

    public void share(ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        if (!WbSdk.isWbInstall(activity)) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_wb_uninstall));
            }
            return;
        }
        wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();
        //
        WeiboMultiMessage weiboMessage = getShareMessage(shareDataBean);
        if (weiboMessage == null) {
            if (shareCallback != null) {
                shareCallback.onError("weiboMessage == null");
            }
            return;
        }
        wbShareHandler.shareMessage(weiboMessage, false);
    }

    private WeiboMultiMessage getShareMessage(ShareDataBean shareDataBean) {
        WeiboMultiMessage msg = new WeiboMultiMessage();
        BaseMediaObject mediaObject = null;
        switch (shareDataBean.type) {
            case ShareWBDataBean.TYPE_TEXT:
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                mediaObject = msg.textObject;
                break;
            case ShareWBDataBean.TYPE_IMAGE_TEXT:
                msg.imageObject = getImageObj(shareDataBean.shareImage);
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                mediaObject = msg.imageObject;
                break;
            case ShareWBDataBean.TYPE_WEB:
                msg.mediaObject = getWebPageObj(shareDataBean.shareUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                mediaObject = msg.mediaObject;
                break;
        }
        if (mediaObject == null) {
            if (shareCallback != null) {
                shareCallback.onError("mediaObject == null");
            }
            return null;
        }
        return msg;
    }

    private TextObject getTextObj(String text) {
        TextObject textObj = new TextObject();
        textObj.text = text;
        return textObj;
    }

    private ImageObject getImageObj(String imageUrl) {
        ImageObject imgObj = new ImageObject();
        imgObj.imagePath = imageUrl;
        return imgObj;
    }

    private WebpageObject getWebPageObj(String url, String title, String desc, String imageUrl) {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = Utility.generateGUID();
        webpageObject.actionUrl = url;
        if (addTitleSummaryAndThumb(webpageObject, title, desc, imageUrl)) {
            return null;
        }
        return webpageObject;
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(BaseMediaObject msg, String title, String desc, String imageUrl) {
        msg.title = title;
        msg.description = desc;
        Bitmap bitmap = ShareUtil.getImageBitmap(imageUrl);
        if (bitmap == null) {
            return true;
        }
        msg.thumbData = ShareUtil.bmpToByteArray(bitmap, true);
        bitmap.recycle();
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onNewIntent(Intent intent) {
        if (wbShareHandler != null) {
            wbShareHandler.doResultIntent(intent, wbShareCallback);
        }
    }

    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
    }

    private WbShareCallback wbShareCallback = new WbShareCallback() {
        @Override
        public void onWbShareSuccess() {
            if (shareCallback != null) {
                shareCallback.onSuccess();
            }
        }

        @Override
        public void onWbShareCancel() {
            if (shareCallback != null) {
                shareCallback.onCancel();
            }
        }

        @Override
        public void onWbShareFail() {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_error));
            }
        }
    };

}
