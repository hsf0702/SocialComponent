package com.fqxyi.library;

import android.app.Activity;
import android.content.Intent;

import com.fqxyi.library.bean.ShareDataBean;
import com.fqxyi.library.callback.IShareCallback;

/**
 * 分享入口类
 */
public class ShareHelper {

    private Builder builder;

    private QQHelper qqHelper;

    public ShareHelper(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void shareQQ(Activity activity, int type, ShareDataBean shareDataBean, IShareCallback shareCallback) {
        if (qqHelper == null) {
            qqHelper = new QQHelper(activity, builder.getQqAppId());
        }
        qqHelper.share(type, shareDataBean, shareCallback);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqHelper != null) {
            qqHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDestroy() {
        if (qqHelper != null) {
            qqHelper.onDestroy();
            qqHelper = null;
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
