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

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView

object WebViewUtils {
    @JvmStatic
    @JvmOverloads
    @SuppressLint("SetJavaScriptEnabled")
    fun init(webView: WebView, fitWindow: Boolean = true) {

        val settings = webView.settings
        // 自适应屏幕大小
        settings.useWideViewPort = fitWindow
        settings.loadWithOverviewMode = fitWindow

        // WebView 可以加载JavaScript
        settings.javaScriptEnabled = true
        //支持js
        settings.javaScriptCanOpenWindowsAutomatically = true
        //设置编码
        settings.defaultTextEncodingName = "utf-8"

        // 放大缩小
        // settings.domStorageEnabled = fitWindow

        // WebView 可以使用数据库
        // settings.databaseEnabled = true
        // settings.databasePath = databasePath

        // 应用可以有缓存
        // settings.setAppCacheEnabled(true);
        // settings.setAppCachePath(cachePath);

        // 设置可以支持缩放
        // settings.setSupportZoom(fitWindow)
        // 设置出现缩放工具
        // settings.builtInZoomControls = fitWindow
        // 为图片添加放大缩小功能
        // settings.useWideViewPort = fitWindow

        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }
}
