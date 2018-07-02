package com.fqxyi.social.library.share;

import java.io.Serializable;
import java.util.List;

/**
 * 分享数据 数据结构
 */
public class ShareDataBean implements Serializable {

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

    public ShareDataBean() {
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
