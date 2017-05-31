/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtils {
    private static final String TAG = "AppUtils";
    /**
     * 缓存的versioncode 的key值
     */
    public final static String VERSION_CODE = "cache_saved_version_code";
    /**
     * 缓存的versionname 的key值
     */
    public final static String VERSION_NAME = "cache_saved_version_name";

    /**
     * 获取版本code
     */
    public static int getVersionCode(Context context) {
        return getVersionCode(context, context.getPackageName());
    }

    /**
     * 获取版本code
     */
    @SuppressWarnings("WeakerAccess")
    public static int getVersionCode(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    /**
     * 获取版本名称
     */
    @SuppressWarnings("WeakerAccess")
    public static String getVersionName(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {

        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 获取当前用户安装的App
     */
    @SuppressWarnings("unused")
    public static Map<String, String> getUserApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        Map<String, String> apps = new HashMap<>();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) && !TextUtils.equals(packageInfo.packageName, context.getPackageName())) {
                apps.put(packageInfo.packageName, packageInfo.versionName);
                Log.i(TAG, "appName = " + packageInfo.applicationInfo.loadLabel(packageManager).toString());
            }
        }
        return apps;
    }
}
