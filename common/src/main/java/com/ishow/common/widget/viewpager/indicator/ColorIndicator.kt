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

package com.ishow.common.widget.viewpager.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.ishow.common.R
import com.ishow.common.utils.UnitUtils
import kotlin.math.min

class ColorIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : View(context, attrs, defStyle), ViewPager.OnPageChangeListener {

    private val mThumbPaint: Paint
    private val mBarPaint: Paint
    private val mBarBorderPaint: Paint

    private val mRadius: Int
    private val mItemWidth: Int
    private val mItemHeight: Int
    private var mCurrentPage: Int = 0

    val thumbColor: Int
    val barColor: Int
    val barBorderColor: Int

    private var mPageOffset: Float = 0.toFloat()

    private var mViewPager: ViewPager? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorIndicator)
        thumbColor = a.getColor(R.styleable.ColorIndicator_thumbColor, DEFAULT_THUMB_COLOR)
        barColor = a.getColor(R.styleable.ColorIndicator_barColor, DEFAULT_BAR_COLOR)
        barBorderColor = a.getColor(R.styleable.ColorIndicator_barBorderColor, DEFAULT_BAR_BORDER_COLOR)
        mRadius = a.getDimensionPixelOffset(R.styleable.ColorIndicator_radius, UnitUtils.dip2px(DEFAULT_RADIUS))
        val itemPadding = a.getDimensionPixelOffset(R.styleable.ColorIndicator_itemPadding, 0)
        a.recycle()

        mItemWidth = 4 * mRadius + itemPadding
        mItemHeight = 3 * mRadius

        mBarPaint = Paint()
        mBarPaint.color = barColor
        mBarPaint.isAntiAlias = true
        mBarPaint.isDither = true

        mBarBorderPaint = Paint()
        mBarBorderPaint.color = barBorderColor
        mBarBorderPaint.isAntiAlias = true
        mBarBorderPaint.isDither = true

        mThumbPaint = Paint()
        mThumbPaint.color = thumbColor
        mThumbPaint.isAntiAlias = true
        mThumbPaint.isDither = true
    }

    /**
     * @see View.onMeasure
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    /**
     * Determines the with of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The with of the view, honoring constraints from measureSpec
     */
    private fun measureWidth(measureSpec: Int): Int {
        if (mViewPager == null) {
            return 0
        }

        if (mViewPager!!.adapter == null) {
            return 0
        }
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY || mViewPager == null) {
            // We were told how big to be
            result = specSize
        } else {
            // Calculate the with according the views count
            val count = mViewPager!!.adapter!!.count
            result = paddingLeft + paddingRight + count * mItemWidth
            // Respect AT_MOST value if that was what is called for by
            // measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private fun measureHeight(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize
        } else {
            // Measure the height
            result = mItemHeight + paddingTop + paddingBottom
            // Respect AT_MOST value if that was what is called for by
            // measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        if (mViewPager == null) {
            return
        }

        if (mViewPager!!.adapter == null) {
            return
        }

        val count = mViewPager!!.adapter!!.count
        if (count == 0 || count == 1) {
            return
        }


        val realWidth = mItemWidth * count
        val startX = width / 2 - realWidth / 2

        drawBars(canvas, count, startX)
        drawThumb(canvas, startX)
    }

    private fun drawBars(canvas: Canvas, count: Int, startX: Int) {

        for (i in 0 until count) {
            // 求圆圈的圆心坐标
            val x = startX + i * mItemWidth + (mItemWidth - mRadius) / 2
            val y = mItemHeight / 2
            canvas.drawCircle(x.toFloat(), y.toFloat(), mRadius.toFloat(), mBarBorderPaint)
            canvas.drawCircle(x.toFloat(), y.toFloat(), (mRadius - 1).toFloat(), mBarPaint)
        }
    }

    private fun drawThumb(canvas: Canvas, startX: Int) {
        val cx = (mPageOffset * mItemWidth).toInt()
        val x = startX + mCurrentPage * mItemWidth + cx + (mItemWidth - mRadius) / 2
        val y = mItemHeight / 2

        canvas.drawCircle(x.toFloat(), y.toFloat(), mRadius.toFloat(), mThumbPaint)
    }

    fun setViewPager(view: ViewPager) {
        if (mViewPager === view) {
            return
        }
        mViewPager?.removeOnPageChangeListener(this)
        if (view.adapter == null) {
            throw IllegalStateException("ViewPager does not have adapter instance.")
        }

        mViewPager = view
        mViewPager?.addOnPageChangeListener(this)
        invalidate()
    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mCurrentPage = position
        mPageOffset = positionOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        mCurrentPage = savedState.currentPage
        requestLayout()
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState!!)
        savedState.currentPage = mCurrentPage
        return savedState
    }


    internal class SavedState : BaseSavedState {
        var currentPage: Int = 0

        constructor(superState: Parcelable) : super(superState)

        private constructor(input: Parcel) : super(input) {
            currentPage = input.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(currentPage)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(input: Parcel): SavedState {
                    return SavedState(input)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        /**
         * 默认进度颜色
         */
        private const val DEFAULT_THUMB_COLOR = Color.BLUE
        /**
         * 默认进度条的颜色
         */
        private const val DEFAULT_BAR_COLOR = 0xFFFFFFFF.toInt()
        /**
         * 默认Bar边框的颜色
         */
        private const val DEFAULT_BAR_BORDER_COLOR = 0xFF999999.toInt()
        /**
         * 默认半径大小
         */
        private const val DEFAULT_RADIUS = 4
    }
}
