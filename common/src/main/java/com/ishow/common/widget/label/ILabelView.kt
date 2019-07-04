package com.ishow.common.widget.label

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes

/**
 * Created by yuhaiyang on 2017/5/19.
 * LabelView的接口
 */

interface ILabelView {

    /**
     * Label的高度
     */
    var labelHeight: Int

    /**
     * 距离边的距离
     */
    var labelDistance: Int

    /**
     * Label是否显示
     */
    var isLabelEnable: Boolean

    /**
     * Label的位置
     */
    var labelGravity: Int

    /**
     * Label的字体颜色
     */
    var labelTextColor: Int

    /**
     * 背景颜色
     */
    var labelBackgroundColor: Int

    /**
     * 显示的文本
     */
    var labelText: String

    /**
     * 当前字体的大小
     */
    var labelTextSize: Int

    /**
     * 获取当前的View
     */
    val view: View

    /**
     * 设置字体样式
     *
     * @param textStyle 字体样式
     */
    fun setLabelTextStyle(@LabelViewHelper.TextStyle textStyle: Int)

    /**
     * 设置边框的颜色
     *
     * @param color 边框颜色
     */
    fun setLabelStrokeColor(@ColorInt color: Int)

    /**
     * 设置边框的宽度
     *
     * @param widthRes 边框的宽度
     */
    fun setLabelStrokeWidth(@DimenRes widthRes: Int)
}
