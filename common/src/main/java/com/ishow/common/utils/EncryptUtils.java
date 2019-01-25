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

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 */
@SuppressWarnings("unused")
public class EncryptUtils {
    /**
     * 32位 MD5加密
     */
    public static String md5(long valueL) {
        String value = String.valueOf(valueL);
        return md5(value);
    }

    /**
     * 32位 MD5加密
     */
    @SuppressWarnings("WeakerAccess")
    public static String md5(String value) {
        return md5(value, true);
    }


    /**
     * 32位 MD5加密
     */
    @SuppressWarnings("WeakerAccess")
    public static String md5(String value, boolean is32) {
        if (value == null) {
            value = StringUtils.EMPTY;
        }

        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return is32 ? hex.toString() : hex.substring(8, 24);
    }


    // *******************Base64 加密解密 *******************

    /**
     * base64加密
     */
    public static byte[] encodeBase64(byte[] input) {
        return Base64.encode(input, Base64.DEFAULT);
    }

    /**
     * base64加密
     */
    public static String encodeBase64(String s) {
        return Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
    }

    /**
     * base64解码
     */
    public static byte[] decodeBase64(byte[] input) {
        return Base64.decode(input, Base64.DEFAULT);
    }

    /**
     * base64解码
     */
    public static String decodeBase64(String s) {
        return new String(Base64.decode(s, Base64.DEFAULT));
    }
}
