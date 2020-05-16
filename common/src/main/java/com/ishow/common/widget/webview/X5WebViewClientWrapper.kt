package com.ishow.common.widget.webview

import android.graphics.Bitmap
import android.os.Build
import android.os.Message
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * Created by yuhaiyang on 2019-12-19.
 *
 */
@Suppress("DEPRECATION")
open class X5WebViewClientWrapper : WebViewClient() {
    var client: WebViewClient? = null

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        client?.onPageFinished(view, url)
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        return if (client == null) {
            super.shouldInterceptRequest(view, url)
        } else {
            client?.shouldInterceptRequest(view, url)
        }
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        return if (client == null) {
            super.shouldInterceptRequest(view, request)
        } else {
            client?.shouldInterceptRequest(view, request)
        }
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
        return if (client == null) {
            super.shouldOverrideKeyEvent(view, event)
        } else {
            return client!!.shouldOverrideKeyEvent(view, event)
        }
    }


    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        super.doUpdateVisitedHistory(view, url, isReload)
        client?.doUpdateVisitedHistory(view, url, isReload)
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        client?.onReceivedError(view, errorCode, description, failingUrl)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
        client?.onReceivedError(view, request, error)
    }


    override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
        super.onReceivedLoginRequest(view, realm, account, args)
        client?.onReceivedLoginRequest(view, realm, account, args)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        client?.onReceivedHttpError(view, request, errorResponse)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        client?.onPageStarted(view, url, favicon)
    }

    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        super.onScaleChanged(view, oldScale, newScale)
        client?.onScaleChanged(view, oldScale, newScale)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return if (client == null) {
            super.shouldOverrideUrlLoading(view, url)
        } else {
            client!!.shouldOverrideUrlLoading(view, url)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return if (client == null) {
            super.shouldOverrideUrlLoading(view, request)
        } else {
            client!!.shouldOverrideUrlLoading(view, request)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)
        client?.onPageCommitVisible(view, url)
    }

    override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
        super.onUnhandledKeyEvent(view, event)
        client?.onUnhandledKeyEvent(view, event)
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        super.onReceivedClientCertRequest(view, request)
        client?.onReceivedClientCertRequest(view, request)
    }

    override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm)
        client?.onReceivedHttpAuthRequest(view, handler, host, realm)
    }


    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
        client?.onReceivedSslError(view, handler, error)
    }


    override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
        super.onTooManyRedirects(view, cancelMsg, continueMsg)
        client?.onTooManyRedirects(view, cancelMsg, continueMsg)
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        super.onFormResubmission(view, dontResend, resend)
        client?.onFormResubmission(view, dontResend, resend)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        client?.onLoadResource(view, url)
    }
}