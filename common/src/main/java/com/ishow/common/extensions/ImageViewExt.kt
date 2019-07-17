package com.ishow.common.extensions

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    url: String?,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Int = -1
) {
    if (url.isNullOrEmpty()) {
        return
    }

    val finalOptions = if (placeHolder == -1) {
        RequestOptions().apply(options)
    } else {
        RequestOptions.placeholderOf(placeHolder).apply(options)
    }

    Glide.with(context)
        .load(url)
        .apply(finalOptions)
        .into(this)
}
