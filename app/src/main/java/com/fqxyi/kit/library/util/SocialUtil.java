package com.fqxyi.kit.library.util;

import com.fqxyi.kit.library.share.ShareHelper;

public class SocialUtil {
    private static SocialUtil sInstance = new SocialUtil();

    private ShareHelper shareHelper;

    private SocialUtil() {
        shareHelper = new ShareHelper.Builder()
                .setQqAppId("1107001192")
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
