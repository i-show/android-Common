@file:Suppress("unused")

package com.ishow.common.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import com.ishow.common.utils.JsonUtils
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
fun String.spanSize(size: Int, start: Int, end: Int, dip: Boolean = false): SpannableString {
    val span = SpannableString(this)
    span.setSpan(AbsoluteSizeSpan(size, dip), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 通过Span来 修改字体 相对大小
 */
fun String.spanSize(size: Float, start: Int, end: Int): SpannableString {
    val span = SpannableString(this)
    span.setSpan(RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 通过Span来 设置字体类型
 * 字体类型：字体样式 Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体  Typeface.BOLD_ITALIC粗斜体
 */
fun String.spanStyle(style: Int, start: Int, end: Int): SpannableString {
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

fun String.phoneDesensitization(): String {
    return if (TextUtils.isEmpty(this) || this.length != 11) {
        this
    } else {
        this.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }
}

fun String.phoneFormat(): String {
    return if (TextUtils.isEmpty(this) || this.length != 11) {
        this
    } else {
        this.replace("(\\d{3})(\\d{0,4})(\\d{0,4})".toRegex(), "$1 $2 $3")
    }
}

/**
 * 是否是邮箱
 */
fun String.isEmail(): Boolean {
    if (!contains("@")) {
        return false
    }
    if (split("@").size != 2) {
        return false
    }
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

/**
 * 解析JSON转换成对象
 */
inline fun <reified T> String.parseJSON(): T = JsonUtils.gson.parseJSON(this)
