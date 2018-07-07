package com.fqxyi.social.library.pay;

import android.app.Activity;

/**
 * 支付宝支付帮助类
 *
 * 相关文档：
 * 1、开发文档 /  资源下载 /  概览 https://docs.open.alipay.com/54/cyz7do/
 * 2、开发文档 /  App支付 /  产品介绍 https://docs.open.alipay.com/204
 */
public class AlipayPayHelper {

    //上下文
    private Activity activity;

    public AlipayPayHelper() {

    }

    public void pay(Activity activity, IPayCallback payCallback, boolean needFinishActivity) {

    }

    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
    }

}
