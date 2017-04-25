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
        mStart = start;
        if (mStart > mEnd) {
            mEnd = mStart;
        }
        notifyDataSetChanged();
    }

    public void setEnd(@IntRange(from = 1) int end) {
        mEnd = end;
        if (mStart > mEnd) {
            mStart = mEnd;
        }
        notifyDataSetChanged();
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
