/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.widget.viewpager.looping

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ishow.common.R
import com.ishow.common.utils.log.LogUtils
import com.ishow.common.widget.viewpager.looping.indicator.DefaultLoopingIndicator

/**
 * 轮播图
 */
class LoopingViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {


    private var mPositionOffset = 0F
    private var mRealCurrentPosition: Int = 0
    private val mWidthRatio: Int
    private val mHeightRatio: Int
    private var mLoopingTime: Int = 0

    private var isAutoLooping: Boolean = false
    private var isRegisterDataSetObserver: Boolean = false

    private var mAdapter: LoopingViewPagerAdapter<*, *>? = null
    private var mLoopingIndicator: ILoopingIndicator? = null
    private var mHandler: LoopingHandler? = null

    private val onPageChangeListener = object : OnPageChangeListener {
        private var mPreviousPosition = -1

        override fun onPageSelected(position: Int) {
            if (mAdapter == null) {
                LogUtils.i(TAG, "onPageSelected: adapter is null")
                return
            }

            mRealCurrentPosition = mAdapter!!.getRealPosition(position)
            if (mPreviousPosition != mRealCurrentPosition) {
                mPreviousPosition = mRealCurrentPosition
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            if (mAdapter == null) {
                LogUtils.i(TAG, "onPageScrolled: adapter is null")
                return
            }

            mRealCurrentPosition = mAdapter!!.getRealPosition(position)
            if (positionOffset == 0f && mPositionOffset == 0f && (position < mAdapter!!.realCount || position >= mAdapter!!.realCount * 2)) {
                setCurrentItem(mRealCurrentPosition, false)
            }
            mPositionOffset = positionOffset

        }

        override fun onPageScrollStateChanged(state: Int) {
            if (mAdapter == null) {
                return
            }

            val position = super@LoopingViewPager.getCurrentItem()
            val realPosition = mAdapter!!.getRealPosition(position)
            if (state == SCROLL_STATE_IDLE && (position < mAdapter!!.realCount || position >= mAdapter!!.realCount * 2)) {
                setCurrentItem(realPosition, false)
            }
        }
    }

    private val mDataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            val position = super@LoopingViewPager.getCurrentItem()
            if (position == 0) {
                setCurrentItem(position, false)
            }
            startLooping()
            notifyLoopingIndicatorDataChanged()
            requestLayout()
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoopingViewPager)
        mWidthRatio = a.getInt(R.styleable.LoopingViewPager_widthRatio, 16)
        mHeightRatio = a.getInt(R.styleable.LoopingViewPager_heightRatio, 9)
        a.recycle()
        init()
    }

    private fun init() {
        addOnPageChangeListener(onPageChangeListener)
        isAutoLooping = true
        mLoopingTime = DEFAULT_LOOPING_TIME
        mLoopingIndicator = DefaultLoopingIndicator()
        mPositionOffset = -1F
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        mAdapter?.unregisterDataSetObserver(mDataSetObserver)

        if (adapter is LoopingViewPagerAdapter<*, *>) {
            mAdapter = adapter
            mAdapter?.registerDataSetObserver(mDataSetObserver)
            isRegisterDataSetObserver = true
            notifyLoopingIndicatorDataChanged()
            super.setAdapter(adapter)
            setCurrentItem(0, false)
        } else {
            throw IllegalArgumentException("need set a LoopingViewPagerAdapter")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightSpec: Int) {
        var heightMeasureSpec = heightSpec
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) {
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize * mHeightRatio / mWidthRatio, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        notifyLoopingIndicatorViewSizeChanged()
    }

    override fun getCurrentItem(): Int {
        return if (mAdapter != null) mAdapter!!.getRealPosition(super.getCurrentItem()) else 0
    }

    override fun setCurrentItem(item: Int) {
        if (currentItem != item) {
            setCurrentItem(item, true)
        }
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        mAdapter?.let {
            val innerPosition = it.getInnerPosition(item)
            super.setCurrentItem(innerPosition, smoothScroll)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> if (isAutoLooping) stopLooping()
            MotionEvent.ACTION_UP -> if (isAutoLooping) startLooping()
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        mAdapter?.let {
            mLoopingIndicator?.onDraw(canvas, scrollX, it.realCount, mRealCurrentPosition, mPositionOffset)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mHandler = LoopingHandler()
        if (mAdapter != null && !isRegisterDataSetObserver) {
            mAdapter!!.registerDataSetObserver(mDataSetObserver)
            isRegisterDataSetObserver = true
        }
        startLooping()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null

        if (mAdapter != null) {
            mAdapter!!.unregisterDataSetObserver(mDataSetObserver)
            isRegisterDataSetObserver = false
        }
    }

    /**
     * 设置标记
     */
    fun setIndicator(indicator: ILoopingIndicator) {
        mLoopingIndicator = indicator
        notifyLoopingIndicatorViewSizeChanged()
        notifyLoopingIndicatorDataChanged()
        postInvalidate()
    }

    /**
     * 开始轮播
     */
    private fun startLooping() {
        if (mAdapter == null || mAdapter!!.realCount <= 1) {
            return
        }

        if (isAutoLooping) {
            mHandler?.removeMessages(HANDLER_START_LOOPING)
            mHandler?.sendEmptyMessageDelayed(HANDLER_START_LOOPING, mLoopingTime.toLong())
        }
    }

    /**
     * 停止轮播
     */
    fun stopLooping() {
        mHandler?.removeMessages(HANDLER_START_LOOPING)
    }


    private fun notifyLoopingIndicatorDataChanged() {
        if (mAdapter != null) {
            mLoopingIndicator?.onDataSizeChanged(mAdapter!!.realCount)
        }
    }

    private fun notifyLoopingIndicatorViewSizeChanged() {
        mLoopingIndicator?.onViewSizeChanged(measuredWidth, measuredHeight)
    }

    @SuppressLint("HandlerLeak")
    private inner class LoopingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                HANDLER_START_LOOPING -> {
                    currentItem = mRealCurrentPosition + 1
                    startLooping()
                }
            }
        }
    }

    companion object {
        private const val TAG = "LoopingViewPager"
        /**
         * 默认轮播间隙
         */
        private const val DEFAULT_LOOPING_TIME = 6000
        /**
         * Handler 开始轮播
         */
        private const val HANDLER_START_LOOPING = 1
    }
}
