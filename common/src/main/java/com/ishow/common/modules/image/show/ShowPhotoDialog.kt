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
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.ishow.common.R
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.widget.dialog.BaseDialog
import kotlinx.android.synthetic.main.widget_show_photo.*

/**
 * 查看大图的Dialog
 */
class ShowPhotoDialog(context: Context) : BaseDialog(context, R.style.Theme_Dialog_Black) {
    private var mUrls: MutableList<String> = ArrayList()
    private var mBeforeView: View? = null
    private var mCurrentPosition: Int = 0
    private var isShowThumb = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetStatusBar()
        setContentView(R.layout.widget_show_photo)
        val adapter = ShowPhotoAdapter(context)
        adapter.setBeforeView(mBeforeView)
        adapter.setData(mUrls)
        adapter.setDialog(this)
        adapter.setShowThumb(isShowThumb)

        pager.adapter = adapter
        pager.currentItem = mCurrentPosition

        indicator.setViewPager(pager)
        // 只有一张图片的时候不需要显示指示器
        if (mUrls.size == 1) {
            indicator.visibility = View.GONE
        }
    }

    fun setData(url: String?) {
        if (!url.isNullOrEmpty()) {
            mUrls.add(url)
        }
    }

    fun setData(urls: MutableList<String>?) {
        if (urls == null) {
            mUrls.clear()
        } else {
            mUrls = urls
        }
    }

    /**
     * 如果不需要 显示缩略图就不要设置
     */
    fun setBeforeView(view: View) {
        mBeforeView = view
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

        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}
