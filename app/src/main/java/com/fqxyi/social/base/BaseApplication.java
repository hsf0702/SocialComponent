package com.fqxyi.social.base;

import android.app.Application;

import com.fqxyi.social.library.SocialHelper;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据
        SocialHelper.get().setQqAppId("1107009250")
                .setWxAppId("wx2847b18acb41e535")
                .setWxAppSecret("78f713b76c61a38242e63ccdb3a96d68")
                .setWbAppId("2214687859")
                .setWbRedirectUrl("https://github.com/fengqingxiuyi");
    }

}
