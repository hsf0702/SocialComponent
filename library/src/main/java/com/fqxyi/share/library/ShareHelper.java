package com.fqxyi.share.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.fqxyi.share.library.bean.ShareDataBean;
import com.fqxyi.share.library.callback.IShareCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 分享入口类
 */
public class ShareHelper {

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    private Builder builder;

    private QQHelper qqHelper;
    private WXHelper wxHelper;
    private WBHelper wbHelper;

    public ShareHelper(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void shareQQ(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback) {
        if (qqHelper == null) {
            qqHelper = new QQHelper(activity, builder.getQqAppId());
        }
        qqHelper.share(shareDataBean, shareCallback);
    }

    public void shareWX(final Activity activity, final boolean isTimeLine, final ShareDataBean shareDataBean, final IShareCallback shareCallback) {
        if (fixedThreadPool.isShutdown()) {
            return;
        }
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (wxHelper == null) {
                    wxHelper = new WXHelper(activity, builder.getWxAppId(), builder.getWxAppSecret());
                }
                wxHelper.share(isTimeLine, shareDataBean, shareCallback);
            }
        });
    }

    public void shareWB(final Activity activity, final ShareDataBean shareDataBean, final IShareCallback shareCallback) {
        if (fixedThreadPool.isShutdown()) {
            return;
        }
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (wbHelper == null) {
                    wbHelper = new WBHelper(activity, builder.getWbAppId(), builder.getWbRedirectUrl());
                }
                wbHelper.share(shareDataBean, shareCallback);
            }
        });
    }

    /**
     * qq登录和分享以及微博登录都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqHelper != null) {
            qqHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (wbHelper != null) {
            wbHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 微博分享需要在其当前的activity中的onNewIntent中调用该方法
     */
    public void onNewIntent(Intent intent) {
        if (wbHelper != null) {
            wbHelper.onNewIntent(intent);
        }
    }

    /**
     * 微信分享，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param success 表示是否分享成功
     */
    public void sendShareBackBroadcast(Context context, boolean success) {
        Intent intent = new Intent(WXHelper.WX_SHARE_RECEIVER_ACTION);
        intent.putExtra(WXHelper.KEY_WX_SHARE_CALL_BACK, success);
        context.sendBroadcast(intent);
    }

    public void onDestroy() {
        if (qqHelper != null) {
            qqHelper.onDestroy();
            qqHelper = null;
        }
        if (wxHelper != null) {
            wxHelper.onDestroy();
            wxHelper = null;
        }
        if (wbHelper != null) {
            wbHelper.onDestroy();
            wbHelper = null;
        }
    }

    public static final class Builder {
        private String qqAppId;

        private String wxAppId;
        private String wxAppSecret;

        private String wxMomentAppId;
        private String wxMomentAppSecret;

        private String wbAppId;
        private String wbRedirectUrl;

        public String getQqAppId() {
            return qqAppId;
        }

        public Builder setQqAppId(String qqAppId) {
            this.qqAppId = qqAppId;
            return this;
        }

        public String getWxAppId() {
            return wxAppId;
        }

        public Builder setWxAppId(String wxAppId) {
            this.wxAppId = wxAppId;
            return this;
        }

        public String getWxAppSecret() {
            return wxAppSecret;
        }

        public Builder setWxAppSecret(String wxAppSecret) {
            this.wxAppSecret = wxAppSecret;
            return this;
        }

        public String getWxMomentAppId() {
            return wxMomentAppId;
        }

        public Builder setWxMomentAppId(String wxMomentAppId) {
            this.wxMomentAppId = wxMomentAppId;
            return this;
        }

        public String getWxMomentAppSecret() {
            return wxMomentAppSecret;
        }

        public Builder setWxMomentAppSecret(String wxMomentAppSecret) {
            this.wxMomentAppSecret = wxMomentAppSecret;
            return this;
        }

        public String getWbAppId() {
            return wbAppId;
        }

        public Builder setWbAppId(String wbAppId) {
            this.wbAppId = wbAppId;
            return this;
        }

        public String getWbRedirectUrl() {
            return wbRedirectUrl;
        }

        public Builder setWbRedirectUrl(String wbRedirectUrl) {
            this.wbRedirectUrl = wbRedirectUrl;
            return this;
        }

        public ShareHelper build() {
            return new ShareHelper(this);
        }
    }

}
