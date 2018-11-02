package com.ishow.common.extensions

import android.widget.ImageView
import com.ishow.common.utils.image.loader.ImageLoader

/**
 * 加载图片Url
 */
fun ImageView.loadUrl(url: String) {
    ImageLoader.with(context)
            .load(url)
            .into(this)
}