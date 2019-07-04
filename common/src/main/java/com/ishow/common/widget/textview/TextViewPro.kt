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
import com.ishow.common.R
import com.ishow.common.extensions.loadUrl
import com.ishow.common.extensions.setPadding
import com.ishow.common.extensions.tint
import com.ishow.common.utils.UnitUtils
import com.ishow.common.utils.image.loader.ImageLoader
import com.ishow.common.widget.imageview.PromptImageView
import com.ishow.common.widget.prompt.IPrompt
import kotlin.math.max

/**
 * Created by Bright.Yu on 2017/2/9.
 * 加强版本的TextView
 */
class TextViewPro @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ViewGroup(context, attrs, defStyle) {
    /**
     * 左侧图片信息
     */
    private var mLeftImageView: PromptImageView? = null
    private var mLeftImageDrawable: Drawable? = null
    private val mLeftImageBackgroundDrawable: Drawable?
    private val mLeftImageWidth: Int
    private val mLeftImageHeight: Int
    private var mLeftImageVisibility: Int = 0
    private val mLeftImageMarginStart: Int
    private val mLeftImageMarginEnd: Int
    /**
     * 左侧文本信息
     */
    private var mLeftTextView: PromptTextView? = null
    private var mLeftTextString: String? = null
    private val mLeftTextSize: Int
    private val mLeftTextColor: Int
    private val mLeftTextVisibility: Int
    private val mLeftTextMinWidth: Int
    private val mLeftTextMaxWidth: Int
    private val mLeftTextMarginStart: Int
    private val mLeftTextMarginEnd: Int
    private val mLeftTextGravity: Int
    private val mLeftTextStyle: Int
    private val mLeftTextBackgroundDrawable: Drawable?
    /**
     * 中间的View
     */
    private lateinit var mTextView: TextView
    private val mTextBackgroundDrawable: Drawable?
    private val mTextGravity: Int
    private val mTextEllipsize: Int
    private val mTextSize: Int
    private val mTextColor: Int
    private val mTextLines: Int
    private val mTextMarginStart: Int
    private val mTextMarginEnd: Int
    private val mTextString: String?

    /**
     * 右侧文本信息
     */
    private var mRightTextView: PromptTextView? = null
    private var mRightTextString: String? = null
    private val mRightTextSize: Int
    private var mRightTextVisibility: Int = 0
    private val mRightTextMarginStart: Int
    private val mRightTextMarginEnd: Int
    private val mRightTextMinWidth: Int
    private val mRightTextMaxWidth: Int
    private val mRightTextGravity: Int
    private val mRightTextPadding: Int
    private val mRightTextDrawablePadding: Int
    private val mRightTextPaddingHorizontal: Int
    private val mRightTextPaddingVertical: Int
    private var mRightTextColor: ColorStateList? = null
    private val mRightTextBackgroundDrawable: Drawable?
    private var mRightTextRightDrawable: Drawable? = null
    private var mRightTextLeftDrawable: Drawable? = null

    /**
     * 右侧图片信息
     */
    private var mRightImageView: PromptImageView? = null
    private var mRightImageDrawable: Drawable? = null
    private val mRightImageBackgroundDrawable: Drawable?
    private val mRightImageWidth: Int
    private val mRightImageHeight: Int
    private val mRightImageMarginStart: Int
    private val mRightImageMarginEnd: Int
    private var mRightImageVisibility: Int = 0

    private var mRightImageView2: PromptImageView? = null
    private var mRightImage2Drawable: Drawable? = null
    private val mRightImage2BackgroundDrawable: Drawable?
    private val mRightImage2Width: Int
    private val mRightImage2Height: Int
    private val mRightImage2MarginStart: Int
    private val mRightImage2MarginEnd: Int
    private var mRightImage2Visibility: Int = 0

    private val mTopLinePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mTopLineHeight: Int
    private val mTopLineColor: Int
    private val mTopLineVisibility: Int
    private val mTopLinePaddingStart: Int
    private val mTopLinePaddingEnd: Int

    private val mBottomLinePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mBottomLineHeight: Int
    private val mBottomLineColor: Int
    private val mBottomLineVisibility: Int
    private val mBottomLinePaddingStart: Int
    private val mBottomLinePaddingEnd: Int

    /**
     * 自定义插入View
     * index = -1 为最后
     */
    private var mCustomizeView: View? = null
    private val mCustomizeViewId: Int
    private val mCustomizeViewIndex: Int
    private val mCustomizeMarginStart: Int
    private val mCustomizeMarginEnd: Int

    /**
     * 图片的建议宽度
     */
    private var mSuggestIconWidth: Int = 0

    private val mMinHeight: Int
    private val mTintColor: ColorStateList?

    val leftImageView: PromptImageView?
        get() = mLeftImageView

    val leftTextView: PromptTextView?
        get() = mLeftTextView

    val text: String
        get() = mTextView.text.toString().trim { it <= ' ' }

    val textView: TextView?
        get() = mTextView

    val rightTextView: PromptTextView?
        get() = mRightTextView

    val rightImageView: PromptImageView?
        get() = mRightImageView

    val rightImageView2: PromptImageView?
        get() = mRightImageView2

    private val defaultTipTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.H_title)

    private val defaultTipMinWidth: Int
        get() = UnitUtils.dip2px(50)

    private val defaultTipMaxWidth: Int
        get() = UnitUtils.dip2px(120)

    private val defaultTipTextColor: Int
        get() = ContextCompat.getColor(context, R.color.text_grey_light_normal)

    private val defaultTipTextColorStateList: ColorStateList?
        get() = ContextCompat.getColorStateList(context, R.color.text_grey_light_normal)

    private val defaultInputTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.H_title)

    private val defaultInputTextColor: Int
        get() = ContextCompat.getColor(context, R.color.text_grey_normal)

    private val defaultLineColor: Int
        get() = ContextCompat.getColor(context, R.color.line)

    private val defaultLineHeight: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.default_line_height)

    private val defaultMinHeight: Int
        get() = resources.getDimensionPixelSize(R.dimen.default_pro_height)


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPro)

        mTintColor = a.getColorStateList(R.styleable.TextViewPro_tintColor)

        mLeftImageDrawable = a.getDrawable(R.styleable.TextViewPro_leftImage)
        mLeftImageWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageWidth, -1)
        mLeftImageHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageHeight, -1)
        mLeftImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftImageBackground)
        mLeftImageVisibility = a.getInt(R.styleable.TextViewPro_leftImageVisibility, View.VISIBLE)
        mLeftImageMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageMarginStart, 0)
        mLeftImageMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageMarginEnd, 0)

        mLeftTextString = a.getString(R.styleable.TextViewPro_leftText)
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextSize, defaultTipTextSize)
        mLeftTextColor = a.getColor(R.styleable.TextViewPro_leftTextColor, defaultTipTextColor)
        mLeftTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMarginStart, 0)
        mLeftTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMarginEnd, 0)
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMinWidth, defaultTipMinWidth)
        mLeftTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMaxWidth, defaultTipMaxWidth)
        mLeftTextVisibility = a.getInt(R.styleable.TextViewPro_leftTextVisibility, View.VISIBLE)
        mLeftTextGravity = a.getInt(R.styleable.TextViewPro_leftTextGravity, Gravity.CENTER)
        mLeftTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftTextBackground)
        mLeftTextStyle = a.getInt(R.styleable.TextViewPro_leftTextStyle, 0)

        mTextString = a.getString(R.styleable.TextViewPro_text)
        mTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_textSize, defaultInputTextSize)
        mTextColor = a.getColor(R.styleable.TextViewPro_textColor, defaultInputTextColor)
        mTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_textMarginStart, 0)
        mTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_textMarginEnd, 0)
        mTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_textBackground)
        mTextGravity = a.getInt(R.styleable.TextViewPro_textGravity, Gravity.CENTER_VERTICAL)
        mTextEllipsize = a.getInt(R.styleable.TextViewPro_textEllipsize, -1)
        mTextLines = a.getInt(R.styleable.TextViewPro_textLines, 0)

        mRightTextString = a.getString(R.styleable.TextViewPro_rightText)
        mRightTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextSize, defaultTipTextSize)
        mRightTextColor = a.getColorStateList(R.styleable.TextViewPro_rightTextColor)
        mRightTextVisibility = a.getInt(R.styleable.TextViewPro_rightTextVisibility, View.GONE)
        mRightTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMarginStart, 0)
        mRightTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMarginEnd, 0)
        mRightTextPadding = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPadding, 0)
        mRightTextPaddingHorizontal = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPaddingHorizontal, 0)
        mRightTextPaddingVertical = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPaddingVertical, 0)
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMinWidth, defaultTipMinWidth)
        mRightTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMaxWidth, defaultTipMaxWidth)
        mRightTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextDrawablePadding, 5)
        mRightTextLeftDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextLeftDrawable)
        mRightTextRightDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextRightDrawable)
        mRightTextGravity = a.getInt(R.styleable.TextViewPro_rightTextGravity, Gravity.CENTER)
        mRightTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextBackground)

        mRightImageDrawable = a.getDrawable(R.styleable.TextViewPro_rightImage)
        mRightImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightImageBackground)
        mRightImageVisibility = a.getInt(R.styleable.TextViewPro_rightImageVisibility, View.VISIBLE)
        mRightImageWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageWidth, 0)
        mRightImageHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageHeight, 0)
        mRightImageMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageMarginStart, 0)
        mRightImageMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageMarginEnd, 0)

        mRightImage2Drawable = a.getDrawable(R.styleable.TextViewPro_rightImage2)
        mRightImage2BackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightImage2Background)
        mRightImage2Visibility = a.getInt(R.styleable.TextViewPro_rightImage2Visibility, View.GONE)
        mRightImage2Width = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2Width, 0)
        mRightImage2Height = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2Height, 0)
        mRightImage2MarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2MarginStart, 0)
        mRightImage2MarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2MarginEnd, 0)

        mTopLineHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_topLineHeight, defaultLineHeight)
        mTopLineColor = a.getColor(R.styleable.TextViewPro_topLineColor, defaultLineColor)
        mTopLineVisibility = a.getInt(R.styleable.TextViewPro_topLineVisibility, View.GONE)
        mTopLinePaddingStart = a.getDimensionPixelSize(R.styleable.TextViewPro_topLinePaddingStart, 0)
        mTopLinePaddingEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_topLinePaddingEnd, 0)

        mBottomLineHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLineHeight, defaultLineHeight)
        mBottomLineColor = a.getColor(R.styleable.TextViewPro_bottomLineColor, defaultLineColor)
        mBottomLineVisibility = a.getInt(R.styleable.TextViewPro_bottomLineVisibility, View.VISIBLE)
        mBottomLinePaddingStart = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLinePaddingStart, 0)
        mBottomLinePaddingEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLinePaddingEnd, 0)

        mCustomizeViewId = a.getResourceId(R.styleable.TextViewPro_customizeViewId, 0)
        mCustomizeViewIndex = a.getInt(R.styleable.TextViewPro_customizeViewIndex, -1)
        mCustomizeMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_customizeMarginStart, 0)
        mCustomizeMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_customizeMarginEnd, 0)

        mMinHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_android_minHeight, defaultMinHeight)
        a.recycle()

        if (mRightTextColor == null) {
            mRightTextColor = defaultTipTextColorStateList
        }

        initNecessaryData()
        initView()
    }

    private fun initNecessaryData() {
        mSuggestIconWidth = UnitUtils.dip2px(40)

        mTopLinePaint.color = mTopLineColor
        mTopLinePaint.strokeWidth = mTopLineHeight.toFloat()

        mBottomLinePaint.color = mBottomLineColor
        mBottomLinePaint.strokeWidth = mBottomLineHeight.toFloat()
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
        if (mCustomizeViewId > 0) {
            mCustomizeView = findViewById(mCustomizeViewId)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        var height = measureHeight(width, heightMeasureSpec)

        val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        val imageWidthSpec = MeasureSpec.makeMeasureSpec(mSuggestIconWidth, MeasureSpec.EXACTLY)
        val unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)

        var textWidth = width - paddingLeft - paddingRight

        if (mLeftImageView != null && mLeftImageView!!.visibility != View.GONE) {
            if (mLeftImageWidth > 0 && mLeftImageHeight > 0) {
                mLeftImageView!!.measure(
                    getExactlyMeasureSpec(mLeftImageWidth),
                    getExactlyMeasureSpec(mLeftImageHeight)
                )
            } else if (mLeftImageWidth > 0) {
                mLeftImageView!!.measure(getExactlyMeasureSpec(mLeftImageWidth), heightSpec)
            } else if (mLeftImageHeight > 0) {
                mLeftImageView!!.measure(imageWidthSpec, getExactlyMeasureSpec(mLeftImageHeight))
            } else {
                mLeftImageView!!.measure(imageWidthSpec, heightSpec)
            }
            textWidth = textWidth - mLeftImageView!!.measuredWidth - mLeftImageMarginStart - mLeftImageMarginEnd
        }

        if (mLeftTextView != null && mLeftTextView!!.visibility != View.GONE) {
            mLeftTextView!!.measure(unspecified, heightSpec)
            textWidth = textWidth - mLeftTextView!!.measuredWidth - mLeftTextMarginStart - mLeftTextMarginEnd
        }

        if (mRightImageView != null && mRightImageView!!.visibility != View.GONE) {
            if (mRightImageWidth > 0 && mRightImageHeight > 0) {
                mRightImageView!!.measure(
                    getExactlyMeasureSpec(mRightImageWidth),
                    getExactlyMeasureSpec(mRightImageHeight)
                )
            } else if (mRightImageWidth > 0) {
                mRightImageView!!.measure(getExactlyMeasureSpec(mRightImageWidth), heightSpec)
            } else if (mRightImageHeight > 0) {
                mRightImageView!!.measure(imageWidthSpec, getExactlyMeasureSpec(mRightImageHeight))
            } else {
                mRightImageView!!.measure(imageWidthSpec, heightSpec)
            }

            textWidth = textWidth - mRightImageView!!.measuredWidth - mRightImageMarginStart - mRightImageMarginEnd
        }

        if (mRightImageView2 != null && mRightImageView2!!.visibility != View.GONE) {
            if (mRightImage2Width > 0 && mRightImage2Height > 0) {
                mRightImageView2!!.measure(
                    getExactlyMeasureSpec(mRightImage2Width),
                    getExactlyMeasureSpec(mRightImage2Height)
                )
            } else if (mRightImage2Width > 0) {
                mRightImageView2!!.measure(getExactlyMeasureSpec(mRightImage2Width), heightSpec)
            } else if (mRightImage2Height > 0) {
                mRightImageView2!!.measure(imageWidthSpec, getExactlyMeasureSpec(mRightImage2Height))
            } else {
                mRightImageView2!!.measure(imageWidthSpec, heightSpec)
            }

            textWidth = textWidth - mRightImageView2!!.measuredWidth - mRightImage2MarginStart - mRightImage2MarginEnd
        }


        if (mRightTextView != null && mRightTextView!!.visibility != View.GONE) {
            mRightTextView!!.measure(unspecified, unspecified)
            textWidth = textWidth - mRightTextView!!.measuredWidth - mRightTextMarginStart - mRightTextMarginEnd
        }

        if (mCustomizeView != null && mCustomizeView!!.visibility != View.GONE) {
            measureChild(mCustomizeView, widthSpec, heightSpec)
            textWidth = textWidth - mCustomizeView!!.measuredWidth - mCustomizeMarginStart - mCustomizeMarginEnd
        }

        textWidth = textWidth - mTextMarginStart - mTextMarginEnd
        mTextView.measure(MeasureSpec.makeMeasureSpec(textWidth, MeasureSpec.EXACTLY), heightSpec)

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
                var height = mMinHeight

                var inputWidth = width - paddingStart - paddingEnd

                if (mLeftImageView != null && mLeftImageView!!.visibility != View.GONE) {
                    inputWidth = if (mLeftImageWidth > 0) {
                        inputWidth - mLeftImageWidth - mLeftImageMarginStart - mLeftImageMarginEnd
                    } else {
                        inputWidth - mSuggestIconWidth - mLeftImageMarginStart - mLeftImageMarginEnd
                    }
                }

                if (mLeftTextView != null && mLeftTextView!!.visibility != View.GONE) {
                    val unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    mLeftTextView!!.measure(unspecified, unspecified)
                    val leftTextHeight = mLeftTextView!!.measuredHeight
                    val leftTextWidth = mLeftTextView!!.measuredWidth
                    inputWidth = inputWidth - leftTextWidth - mLeftTextMarginStart - mLeftTextMarginEnd
                    height = max(height, leftTextHeight)
                }

                if (mRightTextView != null && mRightTextView!!.visibility != View.GONE) {
                    val unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    mRightTextView!!.measure(unspecified, unspecified)
                    val rightTextHeight = mRightTextView!!.measuredHeight
                    val rightTextWidth = mRightTextView!!.measuredWidth
                    inputWidth = inputWidth - rightTextWidth - mRightTextMarginStart - mRightTextMarginEnd
                    height = max(height, rightTextHeight)
                }

                if (mRightImageView != null && mRightImageView!!.visibility != View.GONE) {
                    inputWidth = if (mRightImageWidth > 0) {
                        inputWidth - mRightImageWidth - mRightImageMarginStart - mRightImageMarginEnd
                    } else {
                        inputWidth - mSuggestIconWidth - mRightImageMarginStart - mRightImageMarginEnd
                    }
                }

                if (mRightImageView2 != null && mRightImageView2!!.visibility != View.GONE) {
                    inputWidth = if (mRightImage2Width > 0) {
                        inputWidth - mRightImage2Width - mRightImage2MarginStart - mRightImage2MarginEnd
                    } else {
                        inputWidth - mSuggestIconWidth - mRightImage2MarginStart - mRightImage2MarginEnd
                    }
                }

                inputWidth = inputWidth - mTextMarginStart - mTextMarginEnd

                val widthSpec = MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY)
                val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                mTextView.measure(widthSpec, heightSpec)
                val inputHeight = mTextView.measuredHeight
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

        if (mLeftImageView != null && mLeftImageView!!.visibility != View.GONE) {
            val width = mLeftImageView!!.measuredWidth
            val height = mLeftImageView!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += mLeftImageMarginStart
            mLeftImageView!!.layout(left, temTop, left + width, temTop + height)
            left += width + mLeftImageMarginEnd
            index++
        }
        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (mLeftTextView != null && mLeftTextView!!.visibility != View.GONE) {
            val width = mLeftTextView!!.measuredWidth
            left += mLeftTextMarginStart
            mLeftTextView!!.layout(left, top, left + width, bottom)
            left += width + mLeftTextMarginEnd
            index++
        }

        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        left += mTextMarginStart
        val inputWidth = mTextView.measuredWidth
        mTextView.layout(left, top, left + inputWidth, bottom)
        left += inputWidth + mTextMarginEnd
        index++

        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (mRightTextView != null && mRightTextView!!.visibility != View.GONE) {
            val width = mRightTextView!!.measuredWidth
            val height = mRightTextView!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += mRightTextMarginStart
            mRightTextView!!.layout(left, temTop, left + width, temTop + height)
            left += width + mRightTextMarginEnd
            index++
        }

        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (mRightImageView2 != null && mRightImageView2!!.visibility != View.GONE) {
            val width = mRightImageView2!!.measuredWidth
            val height = mRightImageView2!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += mRightImage2MarginStart
            mRightImageView2!!.layout(left, temTop, left + width, temTop + height)
            left += width + mRightImage2MarginEnd
            index++
        }

        // 倒数第二个不需要进行处理index了， 因为最后一个的index 肯定为-1
        left = layoutCustomize(left, index)

        if (mRightImageView != null && mRightImageView!!.visibility != View.GONE) {
            val width = mRightImageView!!.measuredWidth
            val height = mRightImageView!!.measuredHeight
            val temTop = (measuredHeight - height) / 2
            left += mRightImageMarginStart
            mRightImageView!!.layout(left, temTop, left + width, temTop + height)
        }

        index = -1
        layoutCustomize(left, index)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight

        if (mTopLineVisibility == View.VISIBLE) {
            canvas.drawLine(
                mTopLinePaddingStart.toFloat(),
                mTopLineHeight.toFloat(),
                (width - mTopLinePaddingEnd).toFloat(),
                mTopLineHeight.toFloat(),
                mTopLinePaint
            )
        }

        if (mBottomLineVisibility == View.VISIBLE) {
            canvas.drawLine(
                mBottomLinePaddingStart.toFloat(),
                (height - mBottomLineHeight).toFloat(),
                (width - mBottomLinePaddingEnd).toFloat(),
                (height - mBottomLineHeight).toFloat(),
                mBottomLinePaint
            )
        }
    }


    fun setLeftImageVisibility(visibility: Int) {
        mLeftImageView?.visibility = visibility
        mLeftImageVisibility = visibility
    }

    fun setLeftImageSelected(selected: Boolean) {
        mLeftImageView?.isSelected = selected
    }

    fun setLeftText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setLeftText(text)
    }

    fun setLeftText(text: String) {
        mLeftTextString = text
        mLeftTextView?.text = text
    }

    fun setLeftTextGravity(gravity: Int) {
        mLeftTextView?.gravity = gravity
    }

    fun setLeftTextMinWidth(width: Int) {
        mLeftTextView?.minWidth = width
        requestLayout()
    }

    fun setText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setText(text)
    }

    fun setText(text: CharSequence) {
        mTextView.text = text
        requestLayout()
    }

    fun setTextSize(@DimenRes resId: Int) {
        val size = resources.getDimensionPixelSize(resId).toFloat()
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        requestLayout()
    }

    fun setTextColor(@ColorInt colorInt: Int) {
        mTextView.setTextColor(colorInt)
    }

    fun setTextMovementMethod(movement: MovementMethod) {
        mTextView.movementMethod = movement
    }

    fun setTextHighlightColor(color: Int) {
        mTextView.highlightColor = color
    }

    fun setRightText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setRightText(text)
    }

    fun setRightText(text: String) {
        mRightTextString = text
        mRightTextView?.text = text
        mRightTextVisibility = View.VISIBLE
        requestLayout()
    }

    fun setRightTextSelected(selected: Boolean) {
        mRightTextView?.isSelected = selected
    }

    fun setRightTextBackgroundResources(@DrawableRes backgroundResources: Int) {
        mRightTextView?.setBackgroundResource(backgroundResources)
    }

    fun setRightImageResource2(@DrawableRes resId: Int) {
        mRightImageView2?.setImageResource(resId)
        mRightImage2Visibility = View.VISIBLE
        requestLayout()
    }

    @JvmOverloads
    fun setRightImageUrl2(url: String?, @ImageLoader.LoaderMode loaderMode: Int = ImageLoader.LoaderMode.CENTER_CROP) {
        if (mRightImageView2 == null) {
            addRightImage2()
        }

        mRightImageView2?.loadUrl(url, loaderMode)
        mRightImage2Visibility = View.VISIBLE
        requestLayout()
    }

    fun setRightImageResource(@DrawableRes resId: Int) {
        mRightImageView?.setImageResource(resId)
        mRightImageVisibility = View.VISIBLE
        requestLayout()
    }

    fun setRightImageVisibility(visibility: Int) {
        mRightImageView?.visibility = visibility
        mRightImageVisibility = visibility
        requestLayout()
    }

    fun setRightImageClickListener(listener: OnClickListener) {
        mRightImageView?.setOnClickListener(listener)
    }

    fun setRightTextClickListener(listener: OnClickListener) {
        mRightTextView?.setOnClickListener(listener)
    }

    /**
     * 设置右侧按钮是否可见
     */
    fun setRightTextVisibility(visibility: Int) {
        mRightTextView?.visibility = visibility
        mRightTextVisibility = visibility
        requestLayout()
    }

    private fun addLeftImage() {
        if (mLeftImageVisibility == View.GONE) {
            return
        }

        if (mLeftImageView == null) {
            mLeftImageDrawable = mLeftImageDrawable?.tint(mTintColor)

            val imageView = PromptImageView(context)
            imageView.id = R.id.leftImage
            imageView.visibility = View.VISIBLE
            imageView.setImageDrawable(mLeftImageDrawable)
            imageView.background = mLeftImageBackgroundDrawable
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            setDefaultPromptState(imageView)
            mLeftImageView = imageView
            addView(mLeftImageView)
        }
    }

    private fun addLeftText() {
        if (mLeftTextVisibility == View.GONE) {
            return
        }

        if (mLeftTextView == null) {
            val textView = PromptTextView(context)
            textView.id = R.id.leftText
            textView.text = mLeftTextString
            textView.setTextColor(mLeftTextColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize.toFloat())
            textView.minWidth = mLeftTextMinWidth
            textView.maxWidth = mLeftTextMaxWidth
            textView.background = mLeftTextBackgroundDrawable
            textView.gravity = mLeftTextGravity
            textView.typeface = Typeface.defaultFromStyle(mLeftTextStyle)
            textView.includeFontPadding = false
            setDefaultPromptState(textView)

            mLeftTextView = textView
            addView(mLeftTextView)
        }
    }

    private fun addTextView() {
        mTextView = AppCompatTextView(context)
        setInputEllipsize()
        mTextView.text = mTextString
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
        mTextView.setTextColor(mTextColor)
        mTextView.background = mTextBackgroundDrawable
        mTextView.gravity = mTextGravity
        if (mTextLines > 0) {
            mTextView.setLines(mTextLines)
        }
        addView(mTextView)
    }

    private fun addRightText() {
        if (mRightTextVisibility == View.GONE) {
            return
        }

        if (mRightTextView == null) {
            val textView = PromptTextView(context)
            textView.id = R.id.rightText
            textView.text = mRightTextString
            textView.gravity = Gravity.CENTER
            textView.setTextColor(mRightTextColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize.toFloat())
            textView.minWidth = mRightTextMinWidth
            textView.maxWidth = mRightTextMaxWidth
            textView.background = mRightTextBackgroundDrawable
            textView.gravity = mRightTextGravity
            textView.includeFontPadding = false
            if (mRightTextPadding > 0) {
                textView.setPadding(mRightTextPadding)
            } else if (mRightTextPaddingVertical > 0 || mRightTextPaddingHorizontal > 0) {
                textView.setPadding(mRightTextPaddingHorizontal, mRightTextPaddingVertical)
            }

            if (mRightTextRightDrawable != null || mRightTextLeftDrawable != null) {
                mRightTextLeftDrawable = mRightTextLeftDrawable?.tint(mTintColor)
                mRightTextRightDrawable = mRightTextRightDrawable?.tint(mTintColor)

                textView.setCompoundDrawablesWithIntrinsicBounds(
                    mRightTextLeftDrawable,
                    null,
                    mRightTextRightDrawable,
                    null
                )
                textView.compoundDrawablePadding = mRightTextDrawablePadding
            }
            setDefaultPromptState(textView)
            mRightTextView = textView
            addView(mRightTextView)
        }
    }

    private fun addRightImage() {
        if (mRightImageVisibility == View.GONE) {
            return
        }

        if (mRightImageView == null) {
            mRightImageDrawable = mRightImageDrawable?.tint(mTintColor)

            val imageView = PromptImageView(context)
            imageView.id = R.id.rightImage
            imageView.visibility = mRightImageVisibility
            imageView.setImageDrawable(mRightImageDrawable)
            imageView.background = mRightImageBackgroundDrawable
            imageView.scaleType = ImageView.ScaleType.CENTER
            setDefaultPromptState(imageView)

            mRightImageView = imageView
            addView(mRightImageView)
        }
    }

    private fun addRightImage2() {
        if (mRightImage2Visibility == View.GONE) {
            return
        }

        if (mRightImageView2 == null) {
            mRightImage2Drawable = mRightImage2Drawable?.tint(mTintColor)

            val imageView = PromptImageView(context)
            imageView.id = R.id.rightImage2
            imageView.visibility = mRightImage2Visibility
            imageView.setImageDrawable(mRightImageDrawable)
            imageView.background = mRightImage2BackgroundDrawable
            imageView.scaleType = ImageView.ScaleType.CENTER
            setDefaultPromptState(imageView)
            mRightImageView2 = imageView
            addView(mRightImageView2)
        }
    }

    /**
     * 设置自定义View的位置
     */
    private fun layoutCustomize(l: Int, index: Int): Int {
        var left = l
        if (index != mCustomizeViewIndex || mCustomizeView == null || mCustomizeView!!.visibility == View.GONE) {
            return left
        }
        left += mCustomizeMarginStart
        val width = mCustomizeView!!.measuredWidth
        val height = mCustomizeView!!.measuredHeight
        val top = (measuredHeight - height) / 2
        mCustomizeView!!.layout(left, top, left + width, top + height)
        left += width + mCustomizeMarginEnd
        return left
    }

    private fun setInputEllipsize() {
        when (mTextEllipsize) {
            1 -> mTextView.ellipsize = TextUtils.TruncateAt.START
            2 -> mTextView.ellipsize = TextUtils.TruncateAt.MIDDLE
            3 -> mTextView.ellipsize = TextUtils.TruncateAt.END
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
