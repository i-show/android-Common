package com.ishow.common.widget.pulltorefresh.headers.google

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

import com.ishow.common.utils.UnitUtils
import com.ishow.common.widget.pulltorefresh.AbsAnimatorListener
import com.ishow.common.widget.pulltorefresh.headers.IPullToRefreshHeader
import com.ishow.common.widget.pulltorefresh.utils.ViewHelper
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class GoogleStyleHeader(context: Context) : LinearLayout(context), IPullToRefreshHeader {

    private lateinit var progressView: CircularProgressDrawable
    /**
     * 当前状态
     */
    override var status: Int = 0
        set(status) {
            if (status == IPullToRefreshHeader.STATUS_NORMAL &&
                this.status != IPullToRefreshHeader.STATUS_NORMAL &&
                this.status != IPullToRefreshHeader.STATUS_READY
            ) {
                progressView.stop()
            }
            field = status
        }
    override var maxPullDownHeight: Int = 0

    override val view: View
        get() = this

    override val movingDistance: Int
        get() = abs(bottom)

    override val headerHeight: Int
        get() = measuredHeight

    override val isEffectiveDistance: Boolean
        get() = abs(bottom) > headerHeight

    init {
        init()
    }

    private fun init() {
        val padding = UnitUtils.dip2px(8)
        gravity = Gravity.CENTER
        val size = UnitUtils.dip2px(40)
        maxPullDownHeight = size * 4
        val lp = LayoutParams(size, size)
        lp.topMargin = padding
        lp.bottomMargin = padding

        progressView = CircularProgressDrawable(context)
        progressView.setStyle(CircularProgressDrawable.DEFAULT)
        progressView.setColorSchemeColors(
            ContextCompat.getColor(context, android.R.color.holo_blue_bright),
            ContextCompat.getColor(context, android.R.color.holo_green_light),
            ContextCompat.getColor(context, android.R.color.holo_orange_light),
            ContextCompat.getColor(context, android.R.color.holo_red_light)
        )

        val imageView = CircleImageView(context, CIRCLE_BG_LIGHT)
        imageView.setImageDrawable(progressView)

        addView(imageView, lp)
    }

    override fun moving(parent: ViewGroup, offset: Int): Int {
        val moving = bottom.toFloat()
        val height = measuredHeight
        val originalDragPercent = max(moving - height, 0f) / (maxPullDownHeight - height)

        val dragPercent = min(1f, abs(originalDragPercent))
        val strokeStart = dragPercent * .8f
        progressView.arrowEnabled = true
        progressView.setStartEndTrim(0f, min(MAX_PROGRESS_ANGLE, strokeStart))
        progressView.arrowScale = min(1f, dragPercent)

        if (abs(moving) >= maxPullDownHeight && offset < 0) {
            return 0
        } else if (top - offset < -headerHeight) {
            val adjust = headerHeight + top
            ViewCompat.offsetTopAndBottom(this, -adjust)
        } else {
            ViewCompat.offsetTopAndBottom(this, -offset)
        }
        return 0
    }

    override fun refreshing(parent: ViewGroup, listener: AbsAnimatorListener?): Int {
        val offset = -top
        ViewHelper.movingY(this, offset, listener)
        progressView.start()
        return 0
    }

    override fun cancelRefresh(parent: ViewGroup): Int {
        val offset = -top - headerHeight
        ViewHelper.movingY(this, offset)
        return 0
    }

    override fun refreshSuccess(parent: ViewGroup): Int {
        val offset = -headerHeight
        ViewHelper.movingY(this, offset)
        return 0
    }

    override fun refreshFailed(parent: ViewGroup): Int {
        val offset = -headerHeight
        ViewHelper.movingY(this, offset)
        return 0
    }

    /**
     * 设置颜色样式
     */
    fun setColorSchemeColors(@ColorInt vararg colors: Int) {
        progressView.setColorSchemeColors(*colors)
    }

    /**
     * 设置颜色样式
     */
    fun setColorSchemeResources(@ColorRes vararg colorResIds: Int) {
        val context = context
        val colorRes = IntArray(colorResIds.size)
        for (i in colorResIds.indices) {
            colorRes[i] = ContextCompat.getColor(context, colorResIds[i])
        }
        setColorSchemeColors(*colorRes)
    }

    companion object {

        // Default background for the progress spinner
        private const val CIRCLE_BG_LIGHT = -0x50506
        // where 1.0 is a full circle
        private const val MAX_PROGRESS_ANGLE = 0.8f
    }
}
