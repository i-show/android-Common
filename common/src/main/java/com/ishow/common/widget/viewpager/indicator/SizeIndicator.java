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

package com.ishow.common.widget.viewpager.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.ishow.common.R;
import com.ishow.common.utils.UnitUtils;

public class SizeIndicator extends View implements ViewPager.OnPageChangeListener {
    private static final String TAG = "SizeIndicator";
    private static final float RADIUS = 3.0f; // This is dp
    /**
     * 最大增幅
     */
    private static final float CURRENT_MAX_INCREASE = 0.6f;
    private int mRadius;
    private int mItemWidth;
    private int mItemHeight;
    private int mCurrentPage;

    private float mCurrentRadius;
    private float mNextRadius;

    private ViewPager mViewPager;

    private Paint mPaint;

    public SizeIndicator(Context context) {
        this(context, null);
    }

    public SizeIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SizeIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SizeIndicator);
        mRadius = a.getDimensionPixelSize(R.styleable.SizeIndicator_thumbRadius, UnitUtils.dip2px(context, RADIUS));

        int left = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingLeft, getDefaultPaddingStart());
        left = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingStart, left);

        int right = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingRight, getDefaultPaddingEnd());
        right = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingEnd, right);
        int top = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingTop, getDefaultPaddingTop());
        int bottom = a.getDimensionPixelSize(R.styleable.SizeIndicator_android_paddingBottom, getDefaultPaddingBottom());
        setPadding(left, top, right, bottom);
        a.recycle();

        mItemWidth = 5 * mRadius;
        mItemHeight = 6 * mRadius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }

        final int count = mViewPager.getAdapter().getCount();
        if (count == 0 || count == 1) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final int realWidth = mItemWidth * count;
        final int startX = getWidth() / 2 - realWidth / 2;
        final int y = getPaddingTop() + mItemHeight / 2;

        for (int i = 0; i < count; i++) {
            // 求圆圈的圆心坐标
            int x = startX + i * mItemWidth + (mItemWidth - mRadius) / 2;
            if (i == mCurrentPage) {
                canvas.drawCircle(x, y, mCurrentRadius, mPaint);
            } else if (i == mCurrentPage + 1) {
                canvas.drawCircle(x, y, mNextRadius, mPaint);
            } else {
                canvas.drawCircle(x, y, mRadius, mPaint);
            }
        }

    }


    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.addOnPageChangeListener(this);
        invalidate();
    }

    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        mCurrentPage = position;
        mCurrentRadius = mRadius + mRadius * (1 - positionOffset) * CURRENT_MAX_INCREASE;
        mNextRadius = mRadius + mRadius * positionOffset * CURRENT_MAX_INCREASE;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        invalidate();
    }

    /**
     * @see View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 计算控件的宽度
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            // We were told how big to be
            result = specSize;
        } else {
            // Calculate the with according the views count
            final int count = mViewPager.getAdapter().getCount();
            result = getPaddingLeft() + getPaddingRight() + (count * mItemWidth);
            // Respect AT_MOST value if that was what is called for by
            // measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算控件的高度
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the height
            result = mItemHeight + getPaddingTop() + getPaddingBottom();
            // Respect AT_MOST value if that was what is called for by
            // measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    private int getDefaultPaddingStart() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.gap_grade_6);
    }

    private int getDefaultPaddingEnd() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.gap_grade_6);
    }

    private int getDefaultPaddingTop() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
    }

    private int getDefaultPaddingBottom() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
    }

    static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
