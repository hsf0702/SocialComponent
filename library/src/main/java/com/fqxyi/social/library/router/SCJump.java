package com.fqxyi.social.library.router;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fqxyi.social.library.dialog.SocialTypeBean;
import com.fqxyi.social.library.share.ShareDataBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity路由跳转工具类
 */
public class SCJump {

    public static void goToShare(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans, ShareDataBean shareDataBean) {
        Intent intent = SCIntent.getIntent(ActivityType.SHARE);
        intent.putExtra("SocialTypeBean", socialTypeBeans);
        intent.putExtra("shareDataBean", shareDataBean);
        activity.startActivity(intent);
    }

    public static void goToAuth(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans) {
        Intent intent = SCIntent.getIntent(ActivityType.AUTH);
        intent.putExtra("SocialTypeBean", socialTypeBeans);
        activity.startActivity(intent);
    }

}
