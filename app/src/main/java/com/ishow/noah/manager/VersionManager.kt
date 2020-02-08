/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.manager

import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.util.Log
import com.alibaba.fastjson.JSON
import com.ishow.common.utils.AppUtils
import com.ishow.common.utils.SpanUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.log.LogUtils
import com.ishow.noah.BuildConfig
import com.ishow.noah.entries.Version

/**
 * Created by yuhaiyang on 2018/8/8.
 * 版本管理器
 */
class VersionManager private constructor() {
    /**
     * 从服务器中获取到的版本信息
     */
    private var version: Version? = null


    fun hasNewVersion(): Boolean {
        SpanUtils.Builder()
            .body("Hello")
            .add(RelativeSizeSpan(1.0F), "111")

        val ignore = StorageUtils.get(Version.Key.IGNORE_NOW, false)

        if (ignore) {
            Log.i(TAG, "hasNewVersion: already ignore")
            return false
        }

        // 获取当前版本号
        val version = getVersion()
        if (version == null) {
            Log.i(TAG, "hasNewVersion: version is null")
            return false
        }

        // 如果当前要升级的版本 比忽略的版本还要低就不进行升级
        val ignoreVersion = getIgnoreVersion()
        if (ignoreVersion != null && ignoreVersion.versionCode >= version.versionCode) {
            Log.i(TAG, "hasNewVersion: ignore this version")
            return false
        }

        val versionCode = BuildConfig.VERSION_CODE
        return version.versionCode > versionCode
    }

    fun getVersion(): Version? {
        if (version == null) {
            val cache = StorageUtils.get(Version.Key.CACHE)
            makeVersion(cache)
        }
        return version
    }


    private fun getIgnoreVersion(): Version? {
        val cache = StorageUtils.get(Version.Key.IGNORE_VERSION)
        return if (TextUtils.isEmpty(cache)) {
            null
        } else {
            JSON.parseObject(cache, Version::class.java)
        }
    }

    private fun makeVersion(versionJson: String?) {
        if (TextUtils.isEmpty(versionJson)) {
            LogUtils.i(TAG, "makeVersion: version is empty")
            return
        }
        version = JSON.parseObject(versionJson, Version::class.java)
    }

    /**
     * 获取版本信息
     */
    private fun getVersionFromServer() {
        val version = Version()
        version.versionCode = BuildConfig.VERSION_CODE
        version.versionName = BuildConfig.VERSION_NAME
        /**
        val cache = StorageUtils.with(context)
        .key(Version.Key.CACHE)
        .get()

        if (!TextUtils.equals(cache, result)) {
        StorageUtils.with(context)
        .key(Version.Key.IGNORE_VERSION)
        .remove()
        }

        StorageUtils.with(context)
        .param(Version.Key.CACHE, result)
        .save()
        makeVersion(result)
         */
    }

    private fun cleanCache() {
        if (!isFirstEnterThisVersion) {
            return
        }
        CacheManager.instance.clearCache()
    }

    companion object {
        private const val TAG = "VersionManager"
        /**
         * 这个东西使用后可以被回收
         */
        @Volatile
        private var sInstance: VersionManager? = null
        /**
         * 是否是第一次进入当前版本
         */
        var isFirstEnterThisVersion: Boolean = false
            private set

        val instance: VersionManager
            get() {

                if (sInstance == null) {
                    synchronized(VersionManager::class.java) {
                        if (sInstance == null) {
                            sInstance = VersionManager()
                        }
                    }
                }

                return sInstance!!
            }

        fun init() {
            val manager: VersionManager = instance
            clear()
            isFirstEnterThisVersion = checkIsFirstEnterThisVersion()
            manager.getVersionFromServer()
            manager.cleanCache()
        }

        /**
         * 检测是否是第一次登录这个版本
         */
        private fun checkIsFirstEnterThisVersion(): Boolean {
            // 获取之前保存的版本信息
            val versionCode = StorageUtils.get(AppUtils.VERSION_CODE, 0)
            val versionName = StorageUtils.get(AppUtils.VERSION_NAME)

            // 获取当前版本号
            val currentCode = BuildConfig.VERSION_CODE
            val currentName = BuildConfig.VERSION_NAME

            // 保存现在的版本号
            StorageUtils.save(AppUtils.VERSION_CODE, currentCode)
            StorageUtils.save(AppUtils.VERSION_NAME, currentName)

            // 如果当前版本比保存的版本大，说明APP更新了
            // 版本名称不相等且版本code比上一个版本大 才进行走ViewPager
            return !TextUtils.equals(versionName, currentName) && currentCode > versionCode
        }

        /**
         * 清除缓存
         */
        private fun clear() {
            StorageUtils.remove(Version.Key.CACHE)
            StorageUtils.remove(Version.Key.IGNORE_NOW)
        }
    }
}
