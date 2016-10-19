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

package com.brightyu.androidcommon.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by yuhaiyang on 2016/10/19.
 */

public class TestScroller extends View {
    private static final String TAG = "nian";
    VelocityTracker mVelocityTracker;
    Paint mPaint;
    Scroller mScroller;
    int mMinVelocity;
    private int mMove;

    public TestScroller(Context context) {
        this(context, null);
    }

    public TestScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mScroller = new Scroller(context);
        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawCircle(width / 2, height / 2 + mMove, 20, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int y = (int) event.getY();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                Log.i("nian", "computeScroll: position == ");


                float yVelocity = mVelocityTracker.getYVelocity();
                Log.i(TAG, "onTouchEvent: yVelocity ===" + yVelocity);
                Log.i(TAG, "onTouchEvent: mMinVelocity ===" + mMinVelocity);
                if (Math.abs(yVelocity) > mMinVelocity) {
                    mScroller.fling(0, 0, 0, (int) yVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    invalidate();
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int position = mScroller.getCurrY();
            Log.i("nian", "computeScroll: position == " + position);
            mMove = position;
            invalidate();
        }
    }
}
