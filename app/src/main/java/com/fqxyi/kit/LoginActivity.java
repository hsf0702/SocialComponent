package com.fqxyi.kit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.fqxyi.kit.library.dialog.IShareType;
import com.fqxyi.kit.library.dialog.ItemClickListener;
import com.fqxyi.kit.library.dialog.ShareDialog;
import com.fqxyi.kit.library.dialog.ShareTypeBean;
import com.fqxyi.kit.library.login.ILoginCallback;
import com.fqxyi.kit.library.login.LoginHelper;
import com.fqxyi.kit.library.util.SocialUtil;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    //登录弹框
    ShareDialog shareDialog;
    //数据源-登录类型
    List<ShareTypeBean> shareTypeBeans;
    //登录入口类
    LoginHelper loginHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化登录类型
        shareTypeBeans = new ArrayList<>();
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_WECHAT));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_QQ));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_SINA));
        //初始化数据
        SocialUtil.get().setQqAppId("1107001192")
                .setWxAppId("")
                .setWxAppSecret("")
                .setWbAppId("2474483531")
                .setWbRedirectUrl("https://www.fqxyi.com/");
        //创建登录入口类
        loginHelper = SocialUtil.get().getLoginHelper();
    }

    public void login(View view) {
        if (shareDialog == null) {
            shareDialog = new ShareDialog(this);
        }
        shareDialog.initShareType(shareTypeBeans);
        shareDialog.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(ShareTypeBean shareTypeBean, int position) {
                initItemClick(shareTypeBean, loginCallback);
            }
        });
        shareDialog.show();
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
    private void initItemClick(ShareTypeBean shareTypeBean, ILoginCallback loginCallback) {
        if (shareTypeBean == null) {
            return;
        }
        switch (shareTypeBean.type) {
            case IShareType.SHARE_WECHAT: //微信
                loginHelper.loginWX(this, loginCallback);
                break;
            case IShareType.SHARE_QQ: //QQ
                loginHelper.loginQQ(this, loginCallback);
                break;
            case IShareType.SHARE_SINA: //新浪微博
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (loginHelper != null) {
            loginHelper.onNewIntent(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareDialog = null;
        if (loginHelper != null) {
            loginHelper.onDestroy();
            loginHelper = null;
        }
    }


}
