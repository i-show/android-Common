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

package com.ishow.common.widget.webview.loading.x5

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.ishow.common.R
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView


class X5LoadingWebView : WebView {
    private lateinit var loadingWebChromeClient: X5LoadingWebChromeClient
    private var loadingPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var loadingProgress: Int = 0
    private var loadingHeight: Int = 0


    private val defaultLoadingColor: Int
        get() = ContextCompat.getColor(context, R.color.color_primary)

    private val defaultLoadingHeight: Int
        get() = context.resources.getDimensionPixelOffset(R.dimen.dp_3)

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.X5LoadingWebView)
        val color = a.getColor(R.styleable.X5LoadingWebView_strokeColor, defaultLoadingColor)
        loadingHeight = a.getColor(R.styleable.X5LoadingWebView_strokeHeight, defaultLoadingHeight)
        a.recycle()

        loadingWebChromeClient = X5LoadingWebChromeClient(this)
        super.setWebChromeClient(loadingWebChromeClient)

        loadingPaint.color = color
    }

    override fun setWebChromeClient(client: WebChromeClient) {
        loadingWebChromeClient.setRealWebChromeClient(client)
    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawLoading(canvas)
    }

    /**
     * 根据进度来画一个进度条
     */
    private fun drawLoading(canvas: Canvas) {
        if (loadingProgress < 100) {
            val right = loadingProgress * width / 100
            canvas.drawRect(0F, 0F, right.toFloat(), loadingHeight.toFloat(), loadingPaint)
        }
    }

    /**
     * 需要主动调用
     * [android.webkit.WebChromeClient.onProgressChanged]
     */
    fun updateLoading(progress: Int) {
        loadingProgress = progress
        postInvalidate()
    }
}
