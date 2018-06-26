package com.fqxyi.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.fqxyi.library.R;
import com.fqxyi.library.ShareKit;
import com.fqxyi.library.bean.ShareDataBean;
import com.fqxyi.library.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享弹框
 */
public class ShareDialog extends Dialog {

    //静态常量
    private static final String TAG = "ShareDialog";
    //上下文
    Context context;
    //view
    private RecyclerView shareRecyclerView;
    private Button shareCancel;
    //adapter
    ShareAdapter shareAdapter;
    //数据源-分享类型
    List<ShareTypeBean> shareTypeBeans;
    //数据源-分享数据
    ShareDataBean shareDataBean;

    public ShareDialog(Context context) {
        super(context, R.style.ShareDialogStyle);
        init(context);
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_dialog);
        //findView
        shareRecyclerView = (RecyclerView) findViewById(R.id.share_recycler_view);
        shareCancel = (Button) findViewById(R.id.share_cancel);
        //初始化RecyclerView
        initRecyclerView();
        //事件初始化
        initEvent();
    }

    private void initRecyclerView() {
        shareRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        shareAdapter = new ShareAdapter(context);
        shareAdapter.updateData(shareTypeBeans);
        shareRecyclerView.setAdapter(shareAdapter);
    }

    private void initEvent() {
        //share item icon 点击事件
        shareAdapter.setItemClickListener(new ShareAdapter.ItemClickListener() {
            @Override
            public void click(ShareTypeBean shareTypeBean, int position) {
                initItemClick(shareTypeBean);
            }
        });
        //share cancel 点击事件
        shareCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 初始化分享类型
     */
    public void initShareType(List<ShareTypeBean> list) {
        if (shareTypeBeans == null) {
            shareTypeBeans = new ArrayList<>();
        } else {
            shareTypeBeans.clear();
        }
        shareTypeBeans.addAll(list);
    }

    /**
     * 初始化分享数据
     */
    public void initShareData(ShareDataBean shareDataBean) {
        this.shareDataBean = shareDataBean;
    }

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(ShareTypeBean shareTypeBean) {
        if (shareTypeBean == null) {
            return;
        }
        switch (shareTypeBean.type) {
            case IShareType.SHARE_WECHAT: //微信
                ShareKit.shareWechat(shareDataBean);
                break;
            case IShareType.SHARE_WECHATMOMENTS: //朋友圈
                ShareKit.shareWechatMoments(shareDataBean);
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
                ShareKit.shareQQ(shareDataBean);
                break;
            case IShareType.SHARE_SINA: //新浪微博
                ShareKit.shareSina(shareDataBean);
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

    /**
     * Dialog View初始化
     */
    private void init(Context context) {
        this.context = context;
        if (!(context instanceof Activity)) {
            LogUtil.e(TAG, "context is not Activity");
            return;
        }
        Window window = getWindow();
        if (window == null) {
            LogUtil.e(TAG, "getWindow() == null");
            return;
        }
        // 设置显示动画
        window.setWindowAnimations(R.style.ShareDialogAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        if (((Activity) context).getWindowManager() != null &&
                ((Activity) context).getWindowManager().getDefaultDisplay() != null) {
            params.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        }
        // 以下这两句是为了保证按钮可以水平满屏
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(params);
    }

}
