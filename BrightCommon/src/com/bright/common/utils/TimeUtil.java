/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tink.Dan on 2015/10/21.
 */
public class TimeUtil {
    /**
     * 1秒
     */
    public static final long SECOND_1 = 1L * 1000L;
    /**
     * 30秒
     */
    public static final long SECOND_30 = 30L * SECOND_1;
    /**
     * 1 分钟
     */
    public static final long MINUTE_1 = 60L * SECOND_1;
    /**
     * 1 小时
     */
    public static final long HOUR_1 = 60L * MINUTE_1;
    /**
     * 1 天
     */
    public static final long DAY_1 = 24L * HOUR_1;
    /**
     * 7 天
     */
    public static final long DAY_7 = 7L * DAY_1;
    /**
     * 一年
     */
    public static final long ONE_YEAR = 365L * 24L * 60L * 60L * 1000L;
    /**
     * "MM-dd"
     */
    public static String Format1 = "MM-dd";

    /**
     * "yyyy年MM月dd日"
     */
    public static String Format2 = "yyyy年MM月dd日";

    /**
     * "MM月dd日"
     */
    public static String Format3 = "MM月dd日";

    /**
     * "yyyy-MM-dd"
     */
    public static String Format4 = "yyyy-MM-dd";

    public static String format(String timeStr, String format) {

        String time = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter1 = new SimpleDateFormat(format);
            Date data = formatter.parse(timeStr);
            time = formatter1.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long formatTolong(String timeStr, String format) {

        String time = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date data = formatter.parse(timeStr);
            return data.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static String format(long timemill, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date data = new Date(timemill);
        return formatter.format(data);
    }

    /**
     * 获取时间
     */
    public static long getTime(int year, int month, int daya) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, daya);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间移除 时分秒
     */
    public static long getCurrentTime() {
        return removeDms(System.currentTimeMillis());
    }

    /**
     * 判断是不是同一天
     */
    public static boolean isSameDay(long time1, long time2) {
        time1 = removeDms(time1);
        time2 = removeDms(time2);
        return time1 == time2;
    }

    /**
     * 移除时分秒
     */
    public static long removeDms(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        //设置当前时刻的时钟为0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //设置当前时刻的分钟为0
        c.set(Calendar.MINUTE, 0);
        //设置当前时刻的秒钟为0
        c.set(Calendar.SECOND, 0);
        //设置当前的毫秒钟为0
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
}
