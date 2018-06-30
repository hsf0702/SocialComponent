package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.social.library.util.SocialUtil;

/**
 * 授权入口类
 */
public class AuthHelper {

    private WXAuthHelper wxAuthHelper;
    private QQAuthHelper qqAuthHelper;
    private WBAuthHelper wbAuthHelper;

    /**
     * 微信授权
     */
    public void authWX(Activity activity, IAuthCallback authCallback) {
        if (wxAuthHelper == null) {
            wxAuthHelper = new WXAuthHelper(activity, SocialUtil.get().getWxAppId(), SocialUtil.get().getWxAppSecret());
        }
        wxAuthHelper.auth(authCallback);
    }

    /**
     * QQ授权
     */
    public void authQQ(Activity activity, IAuthCallback authCallback) {
        if (qqAuthHelper == null) {
            qqAuthHelper = new QQAuthHelper(activity, SocialUtil.get().getQqAppId());
        }
        qqAuthHelper.auth(authCallback);
    }

    /**
     * 微博授权
     */
    public void authWB(Activity activity, IAuthCallback authCallback) {
        if (wbAuthHelper == null) {
            wbAuthHelper = new WBAuthHelper(activity, SocialUtil.get().getWbAppId(), SocialUtil.get().getWbRedirectUrl());
        }
        wbAuthHelper.auth(authCallback);
    }

    /**
     * 微信授权，在微信回调到WXEntryActivity的onResp方法中调用
     * @param code 空表示失败，正常就是有值的
     */
    public void sendAuthBroadcast(Context context, String code) {
        Intent intent = new Intent(WXAuthHelper.ACTION_WX_AUTH_RECEIVER);
        if (TextUtils.isEmpty(code)) {
            code = WXAuthHelper.KEY_WX_AUTH_CODE_CANCEL;
        }
        intent.putExtra(WXAuthHelper.KEY_WX_AUTH_CODE, code);
        context.sendBroadcast(intent);
    }

    /**
     * QQ授权和分享以及微博授权都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqAuthHelper != null) {
            qqAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (wbAuthHelper != null) {
            wbAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDestroy() {
        if (wxAuthHelper != null) {
            wxAuthHelper.onDestroy();
            wxAuthHelper = null;
        }
        if (qqAuthHelper != null) {
            qqAuthHelper.onDestroy();
            qqAuthHelper = null;
        }
        if (wbAuthHelper != null) {
            wbAuthHelper.onDestroy();
            wbAuthHelper = null;
        }
    }
}
