/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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

package com.bright.common.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 获取android的一些相关信息
 */
public class Android {

    /**
     * 获取手机型号
     */
    public static String modom() {
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
    public static String deviceId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = manager.getDeviceId();
        if (TextUtils.isEmpty(deviceid)) {
            return "000000000";
        }
        return deviceid;
    }
}
