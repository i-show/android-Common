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

package com.ishow.common.utils.log

import android.text.TextUtils
import android.util.Log
import androidx.annotation.IntDef
import com.ishow.common.utils.StringUtils

/**
 * Created by Bright.Yu on 2017/1/5.
 * log的封装
 */

object LogUtils {
    private const val DEFAULT_MIX_TAG = "yhy"

    private const val DEFAULT_TAG = "yhy"
    /**
     * 是否需要混入
     */
    private var isMix: Boolean = false
    /**
     * 设置混入的TAG
     */
    private var mMixTag: String? = null

    /**
     * 输出等级
     */
    /**
     * 获取Log输出等级
     */
    /**
     * 设置Log输出等级
     */
    var level = Level.DEBUG

    /**
     * 是否是Debug状态
     */
    val isDebug: Boolean
        get() = level <= Level.DEBUG

    @JvmStatic
    fun setMix(mix: Boolean) {
        isMix = mix
    }

    @JvmStatic
    fun setMixTag(mixTag: String) {
        mMixTag = mixTag
    }


    /**
     * Info 级别的Log输出
     */
    @JvmStatic
    fun i(msg: String?) {
        i(DEFAULT_TAG, msg)
    }

    @JvmStatic
    fun i(tag: String?, msg: String?) {
        var finalTag = tag
        var finalMsg = msg

        if (level > Level.INFO) {
            return
        }

        if (isMix) {
            finalMsg = StringUtils.plusString(finalTag, "  ", finalMsg)
            finalTag = if (TextUtils.isEmpty(mMixTag)) DEFAULT_MIX_TAG else mMixTag
        }

        if (finalMsg == null) {
            Log.i(finalTag, "print: msg is null ")
        } else {
            Log.i(finalTag, finalMsg.toString())
        }
    }


    /**
     * Debug级别的log输出
     */
    @JvmStatic
    fun d(tag: String?, msg: String?) {
        var finalTag = tag
        var finalMsg = msg

        if (level > Level.DEBUG) {
            return
        }

        if (isMix) {
            finalMsg = StringUtils.plusString(finalTag, "  ", finalMsg)
            finalTag = if (TextUtils.isEmpty(mMixTag)) DEFAULT_MIX_TAG else mMixTag
        }

        if (finalMsg == null) {
            Log.d(finalTag, "msg is null ")
        } else {
            Log.d(finalTag, finalMsg.toString())
        }
    }


    /**
     * Warning 级别的log输出
     */
    @JvmStatic
    fun w(tag: String?, msg: Any?) {
        var finalTag = tag
        var finalMsg = msg

        if (level > Level.WARNING) {
            return
        }

        if (isMix) {
            finalMsg = StringUtils.plusString(finalTag, "  ", finalMsg)
            finalTag = if (TextUtils.isEmpty(mMixTag)) DEFAULT_MIX_TAG else mMixTag
        }

        if (finalMsg == null) {
            Log.e(finalTag, "msg is null ")
        } else {
            Log.e(finalTag, finalMsg.toString())
        }
    }


    /**
     * ERROR 级别的log输出
     */
    @JvmStatic
    fun e(tag: String?, msg: Any?) {
        var finalTag = tag
        var finalMsg = msg
        if (level > Level.ERROR) {
            return
        }

        if (isMix) {
            finalMsg = StringUtils.plusString(tag, "  ", finalMsg)
            finalTag = if (TextUtils.isEmpty(mMixTag)) DEFAULT_MIX_TAG else mMixTag
        }

        if (finalMsg == null) {
            Log.e(finalTag, " msg is null ")
        } else {
            Log.e(finalTag, finalMsg.toString())
        }
    }

    /**
     * 定义Log输出的级别
     */
    @IntDef(Level.INFO, Level.DEBUG, Level.WARNING, Level.ERROR)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Level {
        companion object {
            /**
             * Debug 级别
             */
            const val DEBUG = 1
            /**
             * Info 级别
             */
            const val INFO = 2
            /**
             * WARNING 级别
             */
            const val WARNING = 3
            /**
             * Error级别
             */
            const val ERROR = 4
            /**
             * 没有Log输出
             */
            const val NONE = 5
        }
    }
}
