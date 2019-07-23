package com.ishow.common.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

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