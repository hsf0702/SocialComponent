package com.fqxyi.social.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fqxyi.social.library.util.LogUtil;
import com.fqxyi.social.library.util.SocialUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String wxAppId = SocialUtil.get().getWxAppId();
        wxapi = WXAPIFactory.createWXAPI(this, wxAppId, true);
        wxapi.registerApp(wxAppId);

        wxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtil.d(TAG, baseResp.errCode + baseResp.errStr);
        //微信授权
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                String code = ((SendAuth.Resp) baseResp).code;
                SocialUtil.get().getAuthHelper().sendAuthBroadcast(this, code);
            } else {
                SocialUtil.get().getAuthHelper().sendAuthBroadcast(this, null);
            }
        }
        //微信分享
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                SocialUtil.get().getShareHelper().sendShareBroadcast(this, true);
            } else {
                SocialUtil.get().getShareHelper().sendShareBroadcast(this, false);
            }
        }
        onBackPressed();
    }

}
