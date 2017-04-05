/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.ishow.common.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.ishow.common.R;
import com.ishow.common.widget.YToast;

/**
 * Intent 跳转工具类
 */
public class IntentUtils {

    /**
     * 跳转拨号盘界面
     */
    public static void goToDialpad(Context context, String number) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number.trim()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            YToast.makeText(context, R.string.exception_intent_open, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 跳转 市场进行评分
     */
    public static void goMarket(Context context) {
        goMarket(context, context.getPackageName());
    }

    /**
     * 跳转 市场进行评分
     */
    public static void goMarket(Context context, String packageName) {

        if (TextUtils.isEmpty(packageName)) {
            return;
        }

        try {
            Uri uri = Uri.parse("market://details?id=" + packageName.trim());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            YToast.makeText(context, R.string.exception_intent_open, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转浏览器
     */
    public static void goToBrowser(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.trim()));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            YToast.makeText(context, R.string.exception_intent_open, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开某一个app
     */
    public static void openApp(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName.trim());
            context.startActivity(intent);
        } catch (Exception e) {
            YToast.makeText(context, R.string.exception_intent_open, Toast.LENGTH_SHORT).show();
        }
    }

    public static void goToAppSettings(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception e) {
            YToast.makeText(context, R.string.exception_intent_open, Toast.LENGTH_SHORT).show();
        }
    }
}
