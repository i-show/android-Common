package com.ishow.common.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.ishow.common.utils.DeviceUtils

fun View.getLocationOnScreen(): IntArray {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location
}

fun View.setPadding(horizontal: Int, vertical: Int) {
    setPadding(horizontal, vertical, horizontal, vertical)
}

fun View.setPaddingHorizontal(value: Int) {
    setPadding(value, paddingTop, value, paddingBottom)
}

fun View.setPaddingVertical(value: Int) {
    setPadding(paddingStart, value, paddingEnd, value)
}

/**
 * 获取字符串
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * 展示软键盘
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * 隐藏软键盘
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.fitStatusBar() {
    val statusBarH = DeviceUtils.getStatusBarHeight(context)
    setPadding(paddingLeft, paddingTop + statusBarH, paddingRight, paddingBottom)
}

fun View.fitStatusBarByMargin() {
    val layoutParams = layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val statusBarH = DeviceUtils.getStatusBarHeight(context)
        layoutParams.topMargin = layoutParams.topMargin + statusBarH
        this.layoutParams = layoutParams
    }
}