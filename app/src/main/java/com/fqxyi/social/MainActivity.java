package com.fqxyi.social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 分享
     */
    public void jump2share(View view) {
        startActivity(new Intent(this, ShareActivity.class));
    }

    /**
     * 登录
     */
    public void jump2login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}
