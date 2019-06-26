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

import android.util.Base64

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 加密工具
 */
object EncryptUtils {
    /**
     * 32位 MD5加密
     */
    fun md5(value: Long): String {
        return md5(value.toString())
    }

    /**
     * 32位 MD5加密
     */
    @JvmOverloads
    fun md5(value: String?, is32: Boolean = true): String {
        if(value.isNullOrEmpty()){
            return StringUtils.EMPTY
        }

        val digest: ByteArray
        try {
            digest = MessageDigest.getInstance("MD5").digest(value.toByteArray(StandardCharsets.UTF_8))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, MD5 should be supported?", e)
        }

        val result  = StringBuffer()
        for (b in digest) {
            val i :Int = b.toInt() and 0xff
            var hex = Integer.toHexString(i)
            if (hex.length < 2) {
                hex = "0$hex"
            }
            result.append(hex)
        }
        return if (is32) result.toString() else result.substring(8, 24)
    }

    // *******************Base64 加密解密 *******************
    /**
     * base64加密
     */
    fun encodeBase64(input: ByteArray): ByteArray {
        return Base64.encode(input, Base64.DEFAULT)
    }

    /**
     * base64加密
     */
    fun encodeBase64(s: String): String {
        return Base64.encodeToString(s.toByteArray(), Base64.DEFAULT)
    }

    /**
     * base64解码
     */
    fun decodeBase64(input: ByteArray): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }

    /**
     * base64解码
     */
    fun decodeBase64(s: String): String {
        return String(Base64.decode(s, Base64.DEFAULT))
    }
}

