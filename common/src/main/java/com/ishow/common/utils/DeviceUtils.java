package com.ishow.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by yuhaiyang on 2017/5/10.
 * 设备相关的信息
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    /**
     * 平台
     */
    public static String platform() {
        return "Android";
    }

    /**
     * 获取手机型号
     */
    public static String model() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机的Android版本
     */
    public static String version() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机的deviceId
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String deviceId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = StringUtils.EMPTY;
        try {
            id = manager.getDeviceId();
        } catch (Exception e) {
            Log.i(TAG, "deviceId: e = " + e.toString());
        }
        if (!TextUtils.isEmpty(id)) {
            return id;
        }
        
        try {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.i(TAG, "deviceId: e =" + e.toString());
        }
        return id;
    }

    /**
     * 获取屏幕的宽和高
     */
    public static int[] getScreenSize() {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Activity context) {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 如果 当前activity 是非sensor默认则进行横屏
     */
    public static void setLandscape(Context context) {
        setLandscape(context, false);
    }

    /**
     * 如果 当前activity 是非sensor默认则进行横屏
     */
    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    public static void setLandscape(Context context, boolean force) {
        if (context == null || !(context instanceof Activity)) {
            Log.i(TAG, "setLandscape: context is null or context is not activity");
            return;
        }
        Activity activity = (Activity) context;
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR && !force) {
            return;
        }

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    /**
     * 如果 当前activity 是非sensor默认则进行竖屏
     */
    public static void setPortrait(Context context) {
        setPortrait(context, false);
    }

    /**
     * 如果 当前activity 是非sensor默认则进行竖屏
     */
    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    public static void setPortrait(Context context, boolean force) {
        if (context == null || !(context instanceof Activity)) {
            Log.i(TAG, "setPortrait: context is null or context is not activity");
            return;
        }
        Activity activity = (Activity) context;
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR && !force) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }

    /**
     * 如果 当前activity 是非sensor默认则进行竖屏
     */
    public static void setSensor(Context context) {
        if (context == null || !(context instanceof Activity)) {
            Log.i(TAG, "setPortrait: context is null or context is not activity");
            return;
        }
        Activity activity = (Activity) context;

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    /**
     * 是否是横屏
     */
    public static boolean isLandscape(Context context) {
        if (context == null) {
            Log.i(TAG, "isPortrait: context is null ");
            return false;
        }
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 是否是竖屏
     */
    public static boolean isPortrait(Context context) {
        if (context == null) {
            Log.i(TAG, "isPortrait: context is null ");
            return false;
        }
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

}
