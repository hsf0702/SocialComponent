package com.fqxyi.sharekit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.fqxyi.library.bean.ShareDataBean;
import com.fqxyi.library.dialog.IShareType;
import com.fqxyi.library.dialog.ShareTypeBean;
import com.fqxyi.library.dialog.ShareDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    //分享弹框
    ShareDialog shareDialog;
    //数据源-分享类型
    List<ShareTypeBean> shareTypeBeans;
    //数据源-分享数据
    ShareDataBean shareDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化分享类型
        shareTypeBeans = new ArrayList<>();
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_WECHAT));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_WECHATMOMENTS));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_SHORTMESSAGE));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_COPY));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_REFRESH));
//        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_CUSTOM));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_QQ));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_SINA));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_WXMINIPROGRAM));
//        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_ALIPAYMINPROGRAM));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_COLLECTION));
        shareTypeBeans.add(new ShareTypeBean(IShareType.SHARE_SHOW_ALL));
        //初始化分享数据
        shareDataBean = new ShareDataBean();
        shareDataBean.shareTitle = "百度一下，你就知道";
        shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
        shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
        shareDataBean.shareUrl = "https://www.baidu.com/";
        shareDataBean.shareMiniAppId = "小程序的原始ID";
        shareDataBean.shareMiniPage = "小程序页面地址";
    }

    /**
     * 分享
     */
    public void share(View view) {
        if (shareDialog == null) {
            shareDialog = new ShareDialog(this);
        }
        shareDialog.initShareType(shareTypeBeans);
        shareDialog.initShareData(shareDataBean);
        shareDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareDialog = null;
    }

}
