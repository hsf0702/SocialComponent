package com.fqxyi.kit.library.login;

import android.app.Activity;
import android.text.TextUtils;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * 新浪微博登录帮助类
 */
public class WBLoginHelper {

    //上下文
    private Activity activity;

    public WBLoginHelper(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, ""));
    }

    public void login(ILoginCallback loginCallback) {

    }
}
