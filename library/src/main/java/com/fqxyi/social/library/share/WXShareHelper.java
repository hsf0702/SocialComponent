package com.fqxyi.social.library.share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.fqxyi.social.library.ISocialType;
import com.fqxyi.social.library.R;
import com.fqxyi.social.library.auth.WXAuthHelper;
import com.fqxyi.social.library.util.Utils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

/**
 * 微信分享帮助类
 */
public class WXShareHelper {

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
    public static final String KEY_WX_AUTH_RESULT = "KEY_WX_AUTH_RESULT";
    public static final String KEY_WX_AUTH_MSG = "KEY_WX_AUTH_MSG";

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;
    //分享结果回调
    private IShareCallback shareCallback;
    //图片缓存的父目录
    private File parentDir;
    //true 分享到朋友圈 false 分享到微信
    private boolean isTimeLine;
    //分享类型
    private int shareType;
    //是否需要finishActivity
    private boolean needFinishActivity;

    /**
     * 初始化微信
     */
    public WXShareHelper(Activity activity, String appId, String appSecret, File parentDir) {
        this.activity = activity;
        this.parentDir = parentDir;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
        //
        activity.registerReceiver(wxShareReceiver, new IntentFilter(WXShareHelper.ACTION_WX_SHARE_RECEIVER));
    }

    /**
     * 具体的分享逻辑
     * @param isTimeLine true 朋友圈 false 微信
     */
    public void share(boolean isTimeLine, ShareDataBean shareDataBean, IShareCallback shareCallback, Handler handler, boolean needFinishActivity) {
        this.isTimeLine = isTimeLine;
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        //判断数据源是否为空
        if (shareDataBean == null) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wx_share_data);
            if (isTimeLine) {
                msg.arg1 = ISocialType.SOCIAL_WX_SESSION;
            } else {
                msg.arg1 = ISocialType.SOCIAL_WX_TIMELINE;
            }
            handler.sendMessage(msg);
            return;
        }
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wx_uninstall);
            if (isTimeLine) {
                msg.arg1 = ISocialType.SOCIAL_WX_SESSION;
            } else {
                msg.arg1 = ISocialType.SOCIAL_WX_TIMELINE;
            }
            handler.sendMessage(msg);
            return;
        }
        //是否分享到朋友圈，微信4.2以下不支持朋友圈
        if (isTimeLine && wxapi.getWXAppSupportAPI() < 0x21020001) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wx_version_low);
            msg.arg1 = ISocialType.SOCIAL_WX_TIMELINE;
            handler.sendMessage(msg);
            return;
        }
        //获取分享类型
        if (shareDataBean.shareType == null) {
            shareType = 0;
        } else {
            shareType = shareDataBean.shareType.get(ISocialType.SOCIAL_WX_SESSION);
        }
        //需要传递给微信的分享数据
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = getShareMessage(req, shareDataBean);
        if (req.message == null) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wx_share_data);
            if (isTimeLine) {
                msg.arg1 = ISocialType.SOCIAL_WX_SESSION;
            } else {
                msg.arg1 = ISocialType.SOCIAL_WX_TIMELINE;
            }
            handler.sendMessage(msg);
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
        switch (shareType) {
            case WXShareHelper.TYPE_TEXT:
                success = addText(req, msg, shareDataBean.shareDesc);
                break;
            case WXShareHelper.TYPE_IMAGE:
                success = addImage(req, msg, shareDataBean.shareImage);
                break;
            case WXShareHelper.TYPE_MUSIC:
                success = addMusic(req, msg, shareDataBean.shareMusicUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
            case WXShareHelper.TYPE_VIDEO:
                success = addVideo(req, msg, shareDataBean.shareVideoUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
            case WXShareHelper.TYPE_WEB:
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

        req.transaction = buildTransaction("text");
        return true;
    }

    private boolean addImage(SendMessageToWX.Req req, WXMediaMessage msg, String image) {
        String imagePath = ImageUtil.getLocalImagePath(parentDir, image);
        if (TextUtils.isEmpty(imagePath) || imagePath.length() > 512) {
            return false;
        }
        File file = new File(imagePath);
        if (!file.exists() || file.length() == 0L || file.length() > 10485760L) {
            return false;
        }
        WXImageObject imageObject = new WXImageObject();
        imageObject.imagePath = imagePath;
        msg.mediaObject = imageObject;
        byte[] thumbData = ImageUtil.getImageByte(imagePath, 32768);
        if (thumbData == null) {
            return false;
        }
        msg.thumbData = thumbData;

        req.transaction = buildTransaction("image");
        return true;
    }

    private boolean addMusic(SendMessageToWX.Req req, WXMediaMessage msg, String musicUrl, String title, String desc, String image) {
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = musicUrl;

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = buildTransaction("music");
        return true;
    }

    private boolean addVideo(SendMessageToWX.Req req, WXMediaMessage msg, String videoUrl, String title, String desc, String image) {
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = videoUrl;

        msg.mediaObject = videoObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = buildTransaction("video");
        return true;
    }

    private boolean addWeb(SendMessageToWX.Req req, WXMediaMessage msg, String url, String title, String desc, String image) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;

        msg.mediaObject = webpageObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = buildTransaction("webpage");
        return true;
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, String title, String desc, String image) {
        msg.title = title;
        msg.description = desc;
        byte[] thumbData = ImageUtil.getImageByte(image, 32768);
        if (thumbData == null) {
            return true;
        }
        msg.thumbData = thumbData;
        return false;
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            if (wxShareReceiver != null) {
                activity.unregisterReceiver(wxShareReceiver);
            }
            activity = null;
        }
    }

    /**
     * 用于唯一标识一个请求
     */
    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 微信的分享监听器
     */
    public BroadcastReceiver wxShareReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (shareCallback == null) {
                Utils.finish(activity, needFinishActivity);
                return;
            }
            String msg = intent.getStringExtra(WXAuthHelper.KEY_WX_AUTH_MSG);
            boolean success = intent.getBooleanExtra(WXShareHelper.KEY_WX_AUTH_RESULT, false);
            if (success) {
                if (isTimeLine) {
                    shareCallback.onSuccess(ISocialType.SOCIAL_WX_SESSION, "朋友圈" + msg);
                } else {
                    shareCallback.onSuccess(ISocialType.SOCIAL_WX_TIMELINE, "微信" + msg);
                }
            } else {
                if (isTimeLine) {
                    shareCallback.onError(ISocialType.SOCIAL_WX_SESSION, "朋友圈" + msg);
                } else {
                    shareCallback.onError(ISocialType.SOCIAL_WX_TIMELINE, "微信" + msg);
                }
            }
            Utils.finish(activity, needFinishActivity);
        }
    };
}
