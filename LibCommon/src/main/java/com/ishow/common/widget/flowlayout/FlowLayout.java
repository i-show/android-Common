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

package com.ishow.common.widget.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.R;

public class FlowLayout extends ViewGroup {
    @SuppressWarnings("unused")
    private static final String TAG = "Flowlayout";

    /**
     * 直接包裹形式
     */
    @SuppressWarnings("unused")
    private static final int MODE_WRAP = 1;
    /**
     * Adapter形式
     */
    private static final int MODE_ADAPTER = 2;
    /**
     * Default Gap
     */
    private int mGap;

    private int mMode;
    /**
     * FlowAdapter
     */
    private FlowAdapter mAdapter;
    /**
     * Observer
     */
    private DefaultDataSetObserver mDataSetObserver;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mMode = a.getInt(R.styleable.FlowLayout_flowMode, MODE_ADAPTER);
        a.recycle();
        init();
    }

    private void init() {
        mGap = getDefaultGap();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //计算所有子View的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //宽高设置为wrap_content时记录宽和高
        int desireWidth = 0;
        int desireHeight = 0;

        // width 2端需要空格
        int lineWidth = mGap + getPaddingLeft() + getPaddingRight();
        int lineHeight = mGap;

        int childWidth;
        int childHeight;
        MarginLayoutParams childLayoutParams;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            //当前子空间的实际占据尺寸
            childWidth = childWidth + childLayoutParams.leftMargin + childLayoutParams.rightMargin + mGap;
            childHeight = childHeight + childLayoutParams.topMargin + childLayoutParams.bottomMargin + mGap;

            // 换行
            if (lineWidth + childWidth > widthSize) {
                desireWidth = Math.max(lineWidth, desireWidth);
                desireHeight = desireHeight + lineHeight;

                //换行后重新开始计算
                lineWidth = mGap + childWidth + getPaddingLeft() + getPaddingRight();
                lineHeight = childHeight;
            } else {
                lineWidth = childWidth + lineWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
        }
        // 最后一行的高度和宽度在是没有进行添加的
        desireWidth = Math.max(desireWidth, lineWidth);
        desireHeight = desireHeight + lineHeight + getPaddingTop() + getPaddingBottom() + mGap;

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : desireWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : desireHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        left = getPaddingLeft() + mGap;
        top = getPaddingTop() + mGap;
        int nextLineTop = top;
        int nowLineTop = top;

        int childWidth;
        int childHeight;
        MarginLayoutParams childLayoutParams;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childLayoutParams = (MarginLayoutParams) child.getLayoutParams();

            // 需要换行
            if (left + childLayoutParams.leftMargin + childWidth + childLayoutParams.rightMargin + mGap > right) {
                nowLineTop = nextLineTop;
                left = getPaddingLeft() + mGap + childLayoutParams.leftMargin;
                top = nowLineTop + childLayoutParams.topMargin;

                child.layout(left, top, left + childWidth, top + childHeight);
            } else {
                left = left + childLayoutParams.leftMargin;
                top = nowLineTop + childLayoutParams.topMargin;

                child.layout(left, top, left + childWidth, top + childHeight);
            }

            left = left + childWidth + childLayoutParams.rightMargin + mGap;
            nextLineTop = Math.max(nextLineTop, child.getBottom() + childLayoutParams.bottomMargin + mGap);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * set adapter
     */
    public void setAdapter(@NonNull FlowAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;
        mDataSetObserver = new DefaultDataSetObserver();
        mAdapter.registerDataSetObserver(mDataSetObserver);
    }

    protected int getDefaultGap() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
    }


    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    @Override
    public void addView(View child) {
        if (mMode == MODE_ADAPTER) {
            throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
        } else {
            super.addView(child);
        }
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     * @param index Ignored.
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    @Override
    public void addView(View child, int index) {
        if (mMode == MODE_ADAPTER) {
            throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
        } else {
            super.addView(child, index);
        }
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child  Ignored.
     * @param params Ignored.
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    @Override
    public void addView(View child, LayoutParams params) {
        if (mMode == MODE_ADAPTER) {
            throw new UnsupportedOperationException("addView(View, LayoutParams) "
                    + "is not supported in AdapterView");
        } else {
            super.addView(child, params);
        }
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child  Ignored.
     * @param index  Ignored.
     * @param params Ignored.
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (mMode == MODE_ADAPTER) {
            throw new UnsupportedOperationException("addView(View, int, LayoutParams) "
                    + "is not supported in AdapterView");
        } else {
            super.addView(child, index, params);
        }
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param child Ignored.
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    @Override
    public void removeView(View child) {
        if (mMode == MODE_ADAPTER) {
            throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
        } else {
            super.removeView(child);
        }
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @param index Ignored.
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    @Override
    public void removeViewAt(int index) {
        if (mMode == MODE_ADAPTER) {
            throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
        } else {
            super.removeViewAt(index);
        }
    }

    /**
     * This method is not supported and throws an UnsupportedOperationException when called.
     *
     * @throws UnsupportedOperationException Every time this method is invoked.
     */
    @Override
    public void removeAllViews() {
        if (mMode == MODE_ADAPTER) {
            throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
        } else {
            super.removeAllViews();
        }
    }


    private class DefaultDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
        }
    }
}
