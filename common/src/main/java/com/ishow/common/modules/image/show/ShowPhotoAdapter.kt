/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.modules.image.show

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ishow.common.R
import com.ishow.common.extensions.loadUrl
import com.ishow.common.utils.DeviceUtils
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher
import java.util.*
import kotlin.math.min

/**
 * 查看大图
 */
class ShowPhotoAdapter internal constructor(private val mContext: Context) : PagerAdapter(), PhotoViewAttacher.OnViewTapListener {
    private val mLayoutInflater: LayoutInflater
    private var mUrls: List<String>
    /**
     * 点击的View 用来获取大小
     */
    private var mBeforeView: View? = null
    /**
     * 缩略图的配置
     */
    private val mThumbLayoutParams: LayoutParams
    /**
     * 进度条配置
     */
    private val mProgressParams: LayoutParams
    /**
     * 当前的Dialog
     */
    private var mDialog: ShowPhotoDialog? = null

    private var isShowThumb = true

    init {
        mUrls = ArrayList()
        mLayoutInflater = LayoutInflater.from(mContext)
        mThumbLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        mThumbLayoutParams.gravity = Gravity.CENTER

        mProgressParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        mProgressParams.gravity = Gravity.CENTER
    }

    fun setDialog(dialog: ShowPhotoDialog) {
        mDialog = dialog
    }

    fun setData(url: List<String>?) {
        if (url == null) {
            mUrls = ArrayList()
        } else {
            mUrls = url
        }
        notifyDataSetChanged()
    }

    fun setShowThumb(showThumb: Boolean) {
        isShowThumb = showThumb
    }

    fun setBeforeView(image: View?) {
        mBeforeView = image
        if (mBeforeView != null) {
            var width = mBeforeView!!.width
            var height = mBeforeView!!.height
            val screen = DeviceUtils.screenSize
            mThumbLayoutParams.width = width
            mThumbLayoutParams.height = height

            width = min(screen[0] / 6, width * 3 / 7)
            height = min(screen[1] / 6, height * 3 / 7)

            mProgressParams.width = width
            mProgressParams.height = height
        }
    }

    override fun getCount(): Int {
        return mUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }


    @SuppressLint("CheckResult")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val url = mUrls[position]
        val root = mLayoutInflater.inflate(R.layout.item_show_photo, container, false)
        val imageHD = root.findViewById<PhotoView>(R.id.image)
        imageHD.onViewTapListener = this

        val imageThumb = root.findViewById<PhotoView>(R.id.thumbnails)
        imageThumb.isEnabled = false
        imageThumb.onViewTapListener = this
        imageThumb.layoutParams = mThumbLayoutParams
        if (mBeforeView is ImageView) {
            val image = mBeforeView as ImageView?
            imageThumb.scaleType = image!!.scaleType
        }

        val progress = root.findViewById<ProgressBar>(R.id.progress)
        progress.layoutParams = mProgressParams

        if (isShowThumb) {
            progress.visibility = View.VISIBLE
            imageThumb.visibility = View.VISIBLE
            imageThumb.loadUrl(url)
        } else {
            progress.visibility = View.GONE
            imageThumb.visibility = View.GONE
        }

        imageHD.loadUrl(url, ImageListener(imageThumb, progress))
        container.addView(root)
        return root
    }

    override fun destroyItem(container: ViewGroup, position: Int, child: Any) {
        container.removeView(child as View)
    }

    override fun onViewTap(view: View, v: Float, v1: Float) {
        mDialog?.dismiss()
        mDialog = null
    }


    internal class ImageListener(val imageThumb: ImageView, val progress: View) : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            imageThumb.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            imageThumb.isEnabled = true
            progress.visibility = View.GONE
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            imageThumb.visibility = View.GONE
            progress.visibility = View.GONE
            return false
        }

    }
}