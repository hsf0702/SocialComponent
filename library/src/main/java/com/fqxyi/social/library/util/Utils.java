package com.fqxyi.social.library.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * 简单的包装一下工具类，一个方法创建一个类，没必要
 */
public class Utils {

    //两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 防止多次点击
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * finishActivity
     */
    public static void finish(Activity activity, boolean needFinishActivity) {
        if (!needFinishActivity) {
            return;
        }
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 提示统一使用，如需接入自定义的toast，可在此处替换
     */
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
