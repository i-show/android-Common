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

package com.ishow.common.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.CycleInterpolator;

public class AnimatorUtils {

    /**
     * alpha
     */
    public static void alpha(View v, float fromAlpha, float toAlpha, int duration) {
        alpha(v, fromAlpha, toAlpha, duration, null);
    }

    /**
     * alpha
     */
    public static void alpha(View v, float fromAlpha, float toAlpha, int duration, Animator.AnimatorListener animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA, fromAlpha, toAlpha);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.start();
    }

    /**
     * translation x
     */
    public static void translationX(View v, float fromX, float toX, int duration) {
        translationX(v, fromX, toX, duration, null);
    }

    /**
     * translation x
     */
    public static void translationX(View v, float fromX, float toX, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, fromX, toX);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.start();
    }

    /**
     * translation y
     */
    public static void translationY(View v, float fromY, float toY, int duration) {
        translationY(v, fromY, toY, duration, null);
    }

    /**
     * translation y
     */
    public static void translationY(View v, float fromY, float toY, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, fromY, toY);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.start();
    }

    /**
     * rotation x
     */
    public static void rotationX(View v, float fromX, float toX, int duration) {
        rotationX(v, fromX, toX, duration, null);
    }

    /**
     * rotation x
     */
    public static void rotationX(View v, float fromX, float toX, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ROTATION_X, fromX, toX);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.start();
    }

    /**
     * rotation y
     */
    public static void rotationY(View v, float fromY, float toY, int duration) {
        rotationY(v, fromY, toY, duration, null);
    }

    /**
     * rotation y
     */
    public static void rotationY(View v, float fromY, float toY, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ROTATION_Y, fromY, toY);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.start();
    }

    /**
     * scale x
     */
    public static void scaleX(View v, float fromX, float toX, int duration) {
        scaleX(v, fromX, toX, duration, null);
    }

    /**
     * scale x
     */
    public static void scaleX(View v, float fromX, float toX, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.SCALE_X, fromX, toX);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.start();
    }

    /**
     * scale y
     */
    public static void scaleY(View v, float fromY, float toY, int duration) {
        scaleY(v, fromY, toY, duration, null);
    }

    /**
     * scale y
     */
    public static void scaleY(View v, float fromY, float toY, int duration, Animator.AnimatorListener
            animatorListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.SCALE_Y, fromY, toY);
        animator.setDuration(duration);
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.start();
    }

    /**
     * shake x
     *
     * @param v
     */
    public static void shakeX(View v) {
        shakeX(v, 10, 1000, 5.0f);
    }

    /**
     * shake x
     */
    public static void shakeX(View v, float offset, long duration, float times) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, 0, offset);
        animator.setDuration(duration);
        animator.setInterpolator(new CycleInterpolator(times));
        animator.start();
    }

    /**
     * shake y
     *
     * @param v
     */
    public static void shakeY(View v) {
        shakeY(v, 10, 1000, 5.0f);
    }

    /**
     * shake y
     */
    public static void shakeY(View v, float offset, long duration, float times) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.TRANSLATION_Y, 0, offset);
        animator.setDuration(duration);
        animator.setInterpolator(new CycleInterpolator(times));
        animator.start();
    }

    /**
     * 呼吸灯效果
     *
     * @param v
     */
    public static void breath(View v) {
        breath(v, 0.0f, 1.0f, 1000);
    }

    /**
     * 呼吸灯效果
     */
    public static void breath(View v, float fromRange, float toRange, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA, fromRange, toRange);
        animator.setDuration(duration);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
