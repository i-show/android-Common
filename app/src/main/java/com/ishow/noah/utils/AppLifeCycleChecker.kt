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
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForeground() {
    }
}