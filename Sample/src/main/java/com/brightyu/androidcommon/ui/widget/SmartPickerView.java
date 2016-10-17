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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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

import com.bright.common.constant.DefaultColors;
import com.bright.common.utils.StringUtils;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.ui.widget.pickview.adapter.PickerAdapter;


public class SmartPickerView extends View {
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
    private static final float UNSELECTED_TEXT_SIZE_RATIO = 0.9f;


    private static final int TEXT_SIZE = 18;
    // 最大值
    private static final int MAX_VALUE = 480;
    // 默认值
    private static final int DEFAULT_CURRENT_INDEX = 30;


    private int mCurrentIndex = DEFAULT_CURRENT_INDEX;


    private int mLastY, mMove;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private OnValueChangeListener mListener;

    private int mDividerColor;
    private int mSelectedTextColor;
    private int mUnselectedTextColor;

    private int mTextSize;//选项的文字大小

    /**
     * 一个Item想要的宽度和高度
     */
    private int mItemDesireWidth;
    private int mItemDesireHeight;
    private int mGap;


    // 第一条线Y坐标值
    private float mFirstLineY;
    //第二条线Y坐标
    private float mSecondLineY;

    private int mVisibleCount = 5;
    private Context mContext;

    private TextPaint mTextPaint;
    private Paint mUnselectedTextPaint;
    private Paint mSelectedTextPaint;
    private Paint mDividerPaint;

    private boolean isCyclic;
    private PickerAdapter mAdapter;
    private String mUnit;//附加单位

    public SmartPickerView(Context context) {
        this(context, null);
    }

    public SmartPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView);
        mSelectedTextColor = a.getColor(R.styleable.PickerView_selectedTextColor, getDefaultSelectedTextColor());
        mUnselectedTextColor = a.getColor(R.styleable.PickerView_unselectedTextColor, getDefaultUnselectedTextColor());
        mDividerColor = a.getColor(R.styleable.PickerView_divider, getDefaultDividerColor());
        mTextSize = a.getDimensionPixelOffset(R.styleable.PickerView_textSize, getDefaultTextSize());
        mVisibleCount = a.getInteger(R.styleable.PickerView_visiableCount, DEFAULT_VISIABLE_COUNT);
        a.recycle();


        init(context);
        mItemDesireWidth = mItemDesireHeight = 100;
    }


    private void init(Context context) {

        mScroller = new Scroller(getContext());
        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        mContext = context;
        mGap = context.getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
        mScroller = new Scroller(context);
        isCyclic = true;

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

            mSelectedTextPaint.getTextBounds(centerString, 0, centerString.length(), rect);

            mItemDesireWidth = Math.max(mItemDesireWidth, rect.width());
            mItemDesireHeight = Math.max(mItemDesireHeight, rect.height());
        }
        mItemDesireHeight = (int) (LINE_SPACING_MULTIPLIER * mItemDesireHeight);
    }


    private void initPaints() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(35);
        mTextPaint.setColor(DefaultColors.GERY);

        mDividerPaint = new Paint();
        mDividerPaint.setColor(mDividerColor);
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth((float) 1.0);
        mDividerPaint.setDither(true);

        mSelectedTextPaint = new Paint();
        mSelectedTextPaint.setColor(mSelectedTextColor);
        mSelectedTextPaint.setAntiAlias(true);
        mSelectedTextPaint.setDither(true);
        mSelectedTextPaint.setTypeface(Typeface.MONOSPACE);
        mSelectedTextPaint.setTextSize(mTextSize);

        mUnselectedTextPaint = new Paint();
        mUnselectedTextPaint.setColor(mUnselectedTextColor);
        mUnselectedTextPaint.setAntiAlias(true);
        mUnselectedTextPaint.setDither(true);
        mUnselectedTextPaint.setTypeface(Typeface.MONOSPACE);
        mUnselectedTextPaint.setTextSize(mTextSize * UNSELECTED_TEXT_SIZE_RATIO);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int height = getMeasuredHeight();

        //计算两条横线和控件中间点的Y位置
        mFirstLineY = (height - mItemDesireHeight) / 2;
        mSecondLineY = (height + mItemDesireHeight) / 2;
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
                return mItemDesireWidth;
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(size, mItemDesireWidth);
        }

        return size;
    }


    private int measureHeight(int heightMeasureSpec) {
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        final int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return (mVisibleCount * mItemDesireHeight);
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.AT_MOST:
                return Math.min(mVisibleCount * mItemDesireHeight, size);
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
        canvas.save();
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        float baseline = (float) (mItemDesireHeight / 2.0 - (fmi.bottom / 2.0 + fmi.top / 2.0));
        for (int i = 0; i < mVisibleCount; i++) {
            canvas.drawText(String.valueOf(mCurrentIndex + i), width / 2, baseline + mItemDesireHeight * i - mMove, mTextPaint);
        }
        canvas.restore();
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
                mLastY = y;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastY - y);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
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
        float yVelocity = mVelocityTracker.getYVelocity();
        if (Math.abs(yVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, 0, (int) yVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    private void changeMoveAndValue() {
        int moveCount = mMove / mItemDesireHeight;
        Log.i(TAG, "changeMoveAndValue: moveCount = " + moveCount);
        Log.i(TAG, "changeMoveAndValue: mCurrentIndex = " + mCurrentIndex);
        if (Math.abs(moveCount) > 0) {
            mCurrentIndex += moveCount;
            mMove -= moveCount * mItemDesireHeight;
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
        int roundMove = Math.round(mMove / mItemDesireHeight);
        mCurrentIndex = mCurrentIndex + roundMove;
        mCurrentIndex = mCurrentIndex <= 0 ? 0 : mCurrentIndex;
        mCurrentIndex = mCurrentIndex > MAX_VALUE ? MAX_VALUE : mCurrentIndex;

        mLastY = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrY() == mScroller.getFinalY()) { // over
                countMoveEnd();
            } else {
                int position = mScroller.getCurrY();
                mMove += (mLastY - position);
                changeMoveAndValue();
                mLastY = position;
            }
        }
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mCurrentIndex);
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
            return getResources().getColor(R.color.text_grey_dark_normal, getContext().getTheme());
        } else {
            return getResources().getColor(R.color.text_grey_dark_normal);
        }
    }

    /**
     * 获取未选中的Text的颜色
     */
    protected int getDefaultTextSize() {
        return getResources().getDimensionPixelSize(R.dimen.G_title);
    }
}
