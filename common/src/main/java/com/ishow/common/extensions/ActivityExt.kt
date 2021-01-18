package com.ishow.common.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.ishow.common.R
import com.ishow.common.extensions.binding.ActivityBinding

/**
 * inflate 方便实现
 * @param layoutRes layout的Id
 */
fun Activity.inflate(layoutRes: Int): View {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}

/**
 * 显示Fragment
 */
fun AppCompatActivity.showFragment(fragment: Fragment, layoutId: Int = R.id.fragmentContainer) {
    val transaction = supportFragmentManager.beginTransaction()
    if (fragment.isAdded) {
        transaction.show(fragment)
    } else {
        transaction.add(layoutId, fragment)
    }
    transaction.commit()
}

/**
 * 显示Fragment
 */
@SuppressLint("WrongConstant")
fun AppCompatActivity.showFragment(
    showFragment: Fragment?,
    hideFragment: Fragment? = null,
    transitionStyle: Int = FragmentTransaction.TRANSIT_UNSET,
    layout: Int = R.id.fragmentContainer
) {
    val transaction = supportFragmentManager.beginTransaction()
    if (transitionStyle == FragmentTransaction.TRANSIT_UNSET) {
        transaction.setCustomAnimations(
            R.anim.activity_right_in,
            R.anim.activity_left_out,
            R.anim.activity_left_in,
            R.anim.activity_right_out
        )
    } else {
        transaction.setTransition(transitionStyle)
    }

    if (hideFragment != null) {
        transaction.hide(hideFragment)
    }

    if (showFragment == null) {
        transaction.commit()
        return
    }

    if (showFragment.isAdded) {
        transaction.show(showFragment)
    } else {
        transaction.add(layout, showFragment)
    }

    transaction.addToBackStack(null)
    transaction.commit()
}

/**
 * 隐藏一个Fragment
 */
fun AppCompatActivity.hideFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.hide(fragment)
    transaction.commit()
}

/**
 * 设置状态栏的颜色通过Resource 来进行设置
 */
fun AppCompatActivity.setStatusBarColorRes(@ColorRes color: Int) {
    setStatusBarColor(findColor(color))
}

/**
 * 设置状态栏的颜色
 */
fun AppCompatActivity.setStatusBarColor(@ColorInt color: Int) {
    val window = window
    window.statusBarColor = color
}

/**
 * 全屏模式 设置一个Activity 为一个全屏模式
 */
fun AppCompatActivity.fullWindow(light: Boolean = false) {
    setStatusBarColor(Color.TRANSPARENT)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    val visibility =
        if (light && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    setSystemUiVisibility(visibility)
}

/**
 * 设置一个 Activity为普通模式
 */
fun AppCompatActivity.normalWindow(color: Int? = null) {
    val statusColor = color ?: findColor(R.color.color_accent)
    setStatusBarColor(statusColor)
    setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)
}

/**
 * 设置状态栏是否显示
 */
fun AppCompatActivity.setSystemUiVisibility(visibility: Int) {
    window.decorView.systemUiVisibility = visibility
}

/**
 * 方便获取ViewDataBinding
 */
inline fun <reified T : ViewBinding> AppCompatActivity.binding() = ActivityBinding(T::class.java, this)
