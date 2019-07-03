package com.ishow.common.widget.viewpager.looping.indicator

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

import com.ishow.common.utils.UnitUtils
import com.ishow.common.widget.viewpager.looping.ILoopingIndicator

/**
 * Created by yuhaiyang on 2018/9/13.
 * 默认的
 */
class DefaultLoopingIndicator : ILoopingIndicator {
    private val mIndicatorPaint: Paint
    private val mIndicatorBorderPaint: Paint
    private val mIndicatorRadius: Int = UnitUtils.dip2px(3)
    private val mIndicatorItemWidth: Int
    private var mIndicatorWidth: Int = 0
    private val mIndicatorHeight: Int

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    init {
        mIndicatorItemWidth = 5 * mIndicatorRadius
        mIndicatorHeight = 6 * mIndicatorRadius

        mIndicatorPaint = Paint()
        mIndicatorPaint.isAntiAlias = true
        mIndicatorPaint.isDither = true
        mIndicatorPaint.color = Color.WHITE

        mIndicatorBorderPaint = Paint()
        mIndicatorBorderPaint.isAntiAlias = true
        mIndicatorBorderPaint.isDither = true
        mIndicatorBorderPaint.color = Color.GRAY
        mIndicatorBorderPaint.style = Paint.Style.STROKE
    }

    override fun onViewSizeChanged(w: Int, h: Int) {
        mWidth = w
        mHeight = h
    }

    override fun onDataSizeChanged(count: Int) {
        mIndicatorWidth = mIndicatorItemWidth * count
    }

    override fun onDraw(canvas: Canvas, scrollX: Int, count: Int, currentPosition: Int, positionOffset: Float) {
        val nextPosition = if (currentPosition == count - 1) 0 else currentPosition + 1
        val startX = mWidth / 2 - mIndicatorWidth / 2 + scrollX
        val y = mHeight - mIndicatorHeight
        val currentIndicatorRadius = (mIndicatorRadius + mIndicatorRadius.toFloat() * (1 - positionOffset) * 0.38f).toInt()
        val nextIndicatorRadius = (mIndicatorRadius + mIndicatorRadius.toFloat() * positionOffset * 0.38f).toInt()
        for (i in 0 until count) {
            val x = startX + i * mIndicatorItemWidth + (mIndicatorItemWidth - mIndicatorRadius) / 2
            when (i) {
                currentPosition -> {
                    canvas.drawCircle(x.toFloat(), y.toFloat(), currentIndicatorRadius.toFloat(), mIndicatorPaint)
                    canvas.drawCircle(x.toFloat(), y.toFloat(), currentIndicatorRadius.toFloat(), mIndicatorBorderPaint)
                }
                nextPosition -> {
                    canvas.drawCircle(x.toFloat(), y.toFloat(), nextIndicatorRadius.toFloat(), mIndicatorPaint)
                    canvas.drawCircle(x.toFloat(), y.toFloat(), nextIndicatorRadius.toFloat(), mIndicatorBorderPaint)
                }
                else -> {
                    canvas.drawCircle(x.toFloat(), y.toFloat(), mIndicatorRadius.toFloat(), mIndicatorPaint)
                    canvas.drawCircle(x.toFloat(), y.toFloat(), mIndicatorRadius.toFloat(), mIndicatorBorderPaint)
                }
            }
        }
    }
}
