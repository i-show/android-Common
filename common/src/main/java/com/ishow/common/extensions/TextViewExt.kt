package com.ishow.common.extensions

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.widget.TextView

/**
 * 设置左侧图片
 */
fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

/**
 * 设置左侧图片
 */
fun TextView.setDrawableLeft(drawable: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

/**
 * 设置右侧图片
 */
fun TextView.setDrawableRight(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

/**
 * 设置右侧图片
 */
fun TextView.setDrawableRight(drawable: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
}

/**
 * 设置右侧图片
 */
fun TextView.setDrawableTop(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)
}

/**
 * 设置右侧图片
 */
fun TextView.setDrawableTop(drawable: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
}

/**
 * 半加粗
 */
fun TextView.fakeBoldText() {
    this.paint.isFakeBoldText = true
}

/**
 * 启动可以使用SpanClick
 */
fun TextView.enableClickableSpan() {
    movementMethod = LinkMovementMethod.getInstance()
}


/**
 * 设置字体的
 */
fun TextView.setFont(fontPath: String) {
    val font = Typeface.createFromAsset(context.assets, fontPath)
    typeface = font
}