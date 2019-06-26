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
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;
import com.ishow.common.R;
import com.ishow.common.utils.UnitUtils;

public class ColorIndicator extends View implements ViewPager.OnPageChangeListener {
    /**
     * 默认进度颜色
     */
    private static final int DEFAULT_THUMB_COLOR = Color.BLUE;
    /**
     * 默认进度条的颜色
     */
    private static final int DEFAULT_BAR_COLOR = 0xFFFFFFFF;
    /**
     * 默认Bar边框的颜色
     */
    private static final int DEFAULT_BAR_BORDER_COLOR = 0xFF999999;
    /**
     * 默认半径大小
     */
    private static final int DEFAULT_RADIUS = 4; // This is dp

    private Paint mThumbPaint;
    private Paint mBarPaint;
    private Paint mBarBorderPaint;

    private int mRadius;
    private int mItemWidth;
    private int mItemHeight;
    private int mCurrentPage;

    private int mThumbColor;
    private int mBarColor;
    private int mBarBorderColor;

    private float mPageOffset;

    private ViewPager mViewPager;

    public ColorIndicator(Context context) {
        this(context, null);
    }

    public ColorIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorIndicator);
        mThumbColor = a.getColor(R.styleable.ColorIndicator_thumbColor, DEFAULT_THUMB_COLOR);
        mBarColor = a.getColor(R.styleable.ColorIndicator_barColor, DEFAULT_BAR_COLOR);
        mBarBorderColor = a.getColor(R.styleable.ColorIndicator_barBorderColor, DEFAULT_BAR_BORDER_COLOR);
        mRadius = a.getDimensionPixelOffset(R.styleable.ColorIndicator_radius, UnitUtils.INSTANCE.dip2px(context, DEFAULT_RADIUS));
        final int itemPadding = a.getDimensionPixelOffset(R.styleable.ColorIndicator_itemPadding, 0);
        a.recycle();

        mItemWidth = 4 * mRadius + itemPadding;
        mItemHeight = 3 * mRadius;

        mBarPaint = new Paint();
        mBarPaint.setColor(mBarColor);
        mBarPaint.setAntiAlias(true);
        mBarPaint.setDither(true);

        mBarBorderPaint = new Paint();
        mBarBorderPaint.setColor(mBarBorderColor);
        mBarBorderPaint.setAntiAlias(true);
        mBarBorderPaint.setDither(true);

        mThumbPaint = new Paint();
        mThumbPaint.setColor(mThumbColor);
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setDither(true);
    }

    /**
     * @see View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the with of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The with of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        if (mViewPager == null) {
            return 0;
        }

        if (mViewPager.getAdapter() == null) {
            return 0;
        }
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
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
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
    protected void onDraw(Canvas canvas) {
        if (mViewPager == null) {
            return;
        }

        if (mViewPager.getAdapter() == null) {
            return;
        }

        final int count = mViewPager.getAdapter().getCount();
        if (count == 0 || count == 1) {
            return;
        }


        final int realWidth = mItemWidth * count;
        final int startX = getWidth() / 2 - realWidth / 2;

        drawBars(canvas, count, startX);
        drawThumb(canvas, startX);
    }

    private void drawBars(Canvas canvas, int count, int startX) {

        for (int i = 0; i < count; i++) {
            // 求圆圈的圆心坐标
            int x = startX + i * mItemWidth + (mItemWidth - mRadius) / 2;
            int y = mItemHeight / 2;
            canvas.drawCircle(x, y, mRadius, mBarBorderPaint);
            canvas.drawCircle(x, y, mRadius - 1, mBarPaint);
        }
    }

    private void drawThumb(Canvas canvas, int startX) {
        int cx = (int) (mPageOffset * mItemWidth);
        int x = startX + mCurrentPage * mItemWidth + cx + (mItemWidth - mRadius) / 2;
        int y = mItemHeight / 2;

        canvas.drawCircle(x, y, mRadius, mThumbPaint);
    }

    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.addOnPageChangeListener(this);
        invalidate();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        mPageOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        //mCurrentPage = position;
        //invalidate();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    @SuppressWarnings("unused")
    public int getThumbColor() {
        return mThumbColor;
    }

    @SuppressWarnings("unused")
    public int getBarColor() {
        return mBarColor;
    }

    @SuppressWarnings("unused")
    public int getBarBorderColor() {
        return mBarBorderColor;
    }


    static class SavedState extends BaseSavedState {
        int currentPage;

        SavedState(Parcelable superState) {
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
