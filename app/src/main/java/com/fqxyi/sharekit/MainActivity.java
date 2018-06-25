package com.fqxyi.sharekit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.fqxyi.library.IShareConfig;
import com.fqxyi.library.ShareBean;
import com.fqxyi.library.ShareDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    ShareDialog shareDialog;

    List<ShareBean> originShareBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化假数据
        originShareBean = new ArrayList<>();
        originShareBean.add(new ShareBean(IShareConfig.SHARE_WECHAT));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_WECHATMOMENTS));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_SHORTMESSAGE));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_COPY));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_REFRESH));
//        originShareBean.add(new ShareBean(IShareConfig.SHARE_CUSTOM));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_QQ));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_SINA));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_WXMINIPROGRAM));
//        originShareBean.add(new ShareBean(IShareConfig.SHARE_ALIPAYMINPROGRAM));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_COLLECTION));
        originShareBean.add(new ShareBean(IShareConfig.SHARE_SHOW_ALL));
    }

    /**
     * 分享
     */
    public void share(View view) {
        if (shareDialog == null) {
            shareDialog = new ShareDialog(this);
        }
        shareDialog.initData(originShareBean);
        shareDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareDialog = null;
    }

}
