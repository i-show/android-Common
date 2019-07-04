package com.ishow.common.extensions

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat


fun Drawable.tint(color: ColorStateList?): Drawable {
    return if (color == null) {
        this
    } else {
        val drawable = DrawableCompat.wrap(this)
        DrawableCompat.setTintList(drawable, color)
        drawable
    }
}