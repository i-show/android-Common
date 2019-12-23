/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.ishow.common.modules.image.select

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ishow.common.R
import com.ishow.common.app.mvvm.view.BindActivity
import com.ishow.common.databinding.ActivityPhotoSelectorBinding
import com.ishow.common.entries.Image
import com.ishow.common.extensions.showFragment
import com.ishow.common.extensions.toast

/**
 * Created by Bright.Yu on 2017/1/23.
 * 选择照片的Activity
 */

class ImageSelectorActivity : BindActivity<ActivityPhotoSelectorBinding, ImageSelectorViewModel>() {

    lateinit var viewModel: ImageSelectorViewModel
    private var maxCount: Int = 0
    private var mode: Int = 0
    private val listFragment = ImageListFragment()
    private var previewFragment: ImagePreviewFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PhotoSelector)
        bindContentView(R.layout.activity_photo_selector)
    }


    override fun initViewModel(vm: ImageSelectorViewModel) {
        super.initViewModel(vm)
        vm.init(mode, maxCount)
        viewModel = vm
        showFragment(listFragment)
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        val intent: Intent = intent
        maxCount = intent.getIntExtra(Image.Key.EXTRA_SELECT_COUNT, Image.Key.DEFAULT_MAX_COUNT)
        mode = intent.getIntExtra(Image.Key.EXTRA_SELECT_MODE, Image.Key.MODE_MULTI)
        if (mode == Image.Key.MODE_SINGLE) maxCount = 1
    }

    /**
     * 预览当前选中的图片
     */
    fun previewSelected() {
        val imageList = viewModel.selectedImages.value
        if (imageList.isNullOrEmpty()) {
            toast(R.string.please_select_image)
            return
        }
        viewModel.setPreviewImage(imageList.get(0), 0)
        preview(-1)
    }

    fun preview(position: Int) {
        if (previewFragment == null) {
            previewFragment = ImagePreviewFragment.newInstance(position)
        }

        if (position != -1) {
            previewFragment?.setCurrentItem(position)
        }

        replaceFragment(previewFragment, listFragment)
    }

    override fun onBackPressed() {
        if (previewFragment?.isVisible == true) {
            viewModel.removeCancelSelectImage()
            replaceFragment(listFragment, previewFragment)
        } else {
            super.onBackPressed()
        }
    }


    /**
     * 显示Fragment
     */
    private fun replaceFragment(showFragment: Fragment?, hideFragment: Fragment? = null) {
        val transaction = supportFragmentManager.beginTransaction()

        if (hideFragment != null) {
            transaction.hide(hideFragment)
        }

        if (showFragment == null) {
            transaction.commit()
            return
        }

        if (showFragment.isAdded) {
            transaction.show(showFragment)
        } else {
            transaction.add(R.id.fragmentContainer, showFragment)
        }
        transaction.commit()
    }
}
