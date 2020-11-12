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
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.bumptech.glide.request.RequestOptions
import com.ishow.common.R
import com.ishow.common.extensions.*
import com.ishow.common.widget.imageview.PromptImageView
import com.ishow.common.widget.prompt.IPrompt
import kotlin.math.max

/**
 * Created by Bright.Yu
 * 加强版本的TextView
 */
class TextViewPro @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ViewGroup(context, attrs, defStyle) {
    /**
     * 左侧图片信息
     */
    var leftImageView: PromptImageView? = null
        private set
    private var leftImageDrawable: Drawable? = null
    private val leftImageBackgroundDrawable: Drawable?
    private val leftImageWidth: Int
    private val leftImageHeight: Int
    private var leftImageVisibility: Int = 0
    private val leftImageMarginStart: Int
    private val leftImageMarginEnd: Int

    /**
     * 左侧文本信息
     */
    var leftTextView: PromptTextView? = null
        private set
    private var leftTextString: String? = null
    private val leftTextSize: Int
    private val leftTextColor: Int
    private val leftTextVisibility: Int
    private val leftTextMinWidth: Int
    private val leftTextMaxWidth: Int
    private val leftTextMarginStart: Int
    private val leftTextMarginEnd: Int
    private val leftTextGravity: Int
    private val leftTextStyle: Int
    private val leftTextBackgroundDrawable: Drawable?

    /**
     * 中间的View
     */
    lateinit var mainView: TextView
        private set
    private val textBackgroundDrawable: Drawable?
    private val textGravity: Int
    private val textEllipsize: Int
    private val textSize: Int
    private val textColor: Int
    private val textLines: Int
    private val textMarginStart: Int
    private val textMarginEnd: Int
    private val textString: String?

    /**
     * 右侧文本信息
     */
    var rightTextView: PromptTextView? = null
        private set
    private var rightTextString: String? = null
    private val rightTextSize: Int
    private var rightTextVisibility: Int = 0
    private val rightTextMarginStart: Int
    private val rightTextMarginEnd: Int
    private val rightTextMinWidth: Int
    private val rightTextMaxWidth: Int
    private val rightTextGravity: Int
    private val rightTextPadding: Int
    private val rightTextDrawablePadding: Int
    private val rightTextPaddingHorizontal: Int
    private val rightTextPaddingVertical: Int
    private var rightTextColor: ColorStateList? = null
    private val rightTextBackgroundDrawable: Drawable?
    private var rightTextRightDrawable: Drawable? = null
    private var rightTextLeftDrawable: Drawable? = null

    /**
     * 右侧图片信息
     */
    var rightImageView: PromptImageView? = null
        private set
    private var rightImageDrawable: Drawable? = null
    private val rightImageBackgroundDrawable: Drawable?
    private val rightImageWidth: Int
    private val rightImageHeight: Int
    private val rightImageMarginStart: Int
    private val rightImageMarginEnd: Int
    private var rightImageVisibility: Int = 0

    var rightImageView2: PromptImageView? = null
        private set
    private var rightImage2Drawable: Drawable? = null
    private val rightImage2BackgroundDrawable: Drawable?
    private val rightImage2Width: Int
    private val rightImage2Height: Int
    private val rightImage2MarginStart: Int
    private val rightImage2MarginEnd: Int
    private var rightImage2Visibility: Int = 0

    private val topLinePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val topLineHeight: Int
    private val topLineColor: Int
    private val topLineVisibility: Int
    private val topLinePaddingStart: Int
    private val topLinePaddingEnd: Int

    private val bottomLinePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val bottomLineHeight: Int
    private val bottomLineColor: Int
    private val bottomLineVisibility: Int
    private val bottomLinePaddingStart: Int
    private val bottomLinePaddingEnd: Int

    /**
     * 自定义插入View
     * index = -1 为最后
     */
    private var customizeView: View? = null
    private val customizeViewId: Int
    private val customizeViewIndex: Int
    private val customizeMarginStart: Int
    private val customizeMarginEnd: Int

    /**
     * 图片的建议宽度
     */
    private var suggestIconWidth: Int = 0

    private val minHeight: Int
    private val tintColor: ColorStateList?

    // 默认的信息
    private val defaultTipTextSize: Int = 14.sp2px()
    private val defaultTipMinWidth: Int = 50.dp2px()
    private val defaultTipMaxWidth: Int = 120.dp2px()
    private val defaultTipTextColor: Int = context.findColor(R.color.text_grey_light)
    private val defaultTipTextColorStateList = context.findColorStateList(R.color.text_grey_light)

    private val defaultInputTextSize: Int = 14.sp2px()
    private val defaultInputTextColor: Int = ContextCompat.getColor(context, R.color.text_grey)

    private val defaultLineColor: Int = context.findColor(R.color.line)
    private val defaultLineHeight: Int = 1.dp2px()
    private val defaultMinHeight: Int = 50.dp2px()

    var text: String
        get() = mainView.text.toString().trim()
        set(value) = setText(value)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPro, R.attr.textViewProStyle, 0)

        tintColor = a.getColorStateList(R.styleable.TextViewPro_tintColor)

        leftImageDrawable = a.getDrawable(R.styleable.TextViewPro_leftImage)
        leftImageWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageWidth, -1)
        leftImageHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageHeight, -1)
        leftImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftImageBackground)
        leftImageVisibility = a.getInt(R.styleable.TextViewPro_leftImageVisibility, View.VISIBLE)
        leftImageMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageMarginStart, 0)
        leftImageMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageMarginEnd, 0)

        leftTextString = a.getString(R.styleable.TextViewPro_leftText)
        leftTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextSize, defaultTipTextSize)
        leftTextColor = a.getColor(R.styleable.TextViewPro_leftTextColor, defaultTipTextColor)
        leftTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMarginStart, 0)
        leftTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMarginEnd, 0)
        leftTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMinWidth, defaultTipMinWidth)
        leftTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMaxWidth, defaultTipMaxWidth)
        leftTextVisibility = a.getInt(R.styleable.TextViewPro_leftTextVisibility, View.VISIBLE)
        leftTextGravity = a.getInt(R.styleable.TextViewPro_leftTextGravity, Gravity.CENTER)
        leftTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftTextBackground)
        leftTextStyle = a.getInt(R.styleable.TextViewPro_leftTextStyle, 0)

        textString = a.getString(R.styleable.TextViewPro_text)
        textSize = a.getDimensionPixelSize(R.styleable.TextViewPro_textSize, defaultInputTextSize)
        textColor = a.getColor(R.styleable.TextViewPro_textColor, defaultInputTextColor)
        textMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_textMarginStart, 0)
        textMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_textMarginEnd, 0)
        textBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_textBackground)
        textGravity = a.getInt(R.styleable.TextViewPro_textGravity, Gravity.CENTER_VERTICAL)
        textEllipsize = a.getInt(R.styleable.TextViewPro_textEllipsize, -1)
        textLines = a.getInt(R.styleable.TextViewPro_textLines, 0)

        rightTextString = a.getString(R.styleable.TextViewPro_rightText)
        rightTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextSize, defaultTipTextSize)
        rightTextColor = a.getColorStateList(R.styleable.TextViewPro_rightTextColor)
        rightTextVisibility = a.getInt(R.styleable.TextViewPro_rightTextVisibility, View.GONE)
        rightTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMarginStart, 0)
        rightTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMarginEnd, 0)
        rightTextPadding = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPadding, 0)
        rightTextPaddingHorizontal = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPaddingHorizontal, 0)
        rightTextPaddingVertical = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPaddingVertical, 0)
        rightTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMinWidth, defaultTipMinWidth)
        rightTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMaxWidth, defaultTipMaxWidth)
        rightTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextDrawablePadding, 5)
        rightTextLeftDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextLeftDrawable)
        rightTextRightDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextRightDrawable)
        rightTextGravity = a.getInt(R.styleable.TextViewPro_rightTextGravity, Gravity.CENTER)
        rightTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextBackground)

        rightImageDrawable = a.getDrawable(R.styleable.TextViewPro_rightImage)
        rightImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightImageBackground)
        rightImageVisibility = a.getInt(R.styleable.TextViewPro_rightImageVisibility, View.VISIBLE)
        rightImageWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageWidth, 0)
        rightImageHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageHeight, 0)
        rightImageMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageMarginStart, 0)
        rightImageMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageMarginEnd, 0)

        rightImage2Drawable = a.getDrawable(R.styleable.TextViewPro_rightImage2)
        rightImage2BackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightImage2Background)
        rightImage2Visibility = a.getInt(R.styleable.TextViewPro_rightImage2Visibility, View.GONE)
        rightImage2Width = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2Width, 0)
        rightImage2Height = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2Height, 0)
        rightImage2MarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2MarginStart, 0)
        rightImage2MarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2MarginEnd, 0)

        topLineHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_topLineHeight, defaultLineHeight)
        topLineColor = a.getColor(R.styleable.TextViewPro_topLineColor, defaultLineColor)
        topLineVisibility = a.getInt(R.styleable.TextViewPro_topLineVisibility, View.GONE)
        topLinePaddingStart = a.getDimensionPixelSize(R.styleable.TextViewPro_topLinePaddingStart, 0)
        topLinePaddingEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_topLinePaddingEnd, 0)

        bottomLineHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLineHeight, defaultLineHeight)
        bottomLineColor = a.getColor(R.styleable.TextViewPro_bottomLineColor, defaultLineColor)
        bottomLineVisibility = a.getInt(R.styleable.TextViewPro_bottomLineVisibility, View.VISIBLE)
        bottomLinePaddingStart = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLinePaddingStart, 0)
        bottomLinePaddingEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLinePaddingEnd, 0)

        customizeViewId = a.getResourceId(R.styleable.TextViewPro_customizeViewId, 0)
        customizeViewIndex = a.getInt(R.styleable.TextViewPro_customizeViewIndex, -1)
        customizeMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_customizeMarginStart, 0)
        customizeMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_customizeMarginEnd, 0)

        minHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_android_minHeight, defaultMinHeight)
        a.recycle()

        if (rightTextColor == null) {
            rightTextColor = defaultTipTextColorStateList
        }

        initNecessaryData()
        initView()
    }

    private fun initNecessaryData() {
        suggestIconWidth = 40.dp2px()

        topLinePaint.color = topLineColor
        topLinePaint.strokeWidth = topLineHeight.toFloat()

        bottomLinePaint.color = bottomLineColor
        bottomLinePaint.strokeWidth = bottomLineHeight.toFloat()
    }

    private fun initView() {
        addLeftImage()
        addLeftText()
        addTextView()
        addRightText()
        addRightImage()
        addRightImage2()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (customizeViewId > 0) {
            customizeView = findViewById(customizeViewId)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        var height = measureHeight(width, heightMeasureSpec)

        val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        val imageWidthSpec = MeasureSpec.makeMeasureSpec(suggestIconWidth, MeasureSpec.EXACTLY)
        val unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

        var textWidth = width - paddingLeft - paddingRight

        if (leftImageView != null && leftImageView!!.visibility != View.GONE) {
            if (leftImageWidth > 0 && leftImageHeight > 0) {
                leftImageView!!.measure(getExactlyMeasureSpec(leftImageWidth), getExactlyMeasureSpec(leftImageHeight))
            } else if (leftImageWidth > 0) {
                leftImageView!!.measure(getExactlyMeasureSpec(leftImageWidth), heightSpec)
            } else if (leftImageHeight > 0) {
                leftImageView!!.measure(imageWidthSpec, getExactlyMeasureSpec(leftImageHeight))
            } else {
                leftImageView!!.measure(imageWidthSpec, heightSpec)
            }
            textWidth = textWidth - leftImageView!!.measuredWidth - leftImageMarginStart - leftImageMarginEnd
        }

        if (leftTextView != null && leftTextView!!.visibility != View.GONE) {
            leftTextView!!.measure(unspecified, heightSpec)
            textWidth = textWidth - leftTextView!!.measuredWidth - leftTextMarginStart - leftTextMarginEnd
        }

        if (rightImageView != null && rightImageView!!.visibility != View.GONE) {
            if (rightImageWidth > 0 && rightImageHeight > 0) {
                rightImageView!!.measure(getExactlyMeasureSpec(rightImageWidth), getExactlyMeasureSpec(rightImageHeight))
            } else if (rightImageWidth > 0) {
                rightImageView!!.measure(getExactlyMeasureSpec(rightImageWidth), heightSpec)
            } else if (rightImageHeight > 0) {
                rightImageView!!.measure(imageWidthSpec, getExactlyMeasureSpec(rightImageHeight))
            } else {
                rightImageView!!.measure(imageWidthSpec, heightSpec)
            }

            textWidth = textWidth - rightImageView!!.measuredWidth - rightImageMarginStart - rightImageMarginEnd
        }

        if (rightImageView2 != null && rightImageView2!!.visibility != View.GONE) {
            if (rightImage2Width > 0 && rightImage2Height > 0) {
                rightImageView2!!.measure(getExactlyMeasureSpec(rightImage2Width), getExactlyMeasureSpec(rightImage2Height))
            } else if (rightImage2Width > 0) {
                rightImageView2!!.measure(getExactlyMeasureSpec(rightImage2Width), heightSpec)
            } else if (rightImage2Height > 0) {
                rightImageView2!!.measure(imageWidthSpec, getExactlyMeasureSpec(rightImage2Height))
            } else {
                rightImageView2!!.measure(imageWidthSpec, heightSpec)
            }

            textWidth = textWidth - rightImageView2!!.measuredWidth - rightImage2MarginStart - rightImage2MarginEnd
        }


        if (rightTextView != null && rightTextView!!.visibility != View.GONE) {
            rightTextView!!.measure(unspecified, unspecified)
            textWidth = textWidth - rightTextView!!.measuredWidth - rightTextMarginStart - rightTextMarginEnd
        }

        if (customizeView != null && customizeView!!.visibility != View.GONE) {
            measureChild(customizeView, widthSpec, heightSpec)
            textWidth = textWidth - customizeView!!.measuredWidth - customizeMarginStart - customizeMarginEnd
        }

        textWidth = textWidth - textMarginStart - textMarginEnd
        mainView.measure(MeasureSpec.makeMeasureSpec(textWidth, MeasureSpec.EXACTLY), heightSpec)

        height += paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        return MeasureSpec.getSize(widthMeasureSpec)
    }

    private fun measureHeight(width: Int, heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        when (mode) {
            MeasureSpec.EXACTLY -> return size
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {
                val paddingStart = paddingStart
                val paddingEnd = paddingEnd
                var height = minHeight

                var inputWidth = width - paddingStart - paddingEnd

                if (leftImageView != null && leftImageView!!.visibility != View.GONE) {
                    inputWidth = if (leftImageWidth > 0) {
                        inputWidth - leftImageWidth - leftImageMarginStart - leftImageMarginEnd
                    } else {
                        inputWidth - suggestIconWidth - leftImageMarginStart - leftImageMarginEnd
                    }
                }

                if (leftTextView != null && leftTextView!!.visibility != View.GONE) {
                    val unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    leftTextView!!.measure(unspecified, unspecified)
                    val leftTextHeight = leftTextView!!.measuredHeight
                    val leftTextWidth = leftTextView!!.measuredWidth
                    inputWidth = inputWidth - leftTextWidth - leftTextMarginStart - leftTextMarginEnd
                    height = max(height, leftTextHeight)
                }

                if (rightTextView != null && rightTextView!!.visibility != View.GONE) {
                    val unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    rightTextView!!.measure(unspecified, unspecified)
                    val rightTextHeight = rightTextView!!.measuredHeight
                    val rightTextWidth = rightTextView!!.measuredWidth
                    inputWidth = inputWidth - rightTextWidth - rightTextMarginStart - rightTextMarginEnd
                    height = max(height, rightTextHeight)
                }

                if (rightImageView != null && rightImageView!!.visibility != View.GONE) {
                    inputWidth = if (rightImageWidth > 0) {
                        inputWidth - rightImageWidth - rightImageMarginStart - rightImageMarginEnd
                    } else {
                        inputWidth - suggestIconWidth - rightImageMarginStart - rightImageMarginEnd
                    }
                }

                if (rightImageView2 != null && rightImageView2!!.visibility != View.GONE) {
                    inputWidth = if (rightImage2Width > 0) {
                        inputWidth - rightImage2Width - rightImage2MarginStart - rightImage2MarginEnd
                    } else {
                        inputWidth - suggestIconWidth - rightImage2MarginStart - rightImage2MarginEnd
                    }
                }

                inputWidth = inputWidth - textMarginStart - textMarginEnd

                val widthSpec = MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY)
                val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                mainView.measure(widthSpec, heightSpec)
                val inputHeight = mainView.measuredHeight
                height = max(height, inputHeight)
                return height
            }
        }
        return size
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingStart
        val top = paddingTop
        val bottom = b - t - paddingBottom
        var index = 0
        var lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (leftImageView != null && leftImageView!!.visibility != View.GONE) {
            val width = leftImageView!!.measuredWidth
            val height = leftImageView!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += leftImageMarginStart
            leftImageView!!.layout(left, temTop, left + width, temTop + height)
            left += width + leftImageMarginEnd
            index++
        }
        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (leftTextView != null && leftTextView!!.visibility != View.GONE) {
            val width = leftTextView!!.measuredWidth
            left += leftTextMarginStart
            leftTextView!!.layout(left, top, left + width, bottom)
            left += width + leftTextMarginEnd
            index++
        }

        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        left += textMarginStart
        val inputWidth = mainView.measuredWidth
        mainView.layout(left, top, left + inputWidth, bottom)
        left += inputWidth + textMarginEnd
        index++

        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (rightTextView != null && rightTextView!!.visibility != View.GONE) {
            val width = rightTextView!!.measuredWidth
            val height = rightTextView!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += rightTextMarginStart
            rightTextView!!.layout(left, temTop, left + width, temTop + height)
            left += width + rightTextMarginEnd
            index++
        }

        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (rightImageView2 != null && rightImageView2!!.visibility != View.GONE) {
            val width = rightImageView2!!.measuredWidth
            val height = rightImageView2!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += rightImage2MarginStart
            rightImageView2!!.layout(left, temTop, left + width, temTop + height)
            left += width + rightImage2MarginEnd
            index++
        }

        // 倒数第二个不需要进行处理index了， 因为最后一个的index 肯定为-1
        left = layoutCustomize(left, index)

        if (rightImageView != null && rightImageView!!.visibility != View.GONE) {
            val width = rightImageView!!.measuredWidth
            val height = rightImageView!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += rightImageMarginStart
            rightImageView!!.layout(left, temTop, left + width, temTop + height)
        }

        index = -1
        layoutCustomize(left, index)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight

        if (topLineVisibility == View.VISIBLE) {
            canvas.drawLine(
                topLinePaddingStart.toFloat(),
                topLineHeight.toFloat(),
                (width - topLinePaddingEnd).toFloat(),
                topLineHeight.toFloat(),
                topLinePaint
            )
        }

        if (bottomLineVisibility == View.VISIBLE) {
            canvas.drawLine(
                bottomLinePaddingStart.toFloat(),
                (height - bottomLineHeight).toFloat(),
                (width - bottomLinePaddingEnd).toFloat(),
                (height - bottomLineHeight).toFloat(),
                bottomLinePaint
            )
        }
    }

    fun setLeftImageVisibility(visibility: Int) {
        leftImageView?.visibility = visibility
        leftImageVisibility = visibility
    }

    fun setLeftImageSelected(selected: Boolean) {
        leftImageView?.isSelected = selected
    }

    fun setLeftText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setLeftText(text)
    }

    fun setLeftText(text: String) {
        leftTextString = text
        leftTextView?.text = text
    }

    fun setLeftTextGravity(gravity: Int) {
        leftTextView?.gravity = gravity
    }

    fun setLeftTextMinWidth(width: Int) {
        leftTextView?.minWidth = width
        requestLayout()
    }

    fun setText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setText(text)
    }

    fun setText(text: CharSequence?) {
        mainView.text = text
        requestLayout()
    }

    fun setTextSize(@DimenRes resId: Int) {
        val size = resources.getDimensionPixelSize(resId).toFloat()
        mainView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        requestLayout()
    }

    fun setTextColor(@ColorInt colorInt: Int) {
        mainView.setTextColor(colorInt)
    }

    fun setTextMovementMethod(movement: MovementMethod) {
        mainView.movementMethod = movement
    }

    fun setTextHighlightColor(color: Int) {
        mainView.highlightColor = color
    }

    fun setRightText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setRightText(text)
    }

    fun setRightText(text: String?) {
        rightTextString = text
        rightTextView?.text = text
        rightTextVisibility = View.VISIBLE
        requestLayout()
    }

    fun setRightTextSelected(selected: Boolean) {
        rightTextView?.isSelected = selected
    }

    fun setRightTextBackgroundResources(@DrawableRes backgroundResources: Int) {
        rightTextView?.setBackgroundResource(backgroundResources)
    }

    fun setRightImageResource2(@DrawableRes resId: Int) {
        rightImageView2?.setImageResource(resId)
        rightImage2Visibility = View.VISIBLE
        requestLayout()
    }

    @JvmOverloads
    fun setRightImageUrl2(url: String?, options: RequestOptions = RequestOptions.centerCropTransform()) {
        if (rightImageView2 == null) {
            addRightImage2()
        }

        rightImageView2?.loadUrl(url, options)
        rightImage2Visibility = View.VISIBLE
        requestLayout()
    }

    fun setRightImageResource(@DrawableRes resId: Int) {
        rightImageView?.setImageResource(resId)
        rightImageVisibility = View.VISIBLE
        requestLayout()
    }

    fun setRightImageVisibility(visibility: Int) {
        rightImageView?.visibility = visibility
        rightImageVisibility = visibility
        requestLayout()
    }

    fun setRightImageClickListener(listener: OnClickListener) {
        rightImageView?.setOnClickListener(listener)
    }

    fun setRightTextClickListener(listener: OnClickListener) {
        rightTextView?.setOnClickListener(listener)
    }

    /**
     * 设置右侧按钮是否可见
     */
    fun setRightTextVisibility(visibility: Int) {
        rightTextView?.visibility = visibility
        rightTextVisibility = visibility
        requestLayout()
    }

    private fun addLeftImage() {
        if (leftImageVisibility == View.GONE) {
            return
        }

        if (leftImageView == null) {
            leftImageDrawable = leftImageDrawable?.tint(tintColor)

            val imageView = PromptImageView(context)
            imageView.id = R.id.leftImage
            imageView.visibility = View.VISIBLE
            imageView.setImageDrawable(leftImageDrawable)
            imageView.background = leftImageBackgroundDrawable
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            setDefaultPromptState(imageView)
            leftImageView = imageView
            addView(leftImageView)
        }
    }

    private fun addLeftText() {
        if (leftTextVisibility == View.GONE) {
            return
        }

        if (leftTextView == null) {
            val textView = PromptTextView(context)
            textView.id = R.id.leftText
            textView.text = leftTextString
            textView.setTextColor(leftTextColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize.toFloat())
            textView.minWidth = leftTextMinWidth
            textView.maxWidth = leftTextMaxWidth
            textView.background = leftTextBackgroundDrawable
            textView.gravity = leftTextGravity
            textView.typeface = Typeface.defaultFromStyle(leftTextStyle)
            textView.includeFontPadding = false
            setDefaultPromptState(textView)

            leftTextView = textView
            addView(leftTextView)
        }
    }

    private fun addTextView() {
        mainView = AppCompatTextView(context)
        setInputEllipsize()
        mainView.text = textString
        mainView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        mainView.setTextColor(textColor)
        mainView.background = textBackgroundDrawable
        mainView.gravity = textGravity
        if (textLines > 0) {
            mainView.setLines(textLines)
        }
        addView(mainView)
    }

    private fun addRightText() {
        if (rightTextVisibility == View.GONE) {
            return
        }

        if (rightTextView == null) {
            val textView = PromptTextView(context)
            textView.id = R.id.rightText
            textView.text = rightTextString
            textView.gravity = Gravity.CENTER
            textView.setTextColor(rightTextColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize.toFloat())
            textView.minWidth = rightTextMinWidth
            textView.maxWidth = rightTextMaxWidth
            textView.background = rightTextBackgroundDrawable
            textView.gravity = rightTextGravity
            textView.includeFontPadding = false
            if (rightTextPadding > 0) {
                textView.setPadding(rightTextPadding)
            } else if (rightTextPaddingHorizontal > 0 && rightTextPaddingVertical > 0) {
                textView.setPadding(rightTextPaddingHorizontal, rightTextPaddingVertical)
            } else if (rightTextPaddingHorizontal > 0) {
                textView.setPaddingHorizontal(rightTextPaddingHorizontal)
            } else if (rightTextPaddingVertical > 0) {
                textView.setPaddingVertical(rightTextPaddingVertical)
            }

            if (rightTextRightDrawable != null || rightTextLeftDrawable != null) {
                rightTextLeftDrawable = rightTextLeftDrawable?.tint(tintColor)
                rightTextRightDrawable = rightTextRightDrawable?.tint(tintColor)

                textView.setCompoundDrawablesWithIntrinsicBounds(
                    rightTextLeftDrawable,
                    null,
                    rightTextRightDrawable,
                    null
                )
                textView.compoundDrawablePadding = rightTextDrawablePadding
            }
            setDefaultPromptState(textView)
            rightTextView = textView
            addView(rightTextView)
        }
    }

    private fun addRightImage() {
        if (rightImageVisibility == View.GONE) {
            return
        }

        if (rightImageView == null) {
            rightImageDrawable = rightImageDrawable?.tint(tintColor)

            val imageView = PromptImageView(context)
            imageView.id = R.id.rightImage
            imageView.visibility = rightImageVisibility
            imageView.setImageDrawable(rightImageDrawable)
            imageView.background = rightImageBackgroundDrawable
            imageView.scaleType = ImageView.ScaleType.CENTER
            setDefaultPromptState(imageView)

            rightImageView = imageView
            addView(rightImageView)
        }
    }

    private fun addRightImage2() {
        if (rightImage2Visibility == View.GONE) {
            return
        }

        if (rightImageView2 == null) {
            rightImage2Drawable = rightImage2Drawable?.tint(tintColor)

            val imageView = PromptImageView(context)
            imageView.id = R.id.rightImage2
            imageView.visibility = rightImage2Visibility
            imageView.setImageDrawable(rightImage2Drawable)
            imageView.background = rightImage2BackgroundDrawable
            imageView.scaleType = ImageView.ScaleType.CENTER
            setDefaultPromptState(imageView)
            rightImageView2 = imageView
            addView(rightImageView2)
        }
    }

    /**
     * 设置自定义View的位置
     */
    private fun layoutCustomize(l: Int, index: Int): Int {
        var left = l
        if (index != customizeViewIndex || customizeView == null || customizeView!!.visibility == View.GONE) {
            return left
        }
        left += customizeMarginStart
        val width = customizeView!!.measuredWidth
        val height = customizeView!!.measuredHeight
        val top = (measuredHeight - height) / 2
        customizeView!!.layout(left, top, left + width, top + height)
        left += width + customizeMarginEnd
        return left
    }

    private fun setInputEllipsize() {
        when (textEllipsize) {
            1 -> mainView.ellipsize = TextUtils.TruncateAt.START
            2 -> mainView.ellipsize = TextUtils.TruncateAt.MIDDLE
            3 -> mainView.ellipsize = TextUtils.TruncateAt.END
        }
    }

    private fun setDefaultPromptState(prompt: IPrompt?) {
        prompt?.let {
            it.setPromptMode(IPrompt.PromptMode.NONE)
            it.commit()
        }
    }

    /**
     * 获取精确的MeasureSpec
     */
    private fun getExactlyMeasureSpec(size: Int): Int {
        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
    }
}
