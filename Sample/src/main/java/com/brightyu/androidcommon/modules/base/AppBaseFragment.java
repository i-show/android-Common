package com.brightyu.androidcommon.modules.base;

import android.os.Handler;

import com.bright.common.app.BaseFragment;
import com.bright.common.utils.http.okhttp.OkHttpUtils;

/**
 * 基础Fragment
 * 备用：
 */
public class AppBaseFragment extends BaseFragment {
    private static final String TAG = "AppBaseFragment";
    protected Handler mHandler;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
