package com.fqxyi.library;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.fqxyi.library.bean.ShareDataBean;
import com.fqxyi.library.bean.ShareWXDataBean;
import com.fqxyi.library.callback.IShareCallback;
import com.fqxyi.library.util.LogUtil;
import com.fqxyi.library.util.ShareUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXHelper {

    public static final String WX_SHARE_RECEIVER_ACTION = "wx_auth_receiver_action";
    public static final String KEY_WX_SHARE_CALL_BACK = "key_wx_share_call_back";

    private Activity activity;

    private IWXAPI wxapi;

    private IShareCallback shareCallback;

    public WXHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }

        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
    }

    public void share(ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        if (!wxapi.isWXAppInstalled()) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_wx_uninstall));
            }
            return;
        }

        activity.registerReceiver(broadcastReceiver, new IntentFilter(WXHelper.WX_SHARE_RECEIVER_ACTION));

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = createMessage(req, shareDataBean);
        if (req.message == null) {
            return;
        }
        req.scene = SendMessageToWX.Req.WXSceneSession;
        wxapi.sendReq(req);
    }

    public void shareMoment(ShareDataBean shareDataBean, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        if (!wxapi.isWXAppInstalled()) {
            if (shareCallback != null) {
                shareCallback.onError(activity.getString(R.string.share_wx_uninstall));
            }
            return;
        }

        activity.registerReceiver(broadcastReceiver, new IntentFilter(WXHelper.WX_SHARE_RECEIVER_ACTION));

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = createMessage(req, shareDataBean);
        if (req.message == null) {
            return;
        }
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        wxapi.sendReq(req);
    }

    private WXMediaMessage createMessage(SendMessageToWX.Req req, ShareDataBean shareDataBean) {
        WXMediaMessage msg = new WXMediaMessage();
        boolean success = false;
        switch (shareDataBean.type) {
            case ShareWXDataBean.TYPE_TEXT:
                success = addText(req, msg, shareDataBean.shareDesc);
                break;
            case ShareWXDataBean.TYPE_IMAGE:
                success = addImage(req, msg, shareDataBean.shareImage);
                break;
            case ShareWXDataBean.TYPE_WEB:
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
        WXImageObject imgObj;
        imgObj = new WXImageObject();
        imgObj.imagePath = imageUrl;
        msg.mediaObject = imgObj;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(imageUrl);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        if (bitmap == null) {
            return false;
        }
        msg.thumbData = ShareUtil.bmpToByteArray(bitmap, true);

        req.transaction = ShareUtil.buildTransaction("img");
        return true;
    }

    private boolean addWeb(SendMessageToWX.Req req, WXMediaMessage msg, String url, String title, String desc, String imageUrl) {
        WXWebpageObject musicObject = new WXWebpageObject();
        musicObject.webpageUrl = url;

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, title, desc, imageUrl)) return false;

        req.transaction = ShareUtil.buildTransaction("webpage");
        return true;
    }

    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, String title, String desc, String imageUrl) {
        msg.title = title;
        msg.description = desc;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(imageUrl);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        if (bitmap == null) {
            return true;
        }
        msg.thumbData = ShareUtil.bmpToByteArray(bitmap, true);
        return false;
    }

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
            boolean shareSuccess = intent.getBooleanExtra(WXHelper.KEY_WX_SHARE_CALL_BACK, false);
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
