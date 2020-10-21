package com.ishow.noah.utils

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


/**
 * Created by yuhaiyang on 2020/10/15.
 * App 的生命周期检测
 */
class AppLifeCycleChecker : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackground() {
        // 应用进入后台
        Log.e("yhy", "LifecycleChecker onAppBackground ON_STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForeground() {
        // 应用进入前台
        Log.e("yhy", "LifecycleChecker onAppForeground ON_START")
    }
}