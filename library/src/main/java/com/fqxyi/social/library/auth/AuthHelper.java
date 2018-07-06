package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.fqxyi.social.library.SocialHelper;

/**
 * 授权入口类
 */
public class AuthHelper {

    //静态常量
    private static final int TYPE_AUTH_WX = 1;
    private static final int TYPE_AUTH_QQ = 2;
    private static final int TYPE_AUTH_WB = 3;

    //各模块帮助类
    private WXAuthHelper wxAuthHelper;
    private QQAuthHelper qqAuthHelper;
    private WBAuthHelper wbAuthHelper;

    //当前授权类型
    private int currAuthType;

    /**
     * 微信授权
     */
    public void authWX(Activity activity, IAuthCallback authCallback, boolean needFinishActivity) {
        currAuthType = TYPE_AUTH_WX;
        if (wxAuthHelper == null) {
            wxAuthHelper = new WXAuthHelper(activity, SocialHelper.get().getWxAppId(), SocialHelper.get().getWxAppSecret());
        }
        wxAuthHelper.auth(authCallback, needFinishActivity);
    }

    /**
     * QQ授权
     */
    public void authQQ(Activity activity, IAuthCallback authCallback, boolean needFinishActivity) {
        currAuthType = TYPE_AUTH_QQ;
        if (qqAuthHelper == null) {
            qqAuthHelper = new QQAuthHelper(activity, SocialHelper.get().getQqAppId());
        }
        qqAuthHelper.auth(authCallback, needFinishActivity);
    }

    /**
     * 微博授权
     */
    public void authWB(Activity activity, IAuthCallback authCallback, boolean needFinishActivity) {
        currAuthType = TYPE_AUTH_WB;
        if (wbAuthHelper == null) {
            wbAuthHelper = new WBAuthHelper(activity, SocialHelper.get().getWbAppId(), SocialHelper.get().getWbRedirectUrl());
        }
        wbAuthHelper.auth(authCallback, needFinishActivity);
    }

    /**
     * 微信授权，在微信回调到WXEntryActivity的onResp方法中调用
     * @param success false表示失败，true表示成功
     * @param msg 消息内容
     */
    public void sendAuthBroadcast(Context context, boolean success, String msg) {
        Intent intent = new Intent(WXAuthHelper.ACTION_WX_AUTH_RECEIVER);
        intent.putExtra(WXAuthHelper.KEY_WX_AUTH_RESULT, success);
        intent.putExtra(WXAuthHelper.KEY_WX_AUTH_MSG, msg);
        context.sendBroadcast(intent);
    }

    /**
     * QQ授权和分享以及微博授权都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqAuthHelper != null && currAuthType == TYPE_AUTH_QQ) {
            qqAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (wbAuthHelper != null && currAuthType == TYPE_AUTH_WB) {
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
