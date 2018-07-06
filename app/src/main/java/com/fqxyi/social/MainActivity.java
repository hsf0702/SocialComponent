package com.fqxyi.social;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.fqxyi.social.library.ISocialType;
import com.fqxyi.social.library.SocialHelper;
import com.fqxyi.social.library.auth.IAuthCallback;
import com.fqxyi.social.library.dialog.SocialTypeBean;
import com.fqxyi.social.library.share.IShareCallback;
import com.fqxyi.social.library.share.QQShareHelper;
import com.fqxyi.social.library.share.ShareDataBean;
import com.fqxyi.social.library.share.WBShareHelper;
import com.fqxyi.social.library.share.WXShareHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        //申请读写权限和电话权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    0);
            return;
        }
        //申请读写权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
            return;
        }
        //申请电话权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    0);
            return;
        }

    }

    /**
     * 分享
     */
    public void jump2Share(View view) {
        ShareDataBean shareDataBean = new ShareDataBean();
        HashMap<Integer, Integer> shareTypeList = new HashMap<>();
        shareTypeList.put(ISocialType.SOCIAL_WX_SESSION, WXShareHelper.TYPE_WEB);
        shareTypeList.put(ISocialType.SOCIAL_WX_TIMELINE, WXShareHelper.TYPE_WEB);
        shareTypeList.put(ISocialType.SOCIAL_WX_MINIPROGRAM, WXShareHelper.TYPE_MINIPROGRAM);
        shareTypeList.put(ISocialType.SOCIAL_QQ, QQShareHelper.TYPE_IMAGE_TEXT);
        shareTypeList.put(ISocialType.SOCIAL_WB, WBShareHelper.TYPE_TEXT);
        shareDataBean.shareType = shareTypeList;
        shareDataBean.shareTitle = "百度一下，你就知道";
        shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
        shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
        shareDataBean.shareUrl = "https://www.baidu.com/";
        shareDataBean.shareMiniType = 0; //小程序类型 - 正式版:0，测试版:1，体验版:2
        shareDataBean.shareMiniAppId = "gh_64c734bc4b8d"; //小程序AppId
        shareDataBean.shareMiniPage = "pages/fitting-room/index"; //小程序页面地址

        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_TIMELINE));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SMS));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COPY));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_REFRESH));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_CUSTOM, "https://img.ezprice.com.tw/is/c.rimg.com.tw/s1/4/7e/29/21628111029801_843_s.jpg", "自定义图标需要集成图片库"));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_MINIPROGRAM));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_ALIPAY_MINIPROGRAM));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COLLECTION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SHOW_ALL));

        SocialHelper.get().share(this, socialTypeBeans, shareDataBean, new IShareCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "MainActivity onSuccess, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "MainActivity onError, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(MainActivity.this, "MainActivity onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 分享到QQ
     */
    public void jump2ShareQQ(View view) {
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
        shareDataBean.shareMiniType = 0;
        shareDataBean.shareMiniPage = "小程序页面地址";

        SocialTypeBean socialTypeBean = new SocialTypeBean();
        socialTypeBean.socialType = ISocialType.SOCIAL_QQ;

        SocialHelper.get().share(this, socialTypeBean, shareDataBean, new IShareCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "MainActivity onSuccess, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "MainActivity onError, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(MainActivity.this, "MainActivity onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 授权
     */
    public void jump2Auth(View view) {
        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));

        SocialHelper.get().auth(this, socialTypeBeans, new IAuthCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "onSuccess, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "onError, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(MainActivity.this, "onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void jump2SecondActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void jump2AuthQQ(View view) {
        SocialTypeBean socialTypeBean = new SocialTypeBean();
        socialTypeBean.socialType = ISocialType.SOCIAL_QQ;

        SocialHelper.get().auth(this, socialTypeBean, new IAuthCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "onSuccess, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(MainActivity.this, "onError, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(MainActivity.this, "onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
