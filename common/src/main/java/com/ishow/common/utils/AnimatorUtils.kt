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

package com.ishow.common.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator

object AnimatorUtils {
    /**
     * alpha
     */
    @JvmStatic
    @JvmOverloads
    fun alpha(v: View, from: Float, to: Float, duration: Long, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.ALPHA, from, to)
        animator.duration = duration
        listener?.let {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * translation x
     */
    @JvmStatic
    @JvmOverloads
    fun translationX(v: View, from: Float, to: Float, duration: Long, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, from, to)
        animator.duration = duration
        listener?.let {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * translation y
     */
    @JvmStatic
    @JvmOverloads
    fun translationY(v: View, from: Float, to: Float, duration: Long, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, from, to)
        animator.duration = duration
        listener?.let {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * rotation
     */
    @JvmStatic
    @JvmOverloads
    fun rotation(v: View, from: Float, to: Float, duration: Long, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.ROTATION, from, to)
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.duration = duration
        listener?.let {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * rotation x
     */
    @JvmStatic
    @JvmOverloads
    fun rotationX(v: View, from: Float, to: Float, duration: Long, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.ROTATION_X, from, to)
        animator.duration = duration
        listener?.let {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * rotation y
     */
    @JvmStatic
    @JvmOverloads
    fun rotationY(v: View, from: Float, to: Float, duration: Long, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.ROTATION_Y, from, to)
        animator.duration = duration
        listener?.let {
            animator.addListener(it)
        }
        animator.start()
    }

    /**
     * scale x
     */
    @JvmStatic
    @JvmOverloads
    fun scaleX(v: View, from: Float, to: Float, duration: Long = 600, repeatCount: Int = 1, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.SCALE_X, from, to)
        animator.repeatCount = repeatCount
        animator.duration = duration
        listener?.let {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * scale y
     */
    @JvmStatic
    @JvmOverloads
    fun scaleY(v: View, from: Float, to: Float, duration: Long = 600, repeatCount: Int = 1, listener: Animator.AnimatorListener? = null) {
        val animator = ObjectAnimator.ofFloat(v, View.SCALE_Y, from, to)
        animator.repeatCount = repeatCount
        animator.duration = duration
        listener?.let {
            animator.addListener(listener)
        }
        animator.start()
    }

    /**
     * 左右抖动
     */
    @JvmStatic
    @JvmOverloads
    fun shakeX(v: View, offset: Float = 10F, duration: Long = 1000L, times: Float = 5.0F) {
        val animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, 0F, offset)
        animator.duration = duration
        animator.interpolator = CycleInterpolator(times)
        animator.start()
    }

    /**
     * 上下抖动
     */
    @JvmStatic
    @JvmOverloads
    fun shakeY(v: View, offset: Float = 10F, duration: Long = 1000L, times: Float = 5.0F) {
        val animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, 0F, offset)
        animator.duration = duration
        animator.interpolator = CycleInterpolator(times)
        animator.start()
    }

    /**
     * 呼吸灯效果
     */
    @JvmStatic
    @JvmOverloads
    fun breath(v: View, from: Float = 0.0F, to: Float = 1.0F, duration: Long = 1000) {
        val animator = ObjectAnimator.ofFloat(v, View.ALPHA, from, to)
        animator.duration = duration
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }
}

