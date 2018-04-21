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

package com.ishow.common.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ishow.common.R;

/**
 * Created by Bright.Yu on 2016/11/4.
 * 比例的ImageView
 */

public class ScaleImageView extends AppCompatImageView {
    private int mWidthRatio;
    private int mHeightRatio;

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);
        mWidthRatio = a.getInt(R.styleable.ScaleImageView_widthScale, 16);
        mHeightRatio = a.getInt(R.styleable.ScaleImageView_heightScale, 9);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int[] measureSpecs;
        if (layoutParams != null && (layoutParams.width > 0 || layoutParams.height > 0)) {
            measureSpecs = measureByLayoutParams(layoutParams, widthMeasureSpec, heightMeasureSpec);
        } else {
            measureSpecs = measureByMeasureSpec(widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(measureSpecs[0], measureSpecs[1]);
    }

    /**
     * 通过LayoutParams 来获取值 进行计算
     * 目的:父类为RelativeLayout 时候MeasureSpec 会错乱
     */
    private int[] measureByLayoutParams(ViewGroup.LayoutParams layoutParams, int widthMeasureSpec, int heightMeasureSpec) {
        if (layoutParams.width > 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(layoutParams.width * mHeightRatio / mWidthRatio, MeasureSpec.EXACTLY);
        } else if (layoutParams.height > 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(layoutParams.height * mWidthRatio / mHeightRatio, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
        }
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }

    /**
     * 最原始的measure方法
     */
    private int[] measureByMeasureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize * mHeightRatio / mWidthRatio, MeasureSpec.EXACTLY);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize * mWidthRatio / mHeightRatio, MeasureSpec.EXACTLY);
        }
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }

    public void setRatio(int widthRatio, int heightRatio) {
        mWidthRatio = widthRatio;
        mHeightRatio = heightRatio;
        requestLayout();
    }
}
