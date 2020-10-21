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

package com.ishow.common.utils


import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import com.ishow.common.app.provider.InitProvider

import java.util.HashMap

object AppUtils {
    private const val TAG = "AppUtils"

    /**
     * 缓存的version code 的key值
     */
    const val VERSION_CODE = "cache_saved_version_code"

    /**
     * 缓存的version name 的key值
     */
    const val VERSION_NAME = "cache_saved_version_name"

    /**
     * 上一次点击按钮的时间
     */
    private var sLastClickTime: Long = 0

    /**
     * 根据两次的点击时间防止重复提交
     */
    @JvmStatic
    val isFastDoubleClick: Boolean
        get() {
            val currentTime = System.currentTimeMillis()
            val timeDiffer = currentTime - sLastClickTime
            if (timeDiffer in 1..300) {
                return true
            }
            sLastClickTime = currentTime
            return false
        }


    /**
     * 获取版本code
     */
    @JvmStatic
    fun getVersionCode(context: Context): Long {
        return getVersionCode(context, context.packageName)
    }

    /**
     * 获取版本code
     */
    @JvmStatic
    fun getVersionCode(context: Context, packageName: String): Long {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA).longVersionCode
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA).versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取版本名称
     */
    @JvmStatic
    fun getVersionName(context: Context): String {
        return getVersionName(context, context.packageName)
    }

    /**
     * 获取版本名称
     */
    @JvmStatic
    fun getVersionName(context: Context, packageName: String): String {
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA).versionName
        } catch (e: Exception) {
            e.printStackTrace()
            StringUtils.EMPTY
        }
    }

    /**
     * 获取应用程序名称
     */
    @JvmStatic
    fun getAppName(context: Context): String? {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /***
     * 获取当前用户安装的App
     */
    @JvmStatic
    fun getUserApps(context: Context): Map<String, String> {
        val packages = context.packageManager.getInstalledPackages(0)
        val apps = HashMap<String, String>()
        for (packageInfo in packages) {
            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 && !TextUtils.equals(
                    packageInfo.packageName,
                    context.packageName
                )
            ) {
                apps[packageInfo.packageName] = packageInfo.versionName
            }
        }
        return apps
    }


    /**
     * 获取有icon的app
     */
    @JvmStatic
    fun getLauncherApp(context: Context): Map<String, String> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfoList = packageManager.queryIntentActivities(intent, 0)

        val apps = HashMap<String, String>()
        for (resolveInfo in resolveInfoList) {
            val title = resolveInfo.loadLabel(packageManager) as String
            apps[resolveInfo.activityInfo.packageName] = title
        }
        return apps
    }

    fun isInstall(pkgName: String?): Boolean {
        if (pkgName == null || pkgName.isEmpty()) {
            return false
        }

        return try {
            InitProvider.app.packageManager.getPackageInfo(pkgName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false

        }
    }
}
