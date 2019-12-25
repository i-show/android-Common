package com.ishow.common.extensions

import android.content.res.Resources

/**
 * Created by yuhaiyang on 2019-08-16.
 * Int的扩展工具
 */

/**
 * dp转px
 */
fun Float.dp2px(): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return this * scale + 0.5F
}

/**
 * 将sp值转换为px值，保证文字大小不变
 */
fun Float.sp2px(): Float {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return this * fontScale + 0.5F
}

/**
 * 将px值转换为dip或dp值，保证尺寸大小不变
 */
fun Float.px2dp(): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return this / scale + 0.5F
}

/**
 * 将px值转换为dip或dp值，保证尺寸大小不变
 */
fun Float.px2sp(): Float {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return this / fontScale + 0.5F
}