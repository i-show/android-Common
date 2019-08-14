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

package com.ishow.common.widget.pickview

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.core.content.ContextCompat
import com.ishow.common.R
import com.ishow.common.utils.StringUtils
import com.ishow.common.widget.pickview.adapter.PickerAdapter
import com.ishow.common.widget.pickview.constant.Direction
import com.ishow.common.widget.pickview.listener.OnItemSelectedListener
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


class PickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    /**
     * 校准数据时应该增加或者减少的Position
     */
    private var mAdjustPosition = 0
    private var mCurrentPosition = 0

    private var mLastY = 0
    private var mLastScrollerY = 0
    private var mMinVelocity = 0

    private val mVisibleCount: Int

    private val mDividerColor: Int
    private val mSelectedTextColor: Int
    private val mUnselectedTextColor: Int

    /**
     * 一个Item想要的宽度和高度
     */
    private var mItemWidth = 0
    private var mItemHeight = 0
    private var mUnitWidth = 0

    private var mDrawItemX: Int = 0
    private val mItemMinHeight: Int

    private val mUnitTextSize: Float
    private val mSelectedTextSize: Float
    private var mUnselectedTextSize: Float

    // 第一条线Y坐标值
    private var mFirstLineY = 0F
    //第二条线Y坐标
    private var mSecondLineY = 0F
    // 移动的距离， 注意：这里不是总的移动距离
    private var mMove = 0F
    private val mUnit: String?//附加单位

    private lateinit var mTextPaint: TextPaint
    private lateinit var mUnitPaint: TextPaint
    private lateinit var mDividerPaint: Paint

    private var isCyclic: Boolean = false
    private var mAdapter: PickerAdapter<*>? = null
    private lateinit var mFlingScroller: Scroller
    private lateinit var mAdjustScroller: Scroller
    private lateinit var mVelocityTracker: VelocityTracker

    private var mListener: OnItemSelectedListener? = null
    private var mDataSetObserver: DefaultDataSetObserver? = null

    /**
     * 设置当前的position
     */
    var currentPosition: Int
        get() = mCurrentPosition
        set(position) {
            if (mAdapter == null) {
                Log.i(TAG, "setCurrentPosition: adapter is null")
                return
            }
            if (position >= mAdapter!!.getCount()) {
                throw IllegalArgumentException("position is " + position + "  while total count" + mAdapter!!.getCount())
            }
            mCurrentPosition = position
            postInvalidate()
        }

    /*
     * ============= 默认值区域 ================
     */

    /**
     * 获取未选中的Text的颜色
     */
    private val defaultDividerColor: Int
        get() = ContextCompat.getColor(context, R.color.line)


    /**
     * 获取未选中的Text的颜色
     */
    private val defaultUnselectedTextColor: Int
        get() = ContextCompat.getColor(context, R.color.text_grey_light_normal)


    /**
     * 获取未选中的Text的颜色
     */
    private val defaultSelectedTextColor: Int
        get() = ContextCompat.getColor(context, R.color.color_accent)

    /**
     * 获取未选中的Text的颜色
     */
    private val defaultTextSize: Int
        get() = resources.getDimensionPixelSize(R.dimen.D_title)

    /**
     * 获取默认的自提大小
     */
    private val defaultUnitTextSize: Int
        get() = resources.getDimensionPixelSize(R.dimen.I_title)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PickerView)
        mUnit = a.getString(R.styleable.PickerView_unit)
        mDividerColor = a.getColor(R.styleable.PickerView_divider, defaultDividerColor)

        mSelectedTextColor = a.getColor(R.styleable.PickerView_selectedTextColor, defaultSelectedTextColor)
        mUnselectedTextColor = a.getColor(R.styleable.PickerView_unselectedTextColor, defaultUnselectedTextColor)

        mItemMinHeight = a.getDimensionPixelOffset(R.styleable.PickerView_itemMinHeight, 0)
        mSelectedTextSize = a.getDimensionPixelOffset(R.styleable.PickerView_textSize, defaultTextSize).toFloat()
        mUnselectedTextSize = a.getDimensionPixelOffset(R.styleable.PickerView_unselectedTextSize, 0).toFloat()
        mUnitTextSize = a.getDimensionPixelSize(R.styleable.PickerView_unitTextSize, defaultUnitTextSize).toFloat()
        if (mUnselectedTextSize == 0F) {
            mUnselectedTextSize = mSelectedTextSize * UNSELECTED_TEXT_SIZE_RATIO
        }

        mVisibleCount = a.getInteger(R.styleable.PickerView_visibleCount, DEFAULT_VISIBLE_COUNT)
        a.recycle()

        init(context)
    }


    private fun init(context: Context) {
        isCyclic = true
        mFlingScroller = Scroller(context)
        mAdjustScroller = Scroller(context)

        mVelocityTracker = VelocityTracker.obtain()
        mMinVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity

        initPaints()
        forecast()
    }


    /**
     * 预先计算Item
     */
    private fun forecast() {

        if (mAdapter == null) {
            Log.i(TAG, "forecast: adapter is null")
            return
        }
        mItemHeight = 0
        mCurrentPosition = min(mCurrentPosition, mAdapter!!.getCount() - 1)
        mCurrentPosition = computePosition(mCurrentPosition)

        mTextPaint.textSize = mSelectedTextSize
        val rect = Rect()
        for (i in 0 until mAdapter!!.getCount()) {
            val centerString = mAdapter!!.getItemText(i)
            mTextPaint.getTextBounds(centerString, 0, centerString.length, rect)

            mItemWidth = max(mItemWidth, rect.width())
            mItemHeight = max(mItemHeight, rect.height())
        }

        mItemHeight = (LINE_SPACING_MULTIPLIER * mItemHeight).toInt()
        mItemHeight = max(mItemMinHeight, mItemHeight)

        if (!TextUtils.isEmpty(mUnit)) {
            val gap = context.resources.getDimensionPixelSize(R.dimen.dp_10)
            mUnitWidth = Layout.getDesiredWidth(mUnit, mUnitPaint).toInt() + gap
        }
    }


    private fun initPaints() {
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textSize = mSelectedTextSize
        mTextPaint.textAlign = Paint.Align.CENTER

        mUnitPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mUnitPaint.textSize = mUnitTextSize
        mUnitPaint.color = mSelectedTextColor
        mUnitPaint.textAlign = Paint.Align.CENTER

        mDividerPaint = Paint()
        mDividerPaint.color = mDividerColor
        mDividerPaint.isAntiAlias = true
        mDividerPaint.strokeWidth = 1.0.toFloat()
        mDividerPaint.isDither = true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)

        when (mode) {
            MeasureSpec.UNSPECIFIED -> return mItemWidth + mUnitWidth + paddingStart + paddingEnd
            MeasureSpec.EXACTLY -> return size
            MeasureSpec.AT_MOST -> {
                val wantSize = mItemWidth + mUnitWidth + paddingStart + paddingEnd
                return min(size, wantSize)
            }
        }

        return size
    }


    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)

        when (mode) {
            MeasureSpec.UNSPECIFIED -> return mVisibleCount * mItemHeight + paddingTop + paddingBottom
            MeasureSpec.EXACTLY -> return size
            MeasureSpec.AT_MOST -> {
                val wantSize = mVisibleCount * mItemHeight + paddingTop + paddingBottom
                return min(size, wantSize)
            }
        }

        return size
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val height = measuredHeight

        //计算两条横线和控件中间点的Y位置
        mFirstLineY = ((height - mItemHeight) / 2).toFloat()
        mSecondLineY = ((height + mItemHeight) / 2).toFloat()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = measuredWidth
        // 视觉上有点偏左
        mDrawItemX =
            if (mUnitWidth > 0) {
                (width - mUnitWidth) / 2 + width / 11
            } else {
                width / 2
            }
        drawLine(canvas, width)
        drawText(canvas)
        drawUnit(canvas)
    }


    private fun drawLine(canvas: Canvas, viewWidth: Int) {
        canvas.drawLine(0f, mFirstLineY, viewWidth.toFloat(), mFirstLineY, mDividerPaint)
        canvas.drawLine(0f, mSecondLineY, viewWidth.toFloat(), mSecondLineY, mDividerPaint)
    }

    /**
     * 从中间往两边开始画刻度线
     */
    private fun drawText(canvas: Canvas) {
        /*
         * 画中间的
         */
        val scale = parabola(mItemHeight.toFloat(), mMove)
        val textSize = (mSelectedTextSize - mUnselectedTextSize) * scale + mUnselectedTextSize
        val color = interpolateColor(mUnselectedTextColor, mSelectedTextColor, scale)
        val text = getItemText(mCurrentPosition)

        mTextPaint.color = color
        mTextPaint.textSize = textSize

        canvas.save()
        val fmi = mTextPaint.fontMetricsInt
        val index = mVisibleCount / 2
        val baseline = (mItemHeight - (fmi.bottom + fmi.top)).toFloat() / 2.0f
        val y = baseline + mItemHeight * index - mMove + paddingTop
        canvas.drawText(text, mDrawItemX.toFloat(), y, mTextPaint)

        // 绘制上方data
        // 这里因为要滚动所以要比可见的多画一个 所以要 >= -1
        run {
            var i = 1
            while (mVisibleCount / 2 - i >= -1) {
                drawOtherText(canvas, scale, i, Direction.UP)
                i++
            }
        }
        // 绘制下方data
        // 这里因为要滚动所以要比可见的多画一个 所以要 <= mVisibleCount
        var i = 1
        while (mVisibleCount / 2 + i <= mVisibleCount) {
            drawOtherText(canvas, scale, i, Direction.DOWN)
            i++
        }
        canvas.restore()

    }

    /**
     * @param position  距离mCurrentSelected的差值
     * @param direction 1表示向下绘制，-1表示向上绘制
     */
    private fun drawOtherText(canvas: Canvas, middleScale: Float, position: Int, direction: Direction) {
        val scale = 1 - middleScale
        val size = (mSelectedTextSize - mUnselectedTextSize) * scale + mUnselectedTextSize
        val color = interpolateColor(mUnselectedTextColor, mSelectedTextColor, scale)
        if (position == 1 && mMove > 0 && direction == Direction.DOWN) {
            mTextPaint.color = color
            mTextPaint.textSize = size
        } else if (position == 1 && mMove < 0 && direction == Direction.UP) {
            mTextPaint.color = color
            mTextPaint.textSize = size
        } else {
            mTextPaint.color = mUnselectedTextColor
            mTextPaint.textSize = mUnselectedTextSize
        }

        val fmi = mTextPaint.fontMetricsInt
        val baseline = (mItemHeight - (fmi.bottom + fmi.top)).toFloat() / 2.0f
        val text: String

        val index = mVisibleCount / 2

        if (direction == Direction.UP) {
            val y = baseline + mItemHeight * (index - position) - mMove + paddingTop
            text = getItemText(mCurrentPosition - position)
            canvas.drawText(text, mDrawItemX.toFloat(), y, mTextPaint)
        } else {
            val y = baseline + mItemHeight * (index + position) - mMove + paddingTop
            text = getItemText(mCurrentPosition + position)
            canvas.drawText(text, mDrawItemX.toFloat(), y, mTextPaint)
        }
    }


    private fun drawUnit(canvas: Canvas) {
        mUnit?.let {
            val index = mVisibleCount / 2
            val fmi = mUnitPaint.fontMetricsInt
            val baseline = (mItemHeight - (fmi.bottom + fmi.top)).toFloat() / 2.0f
            val x = mDrawItemX + mItemWidth / 2 + mUnitWidth / 2
            val y = (baseline + (mItemHeight * index).toFloat() + paddingBottom.toFloat()).toInt()
            canvas.drawText(it, x.toFloat(), y.toFloat(), mUnitPaint)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val y = event.y.toInt()
        mVelocityTracker.addMovement(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mFlingScroller.forceFinished(true)
                mAdjustScroller.forceFinished(true)
                mLastY = y
                mMove = 0F
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                mMove += (mLastY - y).toFloat()
                compute()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                countVelocityTracker()
                parent.requestDisallowInterceptTouchEvent(false)
                return false
            }
        }

        mLastY = y
        return true
    }


    private fun countVelocityTracker() {
        mVelocityTracker.computeCurrentVelocity(1000)
        val yVelocity = mVelocityTracker.yVelocity.toInt()
        if (abs(yVelocity) > mMinVelocity) {
            fling(yVelocity)
        } else {
            calibration()
        }
    }

    private fun fling(yVelocity: Int) {
        mLastScrollerY = 0
        mAdjustScroller.forceFinished(true)
        mFlingScroller.fling(0, 0, 0, yVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE)
        postInvalidate()
    }

    /**
     * 校准
     */
    private fun calibration() {
        mLastScrollerY = 0
        mFlingScroller.forceFinished(true)

        if (mMove < 0) {
            if (abs(mMove) > mItemHeight / 2) {
                mAdjustPosition = -1
                mAdjustScroller.startScroll(0, 0, 0, (mItemHeight + mMove).toInt())
            } else {
                mAdjustPosition = 0
                mAdjustScroller.startScroll(0, 0, 0, mMove.toInt())
            }
        } else {
            if (abs(mMove) > mItemHeight / 2) {
                mAdjustPosition = 1
                mAdjustScroller.startScroll(0, 0, 0, (mMove - mItemHeight).toInt())
            } else {
                mAdjustPosition = 0
                mAdjustScroller.startScroll(0, 0, 0, mMove.toInt())
            }
        }

        invalidate()
    }

    /**
     * 计算
     */
    private fun compute() {
        val moveCount = (mMove / mItemHeight).toInt()
        if (abs(moveCount) > 0) {
            // 如果 已经移动了一个Item的高度，那么position 增加或者减少
            // mMove 减少对应Item的高度
            mMove -= (moveCount * mItemHeight).toFloat()
            mCurrentPosition += moveCount
            mCurrentPosition = computePosition(mCurrentPosition)
            notifyValueChange()
        }
        postInvalidate()
    }


    /**
     * 计算位置
     */
    private fun computePosition(position: Int): Int {
        var pos = position
        val itemCount = mAdapter!!.getCount()
        if (itemCount == 0) {
            return position;
        }

        pos %= if (pos < 0) {
            -itemCount
        } else {
            itemCount
        }

        if (pos < 0) {
            pos += itemCount
        }
        return pos
    }


    override fun computeScroll() {
        computeFlingScroll()
        computeAdjustScroll()
    }

    private fun computeFlingScroll() {
        if (!mFlingScroller.computeScrollOffset()) {
            return
        }
        if (mFlingScroller.currY == mFlingScroller.finalY) {
            calibration()
        } else {
            val position = mFlingScroller.currY
            mMove += (mLastScrollerY - position).toFloat()
            compute()
            mLastScrollerY = position
        }
    }

    private fun computeAdjustScroll() {
        if (!mAdjustScroller.computeScrollOffset()) {
            return
        }
        if (mAdjustScroller.currY == mAdjustScroller.finalY) {
            mCurrentPosition += mAdjustPosition
            mCurrentPosition = computePosition(mCurrentPosition)

            notifyValueChange()
            mAdjustPosition = 0
            mLastScrollerY = 0
            mLastY = 0
            mMove = 0f
        } else {
            val position = mAdjustScroller.currY
            mMove += (mLastScrollerY - position).toFloat()
            mLastScrollerY = position
            invalidate()
        }
    }


    fun setAdapter(adapter: PickerAdapter<*>) {
        mAdapter = adapter

        if (mDataSetObserver != null) {
            mAdapter?.unregisterDataSetObserver(mDataSetObserver!!)
        }

        mAdapter = adapter
        mDataSetObserver = DefaultDataSetObserver()
        mAdapter!!.registerDataSetObserver(mDataSetObserver!!)

        forecast()
        requestLayout()
    }

    private fun notifyValueChange() {
        mListener?.onItemSelected(mCurrentPosition)
    }

    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        mListener = listener
    }

    /**
     * 通过Item来显示第几个
     */
    private fun getItemText(position: Int): String {
        var pos = position
        if (mAdapter == null || mAdapter.isEmpty()) {
            Log.i(TAG, "getItemText: mAdapter is null")
            return StringUtils.EMPTY
        }

        val adapter = mAdapter!!

        val itemCount = adapter.getCount()
        if (isCyclic) {
            // 先进行取余，如果还小于0 那么只要加一个count值即可
            pos %= if (pos < 0) {
                -itemCount
            } else {
                itemCount
            }

            if (pos < 0) {
                pos += itemCount
            }
        }
        return mAdapter!!.getItemText(pos)
    }


    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - (x / zero).toDouble().pow(2.00)).toFloat()
        return max(0F, f)
    }

    /**
     * @param colorA 原始颜色
     * @param colorB 目标颜色
     * @param bias   转变比例
     */
    private fun interpolateColor(colorA: Int, colorB: Int, bias: Float): Int {
        return if (bias > 0.5F) {
            colorB
        } else {
            colorA
        }
    }

    private inner class DefaultDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            forecast()
            requestLayout()
        }
    }

    companion object {
        private const val TAG = "PickerView"

        /**
         * 默认可以看到的几个Item
         */
        private const val DEFAULT_VISIBLE_COUNT = 5
        /**
         * ITEM间距倍数
         */
        private const val LINE_SPACING_MULTIPLIER = 2.0F
        /**
         * 未选中TextSize大小比例
         */
        private const val UNSELECTED_TEXT_SIZE_RATIO = 0.78F
    }
}
