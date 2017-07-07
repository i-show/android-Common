package com.ishow.noahark.modules.base;

import android.os.Bundle;
import android.os.Handler;

import com.ishow.common.app.activity.BaseActivity;
import com.ishow.noahark.AppApplication;

import butterknife.ButterKnife;


/**
 * 备用
 */
public abstract class AppBaseActivity extends BaseActivity {
    private static final String TAG = "AppBaseActivity";
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
        ButterKnife.bind(this);
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


}
