@file:Suppress("unused")

package com.ishow.common.extensions

import android.content.ClipData
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.*
import androidx.annotation.ColorRes
import com.ishow.common.app.provider.InitCommonProvider
import com.ishow.common.utils.JsonUtils
import com.ishow.common.utils.MathUtils
import com.ishow.common.utils.StringUtils


/**
 * 解析成人民币
 * @param scale 小数点的位数
 * @param force 是否是强制设置多少位小数，如果是强制结果是后面补0
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
 * 字符串进行Format小数点几位数
 * @param scale 小数点的位数
 * @param force 是否是强制设置多少位小数，如果是强制结果是后面补0
 */
fun String.rounding(scale: Int = -1, force: Boolean = false): String {
    return MathUtils.rounding(this, scale, force)
}

/**
 * 转换为SpannableString
 */
fun String.asSpan(): SpannableString {
    return SpannableString(this)
}

/**
 * 通过Span来 修改字体绝对大小值
 * @param size 修改后的字体大小
 * @param start 开始位置
 * @param end 结束位置
 * @param dip 修改文字的大小是否是Dip
 */
fun String.spanSize(size: Int, start: Int, end: Int, dip: Boolean = false): SpannableString {
    val span = SpannableString(this)
    span.setSpan(AbsoluteSizeSpan(size, dip), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 通过Span来 修改字体大小
 */
fun String.spanColorRes(child: String, @ColorRes color: Int): SpannableString {
    return spanColor(child, InitCommonProvider.app.findColor(color))
}

/**
 * 通过Span来 修改字体大小
 */
fun String.spanColor(child: String, color: Int): SpannableString {
    val start = indexOf(child)
    val span = SpannableString(this)
    span.setSpan(ForegroundColorSpan(color), start, start + child.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 通过Span来 修改字体 相对大小
 * @param size 修改后的字体大小
 * @param start 开始位置
 * @param end 结束位置
 */
fun String.spanSize(size: Float, start: Int, end: Int): SpannableString {
    val span = SpannableString(this)
    span.setSpan(RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 通过Span来 设置字体类型
 * @param style 字体类型：[android.graphics.Typeface]
 * @param start 开始位置
 * @param end 结束位置
 */
fun String.spanStyle(style: Int, start: Int, end: Int): SpannableString {
    val span = SpannableString(this)
    span.setSpan(StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 设置下划线内容
 * @param start 开始位置
 * @param end 结束位置
 */
fun String.spanUnderLine(start: Int = 0, end: Int = this.length): SpannableString {
    val span = SpannableString(this)
    span.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
}

/**
 * 判断当前字符串是否是手机号
 */
fun String.isPhone(): Boolean {
    val p = "^((1[3-9][0-9])\\d{8})\$".toRegex()
    return matches(p)
}

/**
 * 给当前手机号进行脱敏处理
 * 例如：18812348888 -> 188****8888
 */
fun String.phoneDesensitization(hasGap: Boolean = true): String {
    return if (TextUtils.isEmpty(this) || this.length != 11) {
        this
    } else if (hasGap) {
        this.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1 **** $2")
    } else {
        this.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }
}

/**
 * 给当前手机号进行Format处理，增加空格
 * 例如：18812348888 -> 188 1234 8888
 */
fun String.phoneFormat(): String {
    return if (TextUtils.isEmpty(this) || this.length != 11) {
        this
    } else {
        this.replace("(\\d{3})(\\d{0,4})(\\d{0,4})".toRegex(), "$1 $2 $3")
    }
}

/**
 * 判断当前字符串是否是邮箱
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
 * 把字符Copy到剪切板上
 */
fun String.copy2Clipboard(label: String = "label") {
    val manager = InitCommonProvider.app.clipboardManager
    val data = ClipData.newPlainText(label, this)
    manager.setPrimaryClip(data)
}

/**
 * 把当前字符串转换成具体的对象
 */
inline fun <reified T> String.parseJSON(): T = JsonUtils.gson.parseJSON(this)
