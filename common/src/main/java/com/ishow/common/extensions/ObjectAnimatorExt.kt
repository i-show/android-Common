package com.ishow.common.extensions

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator


/**
 * 横向移动方向动画
 * @param from 动画开始值
 * @param to 动画结束值
 * @param duration 动画持续时长
 */
fun ObjectAnimator.translationX(from: Float, to: Float, duration: Long): ObjectAnimator {
    setProperty(View.TRANSLATION_X)
    setFloatValues(from, to)
    setDuration(duration)
    return this
}

/**
 * 竖直方向动画
 * @param from 动画开始值
 * @param to 动画结束值
 * @param duration 动画持续时长
 */
fun ObjectAnimator.translationY(from: Float, to: Float, duration: Long): ObjectAnimator {
    setProperty(View.TRANSLATION_Y)
    setFloatValues(from, to)
    setDuration(duration)
    return this
}

/**
 * Rotation
 * @param from 动画开始值
 * @param to 动画结束值
 * @param duration 动画持续时长
 */
fun ObjectAnimator.rotation(from: Float, to: Float, duration: Long): ObjectAnimator {
    setProperty(View.ROTATION)
    setFloatValues(from, to)
    interpolator = LinearInterpolator()
    setDuration(duration)
    return this
}

/**
 * 左右抖动
 * @param offset 左右抖动的距离
 * @param times [ObjectAnimator.getDuration] 间隔的时间段内可以做几次动画
 */
fun ObjectAnimator.shakeX(offset: Float = 10F, times: Float = 5.0F): ObjectAnimator {
    setProperty(View.TRANSLATION_X)
    setFloatValues(0F, offset)
    duration = 1000L
    interpolator = CycleInterpolator(times)
    return this
}

/**
 * 上下抖动
 * @param offset 上下抖动的距离
 * @param times [ObjectAnimator.getDuration] 间隔的时间段内可以做几次动画
 */
fun ObjectAnimator.shakeY(offset: Float = 10F, times: Float = 5.0F): ObjectAnimator {
    setProperty(View.TRANSLATION_Y)
    setFloatValues(0F, offset)
    duration = 1000L
    interpolator = CycleInterpolator(times)
    return this
}

/**
 * 呼吸灯效果
 * @param from 透明度初始值
 * @param to 透明度结束之
 * @param duration 持续时长
 */
fun ObjectAnimator.breath(from: Float = 0.0F, to: Float = 1.0F, duration: Long = 1000): ObjectAnimator {
    setProperty(View.ALPHA)
    setFloatValues(from, to)
    setDuration(duration)
    repeatMode = ValueAnimator.REVERSE
    repeatCount = ValueAnimator.INFINITE
    interpolator = AccelerateDecelerateInterpolator()
    return this
}

/**
 * 一直重复
 */
fun ObjectAnimator.repeatForever(): ObjectAnimator {
    repeatCount = ValueAnimator.INFINITE
    return this
}
