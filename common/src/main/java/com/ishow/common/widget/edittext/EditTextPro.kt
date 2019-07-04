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

package com.ishow.common.widget.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.*
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.ishow.common.R
import com.ishow.common.extensions.setDrawableRight
import com.ishow.common.extensions.tint
import com.ishow.common.extensions.trimText
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.UnitUtils
import com.ishow.common.widget.imageview.PromptImageView
import com.ishow.common.widget.prompt.IPrompt
import com.ishow.common.widget.textview.PromptTextView
import kotlin.math.max

/**
 * Created by Bright.Yu on 2017/2/9.
 * 加强版本的EditText
 */
class EditTextPro @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ViewGroup(context, attrs, defStyle), View.OnFocusChangeListener, View.OnClickListener {
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
     * 输入EditText的信息
     */
    private lateinit var mInputView: AppCompatEditText
    private val mInputBackgroundDrawable: Drawable?
    private val mInputGravity: Int
    private val mInputTextSize: Int
    private val mInputTextColor: Int
    private val mInputHintTextColor: Int
    private val mInputLines: Int
    private val mInputMaxLength: Int
    private val mInputMarginStart: Int
    private val mInputMarginEnd: Int
    private var mInputType: Int = 0
    private val mInputTextString: String?
    private val mInputHintString: String?
    private val mInputDigitsString: String?

    private var mCancelView: ImageView? = null
    private var mCancelVisibility: Int = 0
    private val isCancelEnable: Boolean

    /**
     * 右侧文本信息
     */
    private var mRightTextView: PromptTextView? = null
    private var mRightTextString: String? = null
    private val mRightTextSize: Int
    private val mRightTextColor: Int
    private val mRightTextVisibility: Int
    private val mRightTextMarginStart: Int
    private val mRightTextMarginEnd: Int
    private val mRightTextMinWidth: Int
    private val mRightTextMaxWidth: Int
    private val mRightTextGravity: Int
    private val mRightTextPadding: Int
    private val mRightTextBackgroundDrawable: Drawable?
    private var mRightTextRightDrawable: Drawable? = null

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
    private val mRightImageAction: Int

    /**
     * 顶部的线
     */
    private val mTopLinePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mTopLineHeight: Int
    private val mTopLinePaddingStart: Int
    private val mTopLinePaddingEnd: Int
    private val mTopLineNormalColor: Int
    private val mTopLineFocusColor: Int
    private val mTopLineVisibility: Int

    /**
     * 底部的线
     */
    private val mBottomLinePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mBottomLineHeight: Int
    private val mBottomLineNormalColor: Int
    private val mBottomLinePaddingStart: Int
    private val mBottomLinePaddingEnd: Int
    private val mBottomLineFocusColor: Int
    private var mBottomLineVisibility: Int = 0

    /**
     * 自定义插入View
     * Index = -1 表示在最后
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
    private var mSuggestCancelWidth: Int = 0
    private val mMinHeight: Int
    private val mTintColor: ColorStateList?
    private var mEditTextListener: OnEditTextListener? = null

    // 是否可以输入
    private var isInputEnable = true

    val leftImageView: PromptImageView?
        get() = mLeftImageView

    val leftTextView: PromptTextView?
        get() = mLeftTextView

    val inputView: EditText
        get() = mInputView

    val inputText: String
        get() = mInputView.trimText()

    val rightTextView: PromptTextView?
        get() = mRightTextView

    val rightImageView: PromptImageView?
        get() = mRightImageView

    private val defaultTipTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.H_title)

    private val defaultTipMinWidth: Int
        get() = UnitUtils.dip2px(50)

    private val defaultTipMaxWidth: Int
        get() = UnitUtils.dip2px(120)

    private val defaultTipTextColor: Int
        get() = ContextCompat.getColor(context, R.color.text_grey_light_normal)

    private val defaultInputTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.I_title)

    private val defaultInputTextColor: Int
        get() = ContextCompat.getColor(context, R.color.text_grey_normal)

    private val defaultInputHintTextColor: Int
        get() = ContextCompat.getColor(context, R.color.text_grey_hint)

    private val defaultNormalLineColor: Int
        get() = ContextCompat.getColor(context, R.color.line)

    private val defaultFocusLineColor: Int
        get() = ContextCompat.getColor(context, R.color.color_accent)

    private val defaultLineHeight: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.default_line_height)

    private val defaultMinHeight: Int
        get() = resources.getDimensionPixelSize(R.dimen.default_pro_height)


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.EditTextPro)

        mTintColor = a.getColorStateList(R.styleable.EditTextPro_tintColor)

        mLeftImageDrawable = a.getDrawable(R.styleable.EditTextPro_leftImage)
        mLeftImageWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_leftImageWidth, -1)
        mLeftImageHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_leftImageHeight, -1)
        mLeftImageBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_leftImageBackground)
        mLeftImageMarginStart = a.getDimensionPixelSize(R.styleable.EditTextPro_leftImageMarginStart, 0)
        mLeftImageMarginEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_leftImageMarginEnd, 0)
        mLeftImageVisibility = a.getInt(R.styleable.EditTextPro_leftImageVisibility, View.VISIBLE)

        mLeftTextString = a.getString(R.styleable.EditTextPro_leftText)
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextSize, defaultTipTextSize)
        mLeftTextColor = a.getColor(R.styleable.EditTextPro_leftTextColor, defaultTipTextColor)
        mLeftTextMarginStart = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMarginStart, 0)
        mLeftTextMarginEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMarginEnd, 0)
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMinWidth, defaultTipMinWidth)
        mLeftTextMaxWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMaxWidth, defaultTipMaxWidth)
        mLeftTextVisibility = a.getInt(R.styleable.EditTextPro_leftTextVisibility, View.VISIBLE)
        mLeftTextGravity = a.getInt(R.styleable.EditTextPro_leftTextGravity, Gravity.CENTER)
        mLeftTextBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_leftTextBackground)
        mLeftTextStyle = a.getInt(R.styleable.EditTextPro_leftTextStyle, 0)

        mInputTextString = a.getString(R.styleable.EditTextPro_inputText)
        mInputTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_inputTextSize, defaultInputTextSize)
        mInputTextColor = a.getColor(R.styleable.EditTextPro_inputTextColor, defaultInputTextColor)
        mInputBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_inputBackground)
        mInputGravity = a.getInt(R.styleable.EditTextPro_inputGravity, Gravity.CENTER_VERTICAL)
        mInputDigitsString = a.getString(R.styleable.EditTextPro_inputDigits)
        mInputMarginStart = a.getDimensionPixelSize(R.styleable.EditTextPro_inputMarginStart, 0)
        mInputMarginEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_inputMarginEnd, 0)
        mInputHintString = a.getString(R.styleable.EditTextPro_inputHint)
        mInputHintTextColor = a.getColor(R.styleable.EditTextPro_inputHintTextColor, defaultInputHintTextColor)
        mInputLines = a.getInt(R.styleable.EditTextPro_inputLines, 0)
        mInputMaxLength = a.getInt(R.styleable.EditTextPro_inputTextMaxLength, 0)
        mInputType = a.getInt(R.styleable.EditTextPro_inputType, InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        isCancelEnable = a.getBoolean(R.styleable.EditTextPro_cancelEnable, true)

        mRightTextString = a.getString(R.styleable.EditTextPro_rightText)
        mRightTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextSize, defaultTipTextSize)
        mRightTextColor = a.getColor(R.styleable.EditTextPro_rightTextColor, defaultTipTextColor)
        mRightTextVisibility = a.getInt(R.styleable.EditTextPro_rightTextVisibility, View.GONE)
        mRightTextMarginStart = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMarginStart, 0)
        mRightTextMarginEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMarginEnd, 0)
        mRightTextPadding = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextPadding, 0)
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMinWidth, defaultTipMinWidth)
        mRightTextMaxWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMaxWidth, defaultTipMaxWidth)
        mRightTextRightDrawable = a.getDrawable(R.styleable.EditTextPro_rightTextRightDrawable)
        mRightTextGravity = a.getInt(R.styleable.EditTextPro_rightTextGravity, Gravity.CENTER)
        mRightTextBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_rightTextBackground)

        mRightImageDrawable = a.getDrawable(R.styleable.EditTextPro_rightImage)
        mRightImageWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_rightImageWidth, 0)
        mRightImageHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_rightImageHeight, 0)
        mRightImageBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_rightImageBackground)
        mRightImageVisibility = a.getInt(R.styleable.EditTextPro_rightImageVisibility, View.VISIBLE)
        mRightImageMarginStart = a.getDimensionPixelSize(R.styleable.EditTextPro_rightImageMarginStart, 0)
        mRightImageMarginEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_rightImageMarginEnd, 0)
        mRightImageAction = a.getInt(R.styleable.EditTextPro_rightImageAction, RightImageAction.NONE)

        mTopLineHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_topLineHeight, defaultLineHeight)
        mTopLineNormalColor = a.getColor(R.styleable.EditTextPro_topLineNormalColor, defaultNormalLineColor)
        mTopLineFocusColor = a.getColor(R.styleable.EditTextPro_topLineFocusColor, defaultFocusLineColor)
        mTopLineVisibility = a.getInt(R.styleable.EditTextPro_topLineVisibility, View.GONE)
        mTopLinePaddingStart = a.getDimensionPixelSize(R.styleable.EditTextPro_topLinePaddingStart, 0)
        mTopLinePaddingEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_topLinePaddingEnd, 0)

        mBottomLineHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_bottomLineHeight, defaultLineHeight)
        mBottomLineNormalColor = a.getColor(R.styleable.EditTextPro_bottomLineNormalColor, defaultNormalLineColor)
        mBottomLineFocusColor = a.getColor(R.styleable.EditTextPro_bottomLineFocusColor, defaultFocusLineColor)
        mBottomLineVisibility = a.getInt(R.styleable.EditTextPro_bottomLineVisibility, View.VISIBLE)
        mBottomLinePaddingStart = a.getDimensionPixelSize(R.styleable.EditTextPro_bottomLinePaddingStart, 0)
        mBottomLinePaddingEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_bottomLinePaddingEnd, 0)

        mCustomizeViewId = a.getResourceId(R.styleable.EditTextPro_customizeViewId, 0)
        mCustomizeViewIndex = a.getInt(R.styleable.EditTextPro_customizeViewIndex, -1)
        mCustomizeMarginStart = a.getDimensionPixelSize(R.styleable.EditTextPro_customizeMarginStart, 0)
        mCustomizeMarginEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_customizeMarginEnd, 0)

        mMinHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_android_minHeight, defaultMinHeight)
        a.recycle()

        initNecessaryData()
        initView()
    }

    private fun initNecessaryData() {
        fixInputType()

        mCancelVisibility = if (!isCancelEnable) {
            View.GONE
        } else if (mInputTextString.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }

        mSuggestIconWidth = UnitUtils.dip2px(40)
        mSuggestCancelWidth = UnitUtils.dip2px(30)

        mTopLinePaint.color = mTopLineNormalColor
        mTopLinePaint.strokeWidth = mTopLineHeight.toFloat()

        mBottomLinePaint.color = mBottomLineNormalColor
        mBottomLinePaint.strokeWidth = mBottomLineHeight.toFloat()
    }

    private fun initView() {
        addLeftImage()
        addLeftTextView()
        addEditView()
        addCancelButton()
        addRightTextView()
        addRightImageView()
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

        var inputWidth = width - paddingLeft - paddingRight

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
            inputWidth = inputWidth - mLeftImageView!!.measuredWidth - mLeftImageMarginStart - mLeftImageMarginEnd
        }

        if (mLeftTextView != null && mLeftTextView!!.visibility != View.GONE) {
            mLeftTextView!!.measure(unspecified, heightSpec)
            inputWidth = inputWidth - mLeftTextView!!.measuredWidth - mLeftTextMarginStart - mLeftTextMarginEnd
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
            inputWidth = inputWidth - mRightImageView!!.measuredWidth - mRightImageMarginStart - mRightImageMarginEnd
        }

        if (mRightTextView != null && mRightTextView!!.visibility != View.GONE) {
            mRightTextView!!.measure(unspecified, unspecified)
            inputWidth = inputWidth - mRightTextView!!.measuredWidth - mRightTextMarginStart - mRightTextMarginEnd
        }

        if (mCustomizeView != null && mCustomizeView!!.visibility != View.GONE) {
            measureChild(mCustomizeView, widthSpec, heightSpec)
            inputWidth = inputWidth - mCustomizeView!!.measuredWidth - mCustomizeMarginStart - mCustomizeMarginEnd
        }

        if (mCancelView != null && mCancelView!!.visibility != View.GONE) {
            inputWidth -= mSuggestCancelWidth
        }
        inputWidth = inputWidth - mInputMarginStart - mInputMarginEnd
        mInputView.measure(MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY), heightSpec)

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
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST -> {
                val paddingStart = paddingStart
                val paddingEnd = paddingEnd
                var height = mMinHeight

                var inputWidth = width - paddingStart - paddingEnd

                if (mLeftImageView != null && mLeftImageView!!.visibility != View.GONE) {
                    inputWidth = inputWidth - mSuggestIconWidth - mLeftImageMarginStart - mLeftImageMarginEnd
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

                if (mCancelView != null && mCancelView!!.visibility != View.GONE) {
                    inputWidth -= mSuggestCancelWidth
                }
                inputWidth = inputWidth - mInputMarginStart - mInputMarginEnd

                val widthSpec = MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY)
                val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                mInputView.measure(widthSpec, heightSpec)
                val inputHeight = mInputView.measuredHeight
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

        left += mInputMarginStart
        val inputWidth = mInputView.measuredWidth
        mInputView.layout(left, top, left + inputWidth, bottom)
        left += inputWidth

        if (mCancelView != null && mCancelView!!.visibility != View.GONE) {
            mCancelView!!.layout(left, top, left + mSuggestCancelWidth, bottom)
            left += mSuggestCancelWidth
        }
        left += mInputMarginEnd
        index++

        lastLeft = left
        left = layoutCustomize(left, index)
        index = if (lastLeft == left) index else index + 1

        if (mRightTextView != null && mRightTextView!!.visibility != View.GONE) {
            val width = mRightTextView!!.measuredWidth
            val height = mRightTextView!!.measuredHeight
            val tmpTop = (measuredHeight - height) / 2
            left += mRightTextMarginStart
            mRightTextView!!.layout(left, tmpTop, left + width, tmpTop + height)
            left += width + mRightTextMarginEnd
            index++
        }
        // 倒数第二个不需要处理index信息
        left = layoutCustomize(left, index)

        if (mRightImageView != null && mRightImageView!!.visibility != View.GONE) {
            val width = mRightImageView!!.measuredWidth
            val height = mRightImageView!!.measuredHeight
            val tmpTop = (measuredHeight - height) / 2
            left += mRightImageMarginStart
            mRightImageView!!.layout(left, tmpTop, left + width, tmpTop + height)
        }
        index = -1
        layoutCustomize(left, index)
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
        val tmpTop = (measuredHeight - height) / 2
        mCustomizeView!!.layout(left, tmpTop, left + width, tmpTop + height)
        left += width + mCustomizeMarginEnd
        return left
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

    private fun addLeftTextView() {
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

    private fun addEditView() {
        mInputView = AppCompatEditText(context)
        mInputView.setPadding(0, 0, 0, 0)
        mInputView.background = mInputBackgroundDrawable
        mInputView.gravity = mInputGravity
        mInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mInputTextSize.toFloat())
        mInputView.setTextColor(mInputTextColor)
        mInputView.setHintTextColor(mInputHintTextColor)
        mInputView.onFocusChangeListener = this
        mInputView.inputType = mInputType

        if (mInputLines > 0) {
            mInputView.setLines(mInputLines)
        }

        if (mInputMaxLength != 0) {
            mInputView.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(mInputMaxLength))
        }

        if (!mInputDigitsString.isNullOrEmpty()) {
            mInputView.keyListener = DigitsKeyListener.getInstance(mInputDigitsString)
        }

        mInputView.setText(mInputTextString)
        mInputView.hint = mInputHintString
        mInputView.addTextChangedListener(InputWatcher())
        addView(mInputView)
    }

    private fun addCancelButton() {
        if (mCancelView == null && isCancelEnable) {
            val imageView = AppCompatImageView(context)
            imageView.id = R.id.cancel
            imageView.setImageResource(R.drawable.ic_cancel)
            imageView.visibility = mCancelVisibility
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageView.setOnClickListener(this)

            mCancelView = imageView
            addView(mCancelView)
        }
    }

    private fun addRightTextView() {
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
            }
            if (mRightTextRightDrawable != null) {
                mRightTextRightDrawable = mRightTextRightDrawable!!.tint(mTintColor)
                textView.setDrawableRight(mRightTextRightDrawable!!)
            }
            setDefaultPromptState(textView)

            mRightTextView = textView
            addView(mRightTextView)
        }
    }

    private fun addRightImageView() {
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

            when (mRightImageAction) {
                RightImageAction.SET_PASSWORD_VISIBILITY,
                RightImageAction.SET_INPUT_ENABLE -> imageView.setOnClickListener(this)
            }
            setDefaultPromptState(imageView)

            mRightImageView = imageView
            addView(mRightImageView)
        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        mTopLinePaint.color = if (hasFocus) mTopLineFocusColor else mTopLineNormalColor
        mBottomLinePaint.color = if (hasFocus) mBottomLineFocusColor else mBottomLineNormalColor
        invalidate()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cancel -> cancel()
            R.id.rightImage -> updateViewByRightImage(v)
        }
    }

    private fun cancel() {
        val text = inputText
        if (!TextUtils.isEmpty(text)) {
            notifyCancel()
        }
        mInputView.setText(StringUtils.EMPTY)
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


    private fun setDefaultPromptState(prompt: IPrompt?) {
        if (prompt == null) {
            Log.i(TAG, "setDefaultPromptState: ")
            return
        }

        prompt.setPromptMode(IPrompt.PromptMode.NONE)
        prompt.commit()
    }

    private fun notifyCancel() {
        mEditTextListener?.onCancel()
    }

    fun setOnEditTextListener(listener: OnEditTextListener) {
        mEditTextListener = listener
    }

    interface OnEditTextListener {
        fun onCancel()
    }

    fun focus() {
        mInputView.requestFocus()
        showInput()
    }

    override fun hasFocus(): Boolean {
        return mInputView.hasFocus()
    }

    fun setLeftImageVisibility(visibility: Int) {
        if (mLeftImageView != null) {
            mLeftImageView!!.visibility = visibility
            mLeftImageVisibility = visibility
        }
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

    fun setInputText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setInputText(text)
    }

    fun setInputText(text: CharSequence) {
        mInputView.setText(text)
    }

    @Synchronized
    fun setInputText(text: CharSequence, selectionEnd: Boolean) {
        mInputView.setText(text)
        if (selectionEnd) {
            val nowText = mInputView.text
            mInputView.setSelection(nowText?.length ?: 0)
        }
    }

    fun setInputHint(text: String) {
        mInputView.hint = text
    }

    fun setInputHint(@StringRes hint: Int) {
        mInputView.setHint(hint)
    }

    fun setInputTextSize(@DimenRes resId: Int) {
        val size = resources.getDimensionPixelSize(resId).toFloat()
        mInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setInputTextColor(@ColorInt colorInt: Int) {
        mInputView.setTextColor(colorInt)
    }

    fun addInputWatcher(watcher: TextWatcher) {
        mInputView.addTextChangedListener(watcher)
    }

    fun removeInputWatcher(watcher: TextWatcher) {
        mInputView.removeTextChangedListener(watcher)
    }

    fun setRightText(@StringRes textRes: Int) {
        val text = context.getString(textRes)
        setRightText(text)
    }

    fun setRightText(text: String) {
        mRightTextString = text
        mRightTextView?.text = text
    }

    fun setBottomLineVisibility(visibility: Int) {
        mBottomLineVisibility = visibility
        postInvalidate()
    }

    fun hideInput() {
        post {
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(mInputView.windowToken, 0)
        }
    }

    fun showInput() {
        post {
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(mInputView, 0)
        }
    }

    fun setRightImageResource(@DrawableRes resId: Int) {
        mRightImageView?.setImageResource(resId)
    }

    fun setRightImageVisibility(visibility: Int) {
        if (mRightImageView != null) {
            mRightImageView!!.visibility = visibility
            mRightImageVisibility = visibility
        }
    }

    fun setRightImageClickListener(listener: OnClickListener) {
        mRightImageView?.setOnClickListener(listener)
    }

    fun setRightTextClickListener(listener: OnClickListener) {
        mRightTextView?.setOnClickListener(listener)
    }

    private inner class InputWatcher : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            if (!isCancelEnable) {
                return
            }

            if (s.isEmpty()) {
                notifyCancel()
                mCancelView?.visibility = View.INVISIBLE
            } else {
                mCancelView?.visibility = View.VISIBLE
            }
        }
    }


    private fun fixInputType() {
        if (mInputType and EditorInfo.TYPE_MASK_CLASS == EditorInfo.TYPE_CLASS_TEXT) {
            mInputType = if (mInputLines == 1) {
                mInputType and EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE.inv()
            } else {
                mInputType or EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
            }
        }
    }

    /**
     * 获取精确的MeasureSpec
     */
    private fun getExactlyMeasureSpec(size: Int): Int {
        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
    }

    /**
     * 通过右侧的 RightAction来设置当前状态
     */
    private fun updateViewByRightImage(v: View) {
        when (mRightImageAction) {
            RightImageAction.SET_PASSWORD_VISIBILITY -> updatePasswordVisibility(v)
            RightImageAction.SET_INPUT_ENABLE -> updateInputEnable(v)
        }
    }

    /**
     * 更新密码是否可见
     */
    private fun updatePasswordVisibility(v: View) {
        val type = mInputView.inputType
        if (type == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            mInputView.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            v.isSelected = false
        } else {
            mInputView.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            v.isSelected = true
        }

        val editable = mInputView.text
        if (editable != null) {
            mInputView.setSelection(editable.length)
        }
    }

    /**
     * 更新是否可以输入
     */
    private fun updateInputEnable(v: View) {
        isInputEnable = !isInputEnable
        if (isInputEnable) {
            mInputView.isFocusableInTouchMode = true
            mInputView.isFocusable = true
            mInputView.requestFocus()
            v.isSelected = true
            showInput()
        } else {
            mInputView.isFocusableInTouchMode = false
            mInputView.isFocusable = false
            v.isSelected = false
            hideInput()
        }
    }

    object RightImageAction {
        /**
         * 没有任何操作
         */
        const val NONE = 0
        /**
         * 设置密码是否可见
         */
        const val SET_PASSWORD_VISIBILITY = 4
        /**
         * 设置是否可以输入
         */
        const val SET_INPUT_ENABLE = 8
    }

    companion object {
        private const val TAG = "EditTextPro"
    }
}
