package com.ishow.common.utils.databinding.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.facebook.drawee.view.SimpleDraweeView
import com.ishow.common.extensions.loadUrl
import com.ishow.common.utils.glide.corner.GlideCorner


object ImageViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["imageUrl", "placeholder", "glideCorner", "glideCornerPosition", "glideOptions"],
        requireAll = false
    )
    fun loadImage(
        view: ImageView,
        imageUrl: String?,
        placeholder: Drawable?,
        corner: Int?,
        position: GlideCorner.Position? = GlideCorner.Position.All,
        requestOptions: RequestOptions? = RequestOptions.centerCropTransform()
    ) {

        val glidePosition = position ?: GlideCorner.Position.All
        val options = if (corner == null) {
            RequestOptions.centerCropTransform()
        } else {
            RequestOptions().transform(CenterCrop(), GlideCorner(corner, glidePosition))
        }

        requestOptions?.let { options.apply(it) }

        view.loadUrl(imageUrl, options, placeholder)
    }


    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: SimpleDraweeView, imageUrl: String) {
        if (imageUrl.startsWith("http")) {
            view.setImageURI(imageUrl)
        } else {
            view.setImageURI("file://$imageUrl")
        }
    }
}
