package com.ishow.common.utils.databinding.adapter

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
}