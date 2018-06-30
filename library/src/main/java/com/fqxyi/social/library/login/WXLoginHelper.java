package com.fqxyi.social.library.login;

import android.app.Activity;
import android.text.TextUtils;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信登录帮助类
 */
public class WXLoginHelper {

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;

    public WXLoginHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
    }

    public void login(ILoginCallback loginCallback) {

    }

    public void onDestroy() {

    }
}
