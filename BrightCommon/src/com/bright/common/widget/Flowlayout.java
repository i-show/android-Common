/*
 * Copyright (C) 2015 Bright Yu Haiyang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author: y.haiyang@qq.com
 */

package com.bright.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.bright.common.R;

public class Flowlayout extends ViewGroup {
    private static final String TAG = "Flowlayout";
    private int mGap;
    private MarginLayoutParams mChildLayoutParams;

    public Flowlayout(Context context) {
        this(context, null);
    }

    public Flowlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Flowlayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            mChildLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            //当前子空间的实际占据尺寸
            childWidth = childWidth + mChildLayoutParams.leftMargin + mChildLayoutParams.rightMargin + mGap;
            childHeight = childHeight + mChildLayoutParams.topMargin + mChildLayoutParams.bottomMargin + mGap;

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

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : desireWidth,
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
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            mChildLayoutParams = (MarginLayoutParams) child.getLayoutParams();

            // 需要换行
            if (left + mChildLayoutParams.leftMargin + childWidth + mChildLayoutParams.rightMargin + mGap > right) {
                nowLineTop = nextLineTop;
                left = getPaddingLeft() + mGap + mChildLayoutParams.leftMargin;
                top = nowLineTop + mChildLayoutParams.topMargin;

                child.layout(left, top, left + childWidth, top + childHeight);
            } else {
                left = left + mChildLayoutParams.leftMargin;
                top = nowLineTop + mChildLayoutParams.topMargin;

                child.layout(left, top, left + childWidth, top + childHeight);
            }

            left = left + childWidth + mChildLayoutParams.rightMargin + mGap;
            nextLineTop = Math.max(nextLineTop, child.getBottom() + mChildLayoutParams.bottomMargin + mGap);
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

    protected int getDefaultGap() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.gap_grade_2);
    }
}
