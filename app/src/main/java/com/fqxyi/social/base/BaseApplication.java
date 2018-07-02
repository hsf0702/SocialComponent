package com.fqxyi.social.base;

import android.app.Application;

import com.fqxyi.social.library.SocialHelper;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据
        SocialHelper.get().setQqAppId("1107009250")
                .setWxAppId("")
                .setWxAppSecret("")
                .setWbAppId("2214687859")
                .setWbRedirectUrl("https://github.com/fengqingxiuyi");
    }

}
