package com.ishow.common.extensions

import android.app.Activity
import android.view.LayoutInflater
import android.view.View

/**
 * inflate 方便实现
 * @param layoutRes layout的Id
 */
fun Activity.inflate(layoutRes: Int): View {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}