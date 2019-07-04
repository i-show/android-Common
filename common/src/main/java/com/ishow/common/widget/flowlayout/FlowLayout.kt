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

package com.ishow.common.widget.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.ishow.common.R
import kotlin.math.max

class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
        ViewGroup(context, attrs, defStyle) {
    private var mGap: Int = 0

    private val defaultGap: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.gap_grade_2)

    init {
        mGap = defaultGap
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //计算所有子View的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        //宽高设置为wrap_content时记录宽和高
        var desireWidth = 0
        var desireHeight = 0

        // with 2端需要空格
        var lineWidth = mGap + paddingLeft + paddingRight
        var lineHeight = mGap

        var childWidth: Int
        var childHeight: Int
        var childLayoutParams: MarginLayoutParams

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            childWidth = child.measuredWidth
            childHeight = child.measuredHeight

            childLayoutParams = child.layoutParams as MarginLayoutParams
            //当前子空间的实际占据尺寸
            childWidth += childLayoutParams.leftMargin + childLayoutParams.rightMargin + mGap
            childHeight += childLayoutParams.topMargin + childLayoutParams.bottomMargin + mGap

            // 换行
            if (lineWidth + childWidth > widthSize) {
                desireWidth = max(lineWidth, desireWidth)
                desireHeight += lineHeight

                //换行后重新开始计算
                lineWidth = mGap + childWidth + paddingLeft + paddingRight
                lineHeight = childHeight
            } else {
                lineWidth += childWidth
                lineHeight = max(lineHeight, childHeight)
            }
        }
        // 最后一行的高度和宽度在是没有进行添加的
        desireWidth = max(desireWidth, lineWidth)
        desireHeight += lineHeight + paddingTop + paddingBottom + mGap

        setMeasuredDimension(
                if (widthMode == MeasureSpec.EXACTLY) widthSize else desireWidth,
                if (heightMode == MeasureSpec.EXACTLY) heightSize else desireHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, right: Int, bottom: Int) {
        var left = paddingLeft + mGap
        var top = paddingTop + mGap
        var nextLineTop = top
        var nowLineTop = top

        var childWidth: Int
        var childHeight: Int
        var childLayoutParams: MarginLayoutParams
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            childWidth = child.measuredWidth
            childHeight = child.measuredHeight
            childLayoutParams = child.layoutParams as MarginLayoutParams

            // 需要换行
            if (left + childLayoutParams.leftMargin + childWidth + childLayoutParams.rightMargin + mGap > right) {
                nowLineTop = nextLineTop
                left = paddingLeft + mGap + childLayoutParams.leftMargin
                top = nowLineTop + childLayoutParams.topMargin

                child.layout(left, top, left + childWidth, top + childHeight)
            } else {
                left += childLayoutParams.leftMargin
                top = nowLineTop + childLayoutParams.topMargin

                child.layout(left, top, left + childWidth, top + childHeight)
            }

            left += childWidth + childLayoutParams.rightMargin + mGap
            nextLineTop = max(nextLineTop, child.bottom + childLayoutParams.bottomMargin + mGap)
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

}
