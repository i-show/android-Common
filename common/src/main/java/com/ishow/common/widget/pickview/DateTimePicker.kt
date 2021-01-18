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
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import com.ishow.common.R
import com.ishow.common.databinding.WidgetDateTimePickerBinding
import com.ishow.common.extensions.binding
import com.ishow.common.widget.pickview.adapter.DateTimeAdapter
import com.ishow.common.widget.pickview.listener.OnItemSelectedListener
import java.util.*

/**
 * Created by yuhaiyang on 2017/4/25.
 * 时间选择器
 */

class DateTimePicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    LinearLayout(context, attrs, defStyle) {

    private lateinit var yearAdapter: DateTimeAdapter
    private lateinit var monthAdapter: DateTimeAdapter
    private lateinit var dayAdapter: DateTimeAdapter
    private lateinit var hourAdapter: DateTimeAdapter
    private lateinit var minAdapter: DateTimeAdapter

    private lateinit var startDate: Calendar
    private lateinit var endDate: Calendar

    private var mStyle: Int = 0

    private val binding: WidgetDateTimePickerBinding by binding()

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
        get() = yearAdapter.start + binding.yearPicker.currentPosition

    /**
     * 获取当前是那一月
     */
    val currentMonth: Int
        get() = monthAdapter.start + binding.monthPicker.currentPosition

    /**
     * 获取当前是那一天
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val currentDay: Int
        get() = dayAdapter.start + binding.dayPicker.currentPosition

    /**
     * 获取当前小时
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val currentHour: Int
        get() = hourAdapter.start + binding.hourPicker.currentPosition

    /**
     * 获取当前分钟
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val currentMin: Int
        get() = minAdapter.start + binding.minPicker.currentPosition

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

        yearAdapter = DateTimeAdapter()
        binding.yearPicker.setAdapter(yearAdapter)
        binding.yearPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.YEAR))

        monthAdapter = DateTimeAdapter()
        binding.monthPicker.setAdapter(monthAdapter)
        binding.monthPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.MONTH))

        dayAdapter = DateTimeAdapter()
        binding.dayPicker.setAdapter(dayAdapter)
        binding.dayPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.DAY_OF_MONTH))

        hourAdapter = DateTimeAdapter()
        binding.hourPicker.setAdapter(hourAdapter)
        binding.hourPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.HOUR_OF_DAY))

        minAdapter = DateTimeAdapter()
        binding.minPicker.setAdapter(minAdapter)
        binding.minPicker.setOnItemSelectedListener(ItemSelectedListener(Calendar.MINUTE))
    }

    private fun initData() {
        startDate = Calendar.getInstance()
        startDate.set(Calendar.YEAR, DEFAULT_START_YEAR)
        startDate.set(Calendar.MONTH, 1)
        startDate.set(Calendar.DAY_OF_MONTH, 1)
        startDate.set(Calendar.HOUR_OF_DAY, 0)
        startDate.set(Calendar.MINUTE, 0)
        startDate.set(Calendar.SECOND, 0)
        startDate.set(Calendar.MILLISECOND, 0)

        endDate = Calendar.getInstance()
        endDate.set(Calendar.YEAR, DEFAULT_END_YEAR)
        endDate.set(Calendar.MONTH, 1)
        endDate.set(Calendar.DAY_OF_MONTH, 1)
        endDate.set(Calendar.HOUR_OF_DAY, 0)
        endDate.set(Calendar.MINUTE, 0)
        endDate.set(Calendar.SECOND, 0)
        endDate.set(Calendar.MILLISECOND, 0)

        val calendar = Calendar.getInstance()
        val nowYear = calendar.get(Calendar.YEAR)
        val nowMonth = calendar.get(Calendar.MONTH) + 1
        var now = nowYear
        yearAdapter.start = DEFAULT_START_YEAR
        yearAdapter.end = DEFAULT_END_YEAR
        binding.yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = nowMonth
        monthAdapter.start = 1
        monthAdapter.end = 12
        binding.monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        dayAdapter.start = 1
        dayAdapter.end = getDay(nowYear, nowMonth)
        binding.dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        hourAdapter.start = 1
        hourAdapter.end = 24
        binding.hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        minAdapter.start = 1
        minAdapter.end = 60
        binding.minPicker.currentPosition = now - 1
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
                binding.yearPicker.visibility = View.VISIBLE
                binding.monthPicker.visibility = View.VISIBLE
                binding.dayPicker.visibility = View.VISIBLE
                binding.hourPicker.visibility = View.VISIBLE
                binding.minPicker.visibility = View.VISIBLE
            }
            Style.DATE -> {
                binding.yearPicker.visibility = View.VISIBLE
                binding.monthPicker.visibility = View.VISIBLE
                binding.dayPicker.visibility = View.VISIBLE
                binding.hourPicker.visibility = View.GONE
                binding.minPicker.visibility = View.GONE
            }
            Style.TIME -> {
                binding.yearPicker.visibility = View.GONE
                binding.monthPicker.visibility = View.GONE
                binding.dayPicker.visibility = View.GONE
                binding.hourPicker.visibility = View.VISIBLE
                binding.minPicker.visibility = View.VISIBLE
            }
        }
    }

    fun setCurrentDate(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        var now = calendar.get(Calendar.YEAR)
        binding.yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = calendar.get(Calendar.MONTH) + 1
        binding.monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        binding.dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        binding.hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        binding.minPicker.currentPosition = now - 1
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
        binding.yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = calendar.get(Calendar.MONTH) + 1
        binding.monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        binding.dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        binding.hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        binding.minPicker.currentPosition = now - 1
    }

    fun setCurrentDate(time: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        var now = calendar.get(Calendar.YEAR)
        binding.yearPicker.currentPosition = now - DEFAULT_START_YEAR

        now = calendar.get(Calendar.MONTH) + 1
        binding.monthPicker.currentPosition = now - 1

        now = calendar.get(Calendar.DAY_OF_MONTH)
        binding.dayPicker.currentPosition = now - 1

        now = calendar.get(Calendar.HOUR_OF_DAY)
        binding.hourPicker.currentPosition = now - 1

        now = calendar.get(Calendar.MINUTE)
        binding.minPicker.currentPosition = now - 1
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
            dayAdapter.end = getDay(year, month)
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
