package com.ishow.common.extensions

import android.app.Activity
import android.app.Dialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.ishow.common.R
import com.ishow.common.widget.dialog.BaseDialog

/**
 * inflate 方便实现
 * @param layoutRes layout的Id
 */
fun Activity.inflate(layoutRes: Int): View {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}