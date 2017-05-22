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

package com.ishow.common.widget.pickview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ishow.common.R;
import com.ishow.common.widget.pickview.adapter.DateTimeAdapter;
import com.ishow.common.widget.pickview.listener.OnItemSelectedListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yuhaiyang on 2017/4/25.
 * 时间选择器
 */

public class DateTimePicker extends LinearLayout {
    private static final String TAG = "DateTimePicker";

    /**
     * 默认的开始的年
     */
    private static final int DEFAULT_START_YEAR = 1900;
    /**
     * 默认的结束年份
     */
    private static final int DEFAULT_END_YEAR = 2600;

    /**
     * 日期+时间
     */
    public static final int STYLE_DATE_TIME = 1;
    /**
     * 仅日期
     */
    public static final int STYLE_DATE = 2;
    /**
     * 仅时间
     */
    public static final int STYLE_TIME = 3;

    private PickerView mYearPicker;
    private DateTimeAdapter mYearAdapter;

    private PickerView mMonthPicker;
    private DateTimeAdapter mMonthAdapter;

    private PickerView mDayPicker;
    private DateTimeAdapter mDayAdapter;

    private PickerView mHourPicker;
    private DateTimeAdapter mHourAdapter;

    private PickerView mMinPicker;
    private DateTimeAdapter mMinAdapter;

    private Calendar mStartDate;
    private Calendar mEndDate;

    private int mStyle;

    @IntDef({STYLE_DATE_TIME, STYLE_DATE, STYLE_TIME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface style {
    }

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker);
        int style = a.getInt(R.styleable.DateTimePicker_datePickerStyle, STYLE_DATE_TIME);
        a.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        initViews();
        initDatas();

        //noinspection WrongConstant
        setStyle(style);
    }


    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View root = inflater.inflate(R.layout.widget_date_time_picker, this, true);
        mYearAdapter = new DateTimeAdapter();
        mYearPicker = (PickerView) root.findViewById(R.id.year);
        mYearPicker.setAdapter(mYearAdapter);
        mYearPicker.setOnItemSelectedListener(new ItemSelectedListener(Calendar.YEAR));

        mMonthAdapter = new DateTimeAdapter();
        mMonthPicker = (PickerView) root.findViewById(R.id.month);
        mMonthPicker.setAdapter(mMonthAdapter);
        mMonthPicker.setOnItemSelectedListener(new ItemSelectedListener(Calendar.MONTH));

        mDayAdapter = new DateTimeAdapter();
        mDayPicker = (PickerView) root.findViewById(R.id.date);
        mDayPicker.setAdapter(mDayAdapter);
        mDayPicker.setOnItemSelectedListener(new ItemSelectedListener(Calendar.DAY_OF_MONTH));

        mHourAdapter = new DateTimeAdapter();
        mHourPicker = (PickerView) root.findViewById(R.id.hour);
        mHourPicker.setAdapter(mHourAdapter);
        mHourPicker.setOnItemSelectedListener(new ItemSelectedListener(Calendar.HOUR_OF_DAY));

        mMinAdapter = new DateTimeAdapter();
        mMinPicker = (PickerView) root.findViewById(R.id.min);
        mMinPicker.setAdapter(mMinAdapter);
        mMinPicker.setOnItemSelectedListener(new ItemSelectedListener(Calendar.MINUTE));
    }

    private void initDatas() {
        mStartDate = Calendar.getInstance();
        mStartDate.set(Calendar.YEAR, DEFAULT_START_YEAR);
        mStartDate.set(Calendar.MONTH, 1);
        mStartDate.set(Calendar.DAY_OF_MONTH, 1);
        mStartDate.set(Calendar.HOUR_OF_DAY, 0);
        mStartDate.set(Calendar.MINUTE, 0);
        mStartDate.set(Calendar.SECOND, 0);
        mStartDate.set(Calendar.MILLISECOND, 0);

        mEndDate = Calendar.getInstance();
        mEndDate.set(Calendar.YEAR, DEFAULT_END_YEAR);
        mEndDate.set(Calendar.MONTH, 1);
        mEndDate.set(Calendar.DAY_OF_MONTH, 1);
        mEndDate.set(Calendar.HOUR_OF_DAY, 0);
        mEndDate.set(Calendar.MINUTE, 0);
        mEndDate.set(Calendar.SECOND, 0);
        mEndDate.set(Calendar.MILLISECOND, 0);

        final Calendar calendar = Calendar.getInstance();
        final int nowYear = calendar.get(Calendar.YEAR);
        final int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int now = nowYear;
        mYearAdapter.setStart(DEFAULT_START_YEAR);
        mYearAdapter.setEnd(DEFAULT_END_YEAR);
        mYearPicker.setCurrentPosition(now - DEFAULT_START_YEAR);

        now = nowMonth;
        mMonthAdapter.setStart(1);
        mMonthAdapter.setEnd(12);
        mMonthPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.DAY_OF_MONTH);
        mDayAdapter.setStart(1);
        mDayAdapter.setEnd(getDay(nowYear, nowMonth));
        mDayPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.HOUR_OF_DAY);
        mHourAdapter.setStart(1);
        mHourAdapter.setEnd(24);
        mHourPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.MINUTE);
        mMinAdapter.setStart(1);
        mMinAdapter.setEnd(60);
        mMinPicker.setCurrentPosition(now - 1);
    }


    /**
     * 设置样式
     */
    public void setStyle(@style int style) {
        if (mStyle == style) {
            Log.i(TAG, "setStyle: style is same");
            return;
        }
        mStyle = style;
        switch (mStyle) {
            case STYLE_DATE_TIME:
                mYearPicker.setVisibility(VISIBLE);
                mMonthPicker.setVisibility(VISIBLE);
                mDayPicker.setVisibility(VISIBLE);
                mHourPicker.setVisibility(VISIBLE);
                mMinPicker.setVisibility(VISIBLE);
                break;
            case STYLE_DATE:
                mYearPicker.setVisibility(VISIBLE);
                mMonthPicker.setVisibility(VISIBLE);
                mDayPicker.setVisibility(VISIBLE);
                mHourPicker.setVisibility(GONE);
                mMinPicker.setVisibility(GONE);
                break;
            case STYLE_TIME:
                mYearPicker.setVisibility(GONE);
                mMonthPicker.setVisibility(GONE);
                mDayPicker.setVisibility(GONE);
                mHourPicker.setVisibility(VISIBLE);
                mMinPicker.setVisibility(VISIBLE);
                break;
        }
    }

    public Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, getCurrentYear());
        calendar.set(Calendar.MONTH, getCurrentMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, getCurrentDay());
        calendar.set(Calendar.HOUR_OF_DAY, getCurrentHour());
        calendar.set(Calendar.MINUTE, getCurrentMin());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public long getCurrentTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, getCurrentYear());
        calendar.set(Calendar.MONTH, getCurrentMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, getCurrentDay());
        calendar.set(Calendar.HOUR_OF_DAY, getCurrentHour());
        calendar.set(Calendar.MINUTE, getCurrentMin());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    /**
     * 获取当前是那一年
     */
    public int getCurrentYear() {
        return mYearAdapter.getStart() + mYearPicker.getCurrentPosition();
    }

    /**
     * 获取当前是那一月
     */
    public int getCurrentMonth() {
        return mMonthAdapter.getStart() + mMonthPicker.getCurrentPosition();
    }

    /**
     * 获取当前是那一天
     */
    public int getCurrentDay() {
        return mDayAdapter.getStart() + mDayPicker.getCurrentPosition();
    }

    /**
     * 获取当前小时
     */
    public int getCurrentHour() {
        return mHourAdapter.getStart() + mHourPicker.getCurrentPosition();
    }

    /**
     * 获取当前分钟
     */
    public int getCurrentMin() {
        return mMinAdapter.getStart() + mMinPicker.getCurrentPosition();
    }

    @SuppressWarnings("unused")
    public void setCurrentDate(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int now = calendar.get(Calendar.YEAR);
        mYearPicker.setCurrentPosition(now - DEFAULT_START_YEAR);

        now = calendar.get(Calendar.MONTH) + 1;
        mMonthPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.DAY_OF_MONTH);
        mDayPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.HOUR_OF_DAY);
        mHourPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.MINUTE);
        mMinPicker.setCurrentPosition(now - 1);
    }

    @SuppressWarnings("unused")
    public void setCurrentDate(int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int now = calendar.get(Calendar.YEAR);
        mYearPicker.setCurrentPosition(now - DEFAULT_START_YEAR);

        now = calendar.get(Calendar.MONTH) + 1;
        mMonthPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.DAY_OF_MONTH);
        mDayPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.HOUR_OF_DAY);
        mHourPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.MINUTE);
        mMinPicker.setCurrentPosition(now - 1);
    }

    public void setCurrentDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int now = calendar.get(Calendar.YEAR);
        mYearPicker.setCurrentPosition(now - DEFAULT_START_YEAR);

        now = calendar.get(Calendar.MONTH) + 1;
        mMonthPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.DAY_OF_MONTH);
        mDayPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.HOUR_OF_DAY);
        mHourPicker.setCurrentPosition(now - 1);

        now = calendar.get(Calendar.MINUTE);
        mMinPicker.setCurrentPosition(now - 1);
    }


    /**
     * 设置开始日期
     * 先暂时不使用
     */
    @SuppressWarnings("unused")
    private void setStartDate(@NonNull Date date) {
        mStartDate.setTime(date);
    }

    /**
     * 设置结束日期
     * 先暂时不使用
     */
    @SuppressWarnings("unused")
    private void setEndDate(@NonNull Date date) {
        mEndDate.setTime(date);
    }

    /**
     * 计算每月多少天
     */
    private int getDay(final @IntRange(from = 0) int year, final @IntRange(from = 0) int month) {
        boolean isLeayYear = isLeayYear(year);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return isLeayYear ? 29 : 28;
            default:
                return 30;
        }
    }

    /**
     * 是否是闰年
     */
    private boolean isLeayYear(final @IntRange(from = 0) int year) {
        /*
         * 可以被400整出的肯定是闰年， 其他情况是 只能被4整除不能被100整除
         */
        return (year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0));
    }

    /**
     * 选中的监听
     */
    private class ItemSelectedListener implements OnItemSelectedListener {
        private int mType;

        private ItemSelectedListener(int type) {
            mType = type;
        }

        @Override
        public void onItemSelected(int position) {
            switch (mType) {
                case Calendar.YEAR:
                    final int month = getCurrentMonth();
                    if (month == 2) {
                        updateDay();
                    }

                    break;
                case Calendar.MONTH:
                    updateDay();
                    break;
            }
        }


        void updateDay() {
            final int year = getCurrentYear();
            final int month = getCurrentMonth();
            mDayAdapter.setEnd(getDay(year, month));
        }


    }
}
