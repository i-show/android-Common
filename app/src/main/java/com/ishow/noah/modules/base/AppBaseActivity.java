package com.ishow.noah.modules.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.baidu.mobstat.StatService;
import com.ishow.common.app.activity.BaseActivity;
import com.ishow.common.widget.watermark.WaterMarkView;
import com.ishow.noah.AppApplication;
import com.ishow.noah.manager.VersionManager;
import com.ishow.noah.ui.widget.dialog.VersionDialog;


/**
 * 备用
 */
public abstract class AppBaseActivity extends BaseActivity {
    private static final String TAG = "AppBaseActivity";

    /**
     * 检测升级的Dialog
     */
    private Dialog mVersionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
    }

    @Override
    protected void initViews() {
        super.initViews();
        WaterMarkView.attachToActivity(this);
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        WaterMarkView.attachToActivity(this);
    }

    @Override
    protected void initViews(View view, ViewGroup.LayoutParams params) {
        super.initViews(view, params);
        WaterMarkView.attachToActivity(this);
    }

    protected AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 百度统计
        StatService.onResume(this);

        if (needShowUpdateVersionDialog() && VersionManager.getInstance().hasNewVersion(this)) {
            showVersionDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 百度统计
        StatService.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissVersionDialog();
    }

    /**
     * 是否需要显示升级dialog
     */
    protected boolean needShowUpdateVersionDialog() {
        return true;
    }

    /**
     * 显示升级Dialog
     */
    private void showVersionDialog() {
        if (mVersionDialog == null) {
            mVersionDialog = new VersionDialog(this);
        }

        if (!mVersionDialog.isShowing()) {
            mVersionDialog.show();
        }
    }

    /**
     * 隐藏升级的Dialog
     */
    private void dismissVersionDialog() {
        if (mVersionDialog != null && mVersionDialog.isShowing()) {
            mVersionDialog.dismiss();
        }
        mVersionDialog = null;
    }


}
