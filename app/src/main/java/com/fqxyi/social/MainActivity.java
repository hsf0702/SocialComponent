package com.fqxyi.social;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.fqxyi.social.library.auth.IAuthCallback;
import com.fqxyi.social.library.dialog.ISocialType;
import com.fqxyi.social.library.dialog.SocialTypeBean;
import com.fqxyi.social.library.share.IShareCallback;
import com.fqxyi.social.library.share.ShareDataBean;
import com.fqxyi.social.library.share.WBShareHelper;
import com.fqxyi.social.library.util.SocialUtil;

import java.util.ArrayList;

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
        shareDataBean.type = WBShareHelper.TYPE_IMAGE_TEXT;
        shareDataBean.shareTitle = "百度一下，你就知道";
        shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
        shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
        shareDataBean.shareUrl = "https://www.baidu.com/";
        shareDataBean.shareMiniAppId = "小程序的原始ID";
        shareDataBean.shareMiniPage = "小程序页面地址";

        SocialUtil.get().share(this, socialTypeBeans, shareDataBean, new IShareCallback() {
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

    /**
     * 授权
     */
    public void jump2Auth(View view) {
        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));

        SocialUtil.get().auth(this, socialTypeBeans, new IAuthCallback() {
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
