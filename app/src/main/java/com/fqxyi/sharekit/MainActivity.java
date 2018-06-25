package com.fqxyi.sharekit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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

        originShareBean = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShareBean shareBean = new ShareBean();
            shareBean.shareIcon = "android.resource://" + getPackageName() + "/" + R.drawable.share_icon_wechat;
            shareBean.shareName = i+"";
            originShareBean.add(shareBean);
        }
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
