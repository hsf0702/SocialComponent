package com.fqxyi.share.library.dialog;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fqxyi.share.library.R;
import com.fqxyi.share.library.util.ShareUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享弹框的适配器
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    //上下文
    Context context;
    //数据源
    List<ShareTypeBean> shareTypeBeans;
    //点击事件回调
    ItemClickListener itemClickListener;

    public ShareAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareViewHolder(View.inflate(context, R.layout.share_dialog_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder holder, final int position) {
        //数据处理
        if (shareTypeBeans == null || shareTypeBeans.get(position) == null) {
            return;
        }
        final ShareTypeBean shareTypeBean = shareTypeBeans.get(position);
        //View展示
        if (shareTypeBean.type != 5) {
            holder.shareItemIcon.setImageResource(ShareUtil.getIcon(shareTypeBean.type));
            holder.shareItemName.setText(ShareUtil.getName(shareTypeBean.type));
        } else {
            if (!TextUtils.isEmpty(shareTypeBean.shareIcon)) { // todo 网络图片，目前无法显示
                holder.shareItemIcon.setImageURI(Uri.parse(shareTypeBean.shareIcon));
            }
            holder.shareItemName.setText(shareTypeBean.shareName);
        }
        //点击事件
        holder.shareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.click(shareTypeBean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareTypeBeans == null ? 0 : shareTypeBeans.size();
    }

    /**
     * 更新数据
     */
    public void updateData(List<ShareTypeBean> list) {
        if (shareTypeBeans == null) {
            shareTypeBeans = new ArrayList<>();
        } else {
            shareTypeBeans.clear();
        }
        shareTypeBeans.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 设置点击事件回调
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout shareItem;
        private ImageView shareItemIcon;
        private TextView shareItemName;

        public ShareViewHolder(View itemView) {
            super(itemView);
            shareItem = (LinearLayout) itemView.findViewById(R.id.share_item);
            shareItemIcon = (ImageView) itemView.findViewById(R.id.share_item_icon);
            shareItemName = (TextView) itemView.findViewById(R.id.share_item_name);
        }
    }

}
