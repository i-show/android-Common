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

package com.ishow.common.widget.framelayout

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ishow.common.R
import com.ishow.common.widget.scale.ScaleHelper

/**
 * Created by Bright.Yu on 2016/11/4.
 * 比例的ImageView
 */
class ScaleFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        FrameLayout(context, attrs, defStyle) {
    private var mScaleHelper: ScaleHelper

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ScaleFrameLayout)
        val widthRatio = a.getInt(R.styleable.ScaleFrameLayout_widthScale, 1)
        val heightRatio = a.getInt(R.styleable.ScaleFrameLayout_heightScale, 1)
        a.recycle()

        mScaleHelper = ScaleHelper(widthRatio, heightRatio)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureSpecs = mScaleHelper.onMeasure(layoutParams, widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(measureSpecs[0], measureSpecs[1])
    }

    fun setRatio(widthRatio: Int, heightRatio: Int) {
        mScaleHelper.setScale(widthRatio, heightRatio)
        requestLayout()
    }
}
