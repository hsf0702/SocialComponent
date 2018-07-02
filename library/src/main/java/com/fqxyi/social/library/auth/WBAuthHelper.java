package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.social.library.R;
import com.fqxyi.social.library.ISocialType;
import com.fqxyi.social.library.util.ActivityUtil;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * 授权帮助类
 *
 * 问题：微博授权21338问题，未通过审核或者是签名不一致
 */
public class WBAuthHelper {

    //上下文
    private Activity activity;
    //
    private SsoHandler ssoHandler;
    //授权结果回调
    private IAuthCallback authCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;

    /**
     * 初始化微博
     */
    public WBAuthHelper(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, ""));
    }

    /**
     * 具体的授权逻辑
     */
    public void auth(IAuthCallback authCallback, boolean needFinishActivity) {
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        //判断是否安装微博
        if (!WbSdk.isWbInstall(activity)) {
            if (authCallback != null) {
                authCallback.onError(ISocialType.SOCIAL_WB, activity.getString(R.string.social_error_wb_uninstall));
            }
            ActivityUtil.finish(activity, needFinishActivity);
            return;
        }
        //开始微博授权
        if (ssoHandler == null) {
            ssoHandler = new SsoHandler(activity);
        }
        ssoHandler.authorize(wbAuthCallback);
    }

    /**
     * 微博开放平台需要
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
    }

    /**
     * 微博的授权监听器
     */
    private WbAuthListener wbAuthCallback = new WbAuthListener() {
        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
            if (oauth2AccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(activity, oauth2AccessToken);
                if (authCallback != null) {
                    authCallback.onSuccess(ISocialType.SOCIAL_WB, null);
                }
            } else {
                if (authCallback != null) {
                    authCallback.onError(ISocialType.SOCIAL_WB, null);
                }
            }
            ActivityUtil.finish(activity, needFinishActivity);
        }

        @Override
        public void cancel() {
            if (authCallback != null) {
                authCallback.onCancel(ISocialType.SOCIAL_WB);
            }
            ActivityUtil.finish(activity, needFinishActivity);
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            if (authCallback != null) {
                authCallback.onError(ISocialType.SOCIAL_WB,
                        "\n错误码：" + wbConnectErrorMessage.getErrorCode()
                        + "\n错误信息：" + wbConnectErrorMessage.getErrorMessage());
            }
            ActivityUtil.finish(activity, needFinishActivity);
        }
    };

}
