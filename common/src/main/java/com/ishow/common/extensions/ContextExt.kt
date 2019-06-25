package com.ishow.common.extensions

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.ishow.common.R
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.dialog.BaseDialog

/**
 * Toast提示
 * @param message 内容
 * @param duration 时长
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(this, message, duration)
}

/**
 * Toast提示
 * @param message 内容
 * @param duration 时长
 */
fun Context.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(this, message, duration)
}

/**
 * Dialog提示
 */
fun Context.dialog(@StringRes message: Int, finishSelf: Boolean = false, cancelable: Boolean = true) {
    dialog(getString(message), finishSelf, cancelable)
}

/**
 * Dialog提示
 */
fun Context.dialog(message: String, finishSelf: Boolean = false, cancelable: Boolean = true) {
    if (this !is Activity) {
        return
    }
    val activity = this
    BaseDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ -> if (finishSelf) activity.finish() }
        .setCancelable(cancelable)
        .show()
}

/**
 * Dialog提示
 */
fun Context.dialog(title: Int, message: String, finishSelf: Boolean = false, cancelable: Boolean = true) {
    dialog(getString(title), message, finishSelf, cancelable)
}

/**
 * Dialog提示
 */
fun Context.dialog(title: String = StringUtils.EMPTY, message: String, finishSelf: Boolean = false, cancelable: Boolean = true) {
    if (this !is Activity) {
        return
    }
    val activity = this
    BaseDialog.Builder(this)
        .setMessage(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ -> if (finishSelf) activity.finish() }
        .setCancelable(cancelable)
        .show()
}

