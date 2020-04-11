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
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Layout
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.ishow.common.R
import com.ishow.common.extensions.setDrawableLeft
import com.ishow.common.extensions.setPadding
import com.ishow.common.extensions.setPaddingHorizontal
import com.ishow.common.extensions.setPaddingVertical
import com.ishow.common.utils.AppUtils
import com.ishow.common.widget.imageview.PromptImageView
import com.ishow.common.widget.prompt.IPrompt
import com.ishow.common.widget.textview.MarqueeTextView
import com.ishow.common.widget.textview.PromptTextView
import kotlin.math.max

class TopBar(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs), OnClickListener {

    /**
     * 左侧第一个图片
     */
    private var mLeftImageView: PromptImageView? = null
    private var mLeftImageWidth: Int = 0
    private val mLeftImageMinWidth: Int
    private val mLeftImageResId: Int
    private var mLeftImageVisibility: Int = 0
    private var mLeftImageWidthSpec: Int = 0
    /**
     * 左侧第二个图片
     */
    private var mLeftImageView2: PromptImageView? = null
    private var mLeftImage2Width: Int = 0
    private val mLeftImage2ResId: Int
    private var mLeftImage2Visibility: Int = 0
    /**
     * 左侧文本
     */
    private var mLeftTextView: PromptTextView? = null
    private val mLeftTextDrawable: Drawable?
    private val mLeftTextBackground: Drawable?
    private var mLeftTextColor: ColorStateList? = null
    private val mLeftStr: String?
    private var mLeftTextSize: Int = 0
    private var mLeftTextViewWidth: Int = 0
    private val mLeftTextMinWidth: Int
    private var mLeftTextVisibility: Int = 0
    private var mLeftTextDrawablePadding: Int = 0
    /**
     * 主标题
     */
    private var mTitleView: MarqueeTextView? = null
    private var mTitleColor: ColorStateList? = null
    private val mTitleStr: String?
    private val mTitleSize: Int
    private var mTitleDesireWidth: Int = 0
    private val mTextStyle: Int
    private var mTitleVisibility: Int = 0
    private var mTitleClickable: Boolean = false
    /**
     * 副标题
     */
    private var mSubTitleView: MarqueeTextView? = null
    private var mSubTitleColor: ColorStateList? = null
    private val mSubTitleStr: String?
    private var mSubTitleDesireWidth: Int = 0
    private val mSubTitleSize: Int
    private var mSubTitleVisibility: Int = 0
    /**
     * 右侧第一个图片
     */
    private var mRightImageView: PromptImageView? = null
    private var mRightImageWidth: Int = 0
    private var mRightImageMinWidth: Int = 0
    private val mRightImageResId: Int
    private var mRightImageVisibility: Int = 0
    private var mRightImageWidthSpec: Int = 0
    /**
     * 右侧第二个图片
     */
    private var mRightImageView2: PromptImageView? = null
    private var mRightImage2Width: Int = 0
    private val mRightImage2ResId: Int
    private var mRightImage2Visibility: Int = 0
    /**
     * 右侧文本
     */
    private var mRightTextView: PromptTextView? = null
    private var mRightTextColor: ColorStateList? = null
    private val mRightTextBackground: Drawable?
    private val mRightTextDrawable: Drawable?
    private val mRightStr: String?
    private var mRightTextWidth: Int = 0
    private val mRightTextMinWidth: Int
    private var mRightTextSize: Int = 0
    private var mRightTextDrawablePadding: Int = 0
    private var mRightTextVisibility: Int = 0
    private var mRightTextPaddingHorizontal: Int = 0
    private var mRightTextPaddingVertical: Int = 0

    private val mBackGround: Int
    private var mLeftBackground: Int = 0
    private var mRightBackground: Int = 0
    private val mItemBackground: Int

    private var fitTopSize: Int = 0
    private val isFitSystemWindow: Boolean
    private val isConsumeSystemWindowInsets: Boolean
    /**
     * TopBar的高度
     */
    private val mHeight: Int
    /**
     * 图片或者文字的最小宽度
     */
    private var mUnitWidth: Int = 0
    /**
     * 相关监听（左右点击事件）
     */
    private var mTopBarListener: OnTopBarListener? = null
    /**
     * 高度的Spec
     */
    private var mExactlyWidthSpec: Int = 0
    private var mExactlyHeightSpec: Int = 0
    private var mExactlyViewHeightSpec: Int = 0
    private var mAtMostHeightSpec: Int = 0

    private var mGapSize: Int = 0
    private var mSmallGapSize: Int = 0

    /**
     * 左后文字的字体大小应该是Title字体的0。8
     */
    private val suggestLeftOrRightTextSize: Int
        get() = (mTitleSize * 0.8f).toInt()

    /**
     * 获取默认TopBar的高度
     */
    private val defaultHeight: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.top_bar_height)

    /**
     * 获取默认标题字体大小
     */
    private val defaultTextSize: Int
        get() = context.resources.getDimensionPixelOffset(R.dimen.top_bar_title_text_size)

    /**
     * 获取默认副标题标题字体大小
     */
    private val defaultSubTitleSize: Int
        get() = context.resources.getDimensionPixelOffset(R.dimen.top_bar_sub_title_text_size)

    var rightTextEnable: Boolean
        get() = if (mRightTextView != null) mRightTextView!!.isEnabled else false
        set(value) {
            mRightTextView?.isEnabled = value
        }


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TopBar, R.attr.topBarStyle, 0)
        mHeight = a.getDimensionPixelOffset(R.styleable.TopBar_android_actionBarSize, defaultHeight)
        mBackGround = a.getResourceId(R.styleable.TopBar_android_background, R.color.transparent)
        mItemBackground = a.getResourceId(R.styleable.TopBar_itemBackground, R.color.transparent)

        mTitleStr = a.getString(R.styleable.TopBar_text)
        mTitleSize = a.getDimensionPixelSize(R.styleable.TopBar_textSize, defaultTextSize)
        mTitleColor = a.getColorStateList(R.styleable.TopBar_textColor)
        mTextStyle = a.getInt(R.styleable.TopBar_textStyle, 0)

        mSubTitleStr = a.getString(R.styleable.TopBar_subText)
        mSubTitleSize = a.getDimensionPixelSize(R.styleable.TopBar_subTextSize, defaultSubTitleSize)
        mSubTitleColor = a.getColorStateList(R.styleable.TopBar_subTextColor)

        mLeftStr = a.getString(R.styleable.TopBar_leftText)
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.TopBar_leftTextSize, suggestLeftOrRightTextSize)
        mLeftTextColor = a.getColorStateList(R.styleable.TopBar_leftTextColor)
        mLeftTextBackground = a.getDrawable(R.styleable.TopBar_leftTextBackground)
        mLeftTextDrawable = a.getDrawable(R.styleable.TopBar_leftTextDrawable)
        mLeftTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TopBar_leftTextDrawablePadding, 0)
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_leftTextMinWidth, 0)

        mLeftBackground = a.getResourceId(R.styleable.TopBar_leftImageBackground, 0)

        mLeftImageResId = a.getResourceId(R.styleable.TopBar_leftImage, 0)
        mLeftImageMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_leftImageMinWidth, 0)
        mLeftImageVisibility = a.getInt(R.styleable.TopBar_leftImageVisibility, View.VISIBLE)

        mLeftImage2ResId = a.getResourceId(R.styleable.TopBar_leftImage2, 0)
        mLeftImage2Visibility = a.getInt(R.styleable.TopBar_leftImage2Visibility, View.VISIBLE)

        mRightStr = a.getString(R.styleable.TopBar_rightText)
        mRightTextSize = a.getDimensionPixelSize(R.styleable.TopBar_rightTextSize, suggestLeftOrRightTextSize)
        mRightTextColor = a.getColorStateList(R.styleable.TopBar_rightTextColor)
        mRightTextBackground = a.getDrawable(R.styleable.TopBar_rightTextBackground)
        mRightTextDrawable = a.getDrawable(R.styleable.TopBar_rightTextDrawable)
        mRightTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TopBar_rightTextDrawablePadding, 0)
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_rightTextMinWidth, 0)
        mRightTextPaddingHorizontal = a.getDimensionPixelSize(R.styleable.TopBar_rightTextPaddingHorizontal, 0)
        mRightTextPaddingVertical = a.getDimensionPixelSize(R.styleable.TopBar_rightTextPaddingVertical, 0)

        mRightBackground = a.getResourceId(R.styleable.TopBar_rightImageBackground, 0)
        mRightImageResId = a.getResourceId(R.styleable.TopBar_rightImage, 0)
        mRightImageVisibility = a.getInt(R.styleable.TopBar_rightImageVisibility, View.VISIBLE)
        mRightImageMinWidth = a.getDimensionPixelSize(R.styleable.TopBar_rightImageMinWidth, 0)
        mRightImage2ResId = a.getResourceId(R.styleable.TopBar_rightImage2, 0)
        mRightImage2Visibility = a.getInt(R.styleable.TopBar_rightImage2Visibility, View.VISIBLE)

        mTitleClickable = a.getBoolean(R.styleable.TopBar_clickable, false)
        isFitSystemWindow = a.getBoolean(R.styleable.TopBar_android_fitsSystemWindows, false)
        isConsumeSystemWindowInsets = a.getBoolean(R.styleable.TopBar_consumeSystemWindowInsets, true)
        a.recycle()

        initNecessaryParams()
        initView()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, mExactlyViewHeightSpec)
        val width = measuredWidth - paddingStart - paddingEnd

        if (mLeftImageVisibility != View.GONE && mLeftImageView != null) {
            mLeftImageView!!.measure(mLeftImageWidthSpec, mExactlyHeightSpec)
            mLeftImageWidth = mLeftImageView!!.measuredWidth
        }

        if (mLeftImage2Visibility != View.GONE && mLeftImageView2 != null) {
            mLeftImageView2!!.measure(mExactlyWidthSpec, mExactlyHeightSpec)
            mLeftImage2Width = mLeftImageView2!!.measuredWidth
        }

        if (mLeftTextVisibility != View.GONE && mLeftTextView != null) {
            val widthSpec = MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST)
            if (mLeftTextBackground != null) {
                mLeftTextView!!.measure(widthSpec, mAtMostHeightSpec)
            } else {
                mLeftTextView!!.measure(widthSpec, mExactlyHeightSpec)
            }
            mLeftTextViewWidth = mLeftTextView!!.measuredWidth
        }

        if (mRightImageVisibility != View.GONE && mRightImageView != null) {
            mRightImageView!!.measure(mRightImageWidthSpec, mExactlyHeightSpec)
            mRightImageWidth = mRightImageView!!.measuredWidth
        }

        if (mRightImage2Visibility != View.GONE && mRightImageView2 != null) {
            mRightImageView2!!.measure(mExactlyWidthSpec, mExactlyHeightSpec)
            mRightImage2Width = mRightImageView2!!.measuredWidth
        }

        if (mRightTextVisibility != View.GONE && mRightTextView != null) {
            val widthSpec = MeasureSpec.makeMeasureSpec(width / 3, MeasureSpec.AT_MOST)
            if (mRightTextBackground != null) {
                mRightTextView!!.measure(widthSpec, mAtMostHeightSpec)
            } else {
                mRightTextView!!.measure(widthSpec, mExactlyHeightSpec)
            }
            mRightTextWidth = mRightTextView!!.measuredWidth
        }


        val titleWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        if (mTitleVisibility != View.GONE && mTitleView != null) {
            mTitleView!!.measure(titleWidthSpec, mAtMostHeightSpec)
        }

        if (mSubTitleVisibility != View.GONE && mSubTitleView != null) {
            mSubTitleView!!.measure(titleWidthSpec, mAtMostHeightSpec)
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, bottom: Int) {
        val childTop = fitTopSize
        val childBottom = fitTopSize + mHeight
        var left = l + paddingStart
        var right = r - paddingEnd
        val width = right - left
        if (mLeftImageVisibility != View.GONE && mLeftImageView != null) {
            mLeftImageView!!.layout(left, childTop, left + mLeftImageWidth, childBottom)
            left += mLeftImageWidth
        }

        if (mLeftImage2Visibility != View.GONE && mLeftImageView2 != null) {
            mLeftImageView2!!.layout(left, childTop, left + mLeftImage2Width, childBottom)
            left += mLeftImage2Width
        }

        if (mLeftTextVisibility != View.GONE && mLeftTextView != null) {
            if (mLeftTextBackground == null) {
                mLeftTextView!!.layout(left, childTop, left + mLeftTextViewWidth, childBottom)
            } else {
                val tmpHeight = mLeftTextView!!.measuredHeight
                val tmpTop = (mHeight - tmpHeight) / 2 + childTop
                if (left == 0) left = mGapSize
                mLeftTextView!!.layout(left, tmpTop, left + mLeftTextViewWidth, tmpTop + tmpHeight)
            }
        }

        if (mRightTextVisibility != View.GONE && mRightTextView != null) {
            if (mRightTextBackground == null) {
                mRightTextView!!.layout(right - mRightTextWidth, childTop, right, childBottom)
            } else {
                val tmpHeight = mRightTextView!!.measuredHeight
                val tempTop = (mHeight - tmpHeight) / 2 + childTop
                if (right == width) {
                    right -= mGapSize
                }
                mRightTextView!!.layout(right - mRightTextWidth, tempTop, right, tempTop + tmpHeight)
            }
            right -= mRightTextWidth
        }
        if (mRightImageVisibility != View.GONE && mRightImageView != null) {
            mRightImageView!!.layout(right - mRightImageWidth, childTop, right, childBottom)
            right -= mRightImageWidth
        }

        if (mRightImage2Visibility != View.GONE && mRightImageView2 != null) {
            mRightImageView2!!.layout(right - mRightImage2Width, childTop, right, childBottom)
        }

        val titleHeight = if (mTitleVisibility == View.GONE) 0 else mTitleView!!.measuredHeight
        val subTitleHeight = if (mSubTitleVisibility == View.GONE) 0 else mSubTitleView!!.measuredHeight
        val rightTotal = mRightImageWidth + mRightImage2Width + mRightTextWidth
        val leftTotal = mLeftImageWidth + mLeftImage2Width + mLeftTextViewWidth

        var top = (mHeight - titleHeight - subTitleHeight) / 2 + childTop
        if (mTitleVisibility != View.GONE && mTitleView != null) {
            mTitleView!!.layout(0, top, width, top + titleHeight)
            top += titleHeight
            resetTitlePadding(width, leftTotal, rightTotal)
        }

        if (mSubTitleVisibility != View.GONE && mSubTitleView != null) {
            mSubTitleView!!.layout(0, top, width, top + subTitleHeight)
            resetSubTitlePadding(width, leftTotal, rightTotal)
        }
    }

    override fun onWindowSystemUiVisibilityChanged(visible: Int) {
        super.onWindowSystemUiVisibilityChanged(visible)
        if(visible == 0 && isFitSystemWindow ) fitTopSize = 0
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        if (!isFitSystemWindow || insets == null) {
            return super.dispatchApplyWindowInsets(insets)
        }

        fitTopSize = insets.systemWindowInsetTop
        mExactlyViewHeightSpec = MeasureSpec.makeMeasureSpec(mHeight + fitTopSize, MeasureSpec.EXACTLY)

        if (!isConsumeSystemWindowInsets) {
            return super.dispatchApplyWindowInsets(insets)
        }

        val windowInsets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val nowSystemWindow = consumeSystemWindowTopInsets(insets)
            WindowInsets.Builder(insets)
                .setSystemWindowInsets(nowSystemWindow)
                .build()
        } else {
            insets.consumeSystemWindowInsets()
            insets.replaceSystemWindowInsets(insets.systemWindowInsetLeft, 0, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
        }

        return super.dispatchApplyWindowInsets(windowInsets)
    }

    /**
     * 消费掉顶部的高度
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun consumeSystemWindowTopInsets(old: WindowInsets): Insets {
        return Insets.of(old.systemWindowInsetLeft, 0, old.systemWindowInsetRight, old.systemWindowInsetBottom)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftText,
            R.id.leftImage,
            R.id.leftImage2 -> performLeftClick(v)

            R.id.rightText,
            R.id.rightImage,
            R.id.rightImage2 -> performRightClick(v)

            R.id.title,
            R.id.subTitle -> performCenterClick(v)
        }
    }

    /**
     * 初始化一些必要的变量
     */
    private fun initNecessaryParams() {
        mUnitWidth = (mHeight * UNIT_WIDTH_RADIO).toInt()
        mExactlyHeightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)
        mExactlyViewHeightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)

        mExactlyWidthSpec = MeasureSpec.makeMeasureSpec(mUnitWidth, MeasureSpec.EXACTLY)
        mLeftImageWidthSpec = MeasureSpec.makeMeasureSpec(max(mLeftImageMinWidth, mUnitWidth), MeasureSpec.EXACTLY)
        mRightImageWidthSpec = MeasureSpec.makeMeasureSpec(max(mRightImageMinWidth, mUnitWidth), MeasureSpec.EXACTLY)
        mAtMostHeightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.AT_MOST)
        mGapSize = resources.getDimensionPixelSize(R.dimen.gap_grade_1)
        mSmallGapSize = resources.getDimensionPixelSize(R.dimen.gap_grade_0)
    }

    private fun initView() {
        // 在加载View之前先确认参数是否正确，或更换为默认值
        checkEffectiveParams()

        getTitle()
        getSubTitle()

        getLeftImageView()
        getLeftImageView2()
        getLeftTextView()

        getRightImageView()
        getRightImageView2()
        getRightTextView()

        setBackgroundResource(mBackGround)
    }


    /**
     * 进行检测参数的有效性
     */
    private fun checkEffectiveParams() {
        if (TextUtils.isEmpty(mTitleStr)) mTitleVisibility = View.GONE
        if (mTitleColor == null) mTitleColor = ColorStateList.valueOf(Color.WHITE)

        if (TextUtils.isEmpty(mSubTitleStr)) mSubTitleVisibility = View.GONE
        if (mSubTitleColor == null) mSubTitleColor = ColorStateList.valueOf(Color.WHITE)

        if (TextUtils.isEmpty(mLeftStr)) mLeftTextVisibility = View.GONE
        if (mLeftTextColor == null) mLeftTextColor = mTitleColor
        if (mLeftBackground == 0) mLeftBackground = mItemBackground

        if (mLeftImageResId == 0) mLeftImageVisibility = View.GONE
        if (mLeftImage2ResId == 0) mLeftImage2Visibility = View.GONE

        if (TextUtils.isEmpty(mRightStr)) mRightTextVisibility = View.GONE
        if (mRightTextColor == null) mRightTextColor = mTitleColor
        if (mRightBackground == 0) mRightBackground = mItemBackground

        if (mRightImageResId == 0) mRightImageVisibility = View.GONE
        if (mRightImage2ResId == 0) mRightImage2Visibility = View.GONE
    }

    fun getTitle(): TextView? {
        if (mTitleVisibility == View.GONE) {
            return null
        }

        if (mTitleView == null) {
            mTitleView = MarqueeTextView(context)
            mTitleView!!.id = R.id.title
            mTitleView!!.text = mTitleStr
            mTitleView!!.setPadding(mGapSize, mSmallGapSize, mGapSize, mSmallGapSize)
            mTitleView!!.setTextColor(mTitleColor)
            mTitleView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize.toFloat())
            mTitleView!!.includeFontPadding = false
            mTitleView!!.setOnClickListener(this)
            mTitleView!!.gravity = Gravity.CENTER
            mTitleView!!.isClickable = mTitleClickable
            mTitleView!!.typeface = Typeface.defaultFromStyle(mTextStyle)
            addView(mTitleView, 0)

            computeTitleDesireWidth()
        }
        return mTitleView
    }

    fun getSubTitle(): TextView? {
        if (mSubTitleVisibility == View.GONE) {
            return null
        }

        if (mSubTitleView == null) {
            mSubTitleView = MarqueeTextView(context)
            mSubTitleView!!.id = R.id.subTitle
            mSubTitleView!!.text = mSubTitleStr
            mSubTitleView!!.setPadding(mGapSize, 0, mGapSize, 0)
            mSubTitleView!!.setOnClickListener(this)
            mSubTitleView!!.setTextColor(mSubTitleColor)
            mSubTitleView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSubTitleSize.toFloat())
            mSubTitleView!!.includeFontPadding = false
            mSubTitleView!!.gravity = Gravity.CENTER
            mSubTitleView!!.isClickable = mTitleClickable
            addView(mSubTitleView, 0)

            computeSubTitleDesireWidth()
        }
        return mSubTitleView
    }


    fun getLeftImageView(): PromptImageView? {
        if (mLeftImageVisibility == View.GONE) {
            return null
        }

        if (mLeftImageView == null) {
            mLeftImageView = PromptImageView(context)
            mLeftImageView!!.id = R.id.leftImage
            mLeftImageView!!.visibility = mLeftImageVisibility
            mLeftImageView!!.setImageResource(mLeftImageResId)
            mLeftImageView!!.setBackgroundResource(mLeftBackground)
            mLeftImageView!!.scaleType = ImageView.ScaleType.CENTER
            mLeftImageView!!.setOnClickListener(this)
            mLeftImageView!!.minimumWidth = mUnitWidth
            mLeftImageView!!.minimumHeight = mHeight
            setDefaultPromptState(mLeftImageView)
            addView(mLeftImageView)
        }
        return mLeftImageView
    }

    fun getLeftImageView2(): PromptImageView? {
        if (mLeftImage2Visibility == View.GONE) {
            return null
        }

        if (mLeftImageView2 == null) {
            mLeftImageView2 = PromptImageView(context)
            mLeftImageView2!!.id = R.id.leftImage2
            mLeftImageView2!!.visibility = mLeftImage2Visibility
            mLeftImageView2!!.setImageResource(mLeftImage2ResId)
            mLeftImageView2!!.setBackgroundResource(mLeftBackground)
            mLeftImageView2!!.scaleType = ImageView.ScaleType.CENTER
            mLeftImageView2!!.minimumWidth = mUnitWidth
            mLeftImageView2!!.minimumHeight = mHeight
            mLeftImageView2!!.setOnClickListener(this)
            setDefaultPromptState(mLeftImageView2)
            addView(mLeftImageView2)
        }
        return mLeftImageView
    }

    // 至少要这么宽 位了美观
    fun getLeftTextView(): PromptTextView? {
        if (mLeftTextVisibility == View.GONE) {
            return null
        }

        if (mLeftTextView == null) {
            mLeftTextView = PromptTextView(context)
            mLeftTextView!!.id = R.id.leftText
            mLeftTextView!!.text = mLeftStr
            mLeftTextView!!.setPadding(mGapSize, mGapSize, mGapSize, mGapSize)
            mLeftTextView!!.gravity = Gravity.CENTER
            mLeftTextView!!.setTextColor(mLeftTextColor)
            mLeftTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize.toFloat())
            mLeftTextView!!.setOnClickListener(this)
            mLeftTextView!!.setLines(1)
            mLeftTextView!!.minWidth = max(mLeftTextMinWidth, mUnitWidth)
            mLeftTextView!!.ellipsize = TextUtils.TruncateAt.END
            if (mLeftTextBackground != null) {
                mLeftTextView!!.background = mLeftTextBackground
            } else {
                mLeftTextView!!.setBackgroundResource(mLeftBackground)
            }

            if (mLeftTextDrawable != null) {
                mLeftTextView!!.setDrawableLeft(mLeftTextDrawable)
                mLeftTextView!!.compoundDrawablePadding = mLeftTextDrawablePadding
            }

            setDefaultPromptState(mLeftTextView)
            addView(mLeftTextView)
        }
        return mLeftTextView
    }

    fun getRightImageView(): PromptImageView? {
        if (mRightImageVisibility == View.GONE) {
            return null
        }

        if (mRightImageView == null) {
            mRightImageView = PromptImageView(context)
            mRightImageView!!.id = R.id.rightImage
            mRightImageView!!.visibility = mRightImageVisibility
            mRightImageView!!.setImageResource(mRightImageResId)
            mRightImageView!!.setBackgroundResource(mRightBackground)
            mRightImageView!!.scaleType = ImageView.ScaleType.CENTER
            mRightImageView!!.setOnClickListener(this)
            setDefaultPromptState(mRightImageView)
            addView(mRightImageView)
        }
        return mRightImageView
    }

    fun getRightImageView2(): PromptImageView? {
        if (mRightImage2Visibility == View.GONE) {
            return null
        }

        if (mRightImageView2 == null) {
            mRightImageView2 = PromptImageView(context)
            mRightImageView2!!.id = R.id.rightImage2
            mRightImageView2!!.visibility = mRightImage2Visibility
            mRightImageView2!!.setImageResource(mRightImage2ResId)
            mRightImageView2!!.setBackgroundResource(mRightBackground)
            mRightImageView2!!.scaleType = ImageView.ScaleType.CENTER
            mRightImageView2!!.setOnClickListener(this)
            setDefaultPromptState(mRightImageView2)
            addView(mRightImageView2)
        }
        return mRightImageView2
    }

    // 至少要这么宽 位了美观
    fun getRightTextView(): PromptTextView? {
        if (mRightTextVisibility == View.GONE) {
            return null
        }

        if (mRightTextView == null) {
            val textView = PromptTextView(context)
            textView.id = R.id.rightText
            textView.text = mRightStr
            textView.setPadding(mGapSize, mGapSize, mGapSize, mGapSize)
            textView.gravity = Gravity.CENTER
            textView.setTextColor(mRightTextColor)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize.toFloat())
            textView.setLines(1)
            textView.setOnClickListener(this)
            textView.minWidth = max(mUnitWidth, mRightTextMinWidth)
            textView.ellipsize = TextUtils.TruncateAt.END
            if (mRightTextBackground != null) {
                textView.background = mRightTextBackground
            } else {
                textView.setBackgroundResource(mRightBackground)
            }

            if (mRightTextDrawable != null) {
                textView.setDrawableLeft(mRightTextDrawable)
                textView.compoundDrawablePadding = mRightTextDrawablePadding
            }

            if (mRightTextPaddingHorizontal > 0 && mRightTextPaddingVertical > 0) {
                textView.setPadding(mRightTextPaddingHorizontal, mRightTextPaddingVertical)
            } else if (mRightTextPaddingHorizontal > 0) {
                textView.setPaddingHorizontal(mRightTextPaddingHorizontal)
            } else if (mRightTextPaddingVertical > 0) {
                textView.setPaddingVertical(mRightTextPaddingVertical)
            }

            setDefaultPromptState(textView)
            mRightTextView = textView
            addView(mRightTextView)
        }
        return mRightTextView
    }


    private fun resetTitlePadding(width: Int, left: Int, right: Int) {
        var leftTotal = left
        var rightTotal = right
        val bestPadding = max(leftTotal, rightTotal)
        val bestWidth = width - bestPadding * 2 - mGapSize * 2
        val maxWidth = width - rightTotal - leftTotal - mGapSize * 2
        // 差值
        val sub = maxWidth - mTitleDesireWidth

        @Suppress("CascadeIf")
        if (mTitleDesireWidth <= bestWidth) {
            mTitleView?.setPadding(bestPadding, mSmallGapSize, bestPadding, mSmallGapSize)
        } else if (mTitleDesireWidth >= maxWidth) {
            mTitleView?.setPadding(leftTotal + mGapSize, mSmallGapSize, rightTotal + mGapSize, mSmallGapSize)
        } else if (leftTotal > rightTotal) {
            if (leftTotal == mLeftTextViewWidth && mLeftTextBackground != null) {
                leftTotal += mGapSize
            }

            if (rightTotal == mRightTextWidth && mRightTextBackground != null) {
                rightTotal += mGapSize
            }
            mTitleView?.setPadding(
                leftTotal + mGapSize,
                mSmallGapSize,
                rightTotal + sub + mGapSize,
                mSmallGapSize
            )
        } else {
            if (leftTotal == mLeftTextViewWidth && mLeftTextBackground != null) {
                leftTotal += mGapSize
            }

            if (rightTotal == mRightTextWidth && mRightTextBackground != null) {
                rightTotal += mGapSize
            }

            mTitleView?.setPadding(
                leftTotal + mGapSize + sub,
                mSmallGapSize,
                rightTotal + mGapSize,
                mSmallGapSize
            )
        }
    }

    private fun resetSubTitlePadding(width: Int, leftTotal: Int, rightTotal: Int) {
        val bestPadding = max(leftTotal, rightTotal)
        val bestWidth = width - bestPadding * 2 - mGapSize * 2
        val maxWidth = width - rightTotal - leftTotal - mGapSize * 2
        // 差值
        val sub = maxWidth - mSubTitleDesireWidth

        when {
            mSubTitleDesireWidth < bestWidth -> {
                mSubTitleView?.setPadding(bestPadding, 0, bestPadding, 0)
            }
            mSubTitleDesireWidth > maxWidth -> {
                mSubTitleView?.setPadding(leftTotal + mGapSize, 0, rightTotal + mGapSize, 0)
            }
            leftTotal > rightTotal -> {
                mSubTitleView?.setPadding(leftTotal + mGapSize, 0, rightTotal + mGapSize + sub, 0)
            }
            else -> {
                mSubTitleView?.setPadding(leftTotal + mGapSize + sub, 0, rightTotal + mGapSize, 0)
            }
        }
    }

    /**
     * 左侧点击
     */
    private fun performLeftClick(v: View) {
        if (!AppUtils.isFastDoubleClick) {
            mTopBarListener?.onLeftClick(v)
        }
    }

    /**
     * 右侧点击
     */
    private fun performRightClick(v: View) {
        if (!AppUtils.isFastDoubleClick) {
            mTopBarListener?.onRightClick(v)
        }
    }

    private fun performCenterClick(v: View) {
        if (!AppUtils.isFastDoubleClick) {
            mTopBarListener?.onTitleClick(v)
        }
    }

    /**
     * 设置Title
     */
    fun setText(@StringRes resId: Int) {
        val title = context.getString(resId)
        setText(title)
    }

    /**
     * 设置Title
     */
    fun setText(title: String?) {
        mTitleVisibility = View.VISIBLE
        getTitle()
        mTitleView?.text = title
        computeTitleDesireWidth()
        requestLayout()
    }

    /**
     * 设置副标题
     */
    fun setSubText(@StringRes resId: Int) {
        val text = context.getString(resId)
        setSubText(text)
    }

    /**
     * 设置subTitle
     */
    fun setSubText(title: String?) {
        mSubTitleVisibility = View.VISIBLE
        getSubTitle()
        mSubTitleView?.text = title
        computeSubTitleDesireWidth()
        requestLayout()
    }

    /**
     * 设置左边字体显示
     */
    fun setLeftText(@StringRes resId: Int) {
        val text = context.getString(resId)
        setLeftText(text)
    }

    /**
     * 设置左边字体显示
     */
    fun setLeftText(text: String) {
        mLeftTextVisibility = View.VISIBLE
        getLeftTextView()
        mLeftTextView?.text = text
        requestLayout()
    }

    /**
     * 右边文本的左侧小图
     */
    fun setLeftTextLeftDrawable(@DrawableRes resId: Int) {
        val drawable = ContextCompat.getDrawable(context, resId)
        setLeftTextLeftDrawable(drawable)
    }

    /**
     * 右边文本的左侧小图
     */
    fun setLeftTextLeftDrawable(drawable: Drawable?) {
        if (drawable == null) {
            Log.i(TAG, "setRightTextLeftDrawable: drawable is null")
            return
        }
        mLeftTextVisibility = View.VISIBLE
        getLeftTextView()
        mLeftTextView?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        mLeftTextView?.compoundDrawablePadding = mLeftTextDrawablePadding
        requestLayout()
    }

    /**
     * 右边文本的左侧小图
     */
    fun setLeftTextRightDrawable(@DrawableRes resId: Int) {
        val drawable = ContextCompat.getDrawable(context, resId)
        setLeftTextRightDrawable(drawable)
    }

    /**
     * 右边文本的左侧小图
     */
    fun setLeftTextRightDrawable(drawable: Drawable?) {
        if (drawable == null) {
            Log.i(TAG, "setRightTextLeftDrawable: drawable is null")
            return
        }
        mLeftTextVisibility = View.VISIBLE
        getLeftTextView()
        mLeftTextView?.setCompoundDrawablesWithIntrinsicBounds(mLeftTextDrawable, null, drawable, null)
        mLeftTextView?.compoundDrawablePadding = mLeftTextDrawablePadding
        requestLayout()
    }

    /**
     * 左侧边文本和图片的padding
     */
    fun setLeftTextLeftDrawablePadding(padding: Int) {
        mLeftTextVisibility = View.VISIBLE
        getLeftTextView()
        mLeftTextDrawablePadding = padding
        mLeftTextView?.compoundDrawablePadding = padding
        requestLayout()
    }

    fun setLeftImageResource(@DrawableRes resId: Int) {
        mLeftImageVisibility = View.VISIBLE
        getLeftImageView()
        mLeftImageView?.setImageResource(resId)
        requestLayout()
    }

    fun setLeftImageDrawable(drawable: Drawable) {
        mLeftImageVisibility = View.VISIBLE
        getLeftImageView()
        mLeftImageView?.setImageDrawable(drawable)
        requestLayout()
    }


    fun setLeft2ImageResource(@DrawableRes resId: Int) {
        mLeftImage2Visibility = View.VISIBLE
        getLeftImageView2()
        mLeftImageView2?.setImageResource(resId)
        requestLayout()
    }

    /**
     * 设置右边字体显示
     */
    fun setRightText(@StringRes resId: Int) {
        val text = context.getString(resId)
        setRightText(text)
    }

    /**
     * 设置右边字体显示
     */
    fun setRightText(text: String?) {
        mRightTextVisibility = View.VISIBLE
        getRightTextView()
        mRightTextView?.text = text
        requestLayout()
    }

    /**
     * 右边文本的左侧小图
     */
    fun setRightTextLeftDrawable(@DrawableRes resId: Int) {
        val drawable = ContextCompat.getDrawable(context, resId)
        setRightTextLeftDrawable(drawable)
    }

    /**
     * 右边文本的左侧小图
     */
    fun setRightTextLeftDrawable(drawable: Drawable?) {
        if (drawable == null) {
            Log.i(TAG, "setRightTextLeftDrawable: drawable is null")
            return
        }
        mRightTextVisibility = View.VISIBLE
        getRightTextView()
        mRightTextView?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        mRightTextView?.compoundDrawablePadding = mRightTextDrawablePadding
        requestLayout()
    }


    /**
     * 右边文本的左侧小图
     */
    fun setRightTextLeftDrawablePadding(padding: Int) {
        mRightTextVisibility = View.VISIBLE
        getRightTextView()
        mRightTextDrawablePadding = padding
        mRightTextView?.compoundDrawablePadding = padding
        requestLayout()
    }

    /**
     * 右侧第一个图
     */
    fun setRightImageResource(@DrawableRes resId: Int) {
        mRightImageVisibility = View.VISIBLE
        getRightImageView()
        mRightImageView?.setImageResource(resId)
        requestLayout()
    }

    fun setRightImageMinWidth(minWidth: Int) {
        mRightImageMinWidth = minWidth
        mRightImageWidthSpec = MeasureSpec.makeMeasureSpec(max(minWidth, mUnitWidth), MeasureSpec.EXACTLY)
        requestLayout()
    }

    /**
     * 右侧第二个图
     */
    fun setRight2ImageResource(@DrawableRes resId: Int) {
        mRightImage2Visibility = View.VISIBLE
        getRightImageView2()
        mRightImageView2?.setImageResource(resId)
        requestLayout()
    }


    /**
     * 设置左边是否可见
     */
    fun setLeftVisibility(visibility: Int) {
        setLeftTextVisibility(visibility)
        setLeftImageVisibility(visibility)
        setLeftImage2Visibility(visibility)
    }

    fun setLeftTextVisibility(visibility: Int) {
        mLeftTextView?.visibility = visibility
        mLeftTextVisibility = visibility
    }

    fun setLeftImageVisibility(visibility: Int) {
        mLeftImageView?.visibility = visibility
        mLeftImageVisibility = visibility
    }

    fun setLeftImage2Visibility(visibility: Int) {
        mLeftImageView2?.visibility = visibility
        mLeftImage2Visibility = visibility
    }

    /**
     * 设置右边是否可见
     */
    fun setRightVisibility(visibility: Int) {
        setRightTextVisibility(visibility)
        setRightImageVisibility(visibility)
        setRightImage2Visibility(visibility)
    }

    fun setRightTextVisibility(visibility: Int) {
        mRightTextView?.visibility = visibility
        mRightTextVisibility = visibility
    }

    fun setRightImageVisibility(visibility: Int) {
        mRightImageView?.visibility = visibility
        mRightImageVisibility = visibility
    }

    fun setRightImage2Visibility(visibility: Int) {
        mRightImageView2?.visibility = visibility
        mRightImage2Visibility = visibility
    }

    /**
     * 计算Title 想要的宽度
     */
    private fun computeTitleDesireWidth() {
        if (mTitleView == null) {
            mTitleDesireWidth = 0
            return
        }
        mTitleDesireWidth = Layout.getDesiredWidth(mTitleView!!.text, mTitleView!!.paint).toInt()
    }


    /**
     * 计算SubTitle 想要的宽度
     */
    private fun computeSubTitleDesireWidth() {
        if (mSubTitleView == null) {
            mSubTitleDesireWidth = 0
            return
        }
        mSubTitleDesireWidth = Layout.getDesiredWidth(mSubTitleView!!.text, mSubTitleView!!.paint).toInt()
    }

    private fun setDefaultPromptState(prompt: IPrompt?) {
        if (prompt == null) {
            Log.i(TAG, "setDefaultPromptState: ")
            return
        }
        if (prompt is PromptTextView) {
            prompt.setPromptWidthPaddingScale(DEFAULT_TEXT_WIDTH_PROMPT_SCALE)
            prompt.setPromptHeightPaddingScale(DEFAULT_TEXT_HEIGHT_PROMPT_SCALE)
        } else {
            prompt.setPromptWidthPaddingScale(DEFAULT_IMAGE_WIDTH_PROMPT_SCALE)
            prompt.setPromptHeightPaddingScale(DEFAULT_IMAGE_HEIGHT_PROMPT_SCALE)
        }
        prompt.setPromptMode(IPrompt.PromptMode.NONE)
        prompt.commit()
    }


    /**
     * 添加监听
     */
    fun setOnTopBarListener(listener: OnTopBarListener) {
        mTopBarListener = listener
    }


    /**
     * 点击监听
     */
    interface OnTopBarListener {
        /**
         * 点击了左边
         */
        fun onLeftClick(v: View)

        /**
         * 点击了右边
         */
        fun onRightClick(v: View)

        /**
         * 点击了标题
         */
        fun onTitleClick(v: View)
    }

    companion object {

        private const val TAG = "TopBar"
        /**
         * 单位宽度和高度的比率
         */
        private const val UNIT_WIDTH_RADIO = 0.88f

        /**
         * 提示信息
         */
        private const val DEFAULT_IMAGE_WIDTH_PROMPT_SCALE = 0.17f
        private const val DEFAULT_IMAGE_HEIGHT_PROMPT_SCALE = 0.19f
        private const val DEFAULT_TEXT_WIDTH_PROMPT_SCALE = 0.14f
        private const val DEFAULT_TEXT_HEIGHT_PROMPT_SCALE = 0.25f
    }
}