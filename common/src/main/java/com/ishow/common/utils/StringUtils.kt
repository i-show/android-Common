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

import com.ishow.common.utils.log.LogUtils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.util.Locale

object StringUtils {
    private const val TAG = "StringUtils"
    /**
     * 空字符串
     */
    const val EMPTY = ""
    /**
     * 换行
     */
    const val NEW_LINE = "\n"
    /**
     * 制表符
     */
    const val TAB = "\t"
    /**
     * 人民币
     */
    const val MONEY = "¥"
    /**
     * 字符串0通常用来计算
     */
    const val ZERO = "0"
    /**
     * 空格
     */
    const val BLANK = " "

    /**
     * 字符串累加
     */
    @JvmStatic
    fun plusString(vararg strArray: Any?): String {
        val builder = StringBuilder()
        for (str in strArray) {
            if (str.toString().isNotEmpty()) {
                builder.append(str)
            }
        }
        return builder.toString()
    }


    /**
     * 字符传int
     */
    @JvmStatic
    fun format2Int(value: String): Int {
        return try {
            value.toInt()
        } catch (e: Exception) {
            LogUtils.d(TAG, "format2int e = $e")
            0
        }

    }

    /**
     * 字符传Float
     */
    fun format2Float(value: String): Float {
        return try {
            value.toFloat()
        } catch (e: Exception) {
            LogUtils.d(TAG, "format2int e = $e")
            0F
        }
    }

    /**
     * 添加人民币符号
     */
    fun format2Money(money: String): String {
        return plusString(MONEY, money)
    }

    /**
     * 2位小数计算
     */
    fun format2Money(money: String, scale: Int): String {
        return plusString(MONEY, MathUtils.rounding(money, scale))
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    fun byteArrayToHexString(data: ByteArray): String {
        val sb = StringBuilder(data.size * 2)
        for (b in data) {
            val v: Int = b.toInt() and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().toUpperCase(Locale.getDefault())
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val d = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character
                .digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return d
    }
}
