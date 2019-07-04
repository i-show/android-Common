package com.ishow.common.extensions

import android.view.View

fun View.getLocationOnScreen(): IntArray {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location
}

fun View.setPadding(horizontal: Int, vertical: Int) {
    setPadding(horizontal, vertical, horizontal, vertical)
}