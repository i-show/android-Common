package com.ishow.common.widget.label

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatTextView
import com.ishow.common.utils.StringUtils

class LabelTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr), ILabelView {

    internal var mLabelViewHelper: LabelViewHelper = LabelViewHelper(this, context, attrs, defStyleAttr)

    /**
     * Label的高度
     */
    override var labelHeight: Int
        get() = mLabelViewHelper.labelHeight
        set(@DimenRes height) {
            mLabelViewHelper.labelHeight = height
        }

    /**
     * Label距离边的距离
     */
    override var labelDistance: Int
        get() = mLabelViewHelper.labelDistance
        set(@DimenRes distance) {
            mLabelViewHelper.labelDistance = distance
        }

    /**
     * Label是否显示
     */
    override var isLabelEnable: Boolean
        get() = mLabelViewHelper.isLabelEnable
        set(enable) {
            mLabelViewHelper.isLabelEnable = enable
        }

    /**
     * Label的位置
     */
    override var labelGravity: Int
        get() = mLabelViewHelper.labelGravity
        set(@LabelViewHelper.Gravity gravity) {
            mLabelViewHelper.labelGravity = gravity
        }

    /**
     * Label的字体颜色
     */
    override var labelTextColor: Int
        get() = mLabelViewHelper.labelTextColor
        set(@ColorInt textColor) {
            mLabelViewHelper.labelTextColor = textColor
        }

    /**
     * 背景颜色
     */
    override var labelBackgroundColor: Int
        get() = mLabelViewHelper.labelBackgroundColor
        set(@ColorInt backgroundColor) {
            mLabelViewHelper.labelBackgroundColor = backgroundColor
        }

    /**
     * 获取显示的文本
     */
    override var labelText: String
        get() = mLabelViewHelper.labelText ?: StringUtils.EMPTY
        set(text) {
            mLabelViewHelper.labelText = text
        }

    /**
     * 当前字体的大小
     */
    override var labelTextSize: Int
        get() = mLabelViewHelper.labelTextSize
        set(@DimenRes textSize) {
            mLabelViewHelper.labelTextSize = textSize
        }

    override val view: View
        get() = this

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mLabelViewHelper.onDraw(canvas)
    }

    /**
     * 设置字体样式
     *
     * @param textStyle 字体样式
     */
    override fun setLabelTextStyle(@LabelViewHelper.TextStyle textStyle: Int) {
        mLabelViewHelper.setLabelTextStyle(textStyle)
    }

    /**
     * 设置边框的颜色
     *
     * @param color 边框颜色
     */
    override fun setLabelStrokeColor(@ColorInt color: Int) {
        mLabelViewHelper.setLabelStrokeColor(color)
    }

    /**
     * 设置边框的宽度
     *
     * @param widthRes 边框的宽度
     */
    override fun setLabelStrokeWidth(@DimenRes widthRes: Int) {
        mLabelViewHelper.setLabelStrokeWidth(widthRes)
    }
}


