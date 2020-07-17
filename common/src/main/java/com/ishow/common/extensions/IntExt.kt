package com.ishow.common.extensions

import android.content.res.Resources
import com.ishow.common.app.provider.InitCommonProvider

/**
 * Created by yuhaiyang on 2019-08-16.
 * Int的扩展工具
 */

/**
 * dp转px
 */
fun Int.dp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * dp转px
 */
fun Int.dp22px(): Int {
    val scale = InitCommonProvider.app.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}


/**
 * 将sp值转换为px值，保证文字大小不变
 */
fun Int.sp2px(): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (this * fontScale + 0.5f).toInt()
}

/**
 * 将px值转换为dip或dp值，保证尺寸大小不变
 */
fun Int.px2dp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * 将px值转换为dip或dp值，保证尺寸大小不变
 */
fun Int.px2sp(): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (this / fontScale + 0.5f).toInt()
}