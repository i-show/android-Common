package com.ishow.common.extensions

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import android.view.Gravity
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import java.lang.IllegalStateException
import kotlin.math.max


/**
 * 设置拖拽区域
 */
@SuppressLint("RtlHardcoded")
fun DrawerLayout.setDragArea(percentage: Float, gravity: Int = Gravity.START) {
    try {
        val dragFiled = when (gravity) {
            Gravity.LEFT,
            Gravity.START -> this.javaClass.getDeclaredField("mLeftDragger")
            Gravity.RIGHT,
            Gravity.END -> this.javaClass.getDeclaredField("mRightDragger")
            else -> throw IllegalStateException("Can not set Top or Bottom")
        }

        dragFiled.isAccessible = true

        val dragHelper = dragFiled[this] as ViewDragHelper
        val edgeSizeField = dragHelper.javaClass.getDeclaredField("mEdgeSize")
        edgeSizeField.isAccessible = true

        val edgeSize = edgeSizeField.getInt(dragHelper)
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val size = max(edgeSize, (screenWidth * percentage).toInt())

        edgeSizeField.setInt(dragHelper, size)
    } catch (e: Exception) {
        Log.e("DrawerLayoutExt", "setDragArea  e: $e")
    }
}