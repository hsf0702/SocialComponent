package com.fqxyi.social.library.login;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.social.library.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ登录帮助类
 */
public class QQLoginHelper {

    //上下文
    private Activity activity;
    //
    private Tencent tencent;
    //登录结果回调
    private ILoginCallback loginCallback;

    public QQLoginHelper(Activity activity, String appId) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }

    public void login(ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        //判断是否安装QQ
        if (!tencent.isQQInstalled(activity)) {
            if (loginCallback != null) {
                loginCallback.onError(activity.getString(R.string.login_qq_error_uninstall));
            }
            return;
        }
        //登录到QQ
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", loginListener);
        }
    }

    /**
     * QQ开放平台需要
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
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
     * QQ的登录监听器
     */
    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (loginCallback != null) {
                loginCallback.onSuccess(activity.getString(R.string.login_qq_success), o.toString());
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (loginCallback != null && uiError != null) {
                loginCallback.onError(activity.getString(R.string.login_qq_error)
                        + ", 错误码：" + uiError.errorCode
                        + ", 错误信息：" + uiError.errorMessage
                        + ", 错误详情：" + uiError.errorDetail);
            }
        }

        @Override
        public void onCancel() {
            if (loginCallback != null) {
                loginCallback.onCancel(activity.getString(R.string.login_qq_cancel));
            }
        }
    };

}
