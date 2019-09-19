package com.ishow.common.widget.pulltorefresh.utils

import android.animation.ValueAnimator
import androidx.annotation.IntRange
import androidx.core.view.ViewCompat

import android.view.View

import com.ishow.common.R
import com.ishow.common.widget.pulltorefresh.AbsAnimatorListener
import kotlin.math.abs
import kotlin.math.max


/**
 * Created by Bright.Yu on 2017/3/27.
 * View Helper
 */

object ViewHelper {
    /**
     * 每1000 像素 需要移动的时间
     */
    private const val DEFAULT_1000_PIXELS_TIMES = 1200

    /**
     * View 进行移动
     */
    @JvmStatic
    fun movingY(view: View, distance: Int) {
        val duration = calculatingDuration(distance)
        movingY(view, duration, distance, null)
    }

    @JvmStatic
    fun movingY(view: View, distance: Int, listener: AbsAnimatorListener?) {
        val duration = calculatingDuration(distance)
        movingY(view, duration, distance, listener)
    }

    @JvmStatic
    fun movingY(view: View, @IntRange(from = 1) duration: Int, distance: Int, listener: AbsAnimatorListener?) {
        view.clearAnimation()

        val animator = ValueAnimator.ofInt(distance)
        animator.setTarget(view)
        animator.duration = duration.toLong()
        if (listener != null) {
            animator.addListener(listener)
        }
        animator.start()
        view.setTag(R.id.tag_pull2refresh_moving_y, 0)
        view.setTag(R.id.tag_pull2refresh_animation, animator)

        animator.addUpdateListener { animation ->
            val last = view.getTag(R.id.tag_pull2refresh_moving_y) as Int
            val move = animation.animatedValue as Int
            val offset = move - last
            view.setTag(R.id.tag_pull2refresh_moving_y, move)
            ViewCompat.offsetTopAndBottom(view, offset)
            listener?.onAnimationUpdate(animation)
        }
    }

    /**
     * 计算移动动画时长
     */
    private fun calculatingDuration(distance: Int): Int {
        val desTime = DEFAULT_1000_PIXELS_TIMES * abs(distance) / 1000
        return max(desTime, 500)
    }
}
