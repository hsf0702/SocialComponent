package com.fqxyi.kit.library.dialog;

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

import com.fqxyi.kit.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 社会化弹框的适配器
 */
public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.SocialViewHolder> {

    //上下文
    Context context;
    //数据源
    List<SocialTypeBean> socialTypeBeans;
    //点击事件回调
    ItemClickListener itemClickListener;

    public SocialAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SocialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SocialViewHolder(View.inflate(context, R.layout.social_dialog_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SocialViewHolder holder, final int position) {
        //数据处理
        if (socialTypeBeans == null || socialTypeBeans.get(position) == null) {
            return;
        }
        final SocialTypeBean socialTypeBean = socialTypeBeans.get(position);
        //View展示
        if (socialTypeBean.type != 5) {
            holder.socialItemIcon.setImageResource(SocialDialogUtil.getIcon(socialTypeBean.type));
            holder.socialItemName.setText(SocialDialogUtil.getName(socialTypeBean.type));
        } else {
            if (!TextUtils.isEmpty(socialTypeBean.socialIcon)) { // todo 网络图片，目前无法显示
                holder.socialItemIcon.setImageURI(Uri.parse(socialTypeBean.socialIcon));
            }
            holder.socialItemName.setText(socialTypeBean.socialName);
        }
        //点击事件
        holder.socialItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.click(socialTypeBean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return socialTypeBeans == null ? 0 : socialTypeBeans.size();
    }

    /**
     * 更新数据
     */
    public void updateData(List<SocialTypeBean> list) {
        if (socialTypeBeans == null) {
            socialTypeBeans = new ArrayList<>();
        } else {
            socialTypeBeans.clear();
        }
        socialTypeBeans.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 设置点击事件回调
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class SocialViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout socialItem;
        private ImageView socialItemIcon;
        private TextView socialItemName;

        public SocialViewHolder(View itemView) {
            super(itemView);
            socialItem = (LinearLayout) itemView.findViewById(R.id.social_item);
            socialItemIcon = (ImageView) itemView.findViewById(R.id.social_item_icon);
            socialItemName = (TextView) itemView.findViewById(R.id.social_item_name);
        }
    }

}
