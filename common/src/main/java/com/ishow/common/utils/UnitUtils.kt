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


import android.content.res.Resources

object UnitUtils {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     */
    @JvmStatic
    fun px2dip(value: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (value / scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    @JvmStatic
    fun dip2px(value: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    @JvmStatic
    fun dip2px(value: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    @JvmStatic
    fun px2sp(value: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (value / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    @JvmStatic
    fun sp2px(value: Float): Int {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (value * fontScale + 0.5f).toInt()
    }
}
