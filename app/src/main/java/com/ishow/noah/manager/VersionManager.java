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

package com.ishow.noah.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ishow.common.utils.AppUtils;
import com.ishow.common.utils.SharedPreferencesUtils;
import com.ishow.common.utils.StorageUtils;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.log.LogManager;
import com.ishow.noah.BuildConfig;
import com.ishow.noah.entries.Version;
import com.ishow.noah.modules.init.splash.SplashActivity;
import com.ishow.noah.utils.http.AppHttpCallBack;

import java.lang.ref.WeakReference;

/**
 * 版本管理器
 */
public class VersionManager {
    private static final String TAG = "VersionManager";
    /**
     * 这个东西使用后可以被回收
     */
    private volatile static WeakReference<VersionManager> sInstance;
    /**
     * 是否是第一次进入当前版本
     */
    private static boolean isFirstEnterThisVersion;
    /**
     * 从服务器中获取到的版本信息
     */
    private Version mVersion;

    private VersionManager() {
    }

    public static VersionManager getInstance() {

        if (sInstance == null || sInstance.get() == null) {
            synchronized (VersionManager.class) {
                if (sInstance == null || sInstance.get() == null) {
                    VersionManager manager = new VersionManager();
                    sInstance = new WeakReference<>(manager);
                }
            }
        }

        return sInstance.get();
    }

    public void init(SplashActivity context) {
        clear(context.getApplicationContext());
        isFirstEnterThisVersion = checkIsFirstEnterThisVerison(context.getApplicationContext());
        getVersionFromServer(context.getApplicationContext());
        cleanCache(context);
    }

    public static boolean isFirstEnterThisVersion() {
        return isFirstEnterThisVersion;
    }

    /**
     * 检测是否是第一次登录这个版本
     */
    private static boolean checkIsFirstEnterThisVerison(Context context) {
        // 获取之前保存的版本信息
        final int versionCode = StorageUtils.with(context)
                .key(AppUtils.VERSION_CODE)
                .get(0);

        final String versionName = StorageUtils.with(context)
                .key(AppUtils.VERSION_NAME)
                .get(null);

        // 获取当前版本号
        final int _versionCode = AppUtils.getVersionCode(context);
        final String _versionName = AppUtils.getVersionName(context);
        Log.d(TAG, "originVersion = " + versionCode + " ,localVersion = " + _versionCode);
        Log.d(TAG, "originVersionName = " + versionName + " ,localVersionName = " + _versionName);

        // 保存现在的版本号
        StorageUtils.with(context)
                .param(AppUtils.VERSION_CODE, _versionCode)
                .save();

        StorageUtils.with(context)
                .param(AppUtils.VERSION_NAME, _versionName)
                .save();

        // 如果当前版本比保存的版本大，说明APP更新了
        // 版本名称不相等且版本code比上一个版本大 才进行走ViewPager
        return (!TextUtils.equals(versionName, _versionName) && _versionCode > versionCode);
    }


    public boolean hasNewVersion(Context context) {
        boolean ignore = StorageUtils.with(context)
                .key(Version.Key.IGNORE_NOW)
                .get(false);

        if (ignore) {
            Log.i(TAG, "hasNewVersion: already ignore");
            return false;
        }

        // 获取当前版本号
        Version version = getVersion(context);
        if (version == null) {
            Log.i(TAG, "hasNewVersion: version is null");
            return false;
        }

        // 如果当前要升级的版本 比忽略的版本还要低就不进行升级
        Version ignoreVersion = getIgnoreVersion(context);
        if (ignoreVersion != null && ignoreVersion.versionCode >= version.versionCode) {
            Log.i(TAG, "hasNewVersion: ignore this version");
            return false;
        }

        final int _versionCode = AppUtils.getVersionCode(context);
        return version.versionCode > _versionCode;
    }

    public Version getVersion(Context context) {
        if (mVersion == null) {
            String cache = StorageUtils.with(context)
                    .key(Version.Key.CACHE)
                    .get(null);
            makeVersion(cache);
        }
        return mVersion;
    }


    private Version getIgnoreVersion(Context context) {
        String cache = StorageUtils.with(context)
                .key(Version.Key.IGNORE_VERSION)
                .get(null);
        if (TextUtils.isEmpty(cache)) {
            return null;
        } else {
            return JSON.parseObject(cache, Version.class);
        }
    }

    private void makeVersion(String versionJson) {
        if (TextUtils.isEmpty(versionJson)) {
            LogManager.i(TAG, "makeVersion: version is empty");
            return;
        }
        mVersion = JSON.parseObject(versionJson, Version.class);
    }

    /**
     * 清除缓存
     */
    private static void clear(Context context) {
        StorageUtils.with(context)
                .key(Version.Key.CACHE)
                .remove();
        StorageUtils.with(context)
                .key(Version.Key.IGNORE_NOW)
                .remove();
    }

    /**
     * 获取版本信息
     */
    private void getVersionFromServer(final Context context) {
        Version version = new Version();
        version.versionCode = BuildConfig.VERSION_CODE;
        version.versionName = BuildConfig.VERSION_NAME;

        Http.post()
                .url("http://10.0.2.55:8080/version/getVersion")
                .params(JSON.toJSONString(version))
                .execute(new AppHttpCallBack<String>(context) {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {

                    }

                    @Override
                    protected void onSuccess(String result) {
                        String cache = StorageUtils.with(context)
                                .key(Version.Key.CACHE)
                                .get(null);

                        if (!TextUtils.equals(cache, result)) {
                            StorageUtils.with(context)
                                    .key(Version.Key.IGNORE_VERSION)
                                    .remove();
                        }

                        StorageUtils.with(context)
                                .param(Version.Key.CACHE, result)
                                .save();
                        makeVersion(result);
                    }
                });
    }

    private void cleanCache(Context context) {
        if (!isFirstEnterThisVersion) {
            return;
        }
        SharedPreferencesUtils.cleanCache(context);
    }
}
