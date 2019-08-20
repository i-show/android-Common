package com.ishow.common.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ishow.common.R

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
 * 隐藏
 */
fun AppCompatActivity.hideFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.hide(fragment)
    transaction.commit()
}
