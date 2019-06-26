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

import android.content.res.ColorStateList

/**
 * 颜色变化管理
 */
object ColorUtils {
    private const val ENABLE_ATTR = android.R.attr.state_enabled
    private const val CHECKED_ATTR = android.R.attr.state_checked
    private const val PRESSED_ATTR = android.R.attr.state_pressed

    fun generateThumbColorWithTintColor(tintColor: Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(-ENABLE_ATTR, CHECKED_ATTR),
            intArrayOf(-ENABLE_ATTR),
            intArrayOf(PRESSED_ATTR, -CHECKED_ATTR),
            intArrayOf(PRESSED_ATTR, CHECKED_ATTR),
            intArrayOf(CHECKED_ATTR),
            intArrayOf(-CHECKED_ATTR)
        )

        val colors = intArrayOf(
            tintColor - 0xAA000000.toInt(),
            0xFFBABABA.toInt(),
            tintColor - 0x99000000.toInt(),
            tintColor - 0x99000000.toInt(),
            tintColor or 0xFF000000.toInt(),
            0xFFEEEEEE.toInt()
        )
        return ColorStateList(states, colors)
    }

    fun generateBackColorWithTintColor(tintColor: Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(-ENABLE_ATTR, CHECKED_ATTR),
            intArrayOf(-ENABLE_ATTR),
            intArrayOf(CHECKED_ATTR, PRESSED_ATTR),
            intArrayOf(-CHECKED_ATTR, PRESSED_ATTR),
            intArrayOf(CHECKED_ATTR),
            intArrayOf(-CHECKED_ATTR)
        )
        val colors = intArrayOf(
            tintColor - 0xE1000000.toInt(),
            0x10000000,
            tintColor - 0xD0000000.toInt(),
            0x20000000,
            tintColor - 0xD0000000.toInt(),
            0x20000000
        )
        return ColorStateList(states, colors)
    }
}
