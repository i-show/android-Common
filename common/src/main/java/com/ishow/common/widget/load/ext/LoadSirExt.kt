package com.ishow.common.widget.load.ext

import android.app.Activity
import android.view.View
import com.ishow.common.R
import com.ishow.common.widget.load.LoadSir
import com.ishow.common.widget.load.Loader

/**
 * Created by yuhaiyang on 2020/9/15.
 * 扩展函数使用
 */

fun View.withLoadSir(): Loader {
    val loader = LoadSir.width(this)
    setTag(R.id.tag_load_sir, loader)
    return loader
}

private val View.loadSir: Loader?
    get() {
        val loader = getTag(R.id.tag_load_sir)
        if (loader !is Loader) {
            return null
        }
        return loader
    }

fun View.sirEmpty() {
    loadSir?.showEmpty()
}

fun View.sirLoading() {
    loadSir?.showLoading()
}

fun View.sirError() {
    loadSir?.showError()
}

fun View.sirSuccess() {
    loadSir?.showSuccess()
}

fun Activity.withLoadSir(): Loader {
    val loader = LoadSir.width(this)
    val rootContainer: View = findViewById(android.R.id.content)
    rootContainer.setTag(R.id.tag_load_sir, loader)
    return loader
}

private val Activity.loadSir: Loader?
    get() {
        val rootContainer: View = findViewById(android.R.id.content)
        val loader = rootContainer.getTag(R.id.tag_load_sir)
        if (loader !is Loader) {
            return null
        }
        return loader
    }


fun Activity.sirEmpty() {
    loadSir?.showEmpty()
}

fun Activity.sirLoading() {
    loadSir?.showLoading()
}

fun Activity.sirError() {
    loadSir?.showError()
}

fun Activity.sirSuccess() {
    loadSir?.showSuccess()
}
