package com.fqxyi.sharekit.util;

import com.fqxyi.library.ShareHelper;

/**
 * Created by arvinljw on 17/11/27 17:33
 * Function：
 * Desc：
 */
public class SocialUtil {
    private static SocialUtil sInstance = new SocialUtil();

    private ShareHelper shareHelper;

    private SocialUtil() {
        shareHelper = new ShareHelper.Builder()
                .setQqAppId("1104746610")
                .setWxAppId("wx49f25cc1d77c9dd3")
                .setWxAppSecret("aef574b855dbb54b736eab080a5a2ada")
                .setWbAppId("2291580382")
                .setWbRedirectUrl("http://www.mamahao.com/")
                .build();
    }

    public static SocialUtil getInstance() {
        return sInstance;
    }

    public ShareHelper shareHelper() {
        return shareHelper;
    }
}
