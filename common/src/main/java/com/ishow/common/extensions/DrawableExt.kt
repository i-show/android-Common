package com.ishow.common.extensions

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat
import com.ishow.common.utils.log.LogUtils

/**
 * 附着颜色
 */
fun Drawable.tint(color: ColorStateList?): Drawable {
    return if (color == null) {
        this
    } else {
        val drawable = DrawableCompat.wrap(this)
        DrawableCompat.setTintList(drawable, color)
        drawable
    }
}

/**
 * 转换成Bitmap
 */
fun Drawable.toBitmap(): Bitmap {
    val w = intrinsicWidth
    val h = intrinsicHeight

    val config = if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    val bitmap = Bitmap.createBitmap(w, h, config)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, w, h)
    draw(canvas)
    return bitmap
}

/**
 * 转换成ByteArray
 */
fun Drawable.toByte(): ByteArray {
    if (this !is BitmapDrawable) {
        LogUtils.e("DrawableExt", "drawableToBytes:  not a bitmap drawable")
        return byteArrayOf()
    }
    return bitmap.toByte()
}