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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ishow.common.R
import com.ishow.common.app.mvvm.view.BindActivity
import com.ishow.common.databinding.ActivityPhotoSelectorBinding
import com.ishow.common.entries.Photo
import com.ishow.common.extensions.showFragment
import com.ishow.common.extensions.toast
import java.util.*

/**
 * Created by Bright.Yu on 2017/1/23.
 * 选择照片的Activity
 */

class ImageSelectorActivity : BindActivity<ActivityPhotoSelectorBinding>() {

    lateinit var viewModel: ImageSelectorViewModel
    private var maxCount: Int = 0
    private var mode: Int = 0
    private val listFragment = ImageListFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PhotoSelector)
        bindContentView(R.layout.activity_photo_selector)
        bindViewModel(ImageSelectorViewModel::class.java) {
            it.init(mode, maxCount)
            dataBinding.vm = it
            viewModel = it
            showFragment(listFragment)
        }
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        val intent: Intent = intent
        maxCount = intent.getIntExtra(Photo.Key.EXTRA_SELECT_COUNT, Photo.Key.DEFAULT_MAX_COUNT)
        mode = intent.getIntExtra(Photo.Key.EXTRA_SELECT_MODE, Photo.Key.MODE_MULTI)
        if (mode == Photo.Key.MODE_SINGLE) maxCount = 1
    }


    override fun onRightClick(v: View) {
        super.onRightClick(v)
        setResult()
    }

    private fun setResult() {
        val photoList: MutableList<Photo> = viewModel.selectedPhotos
        if (photoList.isEmpty()) {
            Log.i(TAG, "setResult: photoList is null or empty")
            toast(R.string.please_select_image)
            return
        }

        when (mode) {
            Photo.Key.MODE_SINGLE -> setSingleResult(photoList)
            Photo.Key.MODE_MULTI -> setMultiResult(photoList)
        }
    }

    private fun setSingleResult(photoList: MutableList<Photo>) {
        val photo = photoList[0]
        val intent = Intent()
        intent.putExtra(Photo.Key.EXTRA_RESULT, photo.getPath())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setMultiResult(photoList: MutableList<Photo>) {
        val photoPaths: ArrayList<String> = photoList.map { it.path } as ArrayList<String>
        val intent = Intent()
        intent.putStringArrayListExtra(Photo.Key.EXTRA_RESULT, photoPaths)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        private const val TAG = "ImageSelectorActivity"
    }

}
