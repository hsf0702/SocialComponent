package com.fqxyi.social.library.util;

import com.fqxyi.social.library.auth.AuthHelper;
import com.fqxyi.social.library.share.ShareHelper;

/**
 * 基本信息初始化工具类
 */
public class SocialUtil {

    private ShareHelper shareHelper;
    private AuthHelper authHelper;

    //单例引用
    private volatile static SocialUtil INSTANCE;
    //构造函数私有化
    private SocialUtil() {
        shareHelper = new ShareHelper();
        authHelper = new AuthHelper();
    }
    //获取单例
    public static SocialUtil get() {
        if (INSTANCE == null) {
            synchronized (SocialUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SocialUtil();
                }
            }
        }
        return INSTANCE;
    }

    public ShareHelper getShareHelper() {
        return shareHelper;
    }

    public AuthHelper getAuthHelper() {
        return authHelper;
    }

    private String qqAppId;

    private String wxAppId;
    private String wxAppSecret;

    private String wbAppId;
    private String wbRedirectUrl;

    public String getQqAppId() {
        return qqAppId;
    }

    public SocialUtil setQqAppId(String qqAppId) {
        this.qqAppId = qqAppId;
        return this;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public SocialUtil setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
        return this;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public SocialUtil setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
        return this;
    }

    public String getWbAppId() {
        return wbAppId;
    }

    public SocialUtil setWbAppId(String wbAppId) {
        this.wbAppId = wbAppId;
        return this;
    }

    public String getWbRedirectUrl() {
        return wbRedirectUrl;
    }

    public SocialUtil setWbRedirectUrl(String wbRedirectUrl) {
        this.wbRedirectUrl = wbRedirectUrl;
        return this;
    }

}
