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
import com.fqxyi.social.library.login.ILoginCallback;
import com.fqxyi.social.library.login.LoginHelper;
import com.fqxyi.social.library.util.SocialUtil;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    //登录弹框
    SocialDialog socialDialog;
    //数据源-登录类型
    List<SocialTypeBean> socialTypeBeans;
    //登录入口类
    LoginHelper loginHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化登录类型
        socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WECHAT));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SINA));
        //创建登录入口类
        loginHelper = SocialUtil.get().getLoginHelper();
    }

    public void login(View view) {
        if (socialDialog == null) {
            socialDialog = new SocialDialog(this);
        }
        socialDialog.initSocialType(socialTypeBeans);
        socialDialog.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(SocialTypeBean socialTypeBean, int position) {
                initItemClick(socialTypeBean, loginCallback);
            }
        });
        socialDialog.show();
    }

    ILoginCallback loginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(String msg, String response) {
            Toast.makeText(LoginActivity.this, "onSuccess, msg =" + msg + ", response = " + response, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String msg) {
            Toast.makeText(LoginActivity.this, "onError, msg = " + msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(String msg) {
            Toast.makeText(LoginActivity.this, "onCancel, msg = " + msg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(SocialTypeBean socialTypeBean, ILoginCallback loginCallback) {
        if (socialTypeBean == null) {
            return;
        }
        switch (socialTypeBean.type) {
            case ISocialType.SOCIAL_WECHAT: //微信
                loginHelper.loginWX(this, loginCallback);
                break;
            case ISocialType.SOCIAL_QQ: //QQ
                loginHelper.loginQQ(this, loginCallback);
                break;
            case ISocialType.SOCIAL_SINA: //新浪微博
                loginHelper.loginWB(this, loginCallback);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (loginHelper != null) {
            loginHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socialDialog = null;
        if (loginHelper != null) {
            loginHelper.onDestroy();
            loginHelper = null;
        }
    }


}
