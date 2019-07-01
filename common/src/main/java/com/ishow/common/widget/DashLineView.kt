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
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
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
    private var mLinePaint: Paint
    /**
     * 破折线颜色
     */
    private var mDashColor: Int = 0
    /**
     * 破折线间距
     */
    private var mDashGap: Int = 0
    /**
     * 破折线宽度
     */
    private var mDashWidth: Int = 0

    /**
     * 破折线颜色
     */
    var dashColor: Int
        get() = mDashColor
        set(@ColorInt dashColor) {
            mDashColor = dashColor
            mLinePaint.color = mDashColor
            postInvalidate()
        }
    /**
     * 破折线间距
     */
    var dashGap: Int
        get() = mDashGap
        set(dashGap) {
            mDashGap = dashGap
            val effects = DashPathEffect(getIntervals(), 1f)
            mLinePaint.pathEffect = effects
            postInvalidate()
        }

    /**
     * 破折线宽度
     */
    var dashWidth: Int
        get() = mDashWidth
        set(dashWidth) {
            mDashWidth = dashWidth
            val effects = DashPathEffect(getIntervals(), 1f)
            mLinePaint.pathEffect = effects
            postInvalidate()
        }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val a = context.obtainStyledAttributes(attrs, R.styleable.DashLineView)
        mDashColor = a.getColor(R.styleable.DashLineView_dashColor, Color.CYAN)
        mDashGap = a.getDimensionPixelSize(R.styleable.DashLineView_dashGap, UnitUtils.dip2px(8))
        mDashWidth = a.getDimensionPixelSize(R.styleable.DashLineView_dashWidth, UnitUtils.dip2px(4))
        a.recycle()

        val effects = DashPathEffect(getIntervals(), 1F)
        mLinePaint = Paint()
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.color = mDashColor
        mLinePaint.strokeWidth = mDashWidth / 2F
        mLinePaint.pathEffect = effects
    }

    private fun getIntervals(): FloatArray {
        return floatArrayOf(mDashWidth.toFloat(), mDashGap.toFloat(), mDashWidth.toFloat(), mDashGap.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = measuredWidth.toFloat()
        val height = measuredHeight
        val y = height / 2F
        canvas.drawLine(0f, y, width, y, mLinePaint)
    }
}