@file:Suppress("unused")

package com.ishow.common.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.ColorRes
import com.ishow.common.app.provider.InitProvider

/**
 * Created by yuhaiyang on 2020/7/7.
 * Span 的扩展使用
 */


/**
 * 通过Span来 修改字体绝对大小值
 * @param size 修改后的字体大小
 * @param child 想要设置字体大小的文字
 * @param dip 修改文字的大小是否是Dip
 */
fun SpannableString.spanAbsSize(child: String, size: Int, dip: Boolean = false): SpannableString {
    val start = indexOf(child)
    val end = start + child.length
    return spanAbsSize(size, start, end, dip)
}


/**
 * 通过Span来 修改字体绝对大小值
 * @param size 修改后的字体大小
 * @param start 开始位置
 * @param end 结束位置
 * @param dip 修改文字的大小是否是Dip
 */
fun SpannableString.spanAbsSize(size: Int, start: Int, end: Int, dip: Boolean = false): SpannableString {
    setSpan(AbsoluteSizeSpan(size, dip), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}


/**
 * 通过Span来 修改字体 相对大小
 * @param size 修改后的字体大小
 * @param child 想要设置字体大小的文字
 */
fun SpannableString.spanSize(child: String, size: Float): SpannableString {
    val start = indexOf(child)
    val end = start + child.length
    return spanSize(size, start, end)
}

/**
 * 通过Span来 修改字体 相对大小
 * @param size 修改后的字体大小
 * @param start 开始位置
 * @param end 结束位置
 */
fun SpannableString.spanSize(size: Float, start: Int, end: Int): SpannableString {
    setSpan(RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

/**
 * 通过Span来 修改字体大小
 * @param child 设置样式的文字
 * @param color 设置文字的颜色
 */
fun SpannableString.spanColorRes(child: String, @ColorRes color: Int): SpannableString {
    return spanColor(child, InitProvider.app.findColor(color))
}

/**
 * 通过Span来 修改字体大小
 * @param start 开始位置
 * @param end 结束位置
 * @param color 设置文字的颜色
 */
fun SpannableString.spanColorRes(start: Int, end: Int, @ColorRes color: Int): SpannableString {
    return spanColor(start, end, InitProvider.app.findColor(color))
}

/**
 * 通过Span来 修改字体大小
 * @param child 设置样式的文字
 * @param color 设置文字的颜色
 */
fun SpannableString.spanColor(child: String, color: Int): SpannableString {
    val start = indexOf(child)
    return spanColor(start, start + child.length, color)
}

/**
 * 通过Span来 修改字体大小
 * @param start 开始位置
 * @param end 结束位置
 * @param color 设置文字的颜色
 */
fun SpannableString.spanColor(start: Int, end: Int, color: Int): SpannableString {
    setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

/**
 * 设置点击事件的Span
 * @param child 要点击的Text
 * @param callback 点击事件的回调
 */
fun SpannableString.spanClick(child: String, underline: Boolean = false, callback: ((view: View) -> Unit)): SpannableString {
    val start = indexOf(child)
    return spanClick(start, start + child.length, underline, callback)
}


/**
 * 设置点击事件的Span
 * @param child 要点击的Text
 * @param callback 点击事件的回调
 */
fun SpannableString.spanClick(start: Int, end: Int, underline: Boolean = false, callback: ((view: View) -> Unit)): SpannableString {
    setSpan(CustomClickableSpan(underline, callback), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}


/**
 * 通过Span来 设置字体类型
 * @param style 字体类型：[android.graphics.Typeface]
 * @param start 开始位置
 * @param end 结束位置
 */
fun SpannableString.spanStyle(style: Int, start: Int, end: Int): SpannableString {
    val span = SpannableString(this)
    span.setSpan(StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 设置下划线内容
 * @param start 开始位置
 * @param end 结束位置
 */
fun SpannableString.spanUnderLine(start: Int = 0, end: Int = this.length): SpannableString {
    val span = SpannableString(this)
    span.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}


internal class CustomClickableSpan(private val underline: Boolean, private val callback: ((view: View) -> Unit)? = null) : ClickableSpan() {
    override fun onClick(view: View) {
        callback?.invoke(view)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = ds.linkColor
        if (!underline) ds.isUnderlineText = false
    }
}