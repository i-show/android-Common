/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.utils.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具
 */
public class HttpUtils {
    /**
     * 判断是否具有网络连接
     */
    public static boolean hasNetWorkConection(Context context) {
        if (context == null) {
            return false;
        }
        // 获取连接活动管理器
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取连接的网络信息
        final NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isAvailable());
    }

    /**
     * 判断是否具有wifi连接
     */
    public static boolean hasWifiConnection(Context context) {
        // 获取连接活动管理器
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 判断是否有GPRS连接
     */
    public static boolean hasGPRSConnection(Context context) {
        // 获取连接活动管理器
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (networkInfo != null && networkInfo.isAvailable());
    }

    /**
     * 判断网络连接类型
     */
    public static int getNetworkConnectionType(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiNetWorkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileNetWorkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifiNetWorkInfo != null && wifiNetWorkInfo.isAvailable()) {
            return ConnectivityManager.TYPE_WIFI;
        } else if (mobileNetWorkInfo != null && mobileNetWorkInfo.isAvailable()) {
            return ConnectivityManager.TYPE_MOBILE;
        } else {
            return -1;
        }
    }
}
