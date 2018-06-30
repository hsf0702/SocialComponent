package com.fqxyi.social.library.login;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.social.library.R;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * 新浪微博登录帮助类
 */
public class WBLoginHelper {

    //上下文
    private Activity activity;
    //
    private SsoHandler ssoHandler;
    //登录结果回调
    private ILoginCallback loginCallback;

    public WBLoginHelper(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, ""));
    }

    public void login(ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        //判断是否安装新浪微博
        if (!WbSdk.isWbInstall(activity)) {
            if (loginCallback != null) {
                loginCallback.onError(activity.getString(R.string.login_wb_error_uninstall));
            }
            return;
        }
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

    private WbAuthListener wbAuthCallback = new WbAuthListener() {
        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
            if (oauth2AccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(activity, oauth2AccessToken);
                if (loginCallback != null) {
                    loginCallback.onSuccess(activity.getString(R.string.login_wb_success), null);
                }
            } else {
                if (loginCallback != null) {
                    loginCallback.onError(activity.getString(R.string.login_wb_error));
                }
            }
        }

        @Override
        public void cancel() {
            if (loginCallback != null) {
                loginCallback.onCancel(activity.getString(R.string.login_wb_cancel));
            }
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            if (loginCallback != null) {
                loginCallback.onError(activity.getString(R.string.login_wb_error)
                        + ", 错误码：" + wbConnectErrorMessage.getErrorCode()
                        + ", 错误信息：" + wbConnectErrorMessage.getErrorMessage());
            }
        }
    };

}
