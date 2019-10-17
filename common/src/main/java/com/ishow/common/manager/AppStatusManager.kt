package com.ishow.common.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.log.LogUtils

/**
 * Created by yuhaiyang on 2019-10-16.
 *
 */

class AppStatusManager private constructor() : Application.ActivityLifecycleCallbacks {
    private var createdCount: Int = 0
    private var startedCount: Int = 0
    private var isRegister = false
    fun registerListener(app: Application) {
        LogUtils.i(TAG, "registerListener: ")
        if (!isRegister) {
            app.registerActivityLifecycleCallbacks(this)
            isRegister = true
        }

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        LogUtils.i(TAG, "onActivityCreated: ")
        createdCount++
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        LogUtils.i(TAG, "onActivitySaveInstanceState: ")
    }

    override fun onActivityStarted(activity: Activity) {
        startedCount++
        LogUtils.i(TAG, "onActivityStarted: startedCount = $startedCount")
    }

    override fun onActivityResumed(activity: Activity) {
        LogUtils.i(TAG, "onActivityResumed: ")
    }


    override fun onActivityPaused(activity: Activity) {
        LogUtils.i(TAG, "onActivityPaused: ")
    }


    override fun onActivityStopped(activity: Activity) {
        startedCount--
        LogUtils.i(TAG, "onActivityStopped: startedCount = $startedCount")
    }

    override fun onActivityDestroyed(activity: Activity) {
        LogUtils.i(TAG, "onActivityDestroyed: ")
        createdCount--
    }


    companion object {
        private const val TAG = "AppStatusManager"
        @Volatile
        @JvmStatic
        private var sInstance: AppStatusManager? = null

        val instance: AppStatusManager
            get() = sInstance ?: synchronized(AppStatusManager::class.java) {
                sInstance ?: AppStatusManager().also { sInstance = it }
            }
    }

}