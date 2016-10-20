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

package com.brightyu.androidcommon.ui.widget.pickview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.bright.common.utils.StringUtils;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.ui.widget.pickview.adapter.PickerAdapter;


public class PickerView extends View {
    private static final String TAG = "SmartPickerView";

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
    private static final float UNSELECTED_TEXT_SIZE_RATIO = 0.80f;

    private int mAdjustPosition;
    private int mCurrentPosition = 0;

    private int mLastY;
    private int mLastScrollerY;

    private int mMinVelocity;
    private float mMove;

    private Scroller mFlingScroller;
    private Scroller mAdjustScroller;
    private VelocityTracker mVelocityTracker;

    private OnValueChangeListener mListener;

    private int mVisibleCount;

    private int mDividerColor;
    private int mSelectedTextColor;
    private int mUnselectedTextColor;

    /**
     * 一个Item想要的宽度和高度
     */
    private int mItemWidth;
    private int mItemHeight;
    private int mGap;

    private float mSelectedTextSize;//选项的文字大小
    private float mUnselectedTextSize;//没有选中的文字大小


    // 第一条线Y坐标值
    private float mFirstLineY;
    //第二条线Y坐标
    private float mSecondLineY;


    private Context mContext;

    private TextPaint mTextPaint;
    private TextPaint mUnitPaint;
    private Paint mDividerPaint;

    private boolean isCyclic;
    private PickerAdapter mAdapter;
    private String mUnit;//附加单位

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView);
        mDividerColor = a.getColor(R.styleable.PickerView_divider, getDefaultDividerColor());

        mSelectedTextColor = a.getColor(R.styleable.PickerView_selectedTextColor, getDefaultSelectedTextColor());
        mUnselectedTextColor = a.getColor(R.styleable.PickerView_unselectedTextColor, getDefaultUnselectedTextColor());

        mSelectedTextSize = a.getDimensionPixelOffset(R.styleable.PickerView_textSize, getDefaultTextSize());
        mUnselectedTextSize = mSelectedTextSize * UNSELECTED_TEXT_SIZE_RATIO;

        mVisibleCount = a.getInteger(R.styleable.PickerView_visiableCount, DEFAULT_VISIABLE_COUNT);
        a.recycle();

        init(context);
    }


    private void init(Context context) {

        isCyclic = true;
        mContext = context;
        mFlingScroller = new Scroller(context);
        mAdjustScroller = new Scroller(context);

        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mGap = context.getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
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

        Rect rect = new Rect();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            String centerString;
            if (TextUtils.isEmpty(mUnit)) {
                centerString = mAdapter.getItemText(i);
            } else {
                centerString = StringUtils.plusString(mAdapter.getItemText(i), " ", mUnit);
            }

            mTextPaint.getTextBounds(centerString, 0, centerString.length(), rect);

            mItemWidth = Math.max(mItemWidth, rect.width());
            mItemHeight = Math.max(mItemHeight, rect.height());
        }
        mItemHeight = (int) (LINE_SPACING_MULTIPLIER * mItemHeight);
    }


    private void initPaints() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mSelectedTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mUnitPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mUnitPaint.setTextSize(mSelectedTextSize);
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
        final int width = measureWidth(widthMeasureSpec);
        final int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
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
        drawLine(canvas, width);
        drawText(canvas, width, height);
    }


    private int measureWidth(int widthMeasureSpec) {
        final int mode = MeasureSpec.getMode(widthMeasureSpec);
        final int size = MeasureSpec.getSize(widthMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return mItemWidth;
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, mItemWidth);
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


    private void drawLine(Canvas canvas, int viewWidth) {
        canvas.drawLine(0, mFirstLineY, viewWidth, mFirstLineY, mDividerPaint);
        canvas.drawLine(0, mSecondLineY, viewWidth, mSecondLineY, mDividerPaint);
    }

    /**
     * 从中间往两边开始画刻度线
     */
    private void drawText(Canvas canvas, final int width, final int height) {
        /**
         * 画中间的
         */
        final float scale = parabola(mItemHeight, mMove);
        final float textSize = (mSelectedTextSize - mUnselectedTextSize) * scale + mUnselectedTextSize;
        final int color = interpolateColor(mUnselectedTextColor, mSelectedTextColor, scale);
        mTextPaint.setColor(color);
        mTextPaint.setTextSize(textSize);
        canvas.save();

        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (mItemHeight / 2.0 - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(getItemText(mCurrentPosition), width / 2, baseline + mItemHeight * (mVisibleCount / 2) - mMove, mTextPaint);

        // 绘制上方data
        // 这里因为要滚动所以要比可见的多画一个 所以要 >= -1
        for (int i = 1; (mVisibleCount / 2 - i) >= -1; i++) {
            drawOtherText(canvas, width, scale, i, -1);
        }
        // 绘制下方data
        // 这里因为要滚动所以要比可见的多画一个 所以要 <= mVisibleCount
        for (int i = 1; (mVisibleCount / 2 + i) <= mVisibleCount; i++) {
            drawOtherText(canvas, width, scale, i, 1);
        }
        canvas.restore();

    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int width, float middleScale, int position, int type) {
        float scale = 1 - middleScale;
        float size = (mSelectedTextSize - mUnselectedTextSize) * scale + mUnselectedTextSize;
        final int color = interpolateColor(mUnselectedTextColor, mSelectedTextColor, scale);
        if (mMove > 0 && type == 1 && position == 1) {
            mTextPaint.setColor(color);
            mTextPaint.setTextSize(size);
        } else if (mMove < 0 && type == -1 && position == 1) {
            mTextPaint.setColor(color);
            mTextPaint.setTextSize(size);
        } else {
            mTextPaint.setColor(mUnselectedTextColor);
            mTextPaint.setTextSize(mUnselectedTextSize);
        }

        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (mItemHeight / 2.0 - (fmi.bottom / 2.0 + fmi.top / 2.0));
        String text;
        if (type == -1) {
            text = getItemText(mCurrentPosition - position);
            canvas.drawText(text, width / 2, baseline + mItemHeight * (mVisibleCount / 2 - position) - mMove, mTextPaint);
        } else {
            text = getItemText(mCurrentPosition + position);
            canvas.drawText(text, width / 2, baseline + mItemHeight * (mVisibleCount / 2 + position) - mMove, mTextPaint);
        }
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
                changeMoveAndValue();
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

    private void changeMoveAndValue() {
        int moveCount = (int) (mMove / mItemHeight);
        if (Math.abs(moveCount) > 0) {
            mCurrentPosition += moveCount;
            mMove -= moveCount * mItemHeight;

            notifyValueChange();
        }
        postInvalidate();
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
            changeMoveAndValue();
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
        forecast();
        requestLayout();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mCurrentPosition);
        }
    }


    /**
     * 设置用于接收结果的监听器
     */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    public interface OnValueChangeListener {
        void onValueChange(int value);

    }

    /**
     * 通过Item来显示第几个
     */
    private String getItemText(int positon) {
        if (mAdapter == null) {
            Log.i(TAG, "getItemText: mAdapter is null");
            return null;
        }

        if (isCyclic) {
            if (positon < 0) {
                positon = -(positon % -mAdapter.getCount());
            } else {
                positon = positon % mAdapter.getCount();
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

    /***
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
        return getResources().getDimensionPixelSize(R.dimen.C_title);
    }

    /**
     * @param colorA 原始颜色
     * @param colorB 目标颜色
     * @param bias   转变比例
     * @return
     */
    private int interpolateColor(int colorA, int colorB, float bias) {
        float[] hsvColorA = new float[3];
        Color.colorToHSV(colorA, hsvColorA);
        float[] hsvColorB = new float[3];
        Color.colorToHSV(colorB, hsvColorB);
        hsvColorB[0] = interpolate(hsvColorA[0], hsvColorB[0], bias);
        hsvColorB[1] = interpolate(hsvColorA[1], hsvColorB[1], bias);
        hsvColorB[2] = interpolate(hsvColorA[2], hsvColorB[2], bias);
        // NOTE For some reason the method HSVToColor fail in edit mode. Just use the start color for now
        if (isInEditMode()) {
            return colorA;
        }
        return Color.HSVToColor(hsvColorB);
    }

    private float interpolate(float a, float b, float bias) {
        return (a + ((b - a) * bias));
    }
}
