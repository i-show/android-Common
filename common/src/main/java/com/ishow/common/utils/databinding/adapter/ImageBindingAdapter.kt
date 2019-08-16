package com.ishow.common.utils.databinding.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView

import androidx.databinding.BindingAdapter

import com.bumptech.glide.request.RequestOptions
import com.facebook.drawee.view.SimpleDraweeView
import com.ishow.common.extensions.loadUrl

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
    fun loadImage(view: ImageView, imageUrl: String, placeholder: Drawable?) {
        view.loadUrl(imageUrl, RequestOptions.centerCropTransform(), placeholder)
    }


    @JvmStatic
    @BindingAdapter("frescoUrl")
    fun loadImage(view: SimpleDraweeView, imageUrl: String) {
        if (imageUrl.startsWith("http")) {
            view.setImageURI(imageUrl)
        } else {
            view.setImageURI("file://$imageUrl")
        }
    }
}
