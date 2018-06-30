package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.fqxyi.social.library.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信授权帮助类
 */
public class WXAuthHelper {

    //
    public static final String ACTION_WX_AUTH_RECEIVER = "ACTION_WX_AUTH_RECEIVER";
    public static final String KEY_WX_AUTH_CODE = "KEY_WX_AUTH_CODE";
    public static final String KEY_WX_AUTH_CODE_CANCEL = "KEY_WX_AUTH_CODE_CANCEL";

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;
    //授权结果回调
    private IAuthCallback authCallback;

    /**
     * 初始化微信
     */
    public WXAuthHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
        //
        activity.registerReceiver(wxAuthReceiver, new IntentFilter(WXAuthHelper.ACTION_WX_AUTH_RECEIVER));
    }

    /**
     * 具体的授权逻辑
     */
    public void auth(IAuthCallback authCallback) {
        this.authCallback = authCallback;
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            if (authCallback != null) {
                authCallback.onError(activity.getString(R.string.auth_wx_error_uninstall));
            }
            return;
        }
        //开始微信授权
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = getAppStateName(activity) + "_app";
        wxapi.sendReq(req);
    }

    private static String getAppStateName(Context context) {
        String packageName = context.getPackageName();
        int beginIndex = 0;
        if (packageName.contains(".")) {
            beginIndex = packageName.lastIndexOf(".");
        }
        return packageName.substring(beginIndex);
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
     * 微信的授权监听器
     */
    public BroadcastReceiver wxAuthReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(WXAuthHelper.KEY_WX_AUTH_CODE);
            if (code.equals(WXAuthHelper.KEY_WX_AUTH_CODE_CANCEL)) {
                if (authCallback != null && activity != null) {
                    authCallback.onError(activity.getString(R.string.auth_wx_error));
                }
                return;
            }
            if (authCallback != null) {
                authCallback.onSuccess(activity.getString(R.string.auth_wx_success), null);
            }
        }
    };
}
