package com.fqxyi.library;

import com.fqxyi.library.bean.ShareDataBean;
import com.fqxyi.library.util.LogUtil;

/**
 * 分享操作类
 */
public class ShareKit {

    private static final String TAG = "ShareKit";

    /**
     * 分享到微信好友
     */
    public static void shareWechat(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareWechat, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 分享到微信朋友圈
     */
    public static void shareWechatMoments(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareWechatMoments, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 分享到短信
     */
    public static void shareShortMessage(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareShortMessage, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 复制
     */
    public static void shareCopy(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareCopy, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 刷新
     */
    public static void shareRefresh(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareRefresh, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 分享到QQ
     */
    public static void shareQQ(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareQQ, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 分享到新浪微博
     */
    public static void shareSina(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareSina, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 分享到微信小程序
     */
    public static void shareWxMiniProgram(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareWxMiniProgram, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 分享到支付宝小程序
     */
    public static void shareAlipayMiniProgram(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareAlipayMiniProgram, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 收藏
     */
    public static void shareCollection(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareCollection, shareDataBean = " + shareDataBean.toString());
    }

    /**
     * 查看全部
     */
    public static void shareShowAll(ShareDataBean shareDataBean) {

        LogUtil.d(TAG, "shareShowAll, shareDataBean = " + shareDataBean.toString());
    }

}
