package com.fqxyi.social.library.pay;

import android.app.Activity; /**
 * 支付宝支付帮助类
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
