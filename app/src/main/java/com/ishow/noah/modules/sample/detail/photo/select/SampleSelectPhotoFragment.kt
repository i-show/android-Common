/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.sample.detail.photo.select

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.modules.image.show.ShowPhotoDialog
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.image.select.OnSelectImageListener
import com.ishow.common.utils.image.select.SelectPhotoUtils
import com.ishow.common.widget.recyclerview.itemdecoration.SpacingDecoration
import com.ishow.noah.BR
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleSelectPhotoBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.android.synthetic.main.fragment_sample_select_photo.*
import java.io.File

/**
 * Created by Bright.Yu on 2017/1/15.
 * 选择图片
 */

class SampleSelectPhotoFragment : AppBindFragment<FragmentSampleSelectPhotoBinding, AppBaseViewModel>(),
    OnSelectImageListener {

    private lateinit var mSelectPhotoUtils: SelectPhotoUtils
    private lateinit var mAdapter: BindAdapter<String>

    override fun getLayout(): Int = R.layout.fragment_sample_select_photo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity as FragmentActivity

        mSelectPhotoUtils = SelectPhotoUtils(context, SelectPhotoUtils.SelectMode.SINGLE)
        mSelectPhotoUtils.fragment = this
        mSelectPhotoUtils.setOnSelectPhotoListener(this)

        mAdapter = BindAdapter()
        mAdapter.setOnItemClickListener { showPhoto(it) }
        mAdapter.addLayout(BR.imgUrl, R.layout.item_sample_select_photo)

        list.addItemDecoration(SpacingDecoration(context, R.dimen.photo_selector_item_gap))
        list.adapter = mAdapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSelectPhotoUtils.onActivityResult(requestCode, resultCode, data)
    }


    fun onViewClick(v: View) {
        when (v.id) {
            R.id.singleCompress -> {
                mSelectPhotoUtils.setSelectMode(SelectPhotoUtils.SelectMode.SINGLE)
                mSelectPhotoUtils.select(Bitmap.CompressFormat.PNG)
            }
            R.id.singleCrop -> {
                mSelectPhotoUtils.setSelectMode(SelectPhotoUtils.SelectMode.SINGLE)
                mSelectPhotoUtils.select(1, 1, Bitmap.CompressFormat.JPEG)
            }
            R.id.multi -> {
                mSelectPhotoUtils.setSelectMode(SelectPhotoUtils.SelectMode.MULTIPLE)
                mSelectPhotoUtils.select(9)
            }
        }
    }

    private fun showPhoto(position: Int) {
        val path = mAdapter.getItem(position)
        val dialog = ShowPhotoDialog(context!!)
        dialog.setData(path)
        dialog.show()
    }

    override fun onSelectedPhoto(imageList: MutableList<File?>, image: File?) {
        mAdapter.data = imageList.map { it?.absolutePath ?: StringUtils.EMPTY }.toMutableList()
    }
}
