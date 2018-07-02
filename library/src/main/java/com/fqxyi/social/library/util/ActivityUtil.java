package com.fqxyi.social.library.util;

import android.app.Activity;

/**
 * Activity辅助工具类
 */
public class ActivityUtil {

    public static void finish(Activity activity, boolean needFinishActivity) {
        if (!needFinishActivity) {
            return;
        }
        if (activity != null) {
            activity.finish();
        }
    }

}
