package com.fqxyi.social.library.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.social.library.util.SocialUtil;

/**
 * 登录入口类
 */
public class LoginHelper {

    private WXLoginHelper wxLoginHelper;
    private QQLoginHelper qqLoginHelper;
    private WBLoginHelper wbLoginHelper;

    /**
     * 微信授权登录
     */
    public void loginWX(Activity activity, ILoginCallback loginCallback) {
        if (wxLoginHelper == null) {
            wxLoginHelper = new WXLoginHelper(activity, SocialUtil.get().getWxAppId(), SocialUtil.get().getWxAppSecret());
        }
        wxLoginHelper.login(loginCallback);
    }

    /**
     * QQ授权登录
     */
    public void loginQQ(Activity activity, ILoginCallback loginCallback) {
        if (qqLoginHelper == null) {
            qqLoginHelper = new QQLoginHelper(activity, SocialUtil.get().getQqAppId());
        }
        qqLoginHelper.login(loginCallback);
    }

    /**
     * 新浪微博授权登录
     */
    public void loginWB(Activity activity, ILoginCallback loginCallback) {
        if (wbLoginHelper == null) {
            wbLoginHelper = new WBLoginHelper(activity, SocialUtil.get().getWbAppId(), SocialUtil.get().getWbRedirectUrl());
        }
        wbLoginHelper.login(loginCallback);
    }

    /**
     * 微信登录，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param code 空表示失败，正常就是有值的
     */
    public void sendLoginBroadcast(Context context, String code) {
        Intent intent = new Intent(WXLoginHelper.ACTION_WX_LOGIN_RECEIVER);
        if (TextUtils.isEmpty(code)) {
            code = WXLoginHelper.KEY_WX_LOGIN_CODE_CANCEL;
        }
        intent.putExtra(WXLoginHelper.KEY_WX_LOGIN_CODE, code);
        context.sendBroadcast(intent);
    }

    /**
     * qq登录和分享以及微博登录都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqLoginHelper != null) {
            qqLoginHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (wbLoginHelper != null) {
            wbLoginHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDestroy() {
        if (wxLoginHelper != null) {
            wxLoginHelper.onDestroy();
            wxLoginHelper = null;
        }
        if (qqLoginHelper != null) {
            qqLoginHelper.onDestroy();
            qqLoginHelper = null;
        }
        if (wbLoginHelper != null) {
            wbLoginHelper.onDestroy();
            wbLoginHelper = null;
        }
    }
}
