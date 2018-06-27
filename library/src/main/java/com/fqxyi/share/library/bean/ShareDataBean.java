package com.fqxyi.share.library.bean;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * 分享数据 数据结构
 */
public class ShareDataBean {

    //应用名
    public String appName;
    //分享标题
    public String shareTitle;
    //分享描述
    public String shareDesc;
    //分享图片
    public String shareImage;
    //分享地址
    public String shareUrl;
    //小程序的原始ID
    public String shareMiniAppId;
    //小程序页面地址
    public String shareMiniPage;

    public int type;

    public Bundle params;

    public ShareDataBean() {
        params = new Bundle();
    }

    public Bundle getParams() {
        return params;
    }

    protected static void addParams(Bundle params, String key, String value) {
        if (params == null || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        params.putString(key, value);
    }

    protected static void addParams(Bundle params, String key, int value) {
        if (params == null || TextUtils.isEmpty(key)) {
            return;
        }
        params.putInt(key, value);
    }

    @Override
    public String toString() {
        return "ShareDataBean{" +
                "shareTitle='" + shareTitle + '\'' +
                ", shareDesc='" + shareDesc + '\'' +
                ", shareImage='" + shareImage + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareMiniAppId='" + shareMiniAppId + '\'' +
                ", shareMiniPage='" + shareMiniPage + '\'' +
                '}';
    }
}
