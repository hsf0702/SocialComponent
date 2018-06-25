package com.fqxyi.library.util;

import com.fqxyi.library.IShareConfig;
import com.fqxyi.library.R;

public class ShareUtil {

    public static int getIcon(int type) {
        switch (type) {
            case IShareConfig.SHARE_WECHAT:
                // 微信
                return R.drawable.share_icon_wechat;
            case IShareConfig.SHARE_WECHATMOMENTS:
                // 朋友圈
                return R.drawable.share_icon_wechatmoments;
            case IShareConfig.SHARE_SHORTMESSAGE:
                // 短信
                return R.drawable.share_icon_shortmessage;
            case IShareConfig.SHARE_COPY:
                // 复制
                return R.drawable.share_icon_copy;
            case IShareConfig.SHARE_REFRESH:
                // 刷新
                return R.drawable.share_icon_refresh;
            case IShareConfig.SHARE_QQ:
                // QQ
                return R.drawable.share_icon_qq;
            case IShareConfig.SHARE_SINA:
                // 新浪微博
                return R.drawable.share_icon_sina;
            case IShareConfig.SHARE_WXMINIPROGRAM:
                // 微信小程序
                return R.drawable.share_icon_wechat;
            case IShareConfig.SHARE_ALIPAYMINPROGRAM:
                // 支付宝小程序
                return R.drawable.share_icon_wechat;
            case IShareConfig.SHARE_COLLECTION:
                //收藏
                return R.drawable.share_icon_collection_normal;
            case IShareConfig.SHARE_SHOW_ALL:
                //查看全部
                return R.drawable.share_icon_show_all;
        }
        return R.drawable.share_icon_refresh;
    }

    public static String getName(int type) {
        switch (type) {
            case IShareConfig.SHARE_WECHAT:
                // 微信好友
                return "微信好友";
            case IShareConfig.SHARE_WECHATMOMENTS:
                // 朋友圈
                return "朋友圈";
            case IShareConfig.SHARE_SHORTMESSAGE:
                // 短信
                return "短信";
            case IShareConfig.SHARE_COPY:
                // 复制
                return "复制链接";
            case IShareConfig.SHARE_REFRESH:
                // 刷新
                return "刷新";
            case IShareConfig.SHARE_QQ:
                // QQ
                return "QQ";
            case IShareConfig.SHARE_SINA:
                // 新浪微博
                return "微博";
            case IShareConfig.SHARE_WXMINIPROGRAM:
                // 微信小程序
                return "微信小程序";
            case IShareConfig.SHARE_ALIPAYMINPROGRAM:
                // 支付宝小程序
                return "支付宝小程序";
            case IShareConfig.SHARE_COLLECTION:
                //收藏
                return "收藏";
            case IShareConfig.SHARE_SHOW_ALL:
                //查看全部
                return "查看全部";
        }
        return "刷新";
    }

}
