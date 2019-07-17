package com.ishow.common.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import java.io.ByteArrayInputStream


/**
 * 转换成Bitmap
 */
fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

/**
 * 转换成Drawable
 */
fun ByteArray.toDrawable(): Drawable {
    return Drawable.createFromStream(ByteArrayInputStream(this), null)
}