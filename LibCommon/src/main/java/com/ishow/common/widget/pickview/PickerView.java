/*
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

package com.ishow.common.widget.pickview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.ishow.common.R;
import com.ishow.common.widget.flowlayout.FlowLayout;
import com.ishow.common.widget.pickview.adapter.PickerAdapter;
import com.ishow.common.widget.pickview.constant.Direction;
import com.ishow.common.widget.pickview.listener.OnItemSelectedListener;


public class PickerView extends View {
    private static final String TAG = "PickerView";

    /**
     * 默认可以看到的几个Item
     */
    private static final int DEFAULT_VISIABLE_COUNT = 5;
    /**
     * ITEM间距倍数
     */
    private static final float LINE_SPACING_MULTIPLIER = 2.0f;
    /**
     * 未选中TextSize大小比例
     */
    private static final float UNSELECTED_TEXT_SIZE_RATIO = 0.78f;

    /**
     * 校准数据时应该增加或者减少的Position
     */
    private int mAdjustPosition;
    private int mCurrentPosition = 0;

    private int mLastY;
    private int mLastScrollerY;
    private int mMinVelocity;

    private int mVisibleCount;

    private int mDividerColor;
    private int mSelectedTextColor;
    private int mUnselectedTextColor;

    /**
     * 一个Item想要的宽度和高度
     */
    private int mItemWidth;
    private int mItemHeight;
    private int mUnitWidth;

    private int mDrawItemX;

    private float mUnitTextSize;
    private float mSelectedTextSize;
    private float mUnselectedTextSize;

    // 第一条线Y坐标值
    private float mFirstLineY;
    //第二条线Y坐标
    private float mSecondLineY;
    // 移动的距离， 注意：这里不是总的移动距离
    private float mMove;
    private String mUnit;//附加单位

    private TextPaint mTextPaint;
    private TextPaint mUnitPaint;
    private Paint mDividerPaint;

    private boolean isCyclic;
    private PickerAdapter mAdapter;
    private Scroller mFlingScroller;
    private Scroller mAdjustScroller;
    private VelocityTracker mVelocityTracker;

    private OnItemSelectedListener mListener;

    private DefaultDataSetObserver mDataSetObserver;

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView);
        mUnit = a.getString(R.styleable.PickerView_unit);
        mDividerColor = a.getColor(R.styleable.PickerView_divider, getDefaultDividerColor());

        mSelectedTextColor = a.getColor(R.styleable.PickerView_selectedTextColor, getDefaultSelectedTextColor());
        mUnselectedTextColor = a.getColor(R.styleable.PickerView_unselectedTextColor, getDefaultUnselectedTextColor());

        mSelectedTextSize = a.getDimensionPixelOffset(R.styleable.PickerView_textSize, getDefaultTextSize());
        mUnitTextSize = a.getDimensionPixelSize(R.styleable.PickerView_unitTextSize, getDefaultUnitTextSize());
        mUnselectedTextSize = mSelectedTextSize * UNSELECTED_TEXT_SIZE_RATIO;

        mVisibleCount = a.getInteger(R.styleable.PickerView_visiableCount, DEFAULT_VISIABLE_COUNT);
        a.recycle();

        init(context);
    }


    private void init(Context context) {

        isCyclic = true;
        mFlingScroller = new Scroller(context);
        mAdjustScroller = new Scroller(context);

        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        initPaints();
        forecast();
    }


    /**
     * 预先计算Item
     */
    private void forecast() {

        if (mAdapter == null) {
            Log.i(TAG, "forecast: adapter is null");
            return;
        }
        mItemHeight = 0;
        Rect rect = new Rect();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            String centerString = mAdapter.getItemText(i);
            mTextPaint.getTextBounds(centerString, 0, centerString.length(), rect);

            mItemWidth = Math.max(mItemWidth, rect.width());
            mItemHeight = Math.max(mItemHeight, rect.height());
        }
        mItemHeight = (int) (LINE_SPACING_MULTIPLIER * mItemHeight);


        if (!TextUtils.isEmpty(mUnit)) {
            int gap = getContext().getResources().getDimensionPixelSize(R.dimen.dp_10);
            mUnitWidth = (int) Layout.getDesiredWidth(mUnit, mUnitPaint) + gap;
        }
    }


    private void initPaints() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mSelectedTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mUnitPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnitPaint.setTextSize(mUnitTextSize);
        mUnitPaint.setColor(mSelectedTextColor);
        mUnitPaint.setTextAlign(Paint.Align.CENTER);

        mDividerPaint = new Paint();
        mDividerPaint.setColor(mDividerColor);
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth((float) 1.0);
        mDividerPaint.setDither(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = measureWidth(widthMeasureSpec) + getPaddingStart() + getPaddingEnd();
        final int height = measureHeight(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        final int mode = MeasureSpec.getMode(widthMeasureSpec);
        final int size = MeasureSpec.getSize(widthMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return mItemWidth + mUnitWidth;
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, (mItemWidth + mUnitWidth));
        }

        return size;
    }


    private int measureHeight(int heightMeasureSpec) {
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        final int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return (mVisibleCount * mItemHeight);
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(mVisibleCount * mItemHeight, size);
        }

        return size;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int height = getMeasuredHeight();

        //计算两条横线和控件中间点的Y位置
        mFirstLineY = (height - mItemHeight) / 2;
        mSecondLineY = (height + mItemHeight) / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        mDrawItemX = (width - mUnitWidth) / 2;
        drawLine(canvas, width);
        drawText(canvas, width, height);
        drawUnit(canvas, width);
    }


    private void drawLine(Canvas canvas, int viewWidth) {
        canvas.drawLine(0, mFirstLineY, viewWidth, mFirstLineY, mDividerPaint);
        canvas.drawLine(0, mSecondLineY, viewWidth, mSecondLineY, mDividerPaint);
    }

    /**
     * 从中间往两边开始画刻度线
     */
    private void drawText(Canvas canvas, final int width, final int height) {
        /*
         * 画中间的
         */
        final float scale = parabola(mItemHeight, mMove);
        final float textSize = (mSelectedTextSize - mUnselectedTextSize) * scale + mUnselectedTextSize;
        final int color = interpolateColor(mUnselectedTextColor, mSelectedTextColor, scale);
        final String text = getItemText(mCurrentPosition);

        mTextPaint.setColor(color);
        mTextPaint.setTextSize(textSize);

        canvas.save();
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (mItemHeight - (fmi.bottom + fmi.top)) / 2.0f;
        final float y = baseline + mItemHeight * (mVisibleCount / 2) - mMove + getPaddingTop();
        canvas.drawText(text, mDrawItemX, y, mTextPaint);

        // 绘制上方data
        // 这里因为要滚动所以要比可见的多画一个 所以要 >= -1
        for (int i = 1; (mVisibleCount / 2 - i) >= -1; i++) {
            drawOtherText(canvas, scale, i, Direction.UP);
        }
        // 绘制下方data
        // 这里因为要滚动所以要比可见的多画一个 所以要 <= mVisibleCount
        for (int i = 1; (mVisibleCount / 2 + i) <= mVisibleCount; i++) {
            drawOtherText(canvas, scale, i, Direction.DOWN);
        }
        canvas.restore();

    }

    /**
     * @param position  距离mCurrentSelected的差值
     * @param direction 1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, float middleScale, int position, Direction direction) {
        float scale = 1 - middleScale;
        float size = (mSelectedTextSize - mUnselectedTextSize) * scale + mUnselectedTextSize;
        final int color = interpolateColor(mUnselectedTextColor, mSelectedTextColor, scale);
        if (position == 1 && mMove > 0 && direction == Direction.DOWN) {
            mTextPaint.setColor(color);
            mTextPaint.setTextSize(size);
        } else if (position == 1 && mMove < 0 && direction == Direction.UP) {
            mTextPaint.setColor(color);
            mTextPaint.setTextSize(size);
        } else {
            mTextPaint.setColor(mUnselectedTextColor);
            mTextPaint.setTextSize(mUnselectedTextSize);
        }

        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (mItemHeight - (fmi.bottom + fmi.top)) / 2.0f;
        String text;

        if (direction == Direction.UP) {
            final float y = baseline + mItemHeight * (mVisibleCount / 2 - position) - mMove + getPaddingTop();
            text = getItemText(mCurrentPosition - position);
            canvas.drawText(text, mDrawItemX, y, mTextPaint);
        } else {
            final float y = baseline + mItemHeight * (mVisibleCount / 2 + position) - mMove + getPaddingTop();
            text = getItemText(mCurrentPosition + position);
            canvas.drawText(text, mDrawItemX, y, mTextPaint);
        }
    }


    private void drawUnit(Canvas canvas, int width) {
        if (TextUtils.isEmpty(mUnit)) {
            return;
        }

        Paint.FontMetricsInt fmi = mUnitPaint.getFontMetricsInt();
        final float baseline = (float) (mItemHeight - (fmi.bottom + fmi.top)) / 2.0f;
        final int x = mDrawItemX + mItemWidth / 2 + mUnitWidth / 2;
        final int y = (int) (baseline + mItemHeight * (mVisibleCount / 2) + getPaddingBottom());
        canvas.drawText(mUnit, x, y, mUnitPaint);
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
                mFlingScroller.forceFinished(true);
                mAdjustScroller.forceFinished(true);
                mLastY = y;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastY - y);
                compute();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countVelocityTracker();
                return false;
            default:
                break;
        }

        mLastY = y;
        return true;
    }


    private void countVelocityTracker() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int yVelocity = (int) mVelocityTracker.getYVelocity();
        if (Math.abs(yVelocity) > mMinVelocity) {
            fling(yVelocity);
        } else {
            calibration();
        }
    }

    private void fling(int yVelocity) {
        mLastScrollerY = 0;
        mAdjustScroller.forceFinished(true);
        mFlingScroller.fling(0, 0, 0, yVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * 校准
     */
    private void calibration() {
        mLastScrollerY = 0;
        mFlingScroller.forceFinished(true);

        if (mMove < 0) {
            if (Math.abs(mMove) > mItemHeight / 2) {
                mAdjustPosition = -1;
                mAdjustScroller.startScroll(0, 0, 0, (int) (mItemHeight + mMove));
            } else {
                mAdjustPosition = 0;
                mAdjustScroller.startScroll(0, 0, 0, (int) mMove);
            }
        } else {
            if (Math.abs(mMove) > mItemHeight / 2) {
                mAdjustPosition = 1;
                mAdjustScroller.startScroll(0, 0, 0, (int) (mMove - mItemHeight));
            } else {
                mAdjustPosition = 0;
                mAdjustScroller.startScroll(0, 0, 0, (int) mMove);
            }
        }

        invalidate();
    }

    /**
     * 计算
     */
    private void compute() {
        int moveCount = (int) (mMove / mItemHeight);
        if (Math.abs(moveCount) > 0) {
            // 如果 已经移动了一个Item的高度，那么positon 增加或者减少
            // mMove 减少对应Item的高度
            mMove -= moveCount * mItemHeight;
            mCurrentPosition += moveCount;
            mCurrentPosition = computePosition(mCurrentPosition);
            notifyValueChange();
        }
        postInvalidate();
    }


    /**
     * 计算位置
     */
    private int computePosition(int positon) {
        if (positon < 0) {
            positon = positon % -mAdapter.getCount();
        } else {
            positon = positon % mAdapter.getCount();
        }

        if (positon < 0) {
            positon = positon + mAdapter.getCount();
        }
        return positon;
    }


    @Override
    public void computeScroll() {
        computeFlingScroll();
        computeAdjustScroll();
    }

    private void computeFlingScroll() {
        if (!mFlingScroller.computeScrollOffset()) {
            return;
        }
        if (mFlingScroller.getCurrY() == mFlingScroller.getFinalY()) {
            calibration();
        } else {
            int position = mFlingScroller.getCurrY();
            mMove += (mLastScrollerY - position);
            compute();
            mLastScrollerY = position;
        }
    }

    private void computeAdjustScroll() {
        if (!mAdjustScroller.computeScrollOffset()) {
            return;
        }
        if (mAdjustScroller.getCurrY() == mAdjustScroller.getFinalY()) {
            mCurrentPosition = mCurrentPosition + mAdjustPosition;
            mLastScrollerY = 0;
            mLastY = 0;
            mMove = 0;
        } else {
            int position = mAdjustScroller.getCurrY();
            mMove += (mLastScrollerY - position);
            mLastScrollerY = position;
            invalidate();
        }
    }


    public void setAdapter(PickerAdapter adapter) {
        mAdapter = adapter;


        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        mDataSetObserver = new DefaultDataSetObserver();
        mAdapter.registerDataSetObserver(mDataSetObserver);

        forecast();
        requestLayout();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onItemSelected(mCurrentPosition);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mListener = listener;
    }

    /**
     * 设置当前的position
     */
    public void setCurrentPosition(int position) {
        if (mAdapter == null) {
            Log.i(TAG, "setCurrentPosition: adapter is null");
            return;
        }
        if (position >= mAdapter.getCount()) {
            throw new IllegalArgumentException("positon is " + position + "  while totol count" + mAdapter.getCount());
        }
        mCurrentPosition = position;
        postInvalidate();
    }

    /**
     * 通过Item来显示第几个
     */
    private String getItemText(int positon) {
        if (mAdapter == null) {
            Log.i(TAG, "getItemText: mAdapter is null");
            return "";
        }

        if (isCyclic) {
            /**
             * 先进行取于，如果还小于0 那么只要加一个count值即可
             */
            if (positon < 0) {
                positon = positon % -mAdapter.getCount();
            } else {
                positon = positon % mAdapter.getCount();
            }

            if (positon < 0) {
                positon = positon + mAdapter.getCount();
            }
            return mAdapter.getItemText(positon);
        } else {
            return mAdapter.getItemText(positon);
        }
    }


    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    /*
     * ============= 默认值区域 ================
     */

    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultDividerColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.line, getContext().getTheme());
        } else {
            return getResources().getColor(R.color.line);
        }
    }


    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultUnselectedTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.text_grey_light_normal, getContext().getTheme());
        } else {
            return getResources().getColor(R.color.text_grey_light_normal);
        }
    }


    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultSelectedTextColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(R.color.color_accent, getContext().getTheme());
        } else {
            return getResources().getColor(R.color.color_accent);
        }
    }

    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultTextSize() {
        return getResources().getDimensionPixelSize(R.dimen.D_title);
    }

    /**
     * 获取默认的自提大小
     */
    protected int getDefaultUnitTextSize() {
        return getResources().getDimensionPixelSize(R.dimen.I_title);
    }

    /**
     * @param colorA 原始颜色
     * @param colorB 目标颜色
     * @param bias   转变比例
     */
    private int interpolateColor(int colorA, int colorB, float bias) {
        if (bias > 0.5) {
            return colorB;
        } else {
            return colorA;
        }
//        float[] hsvColorA = new float[3];
//        Color.colorToHSV(colorA, hsvColorA);
//        float[] hsvColorB = new float[3];
//        Color.colorToHSV(colorB, hsvColorB);
//        hsvColorB[0] = interpolate(hsvColorA[0], hsvColorB[0], bias);
//        hsvColorB[1] = interpolate(hsvColorA[1], hsvColorB[1], bias);
//        hsvColorB[2] = interpolate(hsvColorA[2], hsvColorB[2], bias);
//        // NOTE For some reason the method HSVToColor fail in edit mode. Just use the start color for now
//        if (isInEditMode()) {
//            return colorA;
//        }
        //return Color.HSVToColor(hsvColorB);
    }

    private float interpolate(float a, float b, float bias) {
        return (a + ((b - a) * bias));
    }


    private class DefaultDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            forecast();
            requestLayout();
        }
    }
}
