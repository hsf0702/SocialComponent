package com.fqxyi.social.library.login;

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
 * 微信登录帮助类
 */
public class WXLoginHelper {

    //
    public static final String ACTION_WX_LOGIN_RECEIVER = "ACTION_WX_LOGIN_RECEIVER";
    public static final String KEY_WX_LOGIN_CODE = "KEY_WX_LOGIN_CODE";
    public static final String KEY_WX_LOGIN_CODE_CANCEL = "KEY_WX_LOGIN_CODE_CANCEL";

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;
    //登录结果回调
    private ILoginCallback loginCallback;

    /**
     * 初始化微信
     */
    public WXLoginHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
        //
        activity.registerReceiver(wxLoginReceiver, new IntentFilter(WXLoginHelper.ACTION_WX_LOGIN_RECEIVER));
    }

    /**
     * 具体的登录逻辑
     */
    public void login(ILoginCallback loginCallback) {
        this.loginCallback = loginCallback;
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            if (loginCallback != null) {
                loginCallback.onError(activity.getString(R.string.login_wx_error_uninstall));
            }
            return;
        }
        //登录到微信
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
     * 微信的登录监听器
     */
    public BroadcastReceiver wxLoginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(WXLoginHelper.KEY_WX_LOGIN_CODE);
            if (code.equals(WXLoginHelper.KEY_WX_LOGIN_CODE_CANCEL)) {
                if (loginCallback != null && activity != null) {
                    loginCallback.onError(activity.getString(R.string.login_wx_error));
                }
                return;
            }
            if (loginCallback != null) {
                loginCallback.onSuccess(activity.getString(R.string.login_wx_success), null);
            }
        }
    };
}
