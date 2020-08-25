package com.ishow.noah.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by yuhaiyang on 2020/8/23.
 *
 */
class TestLinearLayout : LinearLayout {
    private val paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    init {
        paint.color = Color.GREEN
        paint.strokeWidth = 2.0F
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(44F, 0F, 44F, height.toFloat(), paint)
        canvas.drawLine(width - 44F, 0F, width - 44F, height.toFloat(), paint)
    }

}