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

package com.ishow.common.widget.pickview.adapter


import androidx.annotation.IntRange

/**
 * Created by yuhaiyang on 2017/4/25.
 * 时间选择器
 */
class DateTimeAdapter : PickerAdapter<Int>() {
    private var mStart: Int = 0
    private var mEnd: Int = 0

    var start: Int
        get() = mStart
        set(@IntRange(from = 0) start) {
            if (mStart == start) {
                return
            }

            mStart = start
            if (mStart > mEnd) {
                mEnd = mStart
            }
            notifyDataSetChanged()
        }

    var end: Int
        get() = mEnd
        set(@IntRange(from = 1) end) {
            if (mEnd == end) {
                return
            }

            mEnd = end
            if (mStart > mEnd) {
                mStart = mEnd
            }
            notifyDataSetChanged()
        }

    init {
        mStart = 0
        mEnd = 0
    }

    override fun getCount(): Int {
        return mEnd - mStart + 1
    }

    override fun getItem(position: Int): Int {
        return mStart + position
    }

    override fun getItemText(position: Int): String {
        return (mStart + position).toString()
    }
}
