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
import android.os.Bundle
import android.webkit.*
import com.ishow.common.R
import com.ishow.common.utils.WebViewUtils
import com.ishow.common.widget.TopBar
import kotlinx.android.synthetic.main.activity_base_only_web.*


/**
 * 只有一个Webview的 Activity
 * Created by yuhaiyang on 2016/8/9.
 */
class OnlyWebActivity : BaseActivity() {

    private var mTitleString: String? = null
    private var mUrl: String? = null
    private var mOnErrorUrl: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_only_web)
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        val intent = intent
        mTitleString = intent.getStringExtra(KEY_TITLE)
        mUrl = intent.getStringExtra(KEY_CONTENT)
    }

    override fun initViews() {
        super.initViews()
        val topBar = findViewById<TopBar>(R.id.top_bar)
        topBar.setOnTopBarListener(this)
        topBar.setText(mTitleString)

        WebViewUtils.init(web)
        //载入js
        web.loadUrl(mUrl)
        //载入js
        web.webViewClient = WebClient()

        web.setDownloadListener { url, _, _, _, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        web.onResume()
    }

    override fun onPause() {
        super.onPause()
        web.onPause()
    }

    private inner class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            if (request == null || !request.isForMainFrame) {
                return
            }
            mOnErrorUrl = true
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)

            if (request == null || !request.isForMainFrame) {
                return
            }

            mOnErrorUrl = true
        }
    }

    override fun onBackPressed() {
        if (web != null && web.canGoBack() && !mOnErrorUrl) {
            web.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val KEY_TITLE = "key_title"
        const val KEY_CONTENT = "key_content"
    }
}
