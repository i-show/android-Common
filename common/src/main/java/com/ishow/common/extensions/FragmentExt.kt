package com.ishow.common.extensions

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ishow.common.R
import com.ishow.common.extensions.binding.ActivityBinding
import com.ishow.common.extensions.binding.FragmentBinding
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.ToastUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.dialog.BaseDialog

/**
 * Toast提示
 * @param message 内容
 * @param duration 时长
 */
fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(context, message, duration)
}

/**
 * Toast提示
 * @param message 内容
 * @param duration 时长
 */
fun Fragment.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.show(context, message, duration)
}


/**
 * Toast提示
 * @param message 内容
 */
fun Fragment.toastLong(message: String) {
    ToastUtils.show(context, message, Toast.LENGTH_LONG)
}

/**
 * Toast提示
 * @param message 内容
 */
fun Fragment.toastLong(@StringRes message: Int) {
    ToastUtils.show(context, message, Toast.LENGTH_LONG)
}

/**
 * Dialog提示
 */
fun Fragment.dialog(@StringRes message: Int, finishSelf: Boolean = false, cancelable: Boolean = true) {
    dialog(getString(message), finishSelf, cancelable)
}

/**
 * Dialog提示
 */
fun Fragment.dialog(message: String?, finishSelf: Boolean = false, cancelable: Boolean = true) {
    val activity = activity
    if (activity !is Activity || message.isNullOrEmpty()) {
        return
    }

    BaseDialog.Builder(activity)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ -> if (finishSelf) activity.finish() }
        .setCancelable(cancelable)
        .show()
}

/**
 * Dialog提示
 */
fun Fragment.dialog(title: Int, message: String, finishSelf: Boolean = false, cancelable: Boolean = true) {
    dialog(getString(title), message, finishSelf, cancelable)
}

/**
 * Dialog提示
 */
fun Fragment.dialog(title: String = StringUtils.EMPTY, message: String, finishSelf: Boolean = false, cancelable: Boolean = true) {
    val activity = activity
    if (activity !is Activity) {
        return
    }
    BaseDialog.Builder(activity)
        .setMessage(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ -> if (finishSelf) activity.finish() }
        .setCancelable(cancelable)
        .show()
}

/**
 * 打开一个Class
 */
fun Fragment.open(cls: Class<*>, finishSelf: Boolean = false) {
    val router = AppRouter.with(context)
        .target(cls)
    if (finishSelf) {
        router.finishSelf()
    }
    router.start()
}


/**
 * 方便获取ViewBinding
 */
inline fun <reified T : ViewBinding> Fragment.binding() = FragmentBinding(T::class.java, this)