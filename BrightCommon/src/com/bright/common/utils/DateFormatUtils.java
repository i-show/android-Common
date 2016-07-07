/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bright.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;

import com.bright.common.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 方便的获取 当前时间是今天还是昨天
 */
public final class DateFormatUtils {

    private static final String TAG = "DateFormatUtils";
    private static final String EMPTY = "";

    private static final long ONE_DAY_IN_MILLISECONDS = 86400000L;
    private static final long MONTH_SHIFT_BIT = 8L;
    private static final long YEAR_SHIFT_BIT = 16L;

    private static final int MAX_HASH_MAP_SIZE = 500;

    private static String sToday = EMPTY;
    private static String sYesterday = EMPTY;

    private static long sFormattedToday;
    private static long sFormattedYesterday;

    private static HashMap<Long, Long> sMapDate;
    private static boolean sInitilized;

    /**
     * @param context to get resource
     * @param time to format date
     * @return string
     */
    public static String getFormatedDateWeek(Context context, final long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date weekDate = new Date(time);
        String week = sdf.format(weekDate);
        String date = getFormatDate(context, time);
        return Utils.plusString(date, context.getString(R.string.spacer), week);
    }

    /**
     * @param context to get resource
     * @param time to format date
     * @return string
     */
    public static String getFormatDate(Context context, final long time) {
        String result = null;
        sInitilized = false;

        if (time <= 0) {
            Log.w(TAG, "Formated Date But Time is Illegal");
            return result;
        }

        Log.i(TAG, "Formated Time = " + time);
        final long formattedTime = formatTime(time);

        if (formattedTime == sFormattedToday) {
            if (TextUtils.isEmpty(sToday)) {
                sToday = context.getResources().getString(R.string.today);
            }
            result = sToday;
        } else if (formattedTime == sFormattedYesterday) {
            if (TextUtils.isEmpty(sYesterday)) {
                sYesterday = context.getResources().getString(R.string.yesterday);
            }
            result = sYesterday;
        } else {
            result = getDate(context, time);
        }

        Log.i(TAG, "Formatted Result = " + result);
        return result;
    }


    /**
     * @param context to get resource
     * @param time to format date
     * @return string
     */
    public static String getFormatDate(Context context, final long time, String formater) {
        String result = null;
        sInitilized = false;

        if (time <= 0) {
            Log.w(TAG, "Formated Date But Time is Illegal");
            return result;
        }

        Log.i(TAG, "Formated Time = " + time);
        final long formattedTime = formatTime(time);

        if (formattedTime == sFormattedToday) {
            if (TextUtils.isEmpty(sToday)) {
                sToday = context.getResources().getString(R.string.today);
            }
            result = sToday;
        } else if (formattedTime == sFormattedYesterday) {
            if (TextUtils.isEmpty(sYesterday)) {
                sYesterday = context.getResources().getString(R.string.yesterday);
            }
            result = sYesterday;
        } else {
            result = format(time, formater);
        }

        Log.i(TAG, "Formatted Result = " + result);
        return result;
    }

    public static String format(long timemill, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date data = new Date(timemill);
        return formatter.format(data);
    }

    private static long formatTime(long milliSeconds) {
        if (!sInitilized) {
            sInitilized = true;
            refreshData();
        }
        Long result = sMapDate.get(milliSeconds);

        if (null == result) {
            Time time = new Time();
            time.set(milliSeconds);
            result = (long) ((time.year << YEAR_SHIFT_BIT) + ((time.month + 1) << MONTH_SHIFT_BIT) + time.monthDay);
            Log.i(TAG, "formattedDate result = " + result);
            sMapDate.put(milliSeconds, result);
        }

        return result;
    }

    /**
     * Ensure that the time is up to date
     */
    private static void refreshData() {
        long curtime = System.currentTimeMillis();
        if (null == sMapDate) {
            sMapDate = new HashMap<Long, Long>();
        }

        sFormattedToday = formatTime(curtime);
        sFormattedYesterday = formatTime(curtime - ONE_DAY_IN_MILLISECONDS);

        sToday = EMPTY;
        sYesterday = EMPTY;
        if (sMapDate.size() > MAX_HASH_MAP_SIZE) {
            sMapDate.clear();
        }

        sInitilized = true;
    }

    /**
     * @param firstDate the first date to diff
     * @param secondDate the second date to diff
     * @return boolean
     */
    public static boolean isSameDay(final long firstDate, final long secondDate) {

        long first = formatTime(firstDate);
        long second = formatTime(secondDate);

        return (first == second);
    }

    private static String getDate(Context context, long milliSeconds) {
        String ret = null;
        if (null != context) {
            ret = DateFormat.getDateFormat(context).format(milliSeconds).toString();
        }
        return ret;
    }
}