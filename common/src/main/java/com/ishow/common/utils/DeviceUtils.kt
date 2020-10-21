package com.ishow.common.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.ishow.common.app.provider.InitProvider
import com.ishow.common.extensions.telephonyManager

/**
 * Created by yuhaiyang on 2017/5/10.
 * 设备相关的信息
 */
object DeviceUtils {
    private val TAG = "DeviceUtils"

    /**
     * 获取屏幕的宽和高
     */
    val screenSize: IntArray
        get() {
            val displayMetrics = Resources.getSystem().displayMetrics
            return intArrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }

    /**
     * 平台信息
     */
    const val platform: String = "Android"

    /**
     * 获取手机型号
     */
    val model: String = android.os.Build.MODEL

    /**
     * 获取手机的Android版本
     */
    val version: String = android.os.Build.VERSION.RELEASE

    val hasSim: Boolean
        get() = when (InitProvider.app.telephonyManager.simState) {
            TelephonyManager.SIM_STATE_ABSENT,
            TelephonyManager.SIM_STATE_UNKNOWN -> false
            else -> true
        }

    /**
     * 获取手机的deviceId
     */
    @Suppress("DEPRECATION")
    fun deviceId(context: Context): String {

        var id: String = StringUtils.EMPTY
        try {
            id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            Log.i(TAG, "deviceId: e =$e")
        }

        return id
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 如果 当前activity 是非sensor默认则进行横屏
     */
    fun setLandscape(context: Context) {
        setLandscape(context, false)
    }

    /**
     * 如果 当前activity 是非sensor默认则进行横屏
     */
    fun setLandscape(context: Context?, force: Boolean) {
        if (context == null || context !is Activity) {
            Log.i(TAG, "setLandscape: context is null or context is not activity")
            return
        }
        val activity = context as Activity?
        if (activity!!.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR && !force) {
            return
        }

        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    /**
     * 如果 当前activity 是非sensor默认则进行竖屏
     */
    fun setPortrait(context: Context) {
        setPortrait(context, false)
    }

    /**
     * 如果 当前activity 是非sensor默认则进行竖屏
     */
    fun setPortrait(context: Context?, force: Boolean) {
        if (context == null || context !is Activity) {
            Log.i(TAG, "setPortrait: context is null or context is not activity")
            return
        }
        val activity = context as Activity?
        if (activity!!.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR && !force) {
            return
        }
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }

    /**
     * 如果 当前activity 是非sensor默认则进行竖屏
     */
    fun setSensor(context: Context?) {
        if (context == null || context !is Activity) {
            Log.i(TAG, "setPortrait: context is null or context is not activity")
            return
        }
        val activity = context as Activity?

        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    /**
     * 是否是横屏
     */
    fun isLandscape(context: Context?): Boolean {
        if (context == null) {
            Log.i(TAG, "isPortrait: context is null ")
            return false
        }
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 是否是竖屏
     */
    fun isPortrait(context: Context?): Boolean {
        if (context == null) {
            Log.i(TAG, "isPortrait: context is null ")
            return false
        }
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

}
