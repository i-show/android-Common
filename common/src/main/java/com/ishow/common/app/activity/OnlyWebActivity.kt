/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.app.activity

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.annotation.RequiresApi
import com.ishow.common.R
import com.ishow.common.extensions.fullWindow
import com.ishow.common.utils.WebViewUtils
import com.ishow.common.widget.webview.WebViewClientWrapper
import kotlinx.android.synthetic.main.activity_base_only_web.*


/**
 * 只有一个Webview的 Activity
 * Created by yuhaiyang on 2016/8/9.
 */
open class OnlyWebActivity : BaseActivity() {
    private var title: String? = null
    private var url: String? = null
    private var isError: Boolean = false

    private var webView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        val intent = intent
        title = intent.getStringExtra(KEY_TITLE)
        url = intent.getStringExtra(KEY_CONTENT)
    }

    override fun initViews() {
        super.initViews()
        topBar.setOnTopBarListener(this)
        topBar.setText(title)
        webView = findViewById(R.id.webView)
        initWebView()
        webView?.webViewClient = getWebViewClient()
        webView?.loadUrl(url)
        webView?.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    /**
     * 获取当前的Layout
     */
    open fun getLayout() = R.layout.activity_base_only_web

    /**
     *
     */
    open fun getWebViewClient(): WebViewClient {
        return WebClient()
    }

    /**
     * 初始化
     */
    open fun initWebView() {
        WebViewUtils.init(webView)
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun resetStatusBar() {
        super.resetStatusBar()
        fullWindow()
    }

    open fun showError() {

    }

    fun retry() {
        webView?.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView?.canGoBack() == true && !isError) {
            webView?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val KEY_TITLE = "key_title"
        const val KEY_CONTENT = "key_content"
    }


    private inner class WebClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            if (request == null || !request.isForMainFrame) {
                return
            }
            isError = true
            showError()
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            if (request == null || !request.isForMainFrame) {
                return
            }
            isError = true
            showError()
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
            handler?.proceed()
        }
    }
}
