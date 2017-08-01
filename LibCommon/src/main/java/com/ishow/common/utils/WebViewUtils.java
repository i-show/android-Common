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
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewUtils {
    private static final String DB_CACHE_PATH = "webview";

    public static void init(Context context, WebView webview) {
        init(context, webview, true);
    }

    public static void init(Context context, WebView webview, boolean fitWindow) {
        final String databasePath = context.getDatabasePath(DB_CACHE_PATH).getPath();
        final String cachePath = context.getExternalCacheDir().getPath();

        WebSettings settings = webview.getSettings();
        // 自适应屏幕大小
        settings.setUseWideViewPort(fitWindow);
        settings.setLoadWithOverviewMode(fitWindow);

        // WebView 可以加载JavaScript
        settings.setJavaScriptEnabled(true);
        //支持js
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置编码
        settings.setDefaultTextEncodingName("utf-8");

        // 提高加载WebView的优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // 放大缩小
        settings.setDomStorageEnabled(fitWindow);

        // WebView 可以使用数据库
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(databasePath);

        // 应用可以有缓存
        //settings.setAppCacheEnabled(true);
        //settings.setAppCachePath(cachePath);

        // 设置可以支持缩放
        settings.setSupportZoom(fitWindow);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(fitWindow);
        // 为图片添加放大缩小功能
        settings.setUseWideViewPort(fitWindow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }
}
