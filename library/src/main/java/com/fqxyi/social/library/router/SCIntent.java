package com.fqxyi.social.library.router;

import android.content.Intent;
import android.net.Uri;

/**
 * 获取跳转到目标Activity的Intent
 */
public class SCIntent {

    static final String SCHEME = "socialcompenent://";
    static final String HOST = "page.sh/";

    public static Intent getIntent(ActivityType activityType) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(activityType.toString())));
    }

    static String getUrl(String type) {
        StringBuilder builder = new StringBuilder(SCHEME);
        builder.append(HOST);
        builder.append(type);
        return builder.toString();
    }

}
