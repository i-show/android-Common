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

package com.ishow.common.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ishow.common.R
import com.ishow.common.widget.prompt.IPrompt
import com.ishow.common.widget.prompt.PromptHelper
import kotlin.math.max
import kotlin.math.min


class ImageTextView(context: Context, attrs: AttributeSet) : View(context, attrs), IPrompt {
    private var mPadding: Int = 0
    private var mTintColor: ColorStateList? = null

    private var mText: String
    private var mTextSize: Float = 0F
    private var mTextColor: Int = 0
    private var mTextStateColor: ColorStateList? = null
    private var mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var mDesireWidth: Int = 0
    private var mTextDesireWidth: Int = 0

    private var mTranslationX: Int = 0
    private var mTranslationY: Int = 0

    private var mImageDrawable: Drawable
    private var mImageWidth: Int = 0
    private var mImageHeight: Int = 0
    private val mImageRect = Rect()
    private var mImageOrientation: Int = 0

    private var mLayout: Layout? = null

    val text: CharSequence?
        get() = mText

    var textSize: Float
        get() = mTextSize
        set(size) {
            if (size < 0) {
                throw IllegalStateException("textSize need larger than 0")
            }
            mTextPaint.textSize = mTextSize
            computeDesireWidth()
            postInvalidate()
        }


    private val defaultTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.I_title)


    @IntDef(Orientation.LEFT, Orientation.TOP, Orientation.RIGHT, Orientation.BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Orientation {
        companion object {
            /**
             * Pictures in the text to the left
             */
            const val LEFT = 0
            /**
             * Pictures in the text to the right
             */
            const val RIGHT = 4
            /**
             * Pictures in the text to the top
             */
            const val TOP = 8
            /**
             * Pictures in the text to the bottom
             */
            const val BOTTOM = 16
        }
    }

    private val prompt = PromptHelper()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView)
        mImageDrawable = a.getDrawable(R.styleable.ImageTextView_image)!!
        mImageWidth = a.getDimensionPixelSize(R.styleable.ImageTextView_imageWidth, 0)
        mImageHeight = a.getDimensionPixelSize(R.styleable.ImageTextView_imageHeight, 0)
        mImageOrientation = a.getInt(R.styleable.ImageTextView_position, Orientation.TOP)
        mText = a.getString(R.styleable.ImageTextView_text)!!
        mTextSize =
            a.getDimensionPixelSize(R.styleable.ImageTextView_textSize, defaultTextSize).toFloat()
        mTextStateColor = a.getColorStateList(R.styleable.ImageTextView_textColor)
        mTintColor = a.getColorStateList(R.styleable.ImageTextView_tint)
        mPadding = a.getDimensionPixelSize(R.styleable.ImageTextView_padding, DEFAULT_PADDING)
        prompt.mode = a.getInt(R.styleable.ImageTextView_promptMode, IPrompt.PromptMode.NONE)
        prompt.text = a.getString(R.styleable.ImageTextView_promptText)
        prompt.textColor = a.getColor(R.styleable.ImageTextView_promptTextColor, Color.WHITE)
        prompt.textSize = a.getDimensionPixelSize(
            R.styleable.ImageTextView_promptTextSize,
            PromptHelper.getDefaultTextSize(context)
        )
        prompt.padding = a.getDimensionPixelSize(
            R.styleable.ImageTextView_promptPadding,
            PromptHelper.getDefaultPadding(context)
        )
        prompt.radius = a.getDimensionPixelSize(
            R.styleable.ImageTextView_promptRadius,
            PromptHelper.getDefaultRadius(context)
        )
        prompt.position =
            a.getInt(R.styleable.ImageTextView_promptPosition, IPrompt.PromptPosition.LEFT)
        prompt.backgroundColor = a.getColor(R.styleable.ImageTextView_promptBackground, Color.RED)
        prompt.paddingWidth =
            a.getFloat(R.styleable.ImageTextView_widthPaddingScale, IPrompt.DEFAULT_PADDING_SCALE)
        prompt.paddingHeight =
            a.getFloat(R.styleable.ImageTextView_heightPaddingScale, IPrompt.DEFAULT_PADDING_SCALE)
        a.recycle()
        mTextColor = if (mTextStateColor == null) {
            ContextCompat.getColor(context, R.color.text_grey)
        } else {
            mTextStateColor!!.defaultColor
        }
        init()
        prompt.init(this)
    }

    private fun init() {
        if (mTintColor != null) {
            mImageDrawable = DrawableCompat.wrap(mImageDrawable)
            DrawableCompat.setTintList(mImageDrawable, mTintColor)
        }

        mTextPaint.color = mTextColor
        mTextPaint.textSize = mTextSize

        computeDesireWidth()
    }


    /**
     * 点击状态有变化就会改变
     */
    override fun drawableStateChanged() {
        super.drawableStateChanged()
        var needInvalidate = false
        if (mImageDrawable is StateListDrawable) {
            val drawable = mImageDrawable as StateListDrawable
            if (mImageDrawable.isStateful) {
                val states = drawableState
                drawable.state = states
                needInvalidate = true
            }
        }

        if (mTextStateColor != null && mTextStateColor!!.isStateful) {
            mTextColor = mTextStateColor!!.getColorForState(drawableState, mTextColor)
            mTextPaint.color = mTextColor
            needInvalidate = true
        }

        if (needInvalidate) postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        prompt.onMeasure(width, height)
        setMeasuredDimension(width, height)
    }


    fun setTextSize(size: Int) {
        if (size < 0) {
            throw IllegalStateException("textSize need larger than 0")
        }
        mTextPaint.textSize = mTextSize
        computeDesireWidth()
        postInvalidate()
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)

        when (mode) {
            MeasureSpec.UNSPECIFIED -> return measureWidthUnspecified()
            MeasureSpec.EXACTLY -> return measureWidthByExactly(size)
            MeasureSpec.AT_MOST -> return measureWidthByAtMost(size)
        }

        return size
    }

    private fun measureWidthUnspecified(): Int {
        var width = 0
        val imageWidth = getImageWidth()
        @Suppress("DEPRECATION")
        mLayout = StaticLayout(
            mText,
            mTextPaint,
            mTextDesireWidth,
            Layout.Alignment.ALIGN_CENTER,
            1.0f,
            0.0f,
            true
        )
        when (mImageOrientation) {
            Orientation.TOP, Orientation.BOTTOM -> {
                width = paddingStart + max(mLayout!!.width, imageWidth) + paddingEnd
            }

            Orientation.LEFT, Orientation.RIGHT -> {
                width = paddingStart + mLayout!!.width + mPadding + imageWidth + paddingEnd
            }
        }
        return width
    }


    private fun measureWidthByExactly(size: Int): Int {
        val maxSize: Int
        when (mImageOrientation) {
            Orientation.TOP, Orientation.BOTTOM -> {
                maxSize = size - paddingStart - paddingEnd
                @Suppress("DEPRECATION")
                mLayout = StaticLayout(
                    mText,
                    mTextPaint,
                    maxSize,
                    Layout.Alignment.ALIGN_CENTER,
                    1.0f,
                    0.0f,
                    true
                )
            }

            Orientation.LEFT, Orientation.RIGHT -> {
                @Suppress("DEPRECATION")
                mLayout = StaticLayout(
                    mText,
                    mTextPaint,
                    mTextDesireWidth,
                    Layout.Alignment.ALIGN_CENTER,
                    1.0f,
                    0.0f,
                    true
                )
            }
        }
        return size
    }

    private fun measureWidthByAtMost(size: Int): Int {
        var width = size
        val imageWidth = getImageWidth()
        val maxSize: Int
        when (mImageOrientation) {
            Orientation.TOP, Orientation.BOTTOM -> {
                if (mDesireWidth < size) {
                    @Suppress("DEPRECATION")
                    mLayout = StaticLayout(
                        mText,
                        mTextPaint,
                        mTextDesireWidth,
                        Layout.Alignment.ALIGN_CENTER,
                        1.0f,
                        0.0f,
                        true
                    )
                    width = paddingStart + max(mLayout!!.width, imageWidth) + paddingEnd
                } else {
                    maxSize = size - paddingStart - paddingEnd
                    @Suppress("DEPRECATION")
                    mLayout = StaticLayout(
                        mText,
                        mTextPaint,
                        maxSize,
                        Layout.Alignment.ALIGN_CENTER,
                        1.0f,
                        0.0f,
                        true
                    )
                    width = size
                }
            }

            Orientation.LEFT, Orientation.RIGHT -> {
                if (mDesireWidth < size) {
                    @Suppress("DEPRECATION")
                    mLayout = StaticLayout(
                        mText,
                        mTextPaint,
                        mTextDesireWidth,
                        Layout.Alignment.ALIGN_CENTER,
                        1.0f,
                        0.0f,
                        true
                    )
                    width = paddingStart + mLayout!!.width + mPadding + imageWidth + paddingEnd
                } else {
                    maxSize = size - imageWidth - paddingStart - paddingEnd - mPadding
                    @Suppress("DEPRECATION")
                    mLayout = StaticLayout(
                        mText,
                        mTextPaint,
                        maxSize,
                        Layout.Alignment.ALIGN_CENTER,
                        1.0f,
                        0.0f,
                        true
                    )
                    width = size
                }
            }
        }
        return width
    }


    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)

        when (mode) {
            MeasureSpec.UNSPECIFIED -> return measureHeightUnspecified()
            MeasureSpec.EXACTLY -> return size
            MeasureSpec.AT_MOST -> return measureHeightByAtMost(size)
        }

        return size
    }

    private fun measureHeightUnspecified(): Int {
        var height = 0
        val imageHeight = getImageHeight()
        when (mImageOrientation) {
            Orientation.TOP, Orientation.BOTTOM -> {
                height = paddingTop + mLayout!!.height + mPadding + imageHeight + paddingEnd
            }

            Orientation.LEFT, Orientation.RIGHT -> {
                height = paddingTop + max(mLayout!!.height, imageHeight) + paddingEnd
            }
        }
        return height
    }

    private fun measureHeightByAtMost(size: Int): Int {
        var desireHeight = size
        val imageHeight = getImageHeight()
        when (mImageOrientation) {
            Orientation.TOP, Orientation.BOTTOM -> {
                desireHeight = paddingTop + mLayout!!.height + mPadding + imageHeight + paddingBottom
            }

            Orientation.LEFT, Orientation.RIGHT -> {
                desireHeight = paddingTop + max(mLayout!!.height, imageHeight) + paddingBottom
            }
        }
        return min(size, desireHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = measuredWidth
        val height = measuredHeight

        val iconWidth = getImageWidth()
        val iconHeight = getImageHeight()

        val textWidth = mLayout!!.width
        val textHeight = mLayout!!.height

        when (mImageOrientation) {
            Orientation.TOP -> {
                // Calculate icon Rect
                mImageRect.left = (width - iconWidth) / 2
                mImageRect.right = mImageRect.left + iconWidth
                mImageRect.top = (height - iconHeight - textHeight - mPadding) / 2
                mImageRect.bottom = mImageRect.top + iconHeight

                // Calculate text Rect
                mTranslationX = (width - textWidth) / 2
                mTranslationY = mImageRect.bottom + mPadding
            }
            Orientation.BOTTOM -> {
                // Calculate text Rect
                mTranslationX = (width - textWidth) / 2
                mTranslationY = (height - iconHeight - textHeight - mPadding) / 2

                // Calculate icon Rect
                mImageRect.left = (width - iconWidth) / 2
                mImageRect.right = mImageRect.left + iconWidth
                mImageRect.top = mTranslationY + textHeight + mPadding
                mImageRect.bottom = mImageRect.top + iconHeight
            }
            Orientation.LEFT -> {
                // Calculate icon Rect
                mImageRect.left = (width - iconWidth - textWidth - mPadding) / 2
                mImageRect.right = mImageRect.left + iconWidth
                mImageRect.top = (height - iconHeight) / 2
                mImageRect.bottom = mImageRect.top + iconHeight

                // Calculate text Rect
                mTranslationX = mImageRect.right + mPadding
                mTranslationY = (height - textHeight) / 2
            }
            Orientation.RIGHT -> {
                // Calculate text Rect
                mTranslationX = (width - iconWidth - textWidth - mPadding) / 2
                mTranslationY = (height - textHeight) / 2

                // Calculate icon Rect
                mImageRect.left = mTranslationX + textWidth + mPadding
                mImageRect.right = mImageRect.left + iconWidth
                mImageRect.top = (height - iconHeight) / 2
                mImageRect.bottom = mImageRect.top + iconHeight
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight
        if (width <= 0 || height <= 0) {
            return
        }

        drawIcon(canvas)
        drawText(canvas)
        prompt.onDraw(canvas)
    }


    private fun drawIcon(canvas: Canvas) {
        val drawable = mImageDrawable
        drawable.bounds = mImageRect
        drawable.draw(canvas)
    }

    private fun drawText(canvas: Canvas) {
        if (TextUtils.isEmpty(mText)) {
            Log.i(TAG, "drawText: mText is empty")
            return
        }
        canvas.save()
        canvas.translate(mTranslationX.toFloat(), mTranslationY.toFloat())
        mLayout!!.draw(canvas)
        canvas.restore()
    }


    private fun computeDesireWidth() {
        mTextDesireWidth = Layout.getDesiredWidth(mText, mTextPaint).toInt()
        val iconWidth = getImageWidth()

        when (mImageOrientation) {
            Orientation.LEFT, Orientation.RIGHT -> {
                mDesireWidth = mTextDesireWidth + iconWidth + mPadding + DEFAULT_PADDING * 2
            }
            Orientation.TOP, Orientation.BOTTOM -> {
                mDesireWidth = max(mTextDesireWidth, iconWidth) + DEFAULT_PADDING * 2
            }
        }
        minimumWidth = max(minimumWidth, mDesireWidth)
    }

    /**
     * Sets the ImageTextButton to display the Text show .
     */
    fun setTint(@ColorInt color: Int) {
        mTintColor = ColorStateList.valueOf(color)
        postInvalidate()
    }

    /**
     * Sets the ImageTextButton to display the Text show .
     */
    fun setTint(color: ColorStateList) {
        mTintColor = color
        postInvalidate()
    }

    fun setText(@StringRes resId: Int) {
        setText(context.getString(resId))
    }

    fun setText(text: CharSequence) {
        if (TextUtils.equals(text, mText)) {
            return
        }
        mText = text.toString()
        computeDesireWidth()
        requestLayout()
    }

    fun setImageOrientation(@Orientation orientation: Int) {
        mImageOrientation = orientation
        computeDesireWidth()
        requestLayout()
        postInvalidate()
    }

    fun setTextColor(@ColorRes color: Int) {
        mTextStateColor = ContextCompat.getColorStateList(context, color)
        mTextColor = mTextStateColor!!.defaultColor
        mTextPaint.color = mTextColor
        postInvalidate()
    }

    @Suppress("unused")
    fun setImage(@DrawableRes resId: Int) {
        mImageDrawable = ContextCompat.getDrawable(context, resId)!!
        postInvalidate()
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

    override fun setPromptWidthPaddingScale(
        @FloatRange(
            from = 0.0,
            to = 1.0
        ) scale: Float
    ): IPrompt {
        return prompt.setPromptWidthPaddingScale(scale)
    }

    override fun setPromptHeightPaddingScale(
        @FloatRange(
            from = 0.0,
            to = 1.0
        ) scale: Float
    ): IPrompt {
        return prompt.setPromptHeightPaddingScale(scale)
    }

    override fun commit(): IPrompt {
        prompt.commit()
        requestLayout()
        return this
    }

    private fun getImageWidth(): Int {
        return if (mImageWidth > 0) {
            mImageWidth
        } else {
            mImageDrawable.intrinsicWidth
        }
    }

    private fun getImageHeight(): Int {
        return if (mImageHeight > 0) {
            mImageHeight
        } else {
            mImageDrawable.intrinsicHeight
        }
    }

    companion object {
        private const val TAG = "ImageTextView"

        /**
         * 默认 文本和图片之间的间距
         */
        private const val DEFAULT_PADDING = 3
    }
}
