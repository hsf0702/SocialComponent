package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.fqxyi.social.library.R;
import com.fqxyi.social.library.ISocialType;
import com.fqxyi.social.library.util.ActivityUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信授权帮助类
 */
public class WXAuthHelper {

    //静态常量
    public static final String ACTION_WX_AUTH_RECEIVER = "ACTION_WX_AUTH_RECEIVER";
    public static final String KEY_WX_AUTH_CODE = "KEY_WX_AUTH_CODE";
    public static final String KEY_WX_AUTH_CODE_CANCEL = "KEY_WX_AUTH_CODE_CANCEL";

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;
    //授权结果回调
    private IAuthCallback authCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;

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
    public void auth(IAuthCallback authCallback, boolean needFinishActivity) {
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            if (authCallback != null) {
                authCallback.onError(ISocialType.SOCIAL_WX_SESSION, activity.getString(R.string.social_error_wx_uninstall));
            }
            ActivityUtil.finish(activity, needFinishActivity);
            return;
        }
        //开始微信授权
        SendAuth.Req req = new SendAuth.Req();
        //应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
        // snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
        req.scope = "snsapi_userinfo";
        //重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节，该值会被微信原样返回，我们可以将其进行比对，防止别人的攻击。
        req.state = activity.getPackageName();
        wxapi.sendReq(req);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            if (wxAuthReceiver != null) {
                activity.unregisterReceiver(wxAuthReceiver);
            }
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
                if (authCallback != null) {
                    authCallback.onError(ISocialType.SOCIAL_WX_SESSION, null);
                }
            } else {
                if (authCallback != null) {
                    authCallback.onSuccess(ISocialType.SOCIAL_WX_SESSION, null);
                }
            }
            ActivityUtil.finish(activity, needFinishActivity);
        }
    };
}
