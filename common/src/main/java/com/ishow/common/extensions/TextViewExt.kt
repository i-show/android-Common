package com.ishow.common.extensions

import android.graphics.drawable.Drawable
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