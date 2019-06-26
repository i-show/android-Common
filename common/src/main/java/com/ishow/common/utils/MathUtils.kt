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

package com.ishow.common.utils

import android.text.TextUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * 一个计算的工具
 */
@Suppress("unused")
object MathUtils {
    /**
     * Format的头部
     */
    private const val FORMAT_HEADER = "###0"
    /**
     * Format的的小数点
     */
    private const val FORMAT_POINT = "."
    /**
     * 除法的小数点位数
     */
    private const val DIV_DECIMAL_PLACES = 2

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    fun plus(value1: Double, value2: Double): String {
        return plus(value1.toString(), value2.toString())
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    fun plus(value1: String, value2: String): String {
        if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)) {
            throw IllegalStateException("value1 or value2 is empty")
        }
        return BigDecimal(value1).add(BigDecimal(value2)).toString()
    }


    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    fun sub(value1: Double, value2: Double): String {
        return sub(value1.toString(), value2.toString())
    }

    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun sub(value1: String, value2: String): String {
        if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)) {
            throw IllegalStateException("value1 or value2 is empty")
        }
        return BigDecimal(value1).subtract(BigDecimal(value2)).toString()
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    fun mul(value1: Double, value2: Double): String {
        return mul(value1.toString(), value2.toString())
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun mul(value1: String, value2: String): String {
        if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)) {
            throw IllegalStateException("value1 or value2 is empty")
        }

        return BigDecimal(value1).multiply(BigDecimal(value2)).toString()
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
    @JvmOverloads
    fun div(v1: Double, v2: Double, scale: Int = DIV_DECIMAL_PLACES): String {
        return div(v1.toString(), v2.toString(), scale)
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
    @JvmOverloads
    fun div(value1: String, value2: String, scale: Int = DIV_DECIMAL_PLACES): String {
        if (scale < 0) {
            throw IllegalArgumentException("div scale < 0")
        }

        if (TextUtils.isEmpty(value1) || TextUtils.isEmpty(value2)) {
            throw IllegalStateException("value1 or value2 is empty")
        }

        return BigDecimal(value1).divide(BigDecimal(value2), scale, BigDecimal.ROUND_HALF_UP).toString()
    }


    /**
     * 提供精确的小数位四舍五入处理
     */
    @JvmOverloads
    fun rounding(value: Float, scale: Int, force: Boolean = true): String {
        return rounding(value.toString(), scale, force)
    }

    /**
     * 提供精确的小数位四舍五入处理
     */
    @JvmOverloads
    fun rounding(value: Double, scale: Int, force: Boolean = true): String {
        return rounding(value.toString(), scale, force)
    }

    /**
     * 提供精确的小数位四舍五入处理
     */
    @JvmOverloads
    fun rounding(value: String, scale: Int, force: Boolean = true): String {
        return rounding(BigDecimal(value), scale, force)
    }


    /**
     * 提供精确的小数位四舍五入处理
     */
    private fun rounding(value: BigDecimal, scale: Int, force: Boolean): String {
        if (scale < 0) {
            throw IllegalArgumentException("rounding scale < 0")
        }

        @Suppress("CascadeIf")
        val pattern = if (scale == 0) {
            FORMAT_HEADER
        } else if (force) {
            StringUtils.plusString(FORMAT_HEADER, FORMAT_POINT, generateString(scale, "0"))
        } else {
            StringUtils.plusString(FORMAT_HEADER, FORMAT_POINT, generateString(scale, "#"))
        }

        val format = DecimalFormat(pattern)
        format.roundingMode = RoundingMode.HALF_UP
        return format.format(value)
    }

    /**
     * 生成固定长度 字符串
     *
     * @param count 字符串长度
     * @param child 字符串
     */
    private fun generateString(count: Int, child: String): String {
        val builder = StringBuilder()
        for (i in 0 until count) {
            builder.append(child)
        }
        return builder.toString()
    }
}
