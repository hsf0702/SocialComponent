package com.fqxyi.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fqxyi.share.library.QQShareHelper;
import com.fqxyi.share.library.ShareHelper;
import com.fqxyi.share.library.ShareKit;
import com.fqxyi.share.library.bean.ShareDataBean;
import com.fqxyi.share.library.callback.IShareCallback;
import com.fqxyi.share.library.callback.ItemClickListener;
import com.fqxyi.share.library.dialog.IShareType;
import com.fqxyi.share.library.dialog.ShareDialog;
import com.fqxyi.share.library.dialog.ShareTypeBean;
import com.fqxyi.share.library.util.LogUtil;
import com.fqxyi.share.util.SocialUtil;

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

    ShareHelper shareHelper;

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
        shareDataBean.type = QQShareHelper.TYPE_IMAGE_TEXT;
        shareDataBean.shareTitle = "百度一下，你就知道";
        shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
        shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
        shareDataBean.shareUrl = "https://www.baidu.com/";
        shareDataBean.shareMiniAppId = "小程序的原始ID";
        shareDataBean.shareMiniPage = "小程序页面地址";
        //
        shareHelper = SocialUtil.getInstance().shareHelper();
    }

    /**
     * 分享
     */
    public void share(View view) {
        if (shareDialog == null) {
            shareDialog = new ShareDialog(this);
        }
        shareDialog.initShareType(shareTypeBeans);
        shareDialog.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(ShareTypeBean shareTypeBean, int position) {
                initItemClick(shareTypeBean, shareCallback);
            }
        });
        shareDialog.show();
    }

    IShareCallback shareCallback = new IShareCallback() {
        @Override
        public void onSuccess() {
            LogUtil.d(TAG, "onSuccess");
        }

        @Override
        public void onError(String msg) {
            LogUtil.d(TAG, "onError, msg = " + msg);
        }

        @Override
        public void onCancel() {
            LogUtil.d(TAG, "onCancel");
        }
    };

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(ShareTypeBean shareTypeBean, IShareCallback shareCallback) {
        if (shareTypeBean == null) {
            return;
        }
        switch (shareTypeBean.type) {
            case IShareType.SHARE_WECHAT: //微信
                shareHelper.shareWX(this, false, shareDataBean, shareCallback);
                break;
            case IShareType.SHARE_WECHATMOMENTS: //朋友圈
                shareHelper.shareWX(this, true, shareDataBean, shareCallback);
                break;
            case IShareType.SHARE_SHORTMESSAGE: //短信
                ShareKit.shareShortMessage(shareDataBean);
                break;
            case IShareType.SHARE_COPY: //复制
                ShareKit.shareCopy(shareDataBean);
                break;
            case IShareType.SHARE_REFRESH: //刷新
                ShareKit.shareRefresh(shareDataBean);
                break;
            case IShareType.SHARE_QQ: //QQ
                shareHelper.shareQQ(this, shareDataBean, shareCallback);
                break;
            case IShareType.SHARE_SINA: //新浪微博
                shareHelper.shareWB(this, shareDataBean, shareCallback);
                break;
            case IShareType.SHARE_WXMINIPROGRAM: //微信小程序
                ShareKit.shareWxMiniProgram(shareDataBean);
                break;
            case IShareType.SHARE_ALIPAYMINPROGRAM: //支付宝小程序
                ShareKit.shareAlipayMiniProgram(shareDataBean);
                break;
            case IShareType.SHARE_COLLECTION: //收藏
                ShareKit.shareCollection(shareDataBean);
                break;
            case IShareType.SHARE_SHOW_ALL: //查看全部
                ShareKit.shareShowAll(shareDataBean);
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
        shareDialog = null;
        if (shareHelper != null) {
            shareHelper.onDestroy();
            shareHelper = null;
        }
    }

}
