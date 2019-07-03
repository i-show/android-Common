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
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import com.ishow.common.R
import com.ishow.common.widget.prompt.IPrompt
import com.ishow.common.widget.prompt.PromptHelper

/**
 * 角标提示的TextView
 */
class PromptImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle), IPrompt {
    private val prompt = PromptHelper()
    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.PromptImageView)
        prompt.mode = a.getInt(R.styleable.PromptImageView_promptMode, IPrompt.PromptMode.GRAPH)
        prompt.text = a.getString(R.styleable.PromptImageView_promptText)
        prompt.textColor = a.getColor(R.styleable.PromptImageView_promptTextColor, Color.WHITE)
        prompt.textSize = a.getDimensionPixelSize(R.styleable.PromptImageView_promptTextSize, PromptHelper.getDefaultTextSize(context))
        prompt.padding = a.getDimensionPixelSize(R.styleable.PromptImageView_promptPadding, PromptHelper.getDefaultPadding(context))
        prompt.radius = a.getDimensionPixelSize(R.styleable.PromptImageView_promptRadius, PromptHelper.getDefaultRadius(context))
        prompt.position = a.getInt(R.styleable.PromptImageView_promptPosition, IPrompt.PromptPosition.LEFT)
        prompt.backgroundColor = a.getColor(R.styleable.PromptImageView_promptBackground, Color.RED)
        prompt.paddingWidth = a.getFloat(R.styleable.PromptImageView_widthPaddingScale, IPrompt.DEFAULT_PADDING_SCALE)
        prompt.paddingHeight = a.getFloat(R.styleable.PromptImageView_heightPaddingScale, IPrompt.DEFAULT_PADDING_SCALE)
        a.recycle()

        prompt.init(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        prompt.onMeasure(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        prompt.onDraw(canvas)
    }

    override fun setPromptMode(@IPrompt.PromptMode mode: Int): IPrompt {
        return prompt.setPromptMode(mode)
    }

    override fun setPromptText(text: String): IPrompt {
        return prompt.setPromptText(text)
    }

    override fun setPromptText(text: Int): IPrompt {
        return prompt.setPromptText(text)
    }

    override fun setPromptTextColor(@ColorRes color: Int): IPrompt {
        return prompt.setPromptTextColor(color)
    }

    override fun setPromptTextSize(@DimenRes size: Int): IPrompt {
        return prompt.setPromptTextSize(size)
    }

    override fun setPromptBackgroundColor(@ColorRes color: Int): IPrompt {
        return prompt.setPromptBackgroundColor(color)
    }

    override fun setPromptRadius(@DimenRes radius: Int): IPrompt {
        return prompt.setPromptRadius(radius)
    }

    override fun setPromptPadding(@DimenRes padding: Int): IPrompt {
        return prompt.setPromptPadding(padding)
    }

    override fun setPromptPosition(@IPrompt.PromptPosition position: Int): IPrompt {
        return prompt.setPromptPosition(position)
    }

    override fun setPromptWidthPaddingScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): IPrompt {
        return prompt.setPromptWidthPaddingScale(scale)
    }

    override fun setPromptHeightPaddingScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): IPrompt {
        return prompt.setPromptHeightPaddingScale(scale)
    }

    override fun commit(): IPrompt {
        prompt.commit()
        requestLayout()
        return this
    }
}
