package com.ishow.common.widget.pickview;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ishow.common.R;
import com.ishow.common.utils.DateUtils;
import com.ishow.common.widget.pickview.adapter.DateTimeAdapter;
import com.ishow.common.widget.pickview.listener.OnItemSelectedListener;

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

    private DateTimeAdapter mYearAdapter;
    private DateTimeAdapter mMonthAdapter;
    private DateTimeAdapter mDateAdapter;
    private DateTimeAdapter mHourAdapter;
    private DateTimeAdapter mMinAdapter;

    private Calendar mStartDate;
    private Calendar mEndDate;

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        final int defatultPadding = context.getResources().getDimensionPixelOffset(R.dimen.date_time_picker_padding);
        int paddingStart = getPaddingStart() + defatultPadding;
        int paddingEnd = getPaddingEnd() + defatultPadding;
        setPadding(paddingStart, getPaddingTop(), paddingEnd, getPaddingBottom());
        initViews();
        initDatas();
    }


    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View root = inflater.inflate(R.layout.view_date_time_picker, this, true);
        mYearAdapter = new DateTimeAdapter();
        PickerView yearPicker = (PickerView) root.findViewById(R.id.year);
        yearPicker.setAdapter(mYearAdapter);

        mMonthAdapter = new DateTimeAdapter();
        PickerView monthPicker = (PickerView) root.findViewById(R.id.month);
        monthPicker.setAdapter(mMonthAdapter);

        mDateAdapter = new DateTimeAdapter();
        PickerView datePicker = (PickerView) root.findViewById(R.id.date);
        datePicker.setAdapter(mDateAdapter);

        mHourAdapter = new DateTimeAdapter();
        PickerView hourPicker = (PickerView) root.findViewById(R.id.hour);
        hourPicker.setAdapter(mHourAdapter);

        mMinAdapter = new DateTimeAdapter();
        PickerView minPicker = (PickerView) root.findViewById(R.id.min);
        minPicker.setAdapter(mMinAdapter);
    }

    private void initDatas() {
        mStartDate = Calendar.getInstance();
        mStartDate.set(Calendar.YEAR, DEFAULT_START_YEAR);
        mStartDate.set(Calendar.MONTH, 1);
        mStartDate.set(Calendar.DAY_OF_YEAR, 1);
        mStartDate.set(Calendar.HOUR_OF_DAY, 0);
        mStartDate.set(Calendar.MINUTE, 0);
        mStartDate.set(Calendar.SECOND, 0);
        mStartDate.set(Calendar.MILLISECOND, 0);

        mEndDate = Calendar.getInstance();
        mEndDate.set(Calendar.YEAR, DEFAULT_END_YEAR);
        mEndDate.set(Calendar.MONTH, 1);
        mEndDate.set(Calendar.DAY_OF_YEAR, 1);
        mEndDate.set(Calendar.HOUR_OF_DAY, 0);
        mEndDate.set(Calendar.MINUTE, 0);
        mEndDate.set(Calendar.SECOND, 0);
        mEndDate.set(Calendar.MILLISECOND, 0);

        mYearAdapter.setStart(DEFAULT_START_YEAR);
        mYearAdapter.setEnd(DEFAULT_END_YEAR);

        mMonthAdapter.setStart(1);
        mMonthAdapter.setEnd(12);

        mDateAdapter.setStart(1);
        mDateAdapter.setEnd(30);

        mHourAdapter.setStart(1);
        mHourAdapter.setEnd(24);

        mMinAdapter.setStart(1);
        mMinAdapter.setEnd(60);
    }

    /**
     * 设置开始日期
     */
    public void setStartDate(@NonNull Date date) {
        mStartDate.setTime(date);
    }

    /**
     * 设置结束日期
     */
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

        }
    }
}
