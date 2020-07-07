package com.ishow.common.utils.databinding.adapter

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Created by yuhaiyang on 2019-08-19.
 *
 */

object TextViewBindingAdapter {
    /**
     * 粗体效果
     * 备注：这个粗体效果比 bold 细一点，看起来好一些
     */
    @JvmStatic
    @BindingAdapter("fakeBoldText")
    fun fakeBoldText(view: TextView, isBold: Boolean) {
        view.paint.isFakeBoldText = isBold
    }


    /**
     * 粗体效果
     * 备注：这个粗体效果比 bold 细一点，看起来好一些
     */
    @JvmStatic
    @BindingAdapter("bindTextColor")
    fun bindTextColor(view: TextView, color: Int) {
        view.setTextColor(color)
    }

    /**
     * 设置下划线效果
     */
    @JvmStatic
    @BindingAdapter("bindUnderLine")
    fun bindUnderLine(view: TextView, status: Boolean) {
        view.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        view.paint.isAntiAlias = true
    }
}