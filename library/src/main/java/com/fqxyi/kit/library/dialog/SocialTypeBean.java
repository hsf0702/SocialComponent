package com.fqxyi.kit.library.dialog;

/**
 * 社会化类型 数据结构
 */
public class SocialTypeBean {

    /***
     * 0.微信 1.朋友圈 2.短信 3.复制 4.刷新 5.自定义 6 QQ 7 微博 8 微信小程序 9 支付宝小程序
     */
    public int type;
    /***
     * url地址,自定义有用
     */
    public String socialIcon;
    /***
     * 显示文字,自定义有用
     */
    public String socialName;

    public SocialTypeBean() {

    }

    public SocialTypeBean(int type) {
        this.type = type;
    }

}
