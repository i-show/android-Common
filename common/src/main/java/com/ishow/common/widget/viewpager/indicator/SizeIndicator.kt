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

class SizeIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        View(context, attrs, defStyle), ViewPager.OnPageChangeListener {
    private val mRadius: Int
    private val mItemWidth: Int
    private val mItemHeight: Int
    private var mCurrentPage: Int = 0

    private var mCurrentRadius: Float = 0.toFloat()
    private var mNextRadius: Float = 0.toFloat()

    private var mViewPager: ViewPager? = null

    private val mPaint: Paint = Paint()

    private val defaultPaddingStart: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.gap_grade_6)

    private val defaultPaddingEnd: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.gap_grade_6)

    private val defaultPaddingTop: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.gap_grade_2)

    private val defaultPaddingBottom: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.gap_grade_2)

    init {
        mPaint.color = Color.WHITE
        mPaint.isAntiAlias = true
        mPaint.isDither = true

        val a = context.obtainStyledAttributes(attrs, R.styleable.SizeIndicator)
        mRadius = a.getDimensionPixelSize(R.styleable.SizeIndicator_thumbRadius, UnitUtils.dip2px(RADIUS))

        var left = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingLeft, defaultPaddingStart)
        left = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingStart, left)

        var right = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingRight, defaultPaddingEnd)
        right = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingEnd, right)
        val top = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingTop, defaultPaddingTop)
        val bottom = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingBottom, defaultPaddingBottom)
        setPadding(left, top, right, bottom)
        a.recycle()

        mItemWidth = 5 * mRadius
        mItemHeight = 6 * mRadius
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mViewPager == null) {
            return
        }

        val count = mViewPager!!.adapter!!.count
        if (count == 0 || count == 1) {
            return
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1)
            return
        }

        val realWidth = mItemWidth * count
        val startX = width / 2 - realWidth / 2
        val y = paddingTop + mItemHeight / 2

        for (i in 0 until count) {
            // 求圆圈的圆心坐标
            val x = startX + i * mItemWidth + (mItemWidth - mRadius) / 2
            when (i) {
                mCurrentPage -> canvas.drawCircle(x.toFloat(), y.toFloat(), mCurrentRadius, mPaint)
                mCurrentPage + 1 -> canvas.drawCircle(x.toFloat(), y.toFloat(), mNextRadius, mPaint)
                else -> canvas.drawCircle(x.toFloat(), y.toFloat(), mRadius.toFloat(), mPaint)
            }
        }
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
        mViewPager!!.addOnPageChangeListener(this)
        invalidate()
    }

    fun setViewPager(view: ViewPager, initialPosition: Int) {
        setViewPager(view)
        setCurrentItem(initialPosition)
    }

    fun setCurrentItem(item: Int) {
        if (mViewPager == null) {
            throw IllegalStateException("ViewPager has not been bound.")
        }
        mViewPager!!.currentItem = item
        mCurrentPage = item
        invalidate()
    }

    fun notifyDataSetChanged() {
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mCurrentPage = position
        mCurrentRadius = mRadius + mRadius.toFloat() * (1 - positionOffset) * CURRENT_MAX_INCREASE
        mNextRadius = mRadius + mRadius.toFloat() * positionOffset * CURRENT_MAX_INCREASE
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        mCurrentPage = position
        invalidate()
    }

    /**
     * @see View.onMeasure
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    /**
     * 计算控件的宽度
     */
    private fun measureWidth(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY || mViewPager == null) {
            result = specSize
        } else {
            val count = mViewPager!!.adapter!!.count
            result = paddingLeft + paddingRight + count * mItemWidth
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    /**
     * 计算控件的高度
     */
    private fun measureHeight(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = mItemHeight + paddingTop + paddingBottom
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        mCurrentPage = savedState.currentPage
        requestLayout()
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.currentPage = mCurrentPage
        return savedState
    }

    internal class SavedState : BaseSavedState {
        var currentPage: Int = 0

        constructor(superState: Parcelable?) : super(superState) {}

        private constructor(`in`: Parcel) : super(`in`) {
            currentPage = `in`.readInt()
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(currentPage)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        private const val RADIUS = 3.0f
        /**
         * 最大增幅
         */
        private const val CURRENT_MAX_INCREASE = 0.6f
    }
}
