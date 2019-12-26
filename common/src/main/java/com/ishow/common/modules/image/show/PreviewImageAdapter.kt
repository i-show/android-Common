package com.ishow.common.modules.image.show

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.ishow.common.R
import com.ishow.common.extensions.inflate
import com.ishow.common.extensions.loadUrl

/**
 * Created by yuhaiyang on 2019-12-25.
 *
 */

class PreviewImageAdapter<T>(val dialog: PreviewImageDialog<*>) : RecyclerView.Adapter<ViewHolder>() {

    private var _data: MutableList<T> = mutableListOf()
    var data: MutableList<T>?
        get() = _data
        set(value) = setData(value, true)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(parent.inflate(R.layout.item_preview_image))
        holder.image.setOnPhotoTapListener { _, _, _ -> dialog.dismiss() }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val data = getItem(position)) {
            is Drawable -> {
                holder.image.setImageDrawable(data)
            }
            is Uri -> {
                holder.image.loadUrl(data)
            }
            is String -> {
                holder.image.loadUrl(data)
            }
            else -> {
                throw IllegalStateException("Data Type Error")
            }
        }
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    fun getItem(position: Int): T {
        return _data[position]
    }

    fun setData(dataList: MutableList<T>?, force: Boolean) {
        if (dataList != null) {
            _data = dataList
            notifyDataSetChanged()
        } else if (force) {
            val size = _data.size
            _data.clear()
            notifyItemRangeRemoved(0, size)
        }
    }
}

class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val image: PhotoView = view.findViewById(R.id.image)
}