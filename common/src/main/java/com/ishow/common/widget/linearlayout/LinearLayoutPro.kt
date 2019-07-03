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

package com.ishow.common.widget.linearlayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.LinearLayout

import com.ishow.common.R


class LinearLayoutPro : LinearLayout {
    private var mMaxHeight: Int = 0
    private var mMaxWidth: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LinearLayoutPro)
        mMaxHeight = a.getDimensionPixelSize(R.styleable.LinearLayoutPro_maxHeight, -1)
        mMaxWidth = a.getDimensionPixelSize(R.styleable.LinearLayoutPro_maxWidth, -1)
        a.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpec = if (mMaxHeight == -1) heightMeasureSpec else MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        val widthSpec = if (mMaxWidth == -1) widthMeasureSpec else MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, heightSpec)
    }
}
