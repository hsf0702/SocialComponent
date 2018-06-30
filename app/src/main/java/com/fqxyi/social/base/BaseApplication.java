package com.fqxyi.social.base;

import android.app.Application;

import com.fqxyi.social.library.util.SocialUtil;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据
        SocialUtil.get().setQqAppId("1107001192")
                .setWxAppId("")
                .setWxAppSecret("")
                .setWbAppId("2474483531")
                .setWbRedirectUrl("https://www.fqxyi.com/");
    }

}
