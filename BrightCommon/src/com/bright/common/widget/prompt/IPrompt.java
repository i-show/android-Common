package com.bright.common.widget.prompt;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;

import com.bright.common.constant.Position;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 接口类
 */
public interface IPrompt {
    /**
     * 位置比例
     */
    float DEFAULT_PADDING_SCALE = 0.06f;
    /**
     * 不显示模式
     */
    int MODE_NONE = 0;
    /**
     * 是文字模式
     */
    int MODE_TEXT = 1;

    /**
     * 是小圈圈模式
     */
    int MODE_GRAPH = 2;

    // 定义Ann
    @IntDef({Position.LEFT, Position.RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface position {
    }

    @IntDef({MODE_NONE, MODE_TEXT, MODE_GRAPH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface mode {
    }

    /**
     * 设置当前模式
     */
    IPrompt setPromptMode(@mode int mode);

    IPrompt setPromptText(String text);

    IPrompt setPromptText(int text);


    IPrompt setPromptTextColor(@ColorRes int color);

    IPrompt setPromptTextSize(@DimenRes int size);

    IPrompt setPromptBackgroundColor(@ColorRes int color);

    IPrompt setPromptRadius(@DimenRes int radius);

    IPrompt setPromptPadding(@DimenRes int padding);

    IPrompt setPromptPosition(@position int position);

    IPrompt setPromptWidthPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale);

    IPrompt setPromptHeightPaddingScale(@FloatRange(from = 0.0f, to = 1.0f) float scale);

    /**
     * 更新信息
     */
    IPrompt commit();

}
