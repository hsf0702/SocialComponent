package com.fqxyi.library;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.fqxyi.library.util.LogUtil;
import com.fqxyi.library.util.ShareUtil;

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
    //数据源
    List<ShareBean> shareBeans;

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
        shareAdapter.updateData(shareBeans);
        shareRecyclerView.setAdapter(shareAdapter);
    }

    private void initEvent() {
        //share item icon 点击事件
        shareAdapter.setItemClickListener(new ShareAdapter.ItemClickListener() {
            @Override
            public void click(ShareBean shareBean, int position) {
                Toast.makeText(context, "点击了" + ShareUtil.getName(shareBean.type), Toast.LENGTH_SHORT).show();
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
     * 初始化数据
     */
    public void initData(List<ShareBean> list) {
        this.shareBeans = list;
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
