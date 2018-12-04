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

package com.ishow.common.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 一个计算的工具
 */
@SuppressWarnings("unused")
public class MathUtils {
    private static final String TAG = "MathUtils";
    /**
     * Format的头部
     */
    private static final String FORMAT_HEADER = "###0";
    /**
     * Format的的小数点
     */
    private static final String FORMAT_POINT = ".";
    /**
     * 除法的小数点位数
     */
    private static final int DIV_DECIMAL_PLACES = 2;


    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static String plus(double value1, double value2) {
        return plus(String.valueOf(value1), String.valueOf(value2));
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    @SuppressWarnings("WeakerAccess")
    public static String plus(String value1, String value2) {
        if (TextUtils.isEmpty(value1)) {
            value1 = "0";
        }

        if (TextUtils.isEmpty(value2)) {
            value2 = "0";
        }

        BigDecimal _value1 = new BigDecimal(value1);
        BigDecimal _value2 = new BigDecimal(value2);
        return _value1.add(_value2).toString();
    }


    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static String sub(double value1, double value2) {
        return sub(String.valueOf(value1), String.valueOf(value2));
    }

    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    @SuppressWarnings("WeakerAccess")
    public static String sub(String value1, String value2) {
        if (TextUtils.isEmpty(value1)) {
            value1 = "0";
        }

        if (TextUtils.isEmpty(value2)) {
            value2 = "0";
        }

        BigDecimal _value1 = new BigDecimal(value1);
        BigDecimal _value2 = new BigDecimal(value2);
        return _value1.subtract(_value2).toString();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static String mul(double value1, double value2) {
        return mul(String.valueOf(value1), String.valueOf(value2));
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    @SuppressWarnings("WeakerAccess")
    public static String mul(String value1, String value2) {
        if (TextUtils.isEmpty(value1)) {
            value1 = "0";
        }

        if (TextUtils.isEmpty(value2)) {
            value2 = "0";
        }

        BigDecimal _value1 = new BigDecimal(value1);
        BigDecimal _value2 = new BigDecimal(value2);
        return _value1.multiply(_value2).toString();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static String div(double v1, double v2) {
        return div(v1, v2, DIV_DECIMAL_PLACES);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    @SuppressWarnings("WeakerAccess")
    public static String div(double v1, double v2, int scale) {
        return div(String.valueOf(v1), String.valueOf(v2), scale);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param value1 被除数
     * @param value2 除数
     * @return 两个参数的商
     */
    public static String div(String value1, String value2) {
        return div(value1, value2, DIV_DECIMAL_PLACES);
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    @SuppressWarnings("WeakerAccess")
    public static String div(String value1, String value2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        if (TextUtils.isEmpty(value1)) {
            value1 = "0";
        }

        if (TextUtils.isEmpty(value2)) {
            value2 = "0";
        }

        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }



    /**
     * 提供精确的小数位四舍五入处理
     */
    public static String rounding(float value, int scale) {
        return rounding(value, scale, true);
    }

    /**
     * 提供精确的小数位四舍五入处理
     */
    @SuppressWarnings("WeakerAccess")
    public static String rounding(float value, int scale, boolean force) {
        return rounding(String.valueOf(value), scale, false);
    }
    /**
     * 提供精确的小数位四舍五入处理
     */
    public static String rounding(String value, int scale) {
        return rounding(value, scale, true);
    }

    /**
     * 提供精确的小数位四舍五入处理
     */
    @SuppressWarnings("WeakerAccess")
    public static String rounding(String value, int scale, boolean force) {
        return rounding(new BigDecimal(value), scale, force);
    }

    /**
     * 提供精确的小数位四舍五入处理
     */
    public static String rounding(double value, int scale) {
        return rounding(value, scale, true);
    }

    /**
     * 提供精确的小数位四舍五入处理
     */
    @SuppressWarnings("WeakerAccess")
    public static String rounding(double value, int scale, boolean force) {
        return rounding(new BigDecimal(value), scale, false);
    }

    /**
     * 提供精确的小数位四舍五入处理
     */
    @SuppressWarnings("WeakerAccess")
    public static String rounding(BigDecimal value, int scale, boolean force) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        String pattern;
        if (scale == 0) {
            pattern = FORMAT_HEADER;
        } else if (force) {
            pattern = StringUtils.plusString(FORMAT_HEADER, FORMAT_POINT, generateString(scale, "0"));
        } else {
            pattern = StringUtils.plusString(FORMAT_HEADER, FORMAT_POINT, generateString(scale, "#"));
        }

        DecimalFormat format = new DecimalFormat(pattern);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(value);
    }

    /**
     * 生成固定长度 字符串
     *
     * @param count 字符串长度
     * @param child 字符串
     */
    @SuppressWarnings("WeakerAccess")
    public static String generateString(int count, String child) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(child);
        }
        return builder.toString();
    }
}
