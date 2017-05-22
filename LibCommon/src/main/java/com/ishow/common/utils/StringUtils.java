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

import java.util.Locale;

public class StringUtils {
    /**
     * 空字符串
     */
    public static final String EMPTY = "";
    /**
     * 换行
     */
    @SuppressWarnings("unused")
    public static final String NEW_LINE = "\n";
    /**
     * 制表符
     */
    @SuppressWarnings("unused")
    public static final String TAB = "\t";
    /**
     * 人民币
     */
    @SuppressWarnings("unused")
    public static final String MONEY = "￥";
    /**
     * 字符串0通常用来计算
     */
    @SuppressWarnings("unused")
    public static final String ZERO = "0";

    /**
     * 字符串累加
     */
    public static String plusString(Object... strs) {
        StringBuilder builder = new StringBuilder();
        for (Object str : strs) {
            String _str = String.valueOf(str);
            if (!TextUtils.isEmpty(_str)) {
                builder.append(str);
            }
        }
        return builder.toString();
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    @SuppressWarnings("unused")
    public static String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    @SuppressWarnings("unused")
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }
}
