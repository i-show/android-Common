package com.ishow.common.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
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
    loadUrl(url, options, context.findDrawable(placeHolder))
}

/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
        url: String?,
        options: RequestOptions = RequestOptions.centerCropTransform(),
        placeHolder: Drawable?
) {
    loadUrl(url, null, options, placeHolder)
}


/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
        url: String?,
        listener: RequestListener<Drawable>? = null,
        options: RequestOptions = RequestOptions.centerCropTransform(),
        placeHolder: Int = -1
) {
    loadUrl(url, listener, options, context.findDrawable(placeHolder))
}


/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
        url: String?,
        listener: RequestListener<Drawable>? = null,
        options: RequestOptions = RequestOptions.centerCropTransform(),
        placeHolder: Drawable?
) {
    if (url.isNullOrEmpty()) {
        return
    }

    val finalOptions = if (placeHolder == null) {
        RequestOptions().apply(options)
    } else {
        RequestOptions.placeholderOf(placeHolder).apply(options)
    }

    Glide.with(context)
            .load(url)
            .apply(finalOptions)
            .listener(listener)
            .into(this)
}