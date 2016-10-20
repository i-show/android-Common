package com.brightyu.androidcommon.modules.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bright.common.app.BaseActivity;
import com.brightyu.androidcommon.AppApplication;
import com.brightyu.androidcommon.modules.init.SplashActivity;


/**
 * 备用
 */
public abstract class AppBaseActivity extends BaseActivity {
    private static final String TAG = "AppBaseActivity";
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetStatusBar();
    }

    protected void resetStatusBar() {
        // TODO
    }

    protected AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * 是否需要显示升级dialog
     */
    protected boolean needShowUpdateVersionDialog() {
        return true;
    }

    @Override
    protected void goSplash() {
        Intent intent = new Intent(AppBaseActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
