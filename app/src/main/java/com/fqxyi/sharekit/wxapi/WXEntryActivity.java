package com.fqxyi.sharekit.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fqxyi.sharekit.util.SocialUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String wxAppId = "wx49f25cc1d77c9dd3";
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
        Log.d("WXEntryActivity", baseResp.errCode + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                SocialUtil.getInstance().shareHelper().sendShareBackBroadcast(this, true);
            } else {
                SocialUtil.getInstance().shareHelper().sendShareBackBroadcast(this, false);
            }
        }
        onBackPressed();
    }

}
