package com.ishow.common.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    url: String?,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Int = -1,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    loadUrl(url, options, context.findDrawable(placeHolder), cacheMode = cacheMode)
}

/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    url: String?,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Drawable?,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    loadUrl(url, null, options, placeHolder, cacheMode)
}


/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    url: String?,
    listener: RequestListener<Drawable>? = null,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Int = -1,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    loadUrl(url, listener, options, context.findDrawable(placeHolder), cacheMode)
}


/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    url: String?,
    listener: RequestListener<Drawable>? = null,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Drawable?,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    val finalOptions = if (placeHolder == null) {
        RequestOptions().apply(options)
    } else {
        RequestOptions.placeholderOf(placeHolder).apply(options)
    }

    Glide.with(context)
        .load(url)
        .diskCacheStrategy(cacheMode)
        .apply(finalOptions)
        .listener(listener)
        .into(this)
}


/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    uri: Uri?,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Int = -1,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    loadUrl(uri, options, context.findDrawable(placeHolder), cacheMode = cacheMode)
}

/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    uri: Uri?,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Drawable?,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    loadUrl(uri, null, options, placeHolder, cacheMode = cacheMode)
}


/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    uri: Uri?,
    listener: RequestListener<Drawable>? = null,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Int = -1,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    loadUrl(uri, listener, options, context.findDrawable(placeHolder), cacheMode = cacheMode)
}


/**
 * 加载图片Url
 */
@SuppressLint("CheckResult")
fun ImageView.loadUrl(
    uri: Uri?,
    listener: RequestListener<Drawable>? = null,
    options: RequestOptions = RequestOptions.centerCropTransform(),
    placeHolder: Drawable?,
    cacheMode: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC
) {
    if (uri == null) {
        return
    }

    val finalOptions = if (placeHolder == null) {
        RequestOptions().apply(options)
    } else {
        RequestOptions.placeholderOf(placeHolder).apply(options)
    }

    Glide.with(context)
        .load(uri)
        .diskCacheStrategy(cacheMode)
        .apply(finalOptions)
        .listener(listener)
        .into(this)
}

fun ImageView.startAnimation() {
    val drawable = drawable
    if (drawable !is AnimationDrawable) {
        return
    }
    drawable.start()
}

fun ImageView.stopAnimation() {
    val drawable = drawable
    if (drawable !is AnimationDrawable) {
        return
    }
    drawable.stop()
    clearAnimation()
}