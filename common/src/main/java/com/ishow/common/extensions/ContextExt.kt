@file:Suppress("unused")

package com.ishow.common.extensions

import android.app.Activity
import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.ishow.common.R
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.ToastUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.dialog.BaseDialog


/**
 * inflate 方便实现
 * @param layoutRes layout的Id
 */
fun Context.inflate(layoutRes: Int): View {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}

/**
 * Toast提示
 * @param message 内容
 * @param duration 时长
 */
fun Context.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
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
fun Context.dialog(@StringRes message: Int, finishSelf: Boolean = false, cancelable: Boolean = true): BaseDialog? {
    return dialog(getString(message), finishSelf, cancelable)
}

/**
 * Dialog提示
 */
fun Context.dialog(message: String, finishSelf: Boolean = false, cancelable: Boolean = true): BaseDialog? {
    if (this !is Activity) {
        return null
    }
    val activity = this
    return BaseDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ -> if (finishSelf) activity.finish() }
        .setCancelable(cancelable)
        .show()
}

/**
 * Dialog提示
 */
fun Context.dialog(title: Int, message: String, finishSelf: Boolean = false, cancelable: Boolean = true): BaseDialog? {
    return dialog(getString(title), message, finishSelf, cancelable)
}

/**
 * Dialog提示
 */
fun Context.dialog(
    title: String = StringUtils.EMPTY,
    message: String,
    finishSelf: Boolean = false,
    cancelable: Boolean = true
): BaseDialog? {
    if (this !is Activity) {
        return null
    }
    val activity = this
    return BaseDialog.Builder(this)
        .setMessage(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ -> if (finishSelf) activity.finish() }
        .setCancelable(cancelable)
        .show()
}

/**
 * 获取颜色
 */
fun Context.findColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

/**
 * 获取Drawable
 */
fun Context.findDrawable(@DrawableRes id: Int): Drawable? {
    return try {
        ContextCompat.getDrawable(this, id)
    } catch (e: Resources.NotFoundException) {
        null
    }
}

/**
 * 获取Boolean
 */
fun Context.getBoolean(@BoolRes id: Int) = resources.getBoolean(id)

/**
 * 获取Integer
 */
fun Context.getInteger(@IntegerRes id: Int) = resources.getInteger(id)

fun Context.getDimensionPixelSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

/**
 * 获取输入法管理
 */
inline val Context.inputManager: InputMethodManager
    get() = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

inline val Context.keyguardManager: KeyguardManager
    get() = getSystemService(KEYGUARD_SERVICE) as KeyguardManager

inline val Context.telephonyManager: TelephonyManager
    get() = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

inline val Context.devicePolicyManager: DevicePolicyManager
    get() = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager

inline val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

inline val Context.alarmManager: AlarmManager
    get() = getSystemService(ALARM_SERVICE) as AlarmManager

inline val Context.clipboardManager: ClipboardManager
    get() = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
inline val Context.locationManager: LocationManager
    get() = getSystemService(LOCATION_SERVICE) as LocationManager

/**
 * 跳转浏览器
 */
fun Context.openBrowser(url: String?) {
    if (TextUtils.isEmpty(url)) {
        return
    }
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(R.string.exception_intent_open)
    }
}

/**
 * 打开拨号盘
 */
fun Context.openDial(number: String?) {
    if (number.isNullOrEmpty()) {
        return
    }
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number.trim { it <= ' ' }))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(R.string.exception_intent_open)
    }
}

/**
 * 打开拨号盘
 */
fun Context.call(number: String?) {
    if (number.isNullOrEmpty()) {
        return
    }
    try {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.trim { it <= ' ' }))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(R.string.exception_intent_open)
    }
}

/**
 * 打开APP设置界面
 */
fun Context.openAppSettings() {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    } catch (e: Exception) {
        toast(R.string.exception_intent_open)
    }
}

/**
 * 打开APP
 */
fun Context.openApp(packageName: String) {
    try {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    } catch (e: Exception) {
        toast(R.string.exception_intent_open)
    }
}

/**
 * 打开一个Class
 */
fun Context.open(cls: Class<*>, finishSelf: Boolean = false) {
    val router = AppRouter.with(this)
        .target(cls)
    if (finishSelf) {
        router.finishSelf()
    }
    router.start()
}

/**
 * 获取APP的名称
 */
fun Context.appName(): String? {
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        getString(packageInfo.applicationInfo.labelRes)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}

fun Context.versionName(): String {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        StringUtils.EMPTY
    }
}