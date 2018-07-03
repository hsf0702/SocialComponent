package com.fqxyi.social.library.share;

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

public class ShareActivity extends Activity {

    //分享弹框
    private SocialDialog socialDialog;
    //数据源-分享数据
    private ShareDataBean shareDataBean;
    //分享入口类
    private ShareHelper shareHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化社会化类型
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
        //初始化分享数据
        shareDataBean = (ShareDataBean) intent.getSerializableExtra("ShareDataBean");
        if (shareDataBean == null) {
            Utils.toast(this, "分享数据为空");
            finish();
            return;
        }
        //创建分享入口类
        shareHelper = SocialHelper.get().getShareHelper();
        if (socialTypeBeans != null && socialTypeBeans.size() > 0) {
            //显示分享弹框
            if (socialDialog == null) {
                socialDialog = new SocialDialog(this);
            }
            socialDialog.initSocialType(socialTypeBeans);
            socialDialog.setItemClickListener(new ItemClickListener() {
                @Override
                public void click(SocialTypeBean socialTypeBean, int position) {
                    initItemClick(socialTypeBean, SocialHelper.get().getShareCallback(), needFinishActivity);
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
            initItemClick(socialTypeBean, SocialHelper.get().getShareCallback(), true);
        } else {
            Utils.toast(this, "分享初始化异常");
            finish();
        }
    }

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(SocialTypeBean socialTypeBean, IShareCallback shareCallback, boolean needFinishActivity) {
        if (socialTypeBean == null || Utils.isFastClick()) {
            return;
        }
        switch (socialTypeBean.type) {
            case ISocialType.SOCIAL_WX_SESSION: //微信
                shareHelper.shareWX(this, false, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_WX_TIMELINE: //朋友圈
                shareHelper.shareWX(this, true, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_SMS: //短信
                shareHelper.shareShortMessage(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_COPY: //复制
                shareHelper.shareCopy(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_REFRESH: //刷新
                shareHelper.shareRefresh(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_QQ: //QQ
                shareHelper.shareQQ(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_WB: //微博
                shareHelper.shareWB(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_WX_MINIPROGRAM: //微信小程序
                shareHelper.shareWxMiniProgram(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_ALIPAY_MINIPROGRAM: //支付宝小程序
                shareHelper.shareAlipayMiniProgram(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_COLLECTION: //收藏
                shareHelper.shareCollection(this, shareDataBean, shareCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_SHOW_ALL: //查看全部
                shareHelper.shareShowAll(this, shareDataBean, shareCallback, needFinishActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareHelper != null) {
            shareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (shareHelper != null) {
            shareHelper.onNewIntent(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socialDialog = null;
        if (shareHelper != null) {
            shareHelper.onDestroy();
            shareHelper = null;
        }
    }

}