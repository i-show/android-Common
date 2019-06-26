package com.ishow.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Rect
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log

import com.ishow.common.utils.permission.PermissionManager

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
     * 平台
     */
    fun platform(): String {
        return "Android"
    }

    /**
     * 获取手机型号
     */
    fun model(): String {
        return android.os.Build.MODEL
    }

    /**
     * 获取手机的Android版本
     */
    fun version(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * 获取手机的deviceId
     */
    @SuppressLint("HardwareIds", "MissingPermission")
    fun deviceId(context: Context): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var id = StringUtils.EMPTY
        try {
            id = manager.deviceId
        } catch (e: Exception) {
            Log.i(TAG, "deviceId: e = $e")
        }

        if (!TextUtils.isEmpty(id)) {
            return id
        }

        try {
            id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            Log.i(TAG, "deviceId: e =$e")
        }

        return id
    }

    fun getOperator(context: Context): String {
        var operator = ""
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (!PermissionManager.hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return StringUtils.EMPTY
        }
        @SuppressLint("MissingPermission")
        val imsi = telephonyManager.subscriberId ?: return StringUtils.EMPTY
        if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
            operator = "中国移动"
        } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
            operator = "中国联通"
        } else if (imsi.startsWith("46003")) {
            operator = "中国电信"
        }
        return operator

    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Activity): Int {
        val rect = Rect()
        context.window.decorView.getWindowVisibleDisplayFrame(rect)
        return rect.top
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
