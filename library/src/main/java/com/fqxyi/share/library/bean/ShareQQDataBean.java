package com.fqxyi.share.library.bean;

import com.tencent.connect.share.QQShare;

/**
 * QQ分享数据
 */
public class ShareQQDataBean extends ShareDataBean {

    public static final int TYPE_IMAGE_TEXT = 0; //图文分享
    public static final int TYPE_IMAGE = 1; //纯图片分享

    /**
     * 创建分享图文类型到qq
     *
     * @param appName   应用名
     * @param title     标题，长度限制30个字符
     * @param summary   摘要，长度限制40个字
     * @param imageUrl  图片地址，本地路径或者url
     * @param targetUrl 跳转地址
     * @return
     */
    public static ShareDataBean createImageTextData(String appName, String title, String summary, String imageUrl, String targetUrl) {
        ShareDataBean dataBean = new ShareDataBean();
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_APP_NAME, appName);
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_TITLE, title);
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_SUMMARY, summary);
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        return dataBean;
    }

    /**
     * 创建分享纯图片到qq
     *
     * @param appName  应用名
     * @param imageUrl 本地图片地址
     * @return
     */
    public static ShareDataBean createImageData(String appName, String imageUrl) {
        ShareDataBean dataBean = new ShareDataBean();
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_APP_NAME, appName);
        addParams(dataBean.params, QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        return dataBean;
    }

}
