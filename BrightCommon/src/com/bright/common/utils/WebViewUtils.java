/**
 * Copyright (C) 2015 The yuhaiyang Android Source Project
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

package com.bright.common.utils;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Bright.Yu on 2015/9/6.
 */
public class WebViewUtils {
    private static final String DB_CACHE_PATH = "webview";

    public static void init(Context context, WebView webview) {
        final String databasePath = context.getDatabasePath(DB_CACHE_PATH).getPath();
        final String cachePath = context.getExternalCacheDir().getPath();

        WebSettings settings = webview.getSettings();
        // 自适应屏幕大小
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // WebView 可以加载JavaScript
        settings.setJavaScriptEnabled(true);

        // 提高加载WebView的优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // 放大缩小
        settings.setDomStorageEnabled(true);

        // WebView 可以使用数据库
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(databasePath);

        // 应用可以有缓存
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(cachePath);
    }
}
