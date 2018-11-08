package com.ishow.common.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * inflate 方便实现
 * @param layoutRes layout的Id
 * @param attachToRoot 是否是直接attachToRoot
 */
fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}