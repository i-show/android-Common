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

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.ishow.common.R
import com.ishow.common.databinding.WidgetShowPhotoBinding
import com.ishow.common.extensions.binding
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.widget.dialog.BaseDialog

/**
 * 查看大图的Dialog
 */
class ShowPhotoDialog(context: Context) : BaseDialog(context, R.style.Theme_Dialog_Black) {
    /**
     * String格式的Url
     */
    private var urlList: MutableList<String> = mutableListOf()

    /**
     * Android 10以后的Uri
     */
    private var uriList: MutableList<Uri> = mutableListOf()
    private var mCurrentPosition: Int = 0
    private var isShowThumb = true
    private val binding: WidgetShowPhotoBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resetStatusBar()
        setContentView(R.layout.widget_show_photo)
        val adapter = ShowPhotoAdapter(context)
        adapter.setData(urlList)
        adapter.setDialog(this)
        adapter.setShowThumb(isShowThumb)

        binding.pager.adapter = adapter
        binding.pager.currentItem = mCurrentPosition

        binding.indicator.setViewPager(binding.pager)
        // 只有一张图片的时候不需要显示指示器
        if (urlList.size == 1) {
            binding.indicator.visibility = View.GONE
        }
    }

    fun setData(url: String?) {
        if (!url.isNullOrEmpty()) {
            urlList.add(url)
        }
    }

    fun setData(urls: MutableList<String>?) {
        if (urls == null) {
            urlList.clear()
        } else {
            urlList = urls
        }
    }

    fun setData(uri: Uri?) {
        uri?.let { uriList.add(uri) }
    }


    fun setShowThumb(showThumb: Boolean) {
        isShowThumb = showThumb
    }

    fun setCurrentPosition(position: Int) {
        mCurrentPosition = position
    }


    override fun show() {
        super.show()
        val screen = DeviceUtils.screenSize
        val window = window
        val lp = window!!.attributes
        lp.width = screen[0]
        lp.height = screen[1]
        window.attributes = lp
    }

    private fun resetStatusBar() {
        val window = window
        window!!.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }
}
