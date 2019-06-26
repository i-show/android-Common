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
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.ishow.common.R;
import com.ishow.common.utils.UnitUtils;

/**
 * Created by yuhaiyang on 2018/9/5.
 * 破折线
 */
@SuppressWarnings("unused")
public class DashLineView extends View {
    /**
     * 划线画笔
     */
    private Paint mLinePaint;
    /**
     * 破折线颜色
     */
    private int mDashColor;
    /**
     * 破折线间距
     */
    private int mDashGap;
    /**
     * 破折线宽度
     */
    private int mDashWidth;

    public DashLineView(Context context) {
        super(context);
        init(context, null);
    }

    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DashLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DashLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashLineView);
        mDashColor = a.getColor(R.styleable.DashLineView_dashColor, Color.CYAN);
        mDashGap = a.getDimensionPixelSize(R.styleable.DashLineView_dashGap,  UnitUtils.INSTANCE.dip2px(8));
        mDashWidth = a.getDimensionPixelSize(R.styleable.DashLineView_dashWidth,  UnitUtils.INSTANCE.dip2px(4));
        a.recycle();

        PathEffect effects = new DashPathEffect(new float[]{mDashWidth, mDashGap, mDashWidth, mDashGap}, 1);
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mDashColor);
        mLinePaint.setStrokeWidth(mDashWidth / 2F);
        mLinePaint.setPathEffect(effects);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int y = height / 2;
        canvas.drawLine(0, y, width, y, mLinePaint);
    }

    /**
     * 获取破折线颜色
     */
    public int getDashColor() {
        return mDashColor;
    }

    /**
     * 设置破折线颜色
     */
    public void setDashColor(@ColorInt int dashColor) {
        mDashColor = dashColor;
        mLinePaint.setColor(mDashColor);
        postInvalidate();
    }

    /**
     * 获取破折线间距
     */
    public int getDashGap() {
        return mDashGap;
    }

    /**
     * 设置破折线间距
     */
    public void setDashGap(int dashGap) {
        mDashGap = dashGap;
        PathEffect effects = new DashPathEffect(new float[]{mDashWidth, mDashGap, mDashWidth, mDashGap}, 1);
        mLinePaint.setPathEffect(effects);
        postInvalidate();
    }

    /**
     * 获取当前破折线宽度
     */
    public int getDashWidth() {
        return mDashWidth;
    }

    /**
     * 设置破折线宽度
     */
    public void setDashWidth(int dashWidth) {
        mDashWidth = dashWidth;
        PathEffect effects = new DashPathEffect(new float[]{mDashWidth, mDashGap, mDashWidth, mDashGap}, 1);
        mLinePaint.setPathEffect(effects);
        postInvalidate();
    }
}