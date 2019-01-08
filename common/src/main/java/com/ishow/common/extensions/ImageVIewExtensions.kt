package com.ishow.common.extensions

import android.text.TextUtils
import android.widget.ImageView
import com.ishow.common.utils.image.loader.ImageLoader

/**
 * 加载图片Url
 */
fun ImageView.loadUrl(url: String?, mode: Int = ImageLoader.LoaderMode.CENTER_CROP, placeHolder: Int = android.R.color.transparent) {
    if (TextUtils.isEmpty(url)) {
        return
    }
    ImageLoader.with(context)
            .load(url!!)
            .mode(mode)
            .placeholder(placeHolder)
            .into(this)
}