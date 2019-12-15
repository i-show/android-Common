package com.ishow.common.widget.prompt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.ishow.common.R
import com.ishow.common.extensions.dp2px
import kotlin.math.max
import kotlin.math.min

class PromptHelper {
    var mode: Int = 0

    var text: String? = null
    var textColor: Int = 0
    var textSize: Int = 0
    var textRect = Rect()

    var textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    var padding: Int = 0
    var paddingWidth: Float = 0F
    var paddingHeight: Float = 0F

    var backgroundColor: Int = 0
    var position = 0
    var radius = 0

    var recordRectF = RectF()
    var usedRectF = RectF()

    private lateinit var prompt: IPrompt
    private lateinit var context: Context

    fun init(prompt: IPrompt) {
        this.prompt = prompt
        context = (prompt as View).context
        paddingWidth = min(1.0f, max(0f, paddingWidth))
        paddingHeight = min(1.0f, max(0f, paddingHeight))

        textPaint.textSize = textSize.toFloat()
        textPaint.color = textColor

        backgroundPaint.color = backgroundColor
        commit()
    }

    fun onMeasure(width: Int, height: Int) {
        if (mode == IPrompt.PromptMode.NONE) {
            return
        }

        if (mode == IPrompt.PromptMode.TEXT && TextUtils.isEmpty(text)) {
            return
        }

        when (position) {
            IPrompt.PromptPosition.LEFT -> {
                usedRectF.set(recordRectF)
                usedRectF.offset(width * paddingWidth, height * paddingHeight)
            }
            IPrompt.PromptPosition.RIGHT -> {
                usedRectF.set(recordRectF)
                usedRectF.offset(
                    width * (1 - paddingWidth) - recordRectF.width(),
                    height * paddingHeight
                )
            }
        }
    }

    fun onDraw(canvas: Canvas) {
        if (mode == IPrompt.PromptMode.NONE) {
            return
        }

        if (mode == IPrompt.PromptMode.TEXT && TextUtils.isEmpty(text)) {
            return
        }

        canvas.drawRoundRect(usedRectF, 999f, 999f, backgroundPaint)
        if (mode == IPrompt.PromptMode.TEXT && !TextUtils.isEmpty(text)) {
            val fontMetrics = textPaint.fontMetricsInt
            val baseline =
                usedRectF.top + (usedRectF.bottom - usedRectF.top - fontMetrics.bottom.toFloat() + fontMetrics.top) / 2 - fontMetrics.top
            textPaint.textAlign = Paint.Align.CENTER
            canvas.drawText(text!!, usedRectF.centerX(), baseline, textPaint)
        }
    }

    fun setPromptMode(mode: Int): IPrompt {
        this.mode = mode
        return prompt
    }

    fun setPromptText(text: Int): IPrompt {
        if (text > 99) {
            this.text = "99+"
        } else {
            this.text.toString()
        }
        return prompt
    }

    fun setPromptText(text: String): IPrompt {
        this.text = text
        return prompt
    }

    fun setPromptTextColor(@ColorInt color: Int): IPrompt {
        this.textColor = color
        textPaint.color = color
        return prompt
    }

    fun setPromptTextColorResource(@ColorRes colorRes: Int): IPrompt {
        val color = ContextCompat.getColor(context, colorRes)
        this.textColor = color
        textPaint.color = color
        return prompt
    }

    fun setPromptTextSize(size: Int): IPrompt {
        textSize = size.dp2px()
        textPaint.textSize = textSize.toFloat()
        return prompt
    }

    fun setPromptTextSizeResource(@DimenRes sizeRes: Int): IPrompt {
        val size = context.resources.getDimensionPixelSize(sizeRes)
        textSize = size
        textPaint.textSize = textSize.toFloat()
        return prompt
    }

    fun setPromptBackgroundColor(@ColorInt color: Int): IPrompt {
        this.backgroundColor = color
        backgroundPaint.color = color
        return prompt
    }

    fun setPromptBackgroundColorResource(@ColorRes colorRes: Int): IPrompt {
        val color = ContextCompat.getColor(context, colorRes)
        this.backgroundColor = color
        backgroundPaint.color = color
        return prompt
    }

    fun setPromptRadius(_radius: Int): IPrompt {
        val radius = _radius.dp2px()
        this.radius = radius
        return prompt
    }

    fun setPromptRadiusResource(@DimenRes radiusRes: Int): IPrompt {
        val radius = context.resources.getDimensionPixelSize(radiusRes)
        this.radius = radius
        return prompt
    }

    fun setPromptPadding(padding: Int): IPrompt {
        this.padding = padding.dp2px()
        return prompt
    }

    fun setPromptPaddingResource(@DimenRes paddingRes: Int): IPrompt {
        val padding = context.resources.getDimensionPixelSize(paddingRes)
        this.padding = padding
        return prompt
    }

    fun setPromptPosition(position: Int): IPrompt {
        this.position = position
        return prompt
    }

    fun setPromptWidthPaddingScale(scale: Float): IPrompt {
        this.paddingWidth = scale
        return prompt
    }

    fun setPromptHeightPaddingScale(scale: Float): IPrompt {
        this.paddingHeight = scale
        return prompt
    }

    fun commit(): IPrompt {
        when (mode) {
            IPrompt.PromptMode.TEXT -> if (TextUtils.isEmpty(text)) {
                textRect.set(0, 0, radius, radius)
                recordRectF.set(textRect)
            } else {
                textPaint.getTextBounds(text, 0, text!!.length, textRect)
                // 保证至少是圆形 视觉上感觉不太圆 所以+
                if (textRect.width() < textRect.height()) {
                    textRect.right = textRect.left + textRect.height() + 1
                }
                recordRectF.set(textRect)
                recordRectF.set(
                    recordRectF.left - padding,
                    recordRectF.top - padding,
                    recordRectF.right + padding,
                    recordRectF.bottom + padding
                )
                recordRectF.offset(padding.toFloat(), (textRect.height() + padding).toFloat())
            }
            IPrompt.PromptMode.GRAPH -> recordRectF.set(0f, 0f, radius.toFloat(), radius.toFloat())
        }

        return prompt
    }


    companion object {
        /**
         * 获取默认标题字体大小
         */
        @JvmStatic
        fun getDefaultTextSize(context: Context): Int {
            return context.resources.getDimensionPixelOffset(R.dimen.K_title)
        }

        /**
         * 获取默认标题字体大小
         */
        @JvmStatic
        fun getDefaultPadding(context: Context): Int {
            return context.resources.getDimensionPixelOffset(R.dimen.dp_5)
        }

        /**
         * 获取默认标题字体大小
         */
        @JvmStatic
        fun getDefaultRadius(context: Context): Int {
            return context.resources.getDimensionPixelOffset(R.dimen.dp_5)
        }
    }
}
