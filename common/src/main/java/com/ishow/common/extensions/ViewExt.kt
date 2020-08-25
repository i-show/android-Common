package com.ishow.common.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Checkable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ishow.common.R
import com.ishow.common.utils.DeviceUtils

/**
 * 获取View 在当前屏幕的位置
 */
fun View.getLocationOnScreen(): IntArray {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location
}

/**
 * 设置Padding
 * @param horizontal 横向的padding
 * @param vertical 纵向padding
 */
fun View.setPadding(horizontal: Int, vertical: Int) {
    setPadding(horizontal, vertical, horizontal, vertical)
}

/**
 * 设置一个横向的Padding
 */
fun View.setPaddingHorizontal(value: Int) {
    setPadding(value, paddingTop, value, paddingBottom)
}

/**
 * 设置一个纵向的padding
 */
fun View.setPaddingVertical(value: Int) {
    setPadding(paddingStart, value, paddingEnd, value)
}

/**
 * 单独设置 MarginStart
 * @param value 设定具体的值
 */
fun View.setMarginStart(value: Int) {
    val layoutParams = layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.marginStart = value
        this.layoutParams = layoutParams
    }
}

/**
 * 单独设置 MarginTop
 * @param value 设定具体的值
 */
fun View.setMarginTop(value: Int) {
    val layoutParams = layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.topMargin = value
        this.layoutParams = layoutParams
    }
}

/**
 * 单独设置 MarginEnd
 * @param value 设定具体的值
 */
fun View.setMarginEnd(value: Int) {
    val layoutParams = layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.marginEnd = value
        this.layoutParams = layoutParams
    }
}

/**
 * 单独设置 MarginBottom
 * @param value 设定具体的值
 */
fun View.setMarginBottom(value: Int) {
    val layoutParams = layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams.bottomMargin = value
        this.layoutParams = layoutParams
    }
}

/**
 * 获取字符串
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * 展示软键盘
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * 隐藏软键盘
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

/**
 * 通过增加PaddingTop的方式来适配沉浸式状态栏
 */
fun View.fitStatusBar() {
    val statusBarH = DeviceUtils.getStatusBarHeight(context)
    setPadding(paddingLeft, paddingTop + statusBarH, paddingRight, paddingBottom)
}

/**
 * 通过设置TopMargin的方式来实现适配状态栏
 */
fun View.fitStatusBarByMargin() {
    val layoutParams = layoutParams
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val statusBarH = DeviceUtils.getStatusBarHeight(context)
        layoutParams.topMargin = layoutParams.topMargin + statusBarH
        this.layoutParams = layoutParams
    }
}

/**
 * 自适应软键盘遮挡底部布局问题
 * @param showChild 界面中必须要展示的View
 */
fun View.fitKeyBoard(showChild: View) {
    val activity = context as Activity

    val listener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        val window = activity.window
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val screenHeight: Int = window.decorView.height
        val softHeight: Int = screenHeight - rect.bottom

        val scrollDistance: Int = softHeight - (screenHeight - showChild.bottom)
        if (scrollDistance > 0) {
            scrollTo(0, scrollDistance + 60)
        } else {
            scrollTo(0, 0)
        }
    }

    viewTreeObserver.addOnGlobalLayoutListener(listener)

    if (activity is AppCompatActivity) {
        activity.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        })
    }
}

inline fun <T : View> T.afterMeasured(crossinline block: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}

/**
 * 防多次点击
 */
inline fun <T : View> T.onClick(time: Long = 800, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(R.id.tag_click, value)
    get() = getTag(R.id.tag_click) as? Long ?: 0