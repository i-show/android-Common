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

package com.ishow.common.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;
import com.ishow.common.R;


public class RecycleViewPro extends RecyclerView {
    private int mMaxHeight;
    private int mMaxWidth;

    public RecycleViewPro(Context context) {
        super(context);
        init(context, null);
    }

    public RecycleViewPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecycleViewPro(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecycleViewPro);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.RecycleViewPro_maxHeight, -1);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.RecycleViewPro_maxWidth, -1);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = mMaxHeight == -1 ? heightMeasureSpec : MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        int widthSpec = mMaxWidth == -1 ? widthMeasureSpec : MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }

}
