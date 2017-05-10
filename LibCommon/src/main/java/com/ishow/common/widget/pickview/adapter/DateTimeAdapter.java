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

package com.ishow.common.widget.pickview.adapter;

import android.support.annotation.IntRange;

import com.ishow.common.widget.pickview.PickerView;

/**
 * Created by yuhaiyang on 2017/4/25.
 * 时间选择器
 */

public class DateTimeAdapter extends PickerAdapter<Integer> {
    private int mStart;
    private int mEnd;

    public DateTimeAdapter() {
        mStart = 0;
        mEnd = 0;
    }

    public void setStart(@IntRange(from = 0) int start) {
        if (mStart == start) {
            return;
        }

        mStart = start;
        if (mStart > mEnd) {
            mEnd = mStart;
        }
        notifyDataSetChanged();
    }

    public void setEnd(@IntRange(from = 1) int end) {
        if (mEnd == end) {
            return;
        }

        mEnd = end;
        if (mStart > mEnd) {
            mStart = mEnd;
        }
        notifyDataSetChanged();
    }

    public int getStart() {
        return mStart;
    }

    public int getEnd() {
        return mEnd;
    }

    @Override
    public int getCount() {
        return mEnd - mStart + 1;
    }

    @Override
    public Integer getItem(int position) {
        return mStart + position;
    }

    @Override
    public String getItemText(int position) {
        return String.valueOf(mStart + position);
    }
}
