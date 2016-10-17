/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brightyu.androidcommon.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.bright.common.constant.DefaultColors;


public class RulerView extends View {
    private static final String TAG = "RulerView";

    public static final int MOD_TYPE_ONE = 10;

    private static final int ITEM_DISTANCE = 16;

    private static final int TEXT_SIZE = 18;
    // 最大值
    private static final int MAX_VALUE = 480;
    // 默认值
    private static final int DEFAULT_CURRENT_INDEX = 30;


    private float mDensity;
    private int mCurrentIndex = DEFAULT_CURRENT_INDEX;


    private int mLastX, mMove;
    private int mWidth, mHeight;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private OnValueChangeListener mListener;

    private int mShortLineTop;
    private int mShortLineBottom;

    private int mLongLineTop;
    private int mLongLineBottom;

    private Paint mShortLinePaint;
    private Paint mLongLinePaint;
    private Paint mPointerPaint;

    private TextPaint mTextPaint;

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        mShortLinePaint = new Paint();
        mShortLinePaint.setAntiAlias(true);
        mShortLinePaint.setDither(true);
        mShortLinePaint.setStrokeWidth((float) (1.2 * mDensity));
        mShortLinePaint.setColor(DefaultColors.ORANGE);

        mLongLinePaint = new Paint();
        mLongLinePaint.setAntiAlias(true);
        mLongLinePaint.setDither(true);
        mLongLinePaint.setStrokeWidth((float) (2.2 * mDensity));
        mLongLinePaint.setColor(DefaultColors.ORANGE);

        mPointerPaint = new Paint();
        mPointerPaint.setDither(true);
        mPointerPaint.setAntiAlias(true);
        mPointerPaint.setColor(DefaultColors.ORANGE);
        mPointerPaint.setStrokeWidth(3 * mDensity);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(TEXT_SIZE * mDensity);
        mTextPaint.setColor(DefaultColors.GERY);
    }

    /**
     * 设置用于接收结果的监听器
     */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    /**
     * 获取当前刻度值
     */
    public int getValue() {
        return mCurrentIndex;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        // 其实是 3/4 后除以2 SmallLine的高度是高度的1/4
        int hight = mHeight / 5;
        mShortLineTop = (mHeight - hight) / 2;
        mShortLineBottom = mShortLineTop + hight;

        mLongLineTop = (int) (mShortLineTop * 0.9);
        mLongLineBottom = (int) (mShortLineBottom * 1.1);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScaleLine(canvas);
        drawMiddleLine(canvas);
    }


    /**
     * 从中间往两边开始画刻度线
     */
    private void drawScaleLine(Canvas canvas) {
        canvas.save();


        int width = mWidth, drawCount = 0;
        float position = 0;
        float textWidth = Layout.getDesiredWidth("0", mTextPaint);

        for (int i = 0; drawCount <= 2 * width; i++) {
            int numSize = String.valueOf(mCurrentIndex + i).length();

            position = (width / 2 - mMove) + i * ITEM_DISTANCE * mDensity;
            if (position + getPaddingRight() < mWidth) {
                if ((mCurrentIndex + i) % MOD_TYPE_ONE == 0) {
                    canvas.drawLine(position, mLongLineTop, position, mLongLineBottom, mLongLinePaint);
                    if (mCurrentIndex + i <= MAX_VALUE && position != (width / 2)) {
                        canvas.drawText(String.valueOf(mCurrentIndex + i), position - (textWidth * numSize / 2), getHeight() - textWidth, mTextPaint);
                    }
                } else {
                    canvas.drawLine(position, mShortLineTop, position, mShortLineBottom, mShortLinePaint);
                }
            }

            position = (width / 2 - mMove) - i * ITEM_DISTANCE * mDensity;
            if (position > getPaddingLeft()) {
                if ((mCurrentIndex - i) % MOD_TYPE_ONE == 0) {
                    canvas.drawLine(position, mLongLineTop, position, mLongLineBottom, mLongLinePaint);

                    if (mCurrentIndex - i >= 0 && position != (width / 2)) {
                        canvas.drawText(String.valueOf(mCurrentIndex - i), position - (textWidth * numSize / 2), getHeight() - textWidth, mTextPaint);
                    }
                } else {
                    canvas.drawLine(position, mShortLineTop, position, mShortLineBottom, mShortLinePaint);
                }
            }

            drawCount += 2 * ITEM_DISTANCE * mDensity;
        }

        canvas.restore();
    }


    /**
     * 画中间指示线
     */
    private void drawMiddleLine(Canvas canvas) {
        canvas.save();
        canvas.drawLine(mWidth / 2, mHeight / 5, mWidth / 2, mHeight * 4 / 5, mPointerPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastX = x;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastX - x);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker(event);
                return false;
            default:
                break;
        }

        mLastX = x;
        return true;
    }

    private void countVelocityTracker(MotionEvent event) {
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = mVelocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    private void changeMoveAndValue() {
        int value = (int) (mMove / (ITEM_DISTANCE * mDensity));
        if (Math.abs(value) > 0) {
            mCurrentIndex += value;
            mMove -= value * ITEM_DISTANCE * mDensity;
            if (mCurrentIndex <= 0 || mCurrentIndex > MAX_VALUE) {
                mCurrentIndex = mCurrentIndex <= 0 ? 0 : MAX_VALUE;
                mMove = 0;
                mScroller.forceFinished(true);
            }
            notifyValueChange();
        }
        postInvalidate();
    }

    private void countMoveEnd() {
        int roundMove = Math.round(mMove / (ITEM_DISTANCE * mDensity));
        mCurrentIndex = mCurrentIndex + roundMove;
        mCurrentIndex = mCurrentIndex <= 0 ? 0 : mCurrentIndex;
        mCurrentIndex = mCurrentIndex > MAX_VALUE ? MAX_VALUE : mCurrentIndex;

        mLastX = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }

    public void setCurrentIndex(int index) {
        if (index <= 0 || index > MAX_VALUE) {
            Log.i(TAG, "setCurrentIndex: index error");
            return;
        }
        mCurrentIndex = index;
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mCurrentIndex);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
                countMoveEnd();
            } else {
                int position = mScroller.getCurrX();
                mMove += (mLastX - position);
                changeMoveAndValue();
                mLastX = position;
            }
        }
    }

    public interface OnValueChangeListener {
        void onValueChange(int value);
    }
}
