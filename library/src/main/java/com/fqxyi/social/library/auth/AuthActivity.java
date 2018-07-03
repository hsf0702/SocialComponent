package com.fqxyi.social.library.auth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fqxyi.social.library.ISocialType;
import com.fqxyi.social.library.SocialHelper;
import com.fqxyi.social.library.dialog.ItemClickListener;
import com.fqxyi.social.library.dialog.SocialDialog;
import com.fqxyi.social.library.dialog.SocialTypeBean;
import com.fqxyi.social.library.util.Utils;

import java.util.ArrayList;

public class AuthActivity extends Activity {

    //社会化弹框
    private SocialDialog socialDialog;
    //授权入口类
    private AuthHelper authHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化授权类型
        Intent intent = getIntent();
        if (intent == null) {
            Utils.toast(this, "社会化类型为空");
            finish();
            return;
        }
        ArrayList<SocialTypeBean> socialTypeBeans = (ArrayList<SocialTypeBean>) intent.getSerializableExtra("SocialTypeBeans");
        SocialTypeBean socialTypeBean = (SocialTypeBean) intent.getSerializableExtra("SocialTypeBean");
        final boolean needFinishActivity = intent.getBooleanExtra("needFinishActivity", false);
        //NPE校验
        if ((socialTypeBeans == null || socialTypeBeans.size() == 0) && socialTypeBean == null) {
            Utils.toast(this, "社会化类型为空");
            finish();
            return;
        }
        //创建授权入口类
        authHelper = SocialHelper.get().getAuthHelper();
        if (socialTypeBeans != null && socialTypeBeans.size() > 0) {
            //显示授权弹框
            if (socialDialog == null) {
                socialDialog = new SocialDialog(this);
            }
            socialDialog.initSocialType(socialTypeBeans);
            socialDialog.setItemClickListener(new ItemClickListener() {
                @Override
                public void click(SocialTypeBean socialTypeBean, int position) {
                    initItemClick(socialTypeBean, SocialHelper.get().getAuthCallback(), needFinishActivity);
                }
            });
            socialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            socialDialog.show();
        } else if (socialTypeBean != null) {
            initItemClick(socialTypeBean, SocialHelper.get().getAuthCallback(), true);
        } else {
            Utils.toast(this, "分享初始化异常");
            finish();
        }
    }

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(SocialTypeBean socialTypeBean, IAuthCallback authCallback, boolean needFinishActivity) {
        if (socialTypeBean == null || Utils.isFastClick()) {
            return;
        }
        switch (socialTypeBean.type) {
            case ISocialType.SOCIAL_WX_SESSION: //微信
                authHelper.authWX(this, authCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_QQ: //QQ
                authHelper.authQQ(this, authCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_WB: //微博
                authHelper.authWB(this, authCallback, needFinishActivity);
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