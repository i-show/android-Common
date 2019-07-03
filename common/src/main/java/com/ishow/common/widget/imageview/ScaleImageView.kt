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

package com.ishow.common.widget.imageview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup

import androidx.appcompat.widget.AppCompatImageView
import com.ishow.common.R
import com.ishow.common.widget.scale.ScaleHelper

/**
 * Created by Bright.Yu on 2016/11/4.
 * 比例的ImageView
 */

class ScaleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        AppCompatImageView(context, attrs, defStyle) {

    private var mScaleHelper: ScaleHelper

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView)
        val widthRatio = a.getInt(R.styleable.ScaleImageView_widthScale, 16)
        val heightRatio = a.getInt(R.styleable.ScaleImageView_heightScale, 9)
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
