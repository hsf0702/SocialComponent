package com.fqxyi.social.base;

import android.app.Application;

import com.fqxyi.social.library.util.SocialUtil;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据
        SocialUtil.get().setQqAppId("1107009250")
                .setWxAppId("")
                .setWxAppSecret("")
                .setWbAppId("2214687859")
                .setWbRedirectUrl("https://github.com/fengqingxiuyi");
    }

}
