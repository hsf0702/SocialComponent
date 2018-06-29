package com.fqxyi.kit.library.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.fqxyi.kit.library.R;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 新浪微博分享帮助类
 */
public class WBShareHelper {

    //分享文本
    public static final int TYPE_TEXT = 0;
    //分享图片（单张）
    public static final int TYPE_IMAGE = 1;
    //分享图片（多张）
    public static final int TYPE_IMAGE_MULTI = 2;
    //分享视频
    public static final int TYPE_VIDEO = 3;
    //分享网页
    public static final int TYPE_WEB = 4;

    //上下文
    private Activity activity;
    //
    private WbShareHandler wbShareHandler;
    //分享结果回调
    private IShareCallback shareCallback;

    /**
     * 初始化新浪微博
     */
    public WBShareHelper(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, ""));
        wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();
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
        //判断是否安装新浪微博
        if (!WbSdk.isWbInstall(activity)) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_wb_uninstall));
            }
            return;
        }
        //需要传递给新浪微博的分享数据
        WeiboMultiMessage weiboMultiMessage = getShareMessage(shareDataBean);
        if (weiboMultiMessage == null) {
            if (shareCallback != null) {
                shareCallback.onError("weiboMultiMessage == null");
            }
            return;
        }
        //分享到新浪微博
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }

    /**
     * 需要传递给新浪微博的分享数据
     */
    private WeiboMultiMessage getShareMessage(ShareDataBean shareDataBean) {
        WeiboMultiMessage msg = new WeiboMultiMessage();
        BaseMediaObject mediaObject = null;
        switch (shareDataBean.type) {
            case WBShareHelper.TYPE_TEXT:
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                mediaObject = msg.textObject;
                break;
            case WBShareHelper.TYPE_IMAGE:
                msg.imageObject = getImageObj(shareDataBean.shareImage);
                mediaObject = msg.imageObject;
                break;
            case WBShareHelper.TYPE_IMAGE_MULTI:
                msg.multiImageObject = getMultiImgObj(shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage, shareDataBean.shareImageList);
                mediaObject = msg.multiImageObject;
                break;
            case WBShareHelper.TYPE_VIDEO:
                msg.videoSourceObject = getVideoObj(shareDataBean.shareImage, shareDataBean.shareVideoUrl);
                mediaObject = msg.videoSourceObject;
                break;
            case WBShareHelper.TYPE_WEB:
                msg.mediaObject = getWebPageObj(shareDataBean.shareUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                mediaObject = msg.mediaObject;
                break;
        }
        if (mediaObject == null) {
            return null;
        }
        return msg;
    }

    private TextObject getTextObj(String desc) {
        TextObject textObject = new TextObject();
        textObject.text = desc;
        return textObject;
    }

    private ImageObject getImageObj(String image) {
        byte[] imageData = ImageUtil.getImageByte(image, 2097152);
        if (imageData == null) {
            return null;
        }
        ImageObject imageObject = new ImageObject();
        imageObject.imageData = imageData;
        return imageObject;
    }

    /**
     * @param imageList 本地图片集合
     */
    private MultiImageObject getMultiImgObj(String title, String desc, String image, List<String> imageList) {
        MultiImageObject multiImageObject = new MultiImageObject();
        ArrayList<Uri> uris = new ArrayList<>();
        if (imageList != null) {
            for (String imageStr : imageList) {
                if (TextUtils.isEmpty(imageStr)) {
                    continue;
                }
                uris.add(Uri.fromFile(new File(imageStr)));
            }
        }
        multiImageObject.setImageList(uris);
        if (addTitleSummaryAndThumb(multiImageObject, title, desc, image)) {
            return null;
        }
        return multiImageObject;
    }

    /**
     * @param image 本地图片
     */
    private VideoSourceObject getVideoObj(String videoUrl, String image) {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        if (!TextUtils.isEmpty(videoUrl)) {
            videoSourceObject.videoPath = Uri.fromFile(new File(videoUrl));
        }
        if (!TextUtils.isEmpty(image)) {
            videoSourceObject.coverPath = Uri.fromFile(new File(image));
        }
        return videoSourceObject;
    }

    private WebpageObject getWebPageObj(String url, String title, String desc, String image) {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = Utility.generateGUID();
        webpageObject.actionUrl = url;
        if (addTitleSummaryAndThumb(webpageObject, title, desc, image)) {
            return null;
        }
        return webpageObject;
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(BaseMediaObject msg, String title, String desc, String image) {
        msg.title = title;
        msg.description = desc;
        byte[] imageData = ImageUtil.getImageByte(image, 32768);
        if (imageData == null) {
            return true;
        }
        msg.thumbData = imageData;
        return false;
    }

    /**
     * 新浪微博开放平台需要
     */
    public void onNewIntent(Intent intent) {
        if (wbShareHandler != null) {
            wbShareHandler.doResultIntent(intent, wbShareCallback);
        }
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
     * 新浪微博的分享监听器
     */
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
