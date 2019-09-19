package com.ishow.common.widget.pulltorefresh

import android.animation.Animator
import android.animation.ValueAnimator

/**
 * Created by yuhaiyang on 2017/9/20.
 * 动画监听
 */

abstract class AbsAnimatorListener : Animator.AnimatorListener {

    abstract fun onAnimationUpdate(animation: ValueAnimator)

    override fun onAnimationStart(animation: Animator) {}

    override fun onAnimationCancel(animation: Animator) {}

    override fun onAnimationRepeat(animation: Animator) {}
}
