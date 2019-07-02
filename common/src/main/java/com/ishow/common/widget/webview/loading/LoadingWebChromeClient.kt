/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.common.widget.webview.loading

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Message
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi

/**
 * Created by yuhaiyang on 2017/6/26.
 * Loading 的加载
 */

internal class LoadingWebChromeClient(private val mLoadingWebView: LoadingWebView) : WebChromeClient() {
    private var mRealWebChromeClient: WebChromeClient? = null

    fun setRealWebChromeClient(chromeClient: WebChromeClient) {
        mRealWebChromeClient = chromeClient
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        mLoadingWebView.updateLoading(newProgress)
        mRealWebChromeClient?.onProgressChanged(view, newProgress)
    }

    override fun onReceivedTitle(view: WebView, title: String) {
        super.onReceivedTitle(view, title)
        mRealWebChromeClient?.onReceivedTitle(view, title)
    }

    override fun onReceivedIcon(view: WebView, icon: Bitmap) {
        super.onReceivedIcon(view, icon)
        mRealWebChromeClient?.onReceivedIcon(view, icon)
    }

    override fun onReceivedTouchIconUrl(view: WebView, url: String, precomposed: Boolean) {
        super.onReceivedTouchIconUrl(view, url, precomposed)
        mRealWebChromeClient?.onReceivedTouchIconUrl(view, url, precomposed)
    }

    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        super.onShowCustomView(view, callback)
        mRealWebChromeClient?.onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        super.onHideCustomView()
        mRealWebChromeClient?.onHideCustomView()
    }

    override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        } else {
            super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        }
    }

    override fun onRequestFocus(view: WebView) {
        super.onRequestFocus(view)
        mRealWebChromeClient?.onRequestFocus(view)
    }

    override fun onCloseWindow(window: WebView) {
        super.onCloseWindow(window)

        mRealWebChromeClient?.onCloseWindow(window)
    }

    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.onJsAlert(view, url, message, result)
        } else {
            super.onJsAlert(view, url, message, result)
        }
    }

    override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.onJsConfirm(view, url, message, result)
        } else {
            super.onJsConfirm(view, url, message, result)
        }
    }

    override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.onJsPrompt(view, url, message, defaultValue, result)
        } else {
            super.onJsPrompt(view, url, message, defaultValue, result)
        }
    }

    override fun onJsBeforeUnload(view: WebView, url: String, message: String, result: JsResult): Boolean {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.onJsBeforeUnload(view, url, message, result)
        } else {
            super.onJsBeforeUnload(view, url, message, result)
        }
    }

    override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback)
        mRealWebChromeClient?.onGeolocationPermissionsShowPrompt(origin, callback)
    }

    override fun onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt()
        mRealWebChromeClient?.onGeolocationPermissionsHidePrompt()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onPermissionRequest(request: PermissionRequest) {
        super.onPermissionRequest(request)
        mRealWebChromeClient?.onPermissionRequest(request)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onPermissionRequestCanceled(request: PermissionRequest) {
        super.onPermissionRequestCanceled(request)
        mRealWebChromeClient?.onPermissionRequestCanceled(request)
    }


    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.onConsoleMessage(consoleMessage)
        } else {
            super.onConsoleMessage(consoleMessage)
        }
    }

    override fun getDefaultVideoPoster(): Bitmap? {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.defaultVideoPoster
        } else {
            super.getDefaultVideoPoster()
        }
    }

    override fun getVideoLoadingProgressView(): View? {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.videoLoadingProgressView
        } else {
            super.getVideoLoadingProgressView()
        }
    }

    override fun getVisitedHistory(callback: ValueCallback<Array<String>>) {
        super.getVisitedHistory(callback)
        mRealWebChromeClient?.getVisitedHistory(callback)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
        return if (mRealWebChromeClient != null) {
            mRealWebChromeClient!!.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        } else {
            super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }
    }
}
