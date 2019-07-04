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

package com.ishow.common.widget.pickview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

import androidx.annotation.IntDef
import androidx.annotation.IntRange
import com.ishow.common.R
import com.ishow.common.widget.pickview.adapter.DateTimeAdapter
import com.ishow.common.widget.pickview.listener.OnItemSelectedListener
import kotlinx.android.synthetic.main.widget_date_time_picker.view.*

import java.util.Calendar
import java.util.Date

/**
 * Created by yuhaiyang on 2017/4/25.
 * 时间选择器
 */

class DateTimePicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        LinearLayout(context, attrs, defStyle) {

    private lateinit var mYearAdapter: DateTimeAdapter
    private lateinit var mMonthAdapter: DateTimeAdapter
    private lateinit var mDayAdapter: DateTimeAdapter
    private lateinit var mHourAdapter: DateTimeAdapter
    private lateinit var mMinAdapter: DateTimeAdapter

    private lateinit var mStartDate: Calendar
    private lateinit var mEndDate: Calendar

    private var mStyle: Int = 0

    val currentTime: Date
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, currentYear)
            calendar.set(Calendar.MONTH, currentMonth - 1)
            calendar.set(Calendar.DAY_OF_MONTH, currentDay)
            calendar.set(Calendar.HOUR_OF_DAY, currentHour)
            calendar.set(Calendar.MINUTE, currentMin)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.time
        }

    val currentTimeInMillis: Long
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, currentYear)
            calendar.set(Calendar.MONTH, currentMonth - 1)
            calendar.set(Calendar.DAY_OF_MONTH, currentDay)
            calendar.set(Calendar.HOUR_OF_DAY, currentHour)
            calendar.set(Calendar.MINUTE, currentMin)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }

    /**
     * 获取当前是那一年
     */
    val currentYear: Int
        get() = mYearAdapter.start + yearPicker.currentPosition

    /**
     * 获取当前是那一月
     */
    val currentMonth: Int
        get() = mMonthAdapter.start + monthPicker.currentPosition

    /**
     * 获取当前是那一天
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val currentDay: Int
        get() = mDayAdapter.start + dayPicker.currentPosition

    /**
     * 获取当前小时
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val currentHour: Int
        get() = mHourAdapter.start + hourPicker.currentPosition

    /**
     * 获取当前分钟
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val currentMin: Int
        get() = mMinAdapter.start + minPicker.currentPosition

    @IntDef(Style.DATE_TIME, Style.DATE, Style.TIME)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Style {
        companion object {
            /**
             * 日期+时间
             */
            const val DATE_TIME = 1
            /**
             * 仅日期
             */
            const val DATE = 2
            /**
             * 仅时间
             */
            const val TIME = 3
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker)
        val style = a.getInt(R.styleable.DateTimePicker_datePickerStyle, Style.DATE_TIME)
        a.recycle()

        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        initViews()
        initData()
        setStyle(style)
    }


    private fun initViews() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.widget_date_time_picker, this, true)

        mYearAdapter = DateTimeAdapter()
        yearPicker.setAdapter(mYearAdapter)
        yearPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.YEAR))

        mMonthAdapter = DateTimeAdapter()
        monthPicker.setAdapter(mMonthAdapter)
        monthPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.MONTH))

        mDayAdapter = DateTimeAdapter()
        dayPicker.setAdapter(mDayAdapter)
        dayPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.DAY_OF_MONTH))

        mHourAdapter = DateTimeAdapter()
        hourPicker.setAdapter(mHourAdapter)
        hourPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.HOUR_OF_DAY))

        mMinAdapter = DateTimeAdapter()
        minPicker.setAdapter(mMinAdapter)
        minPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.MINUTE))
    }

    private fun initData() {
        mStartDate = Calendar.getInstance()
        mStartDate.set(Calendar.YEAR, DEFAULT_START_YEAR)
        mStartDate.set(Calendar.MONTH, 1)
        mStartDate.set(Calendar.DAY_OF_MONTH, 1)
        mStartDate.set(Calendar.HOUR_OF_DAY, 0)
        mStartDate.set(Calendar.MINUTE, 0)
        mStartDate.set(Calendar.SECOND, 0)
        mStartDate.set(Calendar.MILLISECOND, 0)

        mEndDate = Calendar.getInstance()
        mEndDate.set(Calendar.YEAR, DEFAULT_END_YEAR)
        mEndDate.set(Calendar.MONTH, 1)
        mEndDate.set(Calendar.DAY_OF_MONTH, 1)
        mEndDate.set(Calendar.HOUR_OF_DAY, 0)
        mEndDate.set(Calendar.MINUTE, 0)
        mEndDate.set(Calendar.SECOND, 0)
        mEndDate.set(Calendar.MILLISECOND, 0)

        val calendar = Calendar.getInstance()
        val nowYear = calendar.get(Calendar.YEAR)
        val nowMonth = calendar.get(Calendar.MONTH) + 1
        var now = nowYear
        mYearAdapter.start = DEFAULT_START_YEAR
        mYearAdapter.end = DEFAULT_END_YEAR
        yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = nowMonth
        mMonthAdapter.start = 1
        mMonthAdapter.end = 12
        monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        mDayAdapter.start = 1
        mDayAdapter.end = getDay(nowYear, nowMonth)
        dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        mHourAdapter.start = 1
        mHourAdapter.end = 24
        hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        mMinAdapter.start = 1
        mMinAdapter.end = 60
        minPicker.currentPosition = now - 1
    }


    /**
     * 设置样式
     */
    fun setStyle(@Style style: Int) {
        if (mStyle == style) {
            Log.i(TAG, "setStyle: Style is same")
            return
        }
        mStyle = style
        when (mStyle) {
            Style.DATE_TIME -> {
                yearPicker.visibility = View.VISIBLE
                monthPicker.visibility = View.VISIBLE
                dayPicker.visibility = View.VISIBLE
                hourPicker.visibility = View.VISIBLE
                minPicker.visibility = View.VISIBLE
            }
            Style.DATE -> {
                yearPicker.visibility = View.VISIBLE
                monthPicker.visibility = View.VISIBLE
                dayPicker.visibility = View.VISIBLE
                hourPicker.visibility = View.GONE
                minPicker.visibility = View.GONE
            }
            Style.TIME -> {
                yearPicker.visibility = View.GONE
                monthPicker.visibility = View.GONE
                dayPicker.visibility = View.GONE
                hourPicker.visibility = View.VISIBLE
                minPicker.visibility = View.VISIBLE
            }
        }
    }

    fun setCurrentDate(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        var now = calendar.get(Calendar.YEAR)
        yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = calendar.get(Calendar.MONTH) + 1
        monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        minPicker.currentPosition = now - 1
    }

    fun setCurrentDate(year: Int, month: Int, day: Int, hour: Int, min: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        var now = calendar.get(Calendar.YEAR)
        yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = calendar.get(Calendar.MONTH) + 1
        monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        minPicker.currentPosition = now - 1
    }

    fun setCurrentDate(time: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        var now = calendar.get(Calendar.YEAR)
        yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = calendar.get(Calendar.MONTH) + 1
        monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        minPicker.currentPosition = now - 1
    }

    /**
     * 计算每月多少天
     */
    private fun getDay(@IntRange(from = 0) year: Int, @IntRange(from = 0) month: Int): Int {
        val isLeapYear = isLeapYear(year)
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            2 -> if (isLeapYear) 29 else 28
            else -> 30
        }
    }

    /**
     * 是否是闰年
     */
    private fun isLeapYear(@IntRange(from = 0) year: Int): Boolean {
        /*
         * 可以被400整出的肯定是闰年， 其他情况是 只能被4整除不能被100整除
         */
        return year % 400 == 0 || year % 4 == 0 && year % 100 != 0
    }

    /**
     * 选中的监听
     */
    private inner class ItemSelectedListener constructor(private val mType: Int) : OnItemSelectedListener {

        override fun onItemSelected(position: Int) {
            when (mType) {
                Calendar.YEAR -> {
                    val month = currentMonth
                    if (month == 2) {
                        updateDay()
                    }
                }
                Calendar.MONTH -> updateDay()
            }
        }

        internal fun updateDay() {
            val year = currentYear
            val month = currentMonth
            mDayAdapter.end = getDay(year, month)
        }
    }

    companion object {
        private const val TAG = "DateTimePicker"
        /**
         * 默认的开始的年
         */
        private const val DEFAULT_START_YEAR = 1900
        /**
         * 默认的结束年份
         */
        private const val DEFAULT_END_YEAR = 2600
    }
}
