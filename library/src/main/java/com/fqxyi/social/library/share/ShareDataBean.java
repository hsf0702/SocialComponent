package com.fqxyi.social.library.share;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

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
    //分享图片（单张）（本地图片或网络图片）
    public String shareImage;
    //分享图片（多张）（本地图片）- 微博
    public List<String> shareImageList;
    //分享地址
    public String shareUrl;
    //分享音乐地址 - QQ
    public String shareMusicUrl;
    //分享视频地址 - 微博
    public String shareVideoUrl;
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
