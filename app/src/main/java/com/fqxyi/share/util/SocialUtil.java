package com.fqxyi.share.util;

import com.fqxyi.share.library.ShareHelper;

public class SocialUtil {
    private static SocialUtil sInstance = new SocialUtil();

    private ShareHelper shareHelper;

    private SocialUtil() {
        shareHelper = new ShareHelper.Builder()
                .setQqAppId("1105607794")
                .setWxAppId("")
                .setWxAppSecret("")
                .setWbAppId("2474483531")
                .setWbRedirectUrl("https://www.fqxyi.com/")
                .build();
    }

    public static SocialUtil getInstance() {
        return sInstance;
    }

    public ShareHelper shareHelper() {
        return shareHelper;
    }
}
