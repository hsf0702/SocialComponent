package com.fqxyi.social;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.fqxyi.social.library.ISocialType;
import com.fqxyi.social.library.dialog.SocialTypeBean;
import com.fqxyi.social.library.share.IShareCallback;
import com.fqxyi.social.library.share.QQShareHelper;
import com.fqxyi.social.library.share.ShareDataBean;
import com.fqxyi.social.library.share.WBShareHelper;
import com.fqxyi.social.library.share.WXShareHelper;
import com.fqxyi.social.library.SocialHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void jump2Share(View view) {
        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_TIMELINE));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SMS));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COPY));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_REFRESH));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_MINIPROGRAM));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_ALIPAY_MINIPROGRAM));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COLLECTION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SHOW_ALL));

        ShareDataBean shareDataBean = new ShareDataBean();
        HashMap<Integer, Integer> shareTypeList = new HashMap<>();
        shareTypeList.put(ISocialType.SOCIAL_WX_SESSION, WXShareHelper.TYPE_TEXT);
        shareTypeList.put(ISocialType.SOCIAL_QQ, QQShareHelper.TYPE_IMAGE_TEXT);
        shareTypeList.put(ISocialType.SOCIAL_WB, WBShareHelper.TYPE_TEXT);
        shareDataBean.shareType = shareTypeList;
        shareDataBean.shareTitle = "百度一下，你就知道";
        shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
        shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
        shareDataBean.shareUrl = "https://www.baidu.com/";
        shareDataBean.shareMiniAppId = "小程序的原始ID";
        shareDataBean.shareMiniPage = "小程序页面地址";

        SocialHelper.get().share(this, socialTypeBeans, shareDataBean, new IShareCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SecondActivity.this, "SecondActivity onSuccess, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SecondActivity.this, "SecondActivity onError, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SecondActivity.this, "SecondActivity onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
