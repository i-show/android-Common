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

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.ishow.common.R
import kotlinx.android.synthetic.main.widget_status_view.view.*

/**
 * 一个状态显示的View
 */
class StatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val mLoadingText: String?
    private var mLoadingTextColor: ColorStateList? = null
    private val mLoadingTextSize: Int

    private val mEmptyDrawableId: Int
    private val mEmptyText: String?
    private var mEmptyTextColor: ColorStateList? = null
    private val mEmptyTextSize: Int
    private val mEmptySubText: String?
    private var mEmptySubTextColor: ColorStateList? = null
    private val mEmptySubTextSize: Int

    private val mErrorDrawableId: Int
    private val mErrorText: String?
    private var mErrorTextColor: ColorStateList? = null
    private val mErrorTextSize: Int

    private val mErrorSubText: String?
    private var mErrorSubTextColor: ColorStateList? = null
    private val mErrorSubTextSize: Int

    private val mReloadTextBackground: Drawable?
    private val mReloadText: String?
    private var mReloadTextColor: ColorStateList? = null
    private val mReloadTextSize: Int

    private val isInterruptTouchEvent: Boolean
    private val isTitleClickable: Boolean
    private val isSubTitleClickable: Boolean

    private var mTopWeight: Float = 0.toFloat()

    private var mBottomWeight: Float = 0.toFloat()
    private var mOnStatusViewListener: OnStatusViewListener? = null

    /**
     * 默认字体大小
     */
    private val defaultTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.H_title)

    /**
     * 默认字体颜色
     */
    private val defaultTextColor: ColorStateList?
        get() = ContextCompat.getColorStateList(context, R.color.text_grey)

    /**
     * 默认副标题字体大小
     */
    private val defaultSubTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.J_title)

    /**
     * 默认副标题字体颜色
     */
    private val defaultSubTextColor: ColorStateList?
        get() = ContextCompat.getColorStateList(context, R.color.text_grey_light)

    /**
     * 默认重新加载字体大小
     */
    private val defaultReloadTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.H_title)

    /**
     * 默认重新加载字体颜色
     */
    private val defaultReloadTextColor: ColorStateList?
        get() = ContextCompat.getColorStateList(context, R.color.text_grey)

    enum class Which {
        Title, SubTitle, Reload
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StatusView, R.attr.statusStyle, R.style.Default_StatusView)

        mTopWeight = a.getFloat(R.styleable.StatusView_topWeight, 2.6F)
        mBottomWeight = a.getFloat(R.styleable.StatusView_bottomWeight, 5F)

        mLoadingText = a.getString(R.styleable.StatusView_loadingText)
        mLoadingTextColor = a.getColorStateList(R.styleable.StatusView_loadingTextColor)
        mLoadingTextSize = a.getDimensionPixelSize(R.styleable.StatusView_loadingTextSize, defaultTextSize)

        mEmptyDrawableId = a.getResourceId(R.styleable.StatusView_emptyImage, -1)
        mEmptyText = a.getString(R.styleable.StatusView_emptyText)
        mEmptyTextColor = a.getColorStateList(R.styleable.StatusView_emptyTextColor)
        mEmptyTextSize = a.getDimensionPixelSize(R.styleable.StatusView_emptyTextSize, defaultTextSize)
        mEmptySubText = a.getString(R.styleable.StatusView_emptySubText)
        mEmptySubTextColor = a.getColorStateList(R.styleable.StatusView_emptySubTextColor)
        mEmptySubTextSize = a.getDimensionPixelSize(R.styleable.StatusView_emptySubTextSize, defaultSubTextSize)

        mErrorDrawableId = a.getResourceId(R.styleable.StatusView_errorImage, -1)
        mErrorText = a.getString(R.styleable.StatusView_errorText)
        mErrorTextColor = a.getColorStateList(R.styleable.StatusView_errorTextColor)
        mErrorTextSize = a.getDimensionPixelSize(R.styleable.StatusView_errorTextSize, defaultTextSize)
        mErrorSubText = a.getString(R.styleable.StatusView_errorSubText)
        mErrorSubTextColor = a.getColorStateList(R.styleable.StatusView_errorSubTextColor)
        mErrorSubTextSize = a.getDimensionPixelSize(R.styleable.StatusView_errorSubTextSize, defaultSubTextSize)

        mReloadText = a.getString(R.styleable.StatusView_reloadText)
        mReloadTextColor = a.getColorStateList(R.styleable.StatusView_reloadTextColor)
        mReloadTextSize = a.getDimensionPixelSize(R.styleable.StatusView_reloadTextSize, defaultReloadTextSize)
        mReloadTextBackground = a.getDrawable(R.styleable.StatusView_reloadBackground)

        isInterruptTouchEvent = a.getBoolean(R.styleable.StatusView_interruptTouchEvent, true)
        isTitleClickable = a.getBoolean(R.styleable.StatusView_titleClickable, false)
        isSubTitleClickable = a.getBoolean(R.styleable.StatusView_subTitleClickable, false)

        a.recycle()
        checkParams()
        initView()
    }


    private fun initView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.widget_status_view, this, true)
        setTitleClickable(isTitleClickable)
        setSubTitleClickable(isSubTitleClickable)
        reloadButton.setOnClickListener(this)
        setWeight(mTopWeight, mBottomWeight)
    }

    private fun checkParams() {
        if (mLoadingTextColor == null) mLoadingTextColor = defaultSubTextColor

        if (mErrorTextColor == null) mErrorTextColor = defaultTextColor
        if (mErrorSubTextColor == null) mErrorSubTextColor = defaultSubTextColor

        if (mEmptyTextColor == null) mEmptyTextColor = defaultTextColor
        if (mEmptySubTextColor == null) mEmptySubTextColor = defaultSubTextColor

        if (mReloadTextColor == null) mReloadTextColor = defaultReloadTextColor
    }

    fun showError(@StringRes title: Int, @DrawableRes drawable: Int) {
        val context = context
        val titleString = context.getString(title)
        showError(mReloadText, titleString, mErrorSubText, drawable)
    }

    fun showError(@StringRes title: Int, @StringRes subTitle: Int, @DrawableRes icon: Int) {
        val context = context
        val titleString = context.getString(title)
        val subTitleString = context.getString(subTitle)
        showError(mReloadText, titleString, subTitleString, icon)
    }

    fun showError(@StringRes reload: Int, @StringRes title: Int, @StringRes subTitle: Int, icon: Int) {
        val context = context
        val reloadString = context.getString(reload)
        val titleString = context.getString(title)
        val subTitleString = context.getString(subTitle)
        showError(reloadString, titleString, subTitleString, icon)
    }

    @JvmOverloads
    fun showError(reload: String? = mReloadText, title: String? = mErrorText, subTitle: String? = mErrorSubText, icon: Int = mErrorDrawableId) {
        visibility = View.VISIBLE
        imageView.setImageResource(icon)
        imageView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        loadingView.visibility = View.GONE

        titleView.setTextColor(mErrorTextColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mErrorTextSize.toFloat())
        setText(titleView, title)
        subTitleView.setTextColor(mErrorSubTextColor)
        subTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mErrorSubTextSize.toFloat())
        setText(subTitleView, subTitle)
        reloadButton.setTextColor(mReloadTextColor)
        reloadButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mReloadTextSize.toFloat())
        reloadButton.background = mReloadTextBackground
        setText(reloadButton, reload)
    }

    fun showLoading(@StringRes text: Int) {
        showLoading(context.getString(text))
    }

    @JvmOverloads
    fun showLoading(title: String? = mLoadingText) {
        visibility = View.VISIBLE
        imageView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE

        titleView.setTextColor(mLoadingTextColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadingTextSize.toFloat())
        setText(titleView, title)

        subTitleView.visibility = View.GONE
        reloadButton.visibility = View.GONE
    }

    fun showEmpty(@StringRes text: Int, @DrawableRes icon: Int) {
        val title = context.getString(text)
        showEmpty(title, mEmptySubText, icon)
    }

    fun showEmpty(@StringRes text: Int, @StringRes subText: Int, @DrawableRes icon: Int) {
        val title = context.getString(text)
        val subTitle = context.getString(subText)
        showEmpty(title, subTitle, icon)
    }

    @JvmOverloads
    fun showEmpty(title: String? = mEmptyText, subTitle: String? = mEmptySubText, @DrawableRes icon: Int = mEmptyDrawableId) {
        visibility = View.VISIBLE
        imageView.setImageResource(icon)
        imageView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE

        titleView.setTextColor(mEmptyTextColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEmptyTextSize.toFloat())
        setText(titleView, title)

        subTitleView.setTextColor(mEmptySubTextColor)
        subTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEmptySubTextSize.toFloat())
        setText(subTitleView, subTitle)

        reloadButton.visibility = View.GONE
    }

    fun dismiss() {
        visibility = View.GONE
    }

    private fun setText(view: TextView?, text: String?) {
        if (TextUtils.isEmpty(text)) {
            view?.visibility = View.GONE
        } else {
            view?.visibility = View.VISIBLE
        }
        view?.text = text
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isInterruptTouchEvent) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.titleView -> notifyClickTitle()
            R.id.subTitleView -> notifyClickSubTitle()
            R.id.reloadButton -> notifyReload()
        }
    }

    fun setOnStatusViewListener(callBack: OnStatusViewListener) {
        mOnStatusViewListener = callBack
    }

    private fun notifyClickTitle() {
        mOnStatusViewListener?.onStatusClick(this, Which.Title)
    }

    private fun notifyClickSubTitle() {
        mOnStatusViewListener?.onStatusClick(this, Which.SubTitle)
    }

    private fun notifyReload() {
        mOnStatusViewListener?.onStatusClick(this, Which.Reload)
    }

    fun setTitleClickable(clickable: Boolean) {
        titleView.setOnClickListener(if (clickable) this else null)
    }

    fun setSubTitleClickable(clickable: Boolean) {
        subTitleView.setOnClickListener(if (clickable) this else null)
    }

    private fun updateWeight(view: View?, weight: Float) {
        view?.let {
            val lp = it.layoutParams
            if (lp is LinearLayout.LayoutParams) {
                lp.weight = weight
            }
        }
    }

    fun setWeight(@FloatRange(from = 0.0) topWeight: Float, @FloatRange(from = 0.0) bottomWeight: Float) {
        mTopWeight = topWeight
        updateWeight(topWeightView, mTopWeight)
        mBottomWeight = bottomWeight
        updateWeight(bottomWeightView, mBottomWeight)
    }

    interface OnStatusViewListener {
        /**
         * 点击了副标题标题
         */
        fun onStatusClick(v: View, which: Which)
    }

    companion object {
        /**
         * 正在加载
         */
        const val STATUS_LOADING = 1 shl 1
        /**
         * 加载失败
         */
        const val STATUS_ERROR = STATUS_LOADING + 1
        /**
         * 加载为空
         */
        const val STATUS_EMPTY = STATUS_LOADING + 2
    }
}
