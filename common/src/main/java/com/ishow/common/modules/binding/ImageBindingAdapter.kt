package com.ishow.common.modules.binding

import android.util.Log
import android.widget.ImageView

import androidx.databinding.BindingAdapter

import com.bumptech.glide.Glide
import com.facebook.drawee.view.SimpleDraweeView

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context)
            .load(imageUrl)
           .into(view)
    }


    @JvmStatic
    @BindingAdapter("frescoUrl")
    fun loadImage(view: SimpleDraweeView, imageUrl: String) {
        if(imageUrl.startsWith("http")) {
            view.setImageURI(imageUrl)
        }else{
            view.setImageURI("file://$imageUrl")
        }
    }


}
