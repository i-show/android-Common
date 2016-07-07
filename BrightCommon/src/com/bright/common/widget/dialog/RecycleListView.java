/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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

package com.bright.common.widget.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.bright.common.utils.Utils;


public class RecycleListView extends ListView {
    public boolean mRecycleOnMeasure = true;
    public int mMaxHeight;

    public RecycleListView(Context context) {
        super(context);
        init();
    }

    public RecycleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecycleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        int screen[] = Utils.getScreenSize(getContext());
        if (screen[0] > screen[1]) {
            mMaxHeight = screen[1] / 2;
        } else {
            mMaxHeight = screen[1] * 3 / 7;
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
