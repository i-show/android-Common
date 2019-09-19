package com.ishow.common.widget.pulltorefresh.headers.classic

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat

import com.ishow.common.R
import com.ishow.common.widget.pulltorefresh.AbsAnimatorListener
import com.ishow.common.widget.pulltorefresh.headers.IPullToRefreshHeader
import com.ishow.common.widget.pulltorefresh.utils.ViewHelper
import kotlin.math.abs


/**
 * Created by Bright.Yu on 2017/3/22.
 * 经典下拉刷新
 */

class ClassicHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    LinearLayout(context, attrs, defStyle), IPullToRefreshHeader {
    /**
     * 当前状态
     */
    override var status: Int = 0
        set(@IPullToRefreshHeader.HeaderStatus status) {
            when (status) {
                IPullToRefreshHeader.STATUS_NORMAL -> {
                    iconView.setImageResource(R.drawable.ic_pull2refresh_arrow)
                    textView.setText(R.string.pull2refresh_header_normal)
                    if (this.status == IPullToRefreshHeader.STATUS_READY) {
                        iconView.startAnimation(rotateDownAnim)
                    }

                    if (this.status == IPullToRefreshHeader.STATUS_REFRESHING) {
                        iconView.clearAnimation()
                    }
                }
                IPullToRefreshHeader.STATUS_READY -> {
                    if (this.status != IPullToRefreshHeader.STATUS_READY) {
                        iconView.clearAnimation()
                        iconView.startAnimation(rotateUpAnim)
                    }
                    iconView.setImageResource(R.drawable.ic_pull2refresh_arrow)
                    textView.setText(R.string.pull2refresh_header_ready)
                }
                IPullToRefreshHeader.STATUS_REFRESHING -> {
                    iconView.clearAnimation()
                    iconView.setImageResource(R.drawable.ic_pull2refresh_loading)
                    iconView.startAnimation(rotateLoading)
                    textView.setText(R.string.pull2refresh_header_loading)
                    requestLayout()
                }
                IPullToRefreshHeader.STATUS_FAILED -> {
                    iconView.clearAnimation()
                    iconView.visibility = View.VISIBLE
                    iconView.setImageResource(R.drawable.ic_pull2refresh_refresh_fail)
                    textView.setText(R.string.pull2refresh_header_fail)
                }
                IPullToRefreshHeader.STATUS_SUCCESS -> {
                    iconView.clearAnimation()
                    iconView.visibility = View.VISIBLE
                    iconView.setImageResource(R.drawable.ic_pull2refresh_refresh_success)
                    textView.setText(R.string.pull2refresh_header_success)
                }
            }
            field = status
        }
    /**
     * Icon
     */
    private lateinit var iconView: ImageView
    /**
     * Text
     */
    private lateinit var textView: TextView
    /**
     * 进度条
     */
    private val rotateUpAnim: RotateAnimation
    private val rotateDownAnim: RotateAnimation
    private val rotateLoading: RotateAnimation

    override val view: View
        get() = this

    override val movingDistance: Int
        get() = abs(bottom)

    override val maxPullDownHeight: Int
        get() = measuredHeight * 3

    override val headerHeight: Int
        get() = measuredHeight

    override val isEffectiveDistance: Boolean
        get() = abs(bottom) > headerHeight


    private val imageSize: Int
        get() = context.resources.getDimensionPixelOffset(R.dimen.pull2refresh_classic_header_image_size)

    init {
        gravity = Gravity.CENTER
        orientation = HORIZONTAL
        minimumHeight = context.resources.getDimensionPixelOffset(R.dimen.pull2refresh_classic_header_min_h)

        rotateUpAnim = getAnimation(0.0F, -180.0F)
        rotateUpAnim.duration = ROTATE_ANIM_DURATION.toLong()
        rotateUpAnim.fillAfter = true

        rotateDownAnim = getAnimation(-180.0F, 0.0F)
        rotateDownAnim.duration = ROTATE_ANIM_DURATION.toLong()
        rotateDownAnim.fillAfter = true

        rotateLoading = getAnimation(0.0F, 360.0F)
        rotateLoading.duration = (ROTATE_ANIM_DURATION * 2).toLong()
        rotateLoading.repeatCount = Animation.INFINITE
        rotateLoading.fillAfter = false

        addIcon()
        addText()
    }

    override fun moving(parent: ViewGroup, offset: Int): Int {
        return if (abs(bottom) >= maxPullDownHeight && offset < 0) {
            0
        } else if (top - offset < -headerHeight) {
            val adjust = headerHeight + top
            ViewCompat.offsetTopAndBottom(this, -adjust)
            -adjust
        } else {
            ViewCompat.offsetTopAndBottom(this, -offset)
            -offset
        }
    }

    override fun refreshing(parent: ViewGroup, listener: AbsAnimatorListener?): Int {
        val offset = -top
        ViewHelper.movingY(this, offset, listener)
        return offset
    }

    override fun cancelRefresh(parent: ViewGroup): Int {
        val offset = -top - headerHeight
        ViewHelper.movingY(this, offset)
        return offset
    }

    override fun refreshSuccess(parent: ViewGroup): Int {
        val offset = -headerHeight
        ViewHelper.movingY(this, offset)
        return offset
    }

    override fun refreshFailed(parent: ViewGroup): Int {
        val offset = -headerHeight
        ViewHelper.movingY(this, offset)
        return offset
    }


    private fun addIcon() {
        val size = imageSize
        iconView = ImageView(context)
        iconView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        val lp = LayoutParams(size, size)
        iconView.setImageResource(R.drawable.ic_pull2refresh_arrow)
        addView(iconView, lp)
    }


    private fun addText() {
        textView = AppCompatTextView(context)
        val lp = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.leftMargin = 15
        textView.setText(R.string.pull2refresh_header_normal)
        addView(textView, lp)
    }

    private fun getAnimation(fromDegrees: Float, toDegrees: Float): RotateAnimation {
        return RotateAnimation(
            fromDegrees,
            toDegrees,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
    }

    companion object {
        private const val ROTATE_ANIM_DURATION = 380
    }
}
