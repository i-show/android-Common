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
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.ishow.common.R
import com.ishow.common.utils.log.LogUtils
import kotlinx.android.synthetic.main.widget_status_view.view.*

/**
 * 一个状态显示的View
 */
class StatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    var loadingText: String?
    var loadingTextColor: ColorStateList? = null
    var loadingTextSize: Int

    @Suppress("MemberVisibilityCanBePrivate")
    var emptyDrawableId: Int
    var emptyText: String?
    var emptyTextColor: ColorStateList? = null
    var emptyTextSize: Int
    var emptySubText: String?
    var emptySubTextColor: ColorStateList? = null
    var emptySubTextSize: Int

    @Suppress("MemberVisibilityCanBePrivate")
    var errorDrawableId: Int
    var errorText: String?
    var errorTextColor: ColorStateList? = null
    var errorTextSize: Int

    var errorSubText: String?
    var errorSubTextColor: ColorStateList? = null
    var errorSubTextSize: Int

    @Suppress("MemberVisibilityCanBePrivate")
    var reloadTextBackground: Drawable?
    var reloadText: String?
    var reloadTextColor: ColorStateList? = null
    var reloadTextSize: Int

    private val isInterruptTouchEvent: Boolean
    private val isTitleClickable: Boolean
    private val isSubTitleClickable: Boolean

    private var topWeight: Float = 0.toFloat()
    private var bottomWeight: Float = 0.toFloat()

    private var statusViewListener: OnStatusViewListener? = null
    private var statusViewBlock: ((v: View, which: Which) -> Unit)? = null

    private var loadingTagList: MutableList<String>? = null
    private var hasError: Boolean = false
    /**
     * 默认字体大小
     */
    private val defaultTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.H_title)

    /**
     * 默认字体颜色
     */
    private val defaultTextColor: ColorStateList?
        get() = ContextCompat.getColorStateList(context, R.color.text_grey_light)

    /**
     * 默认副标题字体大小
     */
    private val defaultSubTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.J_title)

    /**
     * 默认副标题字体颜色
     */
    private val defaultSubTextColor: ColorStateList?
        get() = ContextCompat.getColorStateList(context, R.color.text_grey_light_more)

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
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.StatusView,
            R.attr.statusStyle,
            R.style.Default_StatusView
        )

        topWeight = a.getFloat(R.styleable.StatusView_topWeight, 2.6F)
        bottomWeight = a.getFloat(R.styleable.StatusView_bottomWeight, 5F)

        loadingText = a.getString(R.styleable.StatusView_loadingText)
        loadingTextColor = a.getColorStateList(R.styleable.StatusView_loadingTextColor)
        loadingTextSize = a.getDimensionPixelSize(R.styleable.StatusView_loadingTextSize, defaultTextSize)

        emptyDrawableId = a.getResourceId(R.styleable.StatusView_emptyImage, -1)
        emptyText = a.getString(R.styleable.StatusView_emptyText)
        emptyTextColor = a.getColorStateList(R.styleable.StatusView_emptyTextColor)
        emptyTextSize = a.getDimensionPixelSize(R.styleable.StatusView_emptyTextSize, defaultTextSize)
        emptySubText = a.getString(R.styleable.StatusView_emptySubText)
        emptySubTextColor = a.getColorStateList(R.styleable.StatusView_emptySubTextColor)
        emptySubTextSize = a.getDimensionPixelSize(R.styleable.StatusView_emptySubTextSize, defaultSubTextSize)

        errorDrawableId = a.getResourceId(R.styleable.StatusView_errorImage, -1)
        errorText = a.getString(R.styleable.StatusView_errorText)
        errorTextColor = a.getColorStateList(R.styleable.StatusView_errorTextColor)
        errorTextSize = a.getDimensionPixelSize(R.styleable.StatusView_errorTextSize, defaultTextSize)
        errorSubText = a.getString(R.styleable.StatusView_errorSubText)
        errorSubTextColor = a.getColorStateList(R.styleable.StatusView_errorSubTextColor)
        errorSubTextSize = a.getDimensionPixelSize(R.styleable.StatusView_errorSubTextSize, defaultSubTextSize)

        reloadText = a.getString(R.styleable.StatusView_reloadText)
        reloadTextColor = a.getColorStateList(R.styleable.StatusView_reloadTextColor)
        reloadTextSize = a.getDimensionPixelSize(R.styleable.StatusView_reloadTextSize, defaultReloadTextSize)
        reloadTextBackground = a.getDrawable(R.styleable.StatusView_reloadBackground)

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
        setWeight(topWeight, bottomWeight)
    }

    private fun checkParams() {
        if (loadingTextColor == null) loadingTextColor = defaultSubTextColor

        if (errorTextColor == null) errorTextColor = defaultTextColor
        if (errorSubTextColor == null) errorSubTextColor = defaultSubTextColor

        if (emptyTextColor == null) emptyTextColor = defaultTextColor
        if (emptySubTextColor == null) emptySubTextColor = defaultSubTextColor

        if (reloadTextColor == null) reloadTextColor = defaultReloadTextColor
    }


    fun showError() {
        hasError = true
        if (loadingTagList != null && loadingTagList!!.isNotEmpty()) {
            return
        }

        visibility = View.VISIBLE
        imageView.setImageResource(errorDrawableId)
        imageView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        loadingView.visibility = View.GONE

        titleView.setTextColor(errorTextColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTextSize.toFloat())
        setText(titleView, errorText)
        subTitleView.setTextColor(errorSubTextColor)
        subTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorSubTextSize.toFloat())
        setText(subTitleView, errorSubText)
        reloadButton.setTextColor(reloadTextColor)
        reloadButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, reloadTextSize.toFloat())
        reloadButton.background = reloadTextBackground
        setText(reloadButton, reloadText)
    }

    fun showLoading(loadingTag: String? = null) {
        loadingTag?.let {
            if (loadingTagList == null) {
                loadingTagList = mutableListOf()
            }
            loadingTagList?.add(loadingTag)
        }

        visibility = View.VISIBLE
        imageView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE

        titleView.setTextColor(loadingTextColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, loadingTextSize.toFloat())
        setText(titleView, loadingText)

        subTitleView.visibility = View.GONE
        reloadButton.visibility = View.GONE
    }

    fun showEmpty() {
        visibility = View.VISIBLE
        imageView.setImageResource(emptyDrawableId)
        imageView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE

        titleView.setTextColor(emptyTextColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, emptyTextSize.toFloat())
        setText(titleView, emptyText)

        subTitleView.setTextColor(emptySubTextColor)
        subTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, emptySubTextSize.toFloat())
        setText(subTitleView, emptySubText)

        reloadButton.visibility = View.GONE
    }

    fun dismiss(loadingTag: String? = null) {
        loadingTagList?.remove(loadingTag)
        if (loadingTag == null || loadingTagList.isNullOrEmpty()) {
            if (hasError) {
                showError()
            } else {
                visibility = View.GONE
            }
        }
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
        statusViewListener = callBack
    }

    @Suppress("unused")
    fun setOnStatusViewBlock(block: ((v: View, which: Which) -> Unit)?) {
        statusViewBlock = block
    }

    private fun notifyClickTitle() {
        statusViewListener?.onStatusClick(this, Which.Title)
        statusViewBlock?.let { it(this, Which.Title) }
    }

    private fun notifyClickSubTitle() {
        statusViewListener?.onStatusClick(this, Which.SubTitle)
        statusViewBlock?.let { it(this, Which.SubTitle) }
    }

    private fun notifyReload() {
        statusViewListener?.onStatusClick(this, Which.Reload)
        statusViewBlock?.let { it(this, Which.Reload) }
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
        this.topWeight = topWeight
        updateWeight(topWeightView, this.topWeight)
        this.bottomWeight = bottomWeight
        updateWeight(bottomWeightView, this.bottomWeight)
    }


    interface OnStatusViewListener {
        /**
         * 点击了副标题标题
         */
        fun onStatusClick(v: View, which: Which)
    }

    companion object {
        private const val TAG = "StatusView"
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


        /**
         * 添加到attachView
         */
        fun attachToActivity(activity: Activity?, topMargin: Int, color: Int = Color.WHITE): StatusView? {
            if (activity == null || activity.isFinishing) {
                return null
            }
            val view: StatusView? = activity.findViewById(R.id.statusView)
            if (view != null) {
                LogUtils.i(TAG, "already add")
                return view
            }

            val statusView = StatusView(activity)
            statusView.setBackgroundColor(color)
            statusView.id = R.id.statusView
            val root = activity.findViewById<ViewGroup>(android.R.id.content)

            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            layoutParams.topMargin = topMargin
            root.addView(statusView, layoutParams)
            return statusView
        }


        /**
         * 添加到attachView
         */
        fun attach(view: ViewGroup, topMargin: Int, color: Int = Color.WHITE): StatusView? {
            val status: StatusView? = view.findViewById(R.id.statusView)
            if (status != null) {
                LogUtils.i(TAG, "already add")
                return status
            }

            val statusView = StatusView(view.context)
            statusView.setBackgroundColor(color)
            statusView.id = R.id.statusView

            val layoutParams =
                MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParams.topMargin = topMargin
            view.addView(statusView, layoutParams)
            return statusView
        }

        fun detach(view: ViewGroup) {
            val status: StatusView? = view.findViewById(R.id.statusView)
            status?.let { view.removeView(it) }
        }
    }
}
