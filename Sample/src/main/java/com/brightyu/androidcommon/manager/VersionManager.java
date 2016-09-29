/**
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

package com.brightyu.androidcommon.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bright.common.utils.PackagesUtils;
import com.bright.common.utils.SharedPreferencesUtils;

import java.lang.ref.WeakReference;

/**
 * 版本管理器
 */

public class VersionManager {
    private static final String TAG = "VersionManager";
    /**
     * 这个东西使用后可以被回收
     */
    private static WeakReference<VersionManager> sIntance;

    private Context mContext;
    private static boolean isFirstEnterThisVerison;

    private VersionManager(Context context) {
        mContext = context;
    }

    public static VersionManager getInstance(Context context) {

        if (sIntance == null || sIntance.get() == null) {
            synchronized (VersionManager.class) {
                if (sIntance == null || sIntance.get() == null) {
                    VersionManager manager = new VersionManager(context.getApplicationContext());
                    sIntance = new WeakReference<>(manager);
                }
            }
        }

        return sIntance.get();
    }

    public static void init(Context context) {
        isFirstEnterThisVerison = checkIsFirstEnterThisVerison(context.getApplicationContext());
    }

    public static boolean isFirstEnterThisVerison() {
        return isFirstEnterThisVerison;
    }

    /**
     * 检测是否是第一次登录这个版本
     */
    private static boolean checkIsFirstEnterThisVerison(Context context) {
        // 获取之前保存的版本信息
        final int versionCode = SharedPreferencesUtils.get(context, PackagesUtils.VERSION_CODE, 0);
        final String versionName = SharedPreferencesUtils.get(context, PackagesUtils.VERSION_NAME, null);
        // 获取当前版本号
        final int _versionCode = PackagesUtils.getVersionCode(context);
        final String _versionName = PackagesUtils.getVersionName(context);
        Log.d(TAG, "originVersion = " + versionCode + " ,localVersion = " + _versionCode);
        Log.d(TAG, "originVersionName = " + versionName + " ,localVersionName = " + _versionName);

        // 保存现在的版本号
        SharedPreferencesUtils.save(context, PackagesUtils.VERSION_CODE, _versionCode);
        SharedPreferencesUtils.save(context, PackagesUtils.VERSION_NAME, _versionName);

        // 如果当前版本比保存的版本大，说明APP更新了
        // 版本名称不相等且版本code比上一个版本大 才进行走ViewPager
        return (!TextUtils.equals(versionName, _versionName) && _versionCode > versionCode);
    }
}
