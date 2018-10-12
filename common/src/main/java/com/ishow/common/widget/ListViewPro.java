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

package com.ishow.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.ishow.common.R;


public class ListViewPro extends ListView {
    private Integer mMaxHeightMeasureSpec;
    private Paint mTopLinePaint;
    private int mTopLineHeight;
    private int mTopLineColor;
    private int mTopLineVisibility;
    private int mTopLinePaddingStart;
    private int mTopLinePaddingEnd;

    public ListViewPro(Context context) {
        super(context);
        init(context, null);
    }

    public ListViewPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ListViewPro(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListViewPro);
        final int maxHeight = a.getDimensionPixelSize(R.styleable.ListViewPro_maxHeight, -1);
        mTopLineHeight = a.getDimensionPixelSize(R.styleable.ListViewPro_topLineHeight, getDefaultLineHeight());
        mTopLineColor = a.getColor(R.styleable.ListViewPro_topLineColor, getDefaultLineColor());
        mTopLineVisibility = a.getInt(R.styleable.ListViewPro_topLineVisibility, View.GONE);
        mTopLinePaddingStart = a.getDimensionPixelSize(R.styleable.ListViewPro_topLinePaddingStart, 0);
        mTopLinePaddingEnd = a.getDimensionPixelSize(R.styleable.ListViewPro_topLinePaddingEnd, 0);
        a.recycle();

        if (maxHeight != -1) {
            mMaxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }

        mTopLinePaint = new Paint();
        mTopLinePaint.setDither(true);
        mTopLinePaint.setAntiAlias(true);
        mTopLinePaint.setColor(mTopLineColor);
        //noinspection SuspiciousNameCombination
        mTopLinePaint.setStrokeWidth(mTopLineHeight);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getMeasuredWidth();

        if (mTopLineVisibility == VISIBLE) {
            //noinspection SuspiciousNameCombination
            canvas.drawLine(mTopLinePaddingStart, mTopLineHeight, width - mTopLinePaddingEnd, mTopLineHeight, mTopLinePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = mMaxHeightMeasureSpec == null ? heightMeasureSpec : mMaxHeightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightSpec);
    }


    public void setTopLineColor(@ColorInt int color){
        mTopLineColor = color;
        mTopLinePaint.setColor(mTopLineColor);
        postInvalidate();
    }

    public void setTopLineVisibility(int visibility) {
        mTopLineVisibility = visibility;
        postInvalidate(0, 0, getWidth(), mTopLineHeight);
    }

    private int getDefaultLineColor() {
        return getContext().getResources().getColor(R.color.line);
    }

    private int getDefaultLineHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.default_line_height);
    }
}
