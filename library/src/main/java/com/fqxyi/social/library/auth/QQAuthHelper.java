package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.social.library.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ授权帮助类
 *
 * 问题：QQ授权100044问题，解决办法：本APP未上线，如果你申请的是“个人开发者”，请确保你创建APP所用的QQ帐号和你测试时用的登陆QQ号一致！
 */
public class QQAuthHelper {

    //上下文
    private Activity activity;
    //
    private Tencent tencent;
    //授权结果回调
    private IAuthCallback authCallback;

    /**
     * 初始化QQ
     */
    public QQAuthHelper(Activity activity, String appId) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }

    /**
     * 具体的授权逻辑
     */
    public void auth(IAuthCallback authCallback) {
        this.authCallback = authCallback;
        //判断是否安装QQ
        if (!tencent.isQQInstalled(activity)) {
            if (authCallback != null) {
                authCallback.onError(activity.getString(R.string.auth_qq_error_uninstall));
            }
            return;
        }
        //开始QQ授权
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", authListener);
        }
    }

    /**
     * QQ开放平台需要
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, authListener);
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
     * QQ的授权监听器
     */
    private IUiListener authListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (authCallback != null) {
                authCallback.onSuccess(activity.getString(R.string.auth_qq_success), o.toString());
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (authCallback != null && uiError != null) {
                authCallback.onError(activity.getString(R.string.auth_qq_error)
                        + ", 错误码：" + uiError.errorCode
                        + ", 错误信息：" + uiError.errorMessage
                        + ", 错误详情：" + uiError.errorDetail);
            }
        }

        @Override
        public void onCancel() {
            if (authCallback != null) {
                authCallback.onCancel(activity.getString(R.string.auth_qq_cancel));
            }
        }
    };

}
