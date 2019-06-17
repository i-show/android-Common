package com.ishow.common.widget.label;

import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;

/**
 * Created by yuhaiyang on 2017/5/19.
 * LabelView的接口
 */

public interface ILabelView {

    /**
     * 设置Label的高度
     *
     * @param height Label高度
     */
    void setLabelHeight(@DimenRes int height);

    /**
     * 获取Label的高度
     *
     * @return Label的高度
     */
    int getLabelHeight();

    /**
     * 设置Label距离边的距离
     *
     * @param distance 距离边的距离
     */
    void setLabelDistance(@DimenRes int distance);

    /**
     * 获取Label距离边的距离
     *
     * @return 距离边的距离
     */
    int getLabelDistance();

    /**
     * Label是否显示
     *
     * @return 是否显示
     */
    boolean isLabelEnable();

    /**
     * 设置Label是否显示
     *
     * @param enable 是否显示
     */
    void setLabelEnable(boolean enable);

    /**
     * 获取Label的位置
     */
    int getLabelGravity();

    /**
     * 设置Label的位置
     *
     * @param gravity Label的位置
     */
    void setLabelGravity(@LabelViewHelper.Gravity int gravity);

    /**
     * 获取Label的字体颜色
     *
     * @return @ColorInt 返回字体颜色
     */
    int getLabelTextColor();

    /**
     * 设置Label的字体颜色
     *
     * @param textColor 字体颜色
     */
    void setLabelTextColor(@ColorInt int textColor);

    /**
     * 设置背景颜色
     *
     * @return 返回背景颜色
     * @see #setLabelBackgroundColor(int)
     */
    int getLabelBackgroundColor();

    /**
     * 获取背景颜色
     *
     * @param backgroundColor 背景颜色
     * @see #getLabelBackgroundColor()
     */
    void setLabelBackgroundColor(@ColorInt int backgroundColor);

    /**
     * 获取显示的文本
     *
     * @return Label Text
     * @see #setLabelText(String)
     */
    String getLabelText();

    /**
     * 设置显示的文本
     *
     * @param text 文本内容
     * @see #getLabelText()
     */
    void setLabelText(String text);

    /**
     * 获取当前字体的大小
     *
     * @return 返回Label的字体大小
     * @see #setLabelTextSize
     */
    int getLabelTextSize();

    /**
     * 设置当前字体的大小
     *
     * @param textSize 字体大小
     */
    void setLabelTextSize(@DimenRes int textSize);

    /**
     * 设置字体样式
     *
     * @param textStyle 字体样式
     */
    void setLabelTextStyle(@LabelViewHelper.TextStyle int textStyle);

    /**
     * 设置边框的颜色
     *
     * @param color 边框颜色
     */
    void setLabelStrokeColor(@ColorInt int color);

    /**
     * 设置边框的宽度
     *
     * @param widthRes 边框的宽度
     */
    void setLabelStrokeWidth(@DimenRes int widthRes);

    /**
     * 获取当前的View
     */
    View getView();
}
