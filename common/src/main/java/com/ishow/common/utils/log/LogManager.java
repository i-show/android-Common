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

package com.ishow.common.utils.log;

import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import com.ishow.common.utils.StringUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Bright.Yu on 2017/1/5.
 * log的封装
 */

public class LogManager {
    private static final String DEFAULT_MIX_TAG = "clubman";
    private static boolean isMix;

    private static String mMixTag;

    public static void setMix(boolean mix) {
        isMix = mix;
    }

    public static void setMixTag(String mixTag) {
        mMixTag = mixTag;
    }

    /**
     * 输出等级
     */
    private static int mLevel = Level.DEBUG;

    /**
     * 设置Log输出等级
     */
    @SuppressWarnings("unused")
    public static void setLevel(@Level int level) {
        mLevel = level;
    }

    /**
     * 获取Log输出等级
     */
    @SuppressWarnings("unused")
    public static int getLevel() {
        return mLevel;
    }

    /**
     * 是否是Debug状态
     */
    public static boolean isDebug() {
        return mLevel <= Level.DEBUG;
    }


    /**
     * Info 级别的Log输出
     */
    public static void i(String tag, String msg) {

        if (mLevel > Level.INFO) {
            return;
        }

        if (isMix) {
            msg = StringUtils.plusString(tag, "  " , msg);
            tag = TextUtils.isEmpty(mMixTag) ? DEFAULT_MIX_TAG : mMixTag;
        }

        if (msg == null) {
            Log.i(tag, "print: msg is null ");
        } else {
            Log.i(tag, msg.toString());
        }
    }


    /**
     * Debug级别的log输出
     */
    public static void d(String tag, String msg) {

        if (mLevel > Level.DEBUG) {
            return;
        }

        if (isMix) {
            msg = StringUtils.plusString(tag, "  " , msg);
            tag = TextUtils.isEmpty(mMixTag) ? DEFAULT_MIX_TAG : mMixTag;
        }

        if (msg == null) {
            Log.d(tag, "msg is null ");
        } else {
            Log.d(tag, msg.toString());
        }
    }


    /**
     * Warning 级别的log输出
     */
    @SuppressWarnings("WeakerAccess")
    public static void w(String tag, Object msg) {

        if (mLevel > Level.WARNING) {
            return;
        }

        if (isMix) {
            msg = StringUtils.plusString(tag, "  " , msg);
            tag = TextUtils.isEmpty(mMixTag) ? DEFAULT_MIX_TAG : mMixTag;
        }

        if (msg == null) {
            Log.e(tag, "msg is null ");
        } else {
            Log.e(tag, msg.toString());
        }
    }


    /**
     * ERROR 级别的log输出
     */
    public static void e(String tag, Object msg) {
        if (mLevel > Level.ERROR) {
            return;
        }

        if (isMix) {
            msg = StringUtils.plusString(tag, "  " , msg);
            tag = TextUtils.isEmpty(mMixTag) ? DEFAULT_MIX_TAG : mMixTag;
        }

        if (msg == null) {
            Log.e(tag, " msg is null ");
        } else {
            Log.e(tag, msg.toString());
        }
    }

    /**
     * 定义Log输出的级别
     */
    @IntDef({Level.INFO, Level.DEBUG, Level.WARNING, Level.ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
        /**
         * Debug 级别
         */
        int DEBUG = 1;
        /**
         * Info 级别
         */
        int INFO = 2;
        /**
         * WARNING 级别
         */
        int WARNING = 3;
        /**
         * Error级别
         */
        int ERROR = 4;
        /**
         * 没有Log输出
         */
        int NONE = 5;
    }
}
