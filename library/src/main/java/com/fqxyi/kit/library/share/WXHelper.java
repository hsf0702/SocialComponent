package com.fqxyi.kit.library.share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.fqxyi.kit.library.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信分享帮助类
 */
public class WXHelper {

    //分享文本
    public static final int TYPE_TEXT = 0;
    //分享图片
    public static final int TYPE_IMAGE = 1;
    //分享音乐
    public static final int TYPE_MUSIC = 2;
    //分享视频
    public static final int TYPE_VIDEO = 3;
    //分享网页
    public static final int TYPE_WEB = 4;

    //
    public static final String ACTION_WX_SHARE_RECEIVER = "ACTION_WX_SHARE_RECEIVER";
    public static final String KEY_WX_SHARE_CALLBACK = "KEY_WX_SHARE_CALLBACK";

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;
    //分享结果回调
    private IShareCallback shareCallback;

    /**
     * 初始化微信
     */
    public WXHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
        //
        activity.registerReceiver(broadcastReceiver, new IntentFilter(WXHelper.ACTION_WX_SHARE_RECEIVER));
    }

    /**
     * 具体的分享逻辑
     * @param isTimeLine true 朋友圈 false 微信
     */
    public void share(boolean isTimeLine, ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        //判断数据源是否为空
        if (shareDataBean == null) {
            if (shareCallback != null) {
                shareCallback.onError("shareDataBean == null");
            }
            return;
        }
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_wx_uninstall));
            }
            return;
        }
        //是否分享到朋友圈，微信4.2以下不支持朋友圈
        if (isTimeLine && wxapi.getWXAppSupportAPI() < 0x21020001) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_wx_version_low_error));
            }
            return;
        }
        //需要传递给微信的分享数据
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = getShareMessage(req, shareDataBean);
        if (req.message == null) {
            if (shareCallback != null) {
                shareCallback.onError("req.message == null");
            }
            return;
        }
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        //分享到scene（微信或朋友圈）
        wxapi.sendReq(req);
    }

    /**
     * 需要传递给微信的分享数据
     */
    private WXMediaMessage getShareMessage(SendMessageToWX.Req req, ShareDataBean shareDataBean) {
        WXMediaMessage msg = new WXMediaMessage();
        boolean success = false;
        switch (shareDataBean.type) {
            case WXHelper.TYPE_TEXT:
                success = addText(req, msg, shareDataBean.shareDesc);
                break;
            case WXHelper.TYPE_IMAGE:
                success = addImage(req, msg, shareDataBean.shareImage);
                break;
            case WXHelper.TYPE_MUSIC:
                success = addMusic(req, msg, shareDataBean.shareMusicUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
            case WXHelper.TYPE_VIDEO:
                success = addVideo(req, msg, shareDataBean.shareVideoUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
            case WXHelper.TYPE_WEB:
                success = addWeb(req, msg, shareDataBean.shareUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
        }
        if (!success) {
            return null;
        }
        return msg;
    }

    private boolean addText(SendMessageToWX.Req req, WXMediaMessage msg, String text) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        msg.mediaObject = textObj;
        msg.description = textObj.text;

        req.transaction = ShareUtil.buildTransaction("text");
        return true;
    }

    private boolean addImage(SendMessageToWX.Req req, WXMediaMessage msg, String imageUrl) {
        Bitmap bitmap = ShareUtil.getImageBitmap(imageUrl);
        if (bitmap == null) {
            return false;
        }
        WXImageObject imageObject = new WXImageObject();
        imageObject.imageData = ShareUtil.bmpToByteArray(bitmap, true);

        msg.mediaObject = imageObject;
        msg.thumbData = imageObject.imageData;

        req.transaction = ShareUtil.buildTransaction("image");
        return true;
    }

    private boolean addMusic(SendMessageToWX.Req req, WXMediaMessage msg, String musicUrl, String title, String desc, String image) {
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = musicUrl;

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = ShareUtil.buildTransaction("music");
        return true;
    }

    private boolean addVideo(SendMessageToWX.Req req, WXMediaMessage msg, String videoUrl, String title, String desc, String image) {
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = videoUrl;

        msg.mediaObject = videoObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = ShareUtil.buildTransaction("video");
        return true;
    }

    private boolean addWeb(SendMessageToWX.Req req, WXMediaMessage msg, String url, String title, String desc, String image) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;

        msg.mediaObject = webpageObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = ShareUtil.buildTransaction("webpage");
        return true;
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, String title, String desc, String image) {
        msg.title = title;
        msg.description = desc;

        Bitmap bitmap = ShareUtil.getImageBitmap(image);
        if (bitmap == null) {
            return true;
        }
        msg.thumbData = ShareUtil.bmpToByteArray(bitmap, true);
        return false;
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            if (broadcastReceiver != null) {
                activity.unregisterReceiver(broadcastReceiver);
            }
            activity = null;
        }
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean shareSuccess = intent.getBooleanExtra(WXHelper.KEY_WX_SHARE_CALLBACK, false);
            if (shareCallback != null) {
                if (shareSuccess) {
                    shareCallback.onSuccess();
                } else {
                    shareCallback.onError(activity.getString(R.string.share_cancel));
                }
            }
        }
    };
}
