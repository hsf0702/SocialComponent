package com.fqxyi.library.util;

import com.fqxyi.library.dialog.IShareType;
import com.fqxyi.library.R;

/**
 * 分享工具类
 */
public class ShareUtil {

    public static int getIcon(int type) {
        switch (type) {
            case IShareType.SHARE_WECHAT: //微信
                return R.drawable.share_icon_wechat;
            case IShareType.SHARE_WECHATMOMENTS: //朋友圈
                return R.drawable.share_icon_wechatmoments;
            case IShareType.SHARE_SHORTMESSAGE: //短信
                return R.drawable.share_icon_shortmessage;
            case IShareType.SHARE_COPY: //复制
                return R.drawable.share_icon_copy;
            case IShareType.SHARE_REFRESH: //刷新
                return R.drawable.share_icon_refresh;
            case IShareType.SHARE_QQ: //QQ
                return R.drawable.share_icon_qq;
            case IShareType.SHARE_SINA: //新浪微博
                return R.drawable.share_icon_sina;
            case IShareType.SHARE_WXMINIPROGRAM: //微信小程序
                return R.drawable.share_icon_wechat;
            case IShareType.SHARE_ALIPAYMINPROGRAM: //支付宝小程序
                return R.drawable.share_icon_alipay;
            case IShareType.SHARE_COLLECTION: //收藏
                return R.drawable.share_icon_collection_normal;
            case IShareType.SHARE_SHOW_ALL: //查看全部
                return R.drawable.share_icon_show_all;
        }
        return R.drawable.share_icon_refresh;
    }

    public static String getName(int type) {
        switch (type) {
            case IShareType.SHARE_WECHAT: //微信好友
                return "微信好友";
            case IShareType.SHARE_WECHATMOMENTS: //朋友圈
                return "朋友圈";
            case IShareType.SHARE_SHORTMESSAGE: //短信
                return "短信";
            case IShareType.SHARE_COPY: //复制
                return "复制链接";
            case IShareType.SHARE_REFRESH: //刷新
                return "刷新";
            case IShareType.SHARE_QQ: //QQ
                return "QQ";
            case IShareType.SHARE_SINA: //新浪微博
                return "微博";
            case IShareType.SHARE_WXMINIPROGRAM: //微信小程序
                return "微信小程序";
            case IShareType.SHARE_ALIPAYMINPROGRAM: //支付宝小程序
                return "支付宝小程序";
            case IShareType.SHARE_COLLECTION: //收藏
                return "收藏";
            case IShareType.SHARE_SHOW_ALL: //查看全部
                return "查看全部";
        }
        return "刷新";
    }

}
