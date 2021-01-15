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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.content.ContextCompat

import com.ishow.common.R
import com.ishow.common.extensions.dp2px


class LoadingWebView : WebView {
    private lateinit var mLoadingWebChromeClient: LoadingWebChromeClient
    private var mLoadingPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var mProgress: Int = 0
    private var mLoadingHeight: Int = 0


    private val defaultLoadingColor: Int
        get() = ContextCompat.getColor(context, R.color.color_primary)

    private val defaultLoadingHeight: Int
        get() = 3.dp2px()

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
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoadingWebView)
        val color = a.getColor(R.styleable.LoadingWebView_strokeColor, defaultLoadingColor)
        mLoadingHeight = a.getColor(R.styleable.LoadingWebView_strokeHeight, defaultLoadingHeight)
        a.recycle()

        mLoadingWebChromeClient = LoadingWebChromeClient(this)
        super.setWebChromeClient(mLoadingWebChromeClient)

        mLoadingPaint.color = color
    }

    override fun setWebChromeClient(client: WebChromeClient) {
        mLoadingWebChromeClient.setRealWebChromeClient(client)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLoading(canvas)
    }

    /**
     * 根据进度来画一个进度条
     */
    private fun drawLoading(canvas: Canvas) {
        if (mProgress < 100) {
            val right = mProgress * width / 100
            canvas.drawRect(0f, scrollY.toFloat(), right.toFloat(), (scrollY + mLoadingHeight).toFloat(), mLoadingPaint)
        }
    }

    /**
     * 需要主动调用
     * [android.webkit.WebChromeClient.onProgressChanged]
     */
    fun updateLoading(progress: Int) {
        mProgress = progress
        postInvalidate(0, scrollY, width, mLoadingHeight + scrollY)
    }
}
