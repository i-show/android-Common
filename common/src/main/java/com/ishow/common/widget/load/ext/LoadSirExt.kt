package com.ishow.common.widget.load.ext

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