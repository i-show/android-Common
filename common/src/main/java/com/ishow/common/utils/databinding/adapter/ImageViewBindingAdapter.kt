package com.ishow.common.utils.databinding.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.facebook.drawee.view.SimpleDraweeView
import com.ishow.common.extensions.loadUrl
import com.ishow.common.utils.glide.corner.GlideCorner


object ImageViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["imageUrl", "placeHolder", "glideCorner", "glideCornerPosition", "glideOptions", "cacheMode"],
        requireAll = false
    )
    fun loadImage(
        view: ImageView,
        imageUrl: String?,
        placeholder: Drawable?,
        corner: Int?,
        position: GlideCorner.Position? = GlideCorner.Position.All,
        requestOptions: RequestOptions? = RequestOptions.centerCropTransform(),
        cacheMode: DiskCacheStrategy? = DiskCacheStrategy.AUTOMATIC
    ) {
        val glidePosition = position ?: GlideCorner.Position.All
        val options = if (corner == null) {
            RequestOptions.centerCropTransform()
        } else {
            RequestOptions().transform(CenterCrop(), GlideCorner(corner, glidePosition))
        }
        requestOptions?.let { options.apply(it) }

        if (cacheMode == null) {
            view.loadUrl(imageUrl, options, placeholder, DiskCacheStrategy.AUTOMATIC)
        } else {
            view.loadUrl(imageUrl, options, placeholder, cacheMode)
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["imageUrl", "placeHolder", "glideCorner", "glideCornerPosition", "glideOptions", "cacheMode"],
        requireAll = false
    )
    fun loadImage(
        view: ImageView,
        uri: Uri?,
        placeholder: Drawable?,
        corner: Int?,
        position: GlideCorner.Position? = GlideCorner.Position.All,
        requestOptions: RequestOptions? = RequestOptions.centerCropTransform(),
        cacheMode: DiskCacheStrategy? = DiskCacheStrategy.AUTOMATIC
    ) {

        val glidePosition = position ?: GlideCorner.Position.All
        val options = if (corner == null) {
            RequestOptions.centerCropTransform()
        } else {
            RequestOptions().transform(CenterCrop(), GlideCorner(corner, glidePosition))
        }

        requestOptions?.let { options.apply(it) }

        view.loadUrl(uri, options, placeholder, cacheMode ?: DiskCacheStrategy.AUTOMATIC)
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

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: SimpleDraweeView, imageUrl: Uri) {
        view.setImageURI(imageUrl.toString())
    }
}
