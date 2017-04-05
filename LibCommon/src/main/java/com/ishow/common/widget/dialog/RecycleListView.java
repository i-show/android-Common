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

package com.ishow.common.widget.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import com.ishow.common.R;
import com.ishow.common.utils.ScreenUtils;


public class RecycleListView extends ListView {
    public boolean mRecycleOnMeasure = true;
    private int mMaxHeight;

    public RecycleListView(Context context) {
        super(context);
        init(context, null);
    }

    public RecycleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecycleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecycleListView);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.RecycleListView_maxHeight, getDefaultHeight());
        a.recycle();
    }

    private int getDefaultHeight() {
        int screen[] = ScreenUtils.getScreenSize();
        if (screen[0] > screen[1]) {
            return screen[1] / 2;
        } else {
            return screen[1] * 5 / 8;
        }
    }

    protected boolean recycleOnMeasure() {
        return mRecycleOnMeasure;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
