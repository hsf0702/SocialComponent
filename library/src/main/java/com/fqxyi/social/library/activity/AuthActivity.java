package com.fqxyi.social.library.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.fqxyi.social.library.R;
import com.fqxyi.social.library.auth.AuthHelper;
import com.fqxyi.social.library.auth.IAuthCallback;
import com.fqxyi.social.library.dialog.ISocialType;
import com.fqxyi.social.library.dialog.ItemClickListener;
import com.fqxyi.social.library.dialog.SocialDialog;
import com.fqxyi.social.library.dialog.SocialTypeBean;
import com.fqxyi.social.library.util.SocialUtil;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends Activity {

    //社会化弹框
    private SocialDialog socialDialog;
    //授权入口类
    private AuthHelper authHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化授权类型
        ArrayList<SocialTypeBean> socialTypeBeans = (ArrayList<SocialTypeBean>) getIntent().getSerializableExtra("SocialTypeBean");
        if (socialTypeBeans == null) {
            Toast.makeText(this, "社会化类型为空", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //创建授权入口类
        authHelper = SocialUtil.get().getAuthHelper();
        //显示授权弹框
        if (socialDialog == null) {
            socialDialog = new SocialDialog(this);
        }
        socialDialog.initSocialType(socialTypeBeans);
        socialDialog.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(SocialTypeBean socialTypeBean, int position) {
                initItemClick(socialTypeBean, SocialUtil.get().getAuthCallback());
            }
        });
        socialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        socialDialog.show();
    }

    public void auth(View view) {
    }

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(SocialTypeBean socialTypeBean, IAuthCallback authCallback) {
        if (socialTypeBean == null) {
            return;
        }
        switch (socialTypeBean.type) {
            case ISocialType.SOCIAL_WX_SESSION: //微信
                authHelper.authWX(this, authCallback);
                break;
            case ISocialType.SOCIAL_QQ: //QQ
                authHelper.authQQ(this, authCallback);
                break;
            case ISocialType.SOCIAL_WB: //微博
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
