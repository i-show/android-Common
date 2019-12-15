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

package com.ishow.common.widget.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatTextView
import com.ishow.common.R
import com.ishow.common.widget.prompt.IPrompt
import com.ishow.common.widget.prompt.PromptHelper

/**
 * 角标提示的TextView
 */
class PromptTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle), IPrompt {
    private val helper = PromptHelper()

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.PromptTextView)
        helper.mode = a.getInt(R.styleable.PromptTextView_promptMode, IPrompt.PromptMode.GRAPH)
        helper.text = a.getString(R.styleable.PromptTextView_promptText)
        helper.textColor = a.getColor(R.styleable.PromptTextView_promptTextColor, Color.WHITE)
        helper.textSize = a.getDimensionPixelSize(
            R.styleable.PromptTextView_promptTextSize,
            PromptHelper.getDefaultTextSize(context)
        )
        helper.padding = a.getDimensionPixelSize(
            R.styleable.PromptTextView_promptPadding,
            PromptHelper.getDefaultPadding(context)
        )
        helper.radius = a.getDimensionPixelSize(
            R.styleable.PromptTextView_promptRadius,
            PromptHelper.getDefaultRadius(context)
        )
        helper.position =
            a.getInt(R.styleable.PromptTextView_promptPosition, IPrompt.PromptPosition.LEFT)
        helper.backgroundColor = a.getColor(R.styleable.PromptTextView_promptBackground, Color.RED)
        helper.paddingWidth =
            a.getFloat(R.styleable.PromptTextView_widthPaddingScale, IPrompt.DEFAULT_PADDING_SCALE)
        helper.paddingHeight =
            a.getFloat(R.styleable.PromptTextView_heightPaddingScale, IPrompt.DEFAULT_PADDING_SCALE)
        a.recycle()

        helper.init(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        helper.onMeasure(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        helper.onDraw(canvas)
    }

    override fun setPromptMode(@IPrompt.PromptMode mode: Int): IPrompt {
        return helper.setPromptMode(mode)
    }

    override fun setPromptText(text: String): IPrompt {
        return helper.setPromptText(text)
    }

    override fun setPromptText(text: Int): IPrompt {
        return helper.setPromptText(text)
    }

    override fun setPromptTextColor(@ColorInt color: Int): IPrompt {
        return helper.setPromptTextColor(color)
    }

    override fun setPromptTextColorResource(@ColorRes color: Int): IPrompt {
        return helper.setPromptTextColorResource(color)
    }

    override fun setPromptTextSize(size: Int): IPrompt {
        return helper.setPromptTextSize(size)
    }

    override fun setPromptTextSizeResource(@DimenRes size: Int): IPrompt {
        return helper.setPromptTextSizeResource(size)
    }

    override fun setPromptBackgroundColor(@ColorInt color: Int): IPrompt {
        return helper.setPromptBackgroundColor(color)
    }

    override fun setPromptBackgroundColorResource(@ColorRes color: Int): IPrompt {
        return helper.setPromptBackgroundColorResource(color)
    }

    override fun setPromptRadius(radius: Int): IPrompt {
        return helper.setPromptRadius(radius)
    }

    override fun setPromptRadiusResource(@DimenRes radius: Int): IPrompt {
        return helper.setPromptRadiusResource(radius)
    }

    override fun setPromptPadding(padding: Int): IPrompt {
        return helper.setPromptPadding(padding)
    }

    override fun setPromptPaddingResource(@DimenRes padding: Int): IPrompt {
        return helper.setPromptPaddingResource(padding)
    }

    override fun setPromptPosition(@IPrompt.PromptPosition position: Int): IPrompt {
        return helper.setPromptPosition(position)
    }

    override fun setPromptWidthPaddingScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): IPrompt {
        return helper.setPromptWidthPaddingScale(scale)
    }

    override fun setPromptHeightPaddingScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): IPrompt {
        return helper.setPromptHeightPaddingScale(scale)
    }

    /**
     * 更新信息
     */
    override fun commit(): IPrompt {
        helper.commit()
        requestLayout()
        return this
    }
}
