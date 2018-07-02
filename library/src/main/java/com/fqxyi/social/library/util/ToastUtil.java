package com.fqxyi.social.library.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类，统一使用，如需接入自定义的toast，可在此处替换
 */
public class ToastUtil {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
