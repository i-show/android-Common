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

package com.ishow.common.widget.recyclerview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ScrollView

import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R


class RecyclerViewPro @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        RecyclerView(context, attrs, defStyleAttr) {
    private var mMaxHeight: Int = 0
    private var mMaxWidth: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewPro)
        mMaxHeight = a.getDimensionPixelSize(R.styleable.RecyclerViewPro_maxHeight, -1)
        mMaxWidth = a.getDimensionPixelSize(R.styleable.RecyclerViewPro_maxWidth, -1)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpec = if (mMaxWidth == -1) widthMeasureSpec else getAtMostSpec(mMaxWidth)
        val heightSpec = if (mMaxHeight == -1) heightMeasureSpec else getAtMostSpec(mMaxHeight)
        super.onMeasure(widthSpec, heightSpec)
    }


    private fun getAtMostSpec(size: Int): Int {
        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST)
    }
}
