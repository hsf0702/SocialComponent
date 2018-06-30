package com.fqxyi.social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.fqxyi.social.library.dialog.ISocialType;
import com.fqxyi.social.library.dialog.ItemClickListener;
import com.fqxyi.social.library.dialog.SocialDialog;
import com.fqxyi.social.library.dialog.SocialTypeBean;
import com.fqxyi.social.library.auth.IAuthCallback;
import com.fqxyi.social.library.auth.AuthHelper;
import com.fqxyi.social.library.util.SocialUtil;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends Activity {

    private static final String TAG = "AuthActivity";

    //社会化弹框
    SocialDialog socialDialog;
    //数据源-社会化类型
    List<SocialTypeBean> socialTypeBeans;
    //授权入口类
    AuthHelper authHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        //初始化授权类型
        socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WECHAT));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SINA));
        //创建授权入口类
        authHelper = SocialUtil.get().getAuthHelper();
    }

    public void auth(View view) {
        if (socialDialog == null) {
            socialDialog = new SocialDialog(this);
        }
        socialDialog.initSocialType(socialTypeBeans);
        socialDialog.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(SocialTypeBean socialTypeBean, int position) {
                initItemClick(socialTypeBean, authCallback);
            }
        });
        socialDialog.show();
    }

    IAuthCallback authCallback = new IAuthCallback() {
        @Override
        public void onSuccess(String msg, String response) {
            Toast.makeText(AuthActivity.this, "onSuccess, msg =" + msg + ", response = " + response, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String msg) {
            Toast.makeText(AuthActivity.this, "onError, msg = " + msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(String msg) {
            Toast.makeText(AuthActivity.this, "onCancel, msg = " + msg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(SocialTypeBean socialTypeBean, IAuthCallback authCallback) {
        if (socialTypeBean == null) {
            return;
        }
        switch (socialTypeBean.type) {
            case ISocialType.SOCIAL_WECHAT: //微信
                authHelper.authWX(this, authCallback);
                break;
            case ISocialType.SOCIAL_QQ: //QQ
                authHelper.authQQ(this, authCallback);
                break;
            case ISocialType.SOCIAL_SINA: //新浪微博
                authHelper.authWB(this, authCallback);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (authHelper != null) {
            authHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socialDialog = null;
        if (authHelper != null) {
            authHelper.onDestroy();
            authHelper = null;
        }
    }


}
