package com.fqxyi.library;

public class ShareBean {

    /***
     * 0.微信 1.朋友圈 2.短信 3.复制 4.刷新 5.自定义 6 QQ 7 微博 8 微信小程序 9 支付宝小程序
     */
    public int type;
    /***
     * url地址,自定义有用
     */
    public String shareIcon;
    /***
     * 显示文字,自定义有用
     */
    public String shareName;

    public ShareBean() {

    }

    public ShareBean(int type) {
        this.type = type;
    }

}
