@file:Suppress("unused")

package com.ishow.common.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.*
import com.ishow.common.utils.MathUtils
import com.ishow.common.utils.StringUtils


/**
 * 解析成人民币
 */
fun String.format2Money(scale: Int = -1, force: Boolean = false): String {
    if (this.isEmpty()) {
        return StringUtils.MONEY + StringUtils.BLANK + "0"
    }

    return if (scale > 0) {
        val money = MathUtils.rounding(this, scale, force)
        StringUtils.MONEY + StringUtils.BLANK + money
    } else {
        StringUtils.MONEY + StringUtils.BLANK + this
    }
}


/**
 * 通过Span来 修改字体大小
 */
fun String.spanSize(start: Int, end: Int, size: Int, dip: Boolean = false): SpannableString {
    val span = SpannableString(this)
    span.setSpan(AbsoluteSizeSpan(size, dip), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 通过Span来 修改字体 相对大小
 */
fun String.spanSize(start: Int, end: Int, size: Float): SpannableString {
    val span = SpannableString(this)
    span.setSpan(RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 通过Span来 设置字体类型
 * 字体类型：字体样式 Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体  Typeface.BOLD_ITALIC粗斜体
 */
fun String.spanStyle(start: Int, end: Int, style: Int): SpannableString {
    val span = SpannableString(this)
    span.setSpan(StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 获取Span内容   下划线
 */
fun String.spanUnderLine(start: Int, end: Int): SpannableString {
    val span = SpannableString(this)
    span.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 是否是手机号
 */
fun String.isPhone(): Boolean {
    val p = "^((1[3-9][0-9])\\d{8})\$".toRegex()
    return matches(p)
}

/**
 * 是否是邮箱
 */
fun String.isEmail(): Boolean {
    if(!contains("@")){
        return false
    }
    if (split("@").size != 2) {
        return false
    }
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

