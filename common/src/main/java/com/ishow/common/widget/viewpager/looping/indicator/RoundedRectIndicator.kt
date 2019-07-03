package com.ishow.common.widget.viewpager.looping.indicator

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

import com.ishow.common.utils.UnitUtils
import com.ishow.common.widget.viewpager.looping.ILoopingIndicator

/**
 * Created by yuhaiyang on 2018/9/13.
 * 圆角矩形
 */
class RoundedRectIndicator : ILoopingIndicator {
    private val mIndicatorPaint = Paint()
    private val mIndicatorBorderPaint = Paint()

    private var mWidth: Int = 0

    private var mIndicatorWidth: Int = 0
    private val mIndicatorGapSize: Int
    private val mIndicatorItemSize: Int
    private val mIndicatorItemIncreaseSize: Int

    private val mCurrentRect: RectF
    private val mNextRect: RectF
    private val mNormalRect: RectF

    init {
        mIndicatorPaint.isAntiAlias = true
        mIndicatorPaint.isDither = true
        mIndicatorPaint.color = Color.WHITE

        mIndicatorBorderPaint.isAntiAlias = true
        mIndicatorBorderPaint.isDither = true
        mIndicatorBorderPaint.color = Color.GRAY
        mIndicatorBorderPaint.style = Paint.Style.STROKE

        mCurrentRect = RectF()
        mNextRect = RectF()
        mNormalRect = RectF()

        mIndicatorItemSize = UnitUtils.dip2px(6)
        mIndicatorGapSize = UnitUtils.dip2px(6)
        mIndicatorItemIncreaseSize = mIndicatorItemSize * 2
    }

    override fun onViewSizeChanged(w: Int, h: Int) {
        mWidth = w
        val bottom = (h * 0.9f).toInt()
        mCurrentRect.top = (bottom - mIndicatorItemSize).toFloat()
        mCurrentRect.bottom = bottom.toFloat()

        mNextRect.top = (bottom - mIndicatorItemSize).toFloat()
        mNextRect.bottom = bottom.toFloat()

        mNormalRect.top = (bottom - mIndicatorItemSize).toFloat()
        mNormalRect.bottom = bottom.toFloat()
    }

    override fun onDataSizeChanged(count: Int) {
        mIndicatorWidth = mIndicatorItemSize * count + mIndicatorGapSize * (count - 1) + mIndicatorItemIncreaseSize
    }

    override fun onDraw(canvas: Canvas, scrollX: Int, count: Int, currentPosition: Int, positionOffset: Float) {
        val nextPosition = if (currentPosition == count - 1) 0 else currentPosition + 1
        val currentWidth = mIndicatorItemSize + (mIndicatorItemIncreaseSize * (1 - positionOffset)).toInt()
        val nextWidth = mIndicatorItemSize + (mIndicatorItemIncreaseSize * positionOffset).toInt()
        var left = (mWidth / 2 - mIndicatorWidth / 2 + scrollX).toFloat()
        for (i in 0 until count) {
            // 求圆圈的圆心坐标
            when (i) {
                currentPosition -> {
                    mCurrentRect.left = left
                    mCurrentRect.right = mCurrentRect.left + currentWidth
                    canvas.drawRoundRect(mCurrentRect, 100f, 100f, mIndicatorPaint)
                    canvas.drawRoundRect(mCurrentRect, 100f, 100f, mIndicatorBorderPaint)
                    left = mCurrentRect.right + mIndicatorGapSize
                }
                nextPosition -> {
                    mNextRect.left = left
                    mNextRect.right = mNextRect.left + nextWidth
                    canvas.drawRoundRect(mNextRect, 100f, 100f, mIndicatorPaint)
                    canvas.drawRoundRect(mNextRect, 100f, 100f, mIndicatorBorderPaint)
                    left = mNextRect.right + mIndicatorGapSize
                }
                else -> {
                    mNormalRect.left = left
                    mNormalRect.right = mNormalRect.left + mIndicatorItemSize
                    canvas.drawRoundRect(mNormalRect, 100f, 100f, mIndicatorPaint)
                    canvas.drawRoundRect(mNormalRect, 100f, 100f, mIndicatorBorderPaint)
                    left = mNormalRect.right + mIndicatorGapSize
                }
            }
        }
    }
}
