package com.ishow.common.extensions

import android.widget.ImageView
import com.ishow.common.R
import com.ishow.common.utils.image.loader.ImageLoader

/**
 * 加载图片Url
 */
fun ImageView.loadUrl(
    url: String?,
    mode: Int = ImageLoader.LoaderMode.CENTER_CROP,
    placeHolder: Int = R.color.transparent
) {
    if (url.isNullOrEmpty()) {
        return
    }
    ImageLoader.with(context)
        .load(url)
        .mode(mode)
        .placeholder(placeHolder)
        .into(this)
}