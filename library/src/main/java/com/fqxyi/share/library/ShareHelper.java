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

    private QQShareHelper qqShareHelper;
    private WXHelper wxHelper;
    private WBShareHelper wbShareHelper;

    public ShareHelper(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void shareQQ(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback) {
        if (qqShareHelper == null) {
            qqShareHelper = new QQShareHelper(activity, builder.getQqAppId());
        }
        qqShareHelper.share(shareDataBean, shareCallback);
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
                if (wbShareHelper == null) {
                    wbShareHelper = new WBShareHelper(activity, builder.getWbAppId(), builder.getWbRedirectUrl());
                }
                wbShareHelper.share(shareDataBean, shareCallback);
            }
        });
    }

    /**
     * qq登录和分享以及微博登录都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqShareHelper != null) {
            qqShareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 微博分享需要在其当前的activity中的onNewIntent中调用该方法
     */
    public void onNewIntent(Intent intent) {
        if (wbShareHelper != null) {
            wbShareHelper.onNewIntent(intent);
        }
    }

    /**
     * 微信分享，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param success 表示是否分享成功
     */
    public void sendShareBackBroadcast(Context context, boolean success) {
        Intent intent = new Intent(WXHelper.ACTION_WX_SHARE_RECEIVER);
        intent.putExtra(WXHelper.KEY_WX_SHARE_CALLBACK, success);
        context.sendBroadcast(intent);
    }

    public void onDestroy() {
        if (qqShareHelper != null) {
            qqShareHelper.onDestroy();
            qqShareHelper = null;
        }
        if (wxHelper != null) {
            wxHelper.onDestroy();
            wxHelper = null;
        }
        if (wbShareHelper != null) {
            wbShareHelper.onDestroy();
            wbShareHelper = null;
        }
    }

    public static final class Builder {
        private String qqAppId;

        private String wxAppId;
        private String wxAppSecret;

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
