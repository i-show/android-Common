package com.ishow.common.widget.watermark

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.ishow.common.R
import com.ishow.common.extensions.dp2px
import kotlin.math.max
import kotlin.math.min

/**
 * Created by yuhaiyang on 2018/1/23.
 * 水印的参数
 */

class WaterMarkHelp {
    private lateinit var mPaint: Paint
    private lateinit var mParams: Params
    private var mView: View? = null
    private var mTextWidth: Int = 0
    private var mTextHeight: Int = 0

    fun init(view: View, params: Params) {
        mParams = params
        mView = view

        params.alpha = min(1.0f, max(0f, params.alpha))
        params.angle = min(360, max(0, params.angle))
        params.text = if (params.text.isNullOrBlank()) sDefaultText else params.text

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mPaint.textSize = params.textSize.toFloat()
        mPaint.color = params.textColor
        mPaint.alpha = (params.alpha * 255).toInt()

        val rect = Rect()
        mPaint.getTextBounds(params.text, 0, params.text!!.length, rect)
        mTextWidth = rect.width() + params.startPadding + params.endPadding
        mTextHeight = rect.height() + params.topPadding + params.bottomPadding
    }

    fun draw(canvas: Canvas?, width: Int, height: Int) {
        if (!sShowWaterMark) {
            return
        }

        if ( canvas == null) {
            return
        }

        if (!mParams.enable) {
            return
        }

        val count = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
        canvas.rotate(mParams.angle.toFloat(), width / 2F, height / 2F)

        val startY = -height
        val endY = height + height
        val startX = -width
        val endX = width + width

        var y = startY
        while (y <= endY) {
            var x = startX
            while (x <= endX) {
                canvas.drawText(mParams.text!!, (x + mParams.startPadding).toFloat(), (y + mParams.topPadding).toFloat(), mPaint)
                x += mTextWidth
            }
            y += mTextHeight
        }

        canvas.restoreToCount(count)
    }


    fun setWaterMarkEnable(enable: Boolean) {
        mParams.enable = enable
        mView?.postInvalidate()
    }

    class Params {
        var text: String? = null
        var textSize: Int = 0
        var textColor: Int = 0
        var topPadding: Int = 0
        var bottomPadding: Int = 0
        var startPadding: Int = 0
        var endPadding: Int = 0
        var alpha: Float = 0.toFloat()
        var angle: Int = 0
        var enable: Boolean = false
    }

    companion object {
        /**
         * 释放一个全局控制的变量
         * 方便以后进行宏控制
         */
        private var sShowWaterMark = true
        private var sDefaultText = "测试版本"

        fun getDefaultTextSize(context: Context): Int {
            return context.resources.getDimensionPixelSize(R.dimen.A_title)
        }

        fun getDefaultTextColor(context: Context): Int {
            return ContextCompat.getColor(context, R.color.text_grey_light)
        }

        fun getDefaultPadding(context: Context): Int {
            return 20.dp2px()
        }

        val defaultAlpha: Float
            get() = 0.2f

        val defaultAngle: Int
            get() = 315

        fun show(show: Boolean) {
            sShowWaterMark = show
        }

        fun defaultText(text: String) {
            if (!TextUtils.isEmpty(text)) {
                sDefaultText = text
            }
        }
    }
}
