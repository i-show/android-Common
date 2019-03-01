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

package com.ishow.common.widget.viewpager.looping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ishow.common.R;
import com.ishow.common.utils.log.LogManager;
import com.ishow.common.widget.viewpager.looping.indicator.DefaultLoopingIndicator;

/**
 * 轮播图
 */
public class LoopingViewPager extends ViewPager {
    private static final String TAG = "LoopingViewPager";
    /**
     * 默认轮播间隙
     */
    private static final int DEFAULT_LOOPING_TIME = 6000;
    /**
     * Handler 开始轮播
     */
    private static final int HANDLER_START_LOOPING = 1;


    private float mPositionOffset;
    private int mRealCurrentPosition;
    private int mWidthRatio;
    private int mHeightRatio;
    private int mLoopingTime;

    private boolean isAutoLooping;
    private boolean isRegisterDataSetObserver;

    private LoopingViewPagerAdapter mAdapter;
    private ILoopingIndicator mLoopingIndicator;
    private LoopingHandler mHandler;

    public LoopingViewPager(Context context) {
        this(context, null);
    }

    public LoopingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopingViewPager);
        mWidthRatio = a.getInt(R.styleable.LoopingViewPager_widthRatio, 16);
        mHeightRatio = a.getInt(R.styleable.LoopingViewPager_heightRatio, 9);
        a.recycle();
        init();
    }

    private void init() {
        addOnPageChangeListener(onPageChangeListener);
        isAutoLooping = true;
        mLoopingTime = DEFAULT_LOOPING_TIME;
        mLoopingIndicator = new DefaultLoopingIndicator();
        mPositionOffset = -1;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        if (adapter instanceof LoopingViewPagerAdapter) {
            mAdapter = (LoopingViewPagerAdapter) adapter;
            mAdapter.registerDataSetObserver(mDataSetObserver);
            isRegisterDataSetObserver = true;
            notifyLoopingIndicatorDataChanged();
            super.setAdapter(adapter);
            setCurrentItem(0, false);
        } else {
            throw new IllegalArgumentException("need set a LoopingViewPagerAdapter");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize * mHeightRatio / mWidthRatio, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        notifyLoopingIndicatorViewSizeChanged();
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.getRealPosition(super.getCurrentItem()) : 0;
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (mAdapter != null) {
            int innerPosition = mAdapter.getInnerPosition(item);
            super.setCurrentItem(innerPosition, smoothScroll);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isAutoLooping) stopLooping();
                break;
            case MotionEvent.ACTION_UP:
                if (isAutoLooping) startLooping();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mLoopingIndicator != null && mAdapter != null) {
            mLoopingIndicator.onDraw(canvas, getScrollX(), mAdapter.getRealCount(), mRealCurrentPosition, mPositionOffset);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler = new LoopingHandler();
        if (mAdapter != null && !isRegisterDataSetObserver) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            isRegisterDataSetObserver = true;
        }
        startLooping();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            isRegisterDataSetObserver = false;
        }
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            if (mAdapter == null) {
                LogManager.i(TAG, "onPageSelected: adapter is null");
                return;
            }

            mRealCurrentPosition = mAdapter.getRealPosition(position);
            if (mPreviousPosition != mRealCurrentPosition) {
                mPreviousPosition = mRealCurrentPosition;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mAdapter == null) {
                LogManager.i(TAG, "onPageScrolled: adapter is null");
                return;
            }

            mRealCurrentPosition = mAdapter.getRealPosition(position);
            if (positionOffset == 0 && mPositionOffset == 0 && (position < mAdapter.getRealCount() || position >= mAdapter.getRealCount() * 2)) {
                setCurrentItem(mRealCurrentPosition, false);
            }
            mPositionOffset = positionOffset;

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter == null) {
                return;
            }

            int position = LoopingViewPager.super.getCurrentItem();
            int realPosition = mAdapter.getRealPosition(position);
            if (state == ViewPager.SCROLL_STATE_IDLE && (position < mAdapter.getRealCount() || position >= mAdapter.getRealCount() * 2)) {
                setCurrentItem(realPosition, false);
            }
        }
    };

    /**
     * 设置标记
     */
    @SuppressWarnings("unused")
    public void setIndicator(ILoopingIndicator indicator) {
        mLoopingIndicator = indicator;
        notifyLoopingIndicatorViewSizeChanged();
        notifyLoopingIndicatorDataChanged();
        postInvalidate();
    }

    /**
     * 开始轮播
     */
    private void startLooping() {
        if (mAdapter == null || mAdapter.getRealCount() <= 1) {
            return;
        }
        if (isAutoLooping && mHandler != null) {
            mHandler.removeMessages(HANDLER_START_LOOPING);
            mHandler.sendEmptyMessageDelayed(HANDLER_START_LOOPING, mLoopingTime);
        }
    }

    /**
     * 停止轮播
     */
    public void stopLooping() {
        mHandler.removeMessages(HANDLER_START_LOOPING);
    }


    private void notifyLoopingIndicatorDataChanged() {
        if (mLoopingIndicator != null && mAdapter != null) {
            mLoopingIndicator.onDataSizeChanged(mAdapter.getRealCount());
        }
    }

    private void notifyLoopingIndicatorViewSizeChanged() {
        if (mLoopingIndicator != null) {
            mLoopingIndicator.onViewSizeChanged(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @SuppressLint("HandlerLeak")
    private class LoopingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_START_LOOPING:
                    setCurrentItem(mRealCurrentPosition + 1);
                    startLooping();
                    break;
            }
        }
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            int position = LoopingViewPager.super.getCurrentItem();
            if (position == 0) {
                setCurrentItem(position, false);
            }
            startLooping();
            notifyLoopingIndicatorDataChanged();
        }
    };
}
