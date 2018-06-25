package com.fqxyi.library;

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

import java.util.ArrayList;
import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    //上下文
    Context context;
    //数据源
    List<ShareBean> shareBeans;
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
        if (shareBeans == null || shareBeans.get(position) == null) {
            return;
        }
        final ShareBean shareBean = shareBeans.get(position);
        //View展示
        if (!TextUtils.isEmpty(shareBean.shareIcon)) {
            holder.shareItemIcon.setImageURI(Uri.parse(shareBean.shareIcon));
        }
        holder.shareItemName.setText(shareBean.shareName);
        //点击事件
        holder.shareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.click(shareBean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareBeans == null ? 0 : shareBeans.size();
    }

    /**
     * 更新数据
     */
    public void updateData(List<ShareBean> list) {
        if (shareBeans == null) {
            shareBeans = new ArrayList<>();
        } else {
            shareBeans.clear();
        }
        shareBeans.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 设置点击事件回调
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void click(ShareBean shareBean, int position);
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
