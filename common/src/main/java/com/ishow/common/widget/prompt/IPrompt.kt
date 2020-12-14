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

package com.ishow.common.widget.prompt

import androidx.annotation.*

/**
 * 接口类
 */
interface IPrompt {


    // 定义Ann
    @IntDef(PromptPosition.LEFT, PromptPosition.RIGHT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class PromptPosition {
        companion object {
            /**
             * Pictures in the text to the left
             */
            const val LEFT = 0

            /**
             * Pictures in the text to the right
             */
            const val RIGHT = 4
        }
    }

    @IntDef(PromptMode.NONE, PromptMode.TEXT, PromptMode.GRAPH)
    @Retention(AnnotationRetention.SOURCE)
    annotation class PromptMode {
        companion object {
            /**
             * 不显示模式
             */
            const val NONE = 0

            /**
             * 是文字模式
             */
            const val TEXT = 1

            /**
             * 是小圈圈模式
             */
            const val GRAPH = 2
        }
    }

    /**
     * 设置当前模式
     */
    fun setPromptMode(@PromptMode mode: Int): IPrompt

    fun setPromptText(text: String): IPrompt

    fun setPromptText(text: Int): IPrompt

    fun setPromptTextColor(@ColorInt color: Int): IPrompt
    fun setPromptTextColorResource(@ColorRes colorRes: Int): IPrompt

    fun setPromptTextSize(size: Int): IPrompt
    fun setPromptTextSizeResource(@DimenRes sizeRes: Int): IPrompt

    fun setPromptBackgroundColor(@ColorInt color: Int): IPrompt
    fun setPromptBackgroundColorResource(@ColorRes colorRes: Int): IPrompt

    fun setPromptRadius(radius: Int): IPrompt
    fun setPromptRadiusResource(@DimenRes radiusRes: Int): IPrompt

    fun setPromptPadding(padding: Int): IPrompt
    fun setPromptPaddingResource(@DimenRes paddingRes: Int): IPrompt

    fun setPromptPaddingHorizontal(padding: Int): IPrompt
    fun setPromptPaddingHorizontalResource(@DimenRes paddingRes: Int): IPrompt

    fun setPromptPaddingVertical(padding: Int): IPrompt
    fun setPromptPaddingVerticalResource(@DimenRes paddingRes: Int): IPrompt

    fun setPromptPosition(@PromptPosition position: Int): IPrompt

    fun setPromptWidthPaddingScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): IPrompt

    fun setPromptHeightPaddingScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): IPrompt

    /**
     * 更新信息
     */
    fun commit(): IPrompt

    companion object {
        /**
         * 位置比例
         */
        const val DEFAULT_PADDING_SCALE = 0.06f
    }

}
