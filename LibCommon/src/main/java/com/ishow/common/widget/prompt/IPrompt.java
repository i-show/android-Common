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

package com.ishow.common.widget.prompt;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 接口类
 */
@SuppressWarnings("unused")
public interface IPrompt {
    /**
     * 位置比例
     */
    float DEFAULT_PADDING_SCALE = 0.06f;


    // 定义Ann
    @SuppressWarnings("UnnecessaryInterfaceModifier")
    @IntDef({PromptPosition.LEFT, PromptPosition.RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PromptPosition {
        /**
         * Pictures in the text to the left
         */
        int LEFT = 0;
        /**
         * Pictures in the text to the right
         */
        int RIGHT = 4;
    }

    @SuppressWarnings("UnnecessaryInterfaceModifier")
    @IntDef({PromptMode.NONE, PromptMode.TEXT, PromptMode.GRAPH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PromptMode {
        /**
         * 不显示模式
         */
        int NONE = 0;
        /**
         * 是文字模式
         */
        int TEXT = 1;

        /**
         * 是小圈圈模式
         */
        int GRAPH = 2;
    }

    /**
     * 设置当前模式
     */
    IPrompt setPromptMode(@PromptMode int mode);

    IPrompt setPromptText(String text);

    IPrompt setPromptText(int text);


    IPrompt setPromptTextColor(@ColorRes int color);

    IPrompt setPromptTextSize(@DimenRes int size);

    IPrompt setPromptBackgroundColor(@ColorRes int color);

    IPrompt setPromptRadius(@DimenRes int radius);

    IPrompt setPromptPadding(@DimenRes int padding);

    IPrompt setPromptPosition(@PromptPosition int position);

    IPrompt setPromptWidthPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale);

    IPrompt setPromptHeightPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale);

    /**
     * 更新信息
     */
    IPrompt commit();

}
