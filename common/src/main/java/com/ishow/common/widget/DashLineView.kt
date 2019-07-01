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

package com.ishow.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import androidx.annotation.ColorInt
import com.ishow.common.R
import com.ishow.common.utils.UnitUtils

/**
 * Created by yuhaiyang on 2018/9/5.
 * 破折线
 */
class DashLineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr) {
    /**
     * 划线画笔
     */
    private var mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 破折线颜色
     */
    var dashColor: Int = Color.GRAY
        set(@ColorInt color) {
            field = color
            mLinePaint.color = color
            postInvalidate()
        }

    /**
     * 破折线间距
     */
    var dashGap: Int = 0
        set(gap) {
            field = gap
            updateEffect()
        }

    /**
     * 破折线宽度
     */
    var dashWidth: Int = 0
        set(width) {
            field = width
            updateEffect()
        }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val a = context.obtainStyledAttributes(attrs, R.styleable.DashLineView)
        dashColor = a.getColor(R.styleable.DashLineView_dashColor, Color.CYAN)
        dashGap = a.getDimensionPixelSize(R.styleable.DashLineView_dashGap, UnitUtils.dip2px(8))
        dashWidth = a.getDimensionPixelSize(R.styleable.DashLineView_dashWidth, UnitUtils.dip2px(4))
        a.recycle()

        mLinePaint = Paint()
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = dashColor
        updateEffect(invalidate = false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mLinePaint.strokeWidth = h.toFloat()
    }

    private fun updateEffect(invalidate: Boolean = true) {
        val intervals = floatArrayOf(dashWidth.toFloat(), dashGap.toFloat())
        mLinePaint.pathEffect = DashPathEffect(intervals, 1F)
        if (invalidate) postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = measuredWidth.toFloat()
        val y = measuredHeight / 2F
        canvas.drawLine(0f, y, width, y, mLinePaint)
    }
}