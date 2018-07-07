package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.fqxyi.social.library.R;
import com.fqxyi.social.library.ISocialType;
import com.fqxyi.social.library.util.Utils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ授权帮助类
 *
 * 相关文档：
 * 1、QQ登录和注销 http://wiki.open.qq.com/wiki/QQ%E7%99%BB%E5%BD%95%E5%92%8C%E6%B3%A8%E9%94%80
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
    //是否需要finishActivity
    private boolean needFinishActivity;

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
    public void auth(IAuthCallback authCallback, boolean needFinishActivity) {
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        //判断是否安装QQ
        if (!tencent.isQQInstalled(activity)) {
            if (authCallback != null) {
                authCallback.onError(ISocialType.SOCIAL_QQ, activity.getString(R.string.social_error_qq_uninstall));
            }
            Utils.finish(activity, needFinishActivity);
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
                authCallback.onSuccess(ISocialType.SOCIAL_QQ, o.toString());
            }
            logout();
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onError(UiError uiError) {
            if (authCallback != null && uiError != null) {
                authCallback.onError(ISocialType.SOCIAL_QQ,
                        "\n错误码：" + uiError.errorCode
                        + "\n错误信息：" + uiError.errorMessage
                        + "\n错误详情：" + uiError.errorDetail);
            }
            logout();
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onCancel() {
            if (authCallback != null) {
                authCallback.onCancel(ISocialType.SOCIAL_QQ);
            }
            logout();
            Utils.finish(activity, needFinishActivity);
        }
    };

    /**
     * 调用QQ注销接口
     */
    private void logout() {
        if (tencent != null) {
            tencent.logout(activity);
        }
    }

}
