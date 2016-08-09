package com.brightyu.androidcommon.modules.base;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.bright.common.app.BaseActivity;
import com.bright.common.utils.PackagesUtils;
import com.bright.common.utils.SharedPreferencesUtils;
import com.bright.common.widget.dialog.BaseDialog;
import com.brightyu.androidcommon.BaseApplication;


/**
 * 备用
 */
public abstract class AppBaseActivity extends BaseActivity {
    private static final String TAG = "AppBaseActivity";
    protected Handler mHandler;

    private BaseDialog mWifiDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetStatusBar();
    }

    protected void resetStatusBar() {
        // TODO
    }

    protected BaseApplication getBaseApplication() {
        return (BaseApplication) getApplication();
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


    /**
     * 是不是第一次进入这个版本
     */
    protected boolean isFirstEnterThisVerison() {
        // 获取之前保存的版本信息
        final int versionCode = SharedPreferencesUtils.get(this, PackagesUtils.VERSION_CODE, 0);
        final String versionName = SharedPreferencesUtils.get(this, PackagesUtils.VERSION_NAME, null);
        // 获取当前版本号
        final int _versionCode = PackagesUtils.getVersionCode(this);
        final String _versionName = PackagesUtils.getVersionName(this);
        Log.d(TAG, "originVersion = " + versionCode + " ,localVersion = " + _versionCode);
        Log.d(TAG, "originVersionName = " + versionName + " ,localVersionName = " + _versionName);

        // 保存现在的版本号
        saveInt(PackagesUtils.VERSION_CODE, _versionCode);
        saveString(PackagesUtils.VERSION_NAME, _versionName);

        // 如果当前版本比保存的版本大，说明APP更新了
        // 版本名称不相等且版本code比上一个版本大 才进行走ViewPager
        return (!TextUtils.equals(versionName, _versionName) && _versionCode > versionCode);
    }

}
