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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.BR
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.app.activity.BindActivity
import com.ishow.common.databinding.ActivityPhotoSelectorBinding
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Photo
import com.ishow.common.utils.AnimatorUtils
import com.ishow.common.utils.DateUtils
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.common.widget.recyclerview.itemdecoration.SpacingDecoration
import kotlinx.android.synthetic.main.activity_photo_selector.*
import java.util.*

/**
 * Created by Bright.Yu on 2017/1/23.
 * 选择照片的Activity
 */

class PhotoSelectorActivity : BindActivity<ActivityPhotoSelectorBinding>() {

    private lateinit var mViewModel: PhotoSelectorViewModel
    private lateinit var mPhotoAdapter: PhotoSelectorAdapter

    private var mMaxCount: Int = 0
    private var mMode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PhotoSelector)
        bindContentView(R.layout.activity_photo_selector)
        mViewModel = getViewModel(PhotoSelectorViewModel::class.java)
        mViewModel.init(context = this, maxCount = mMaxCount, mode = mMode)

        mBindingView.vm = mViewModel
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        val intent: Intent = intent
        mMaxCount = intent.getIntExtra(Photo.Key.EXTRA_SELECT_COUNT, Photo.Key.DEFAULT_MAX_COUNT)
        mMode = intent.getIntExtra(Photo.Key.EXTRA_SELECT_MODE, Photo.Key.MODE_MULTI)
        if (mMode == Photo.Key.MODE_SINGLE) mMaxCount = 1
    }

    override fun initViews() {
        super.initViews()
        mPhotoAdapter = PhotoSelectorAdapter(context, mMaxCount)
        mPhotoAdapter.setSelectedChangedListener { mBindingView.vm?.onPhotoSelectStatusChanged(context, it) }
        list.addItemDecoration(SpacingDecoration(context, R.dimen.photo_selector_item_gap))
        list.adapter = mPhotoAdapter
        list.addOnScrollListener(scrollListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        list.removeOnScrollListener(scrollListener)
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        setResult()
    }


    fun onViewClick(v: View) {
        when (v.id) {
            R.id.folderView -> selectPhotoFolder()
        }
    }

    /**
     * 选择图片文件夹
     */
    private fun selectPhotoFolder() {
        val adapter = BindAdapter<Folder>(context)
        adapter.addLayout(R.layout.item_photo_selector_folder, BR.folder)
        adapter.data = mBindingView.vm?.folderList?.value!!

        BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom2)
                .fromBottom(true)
                .setWidthProportion(1F)
                .setAdapter(adapter) { _, which -> updatePhotos(adapter.getItem(which)) }
                .create()
                .show()
    }

    private fun updatePhotos(folder: Folder) {
        val currentFolder = mViewModel.currentFolder.value
        if (folder == currentFolder) {
            Log.i(TAG, "updatePhotos: is same folder")
            return
        }

        folder.isSelected = true
        currentFolder?.isSelected = false
        mViewModel.updateCurrentFolder(folder)

        mPhotoAdapter.data = folder.getPhotoList()
        folderView.text = folder.getName()
        list.scrollToPosition(0)
    }


    private fun setResult() {
        val photoList: MutableList<Photo> = mPhotoAdapter.selectedPhotos
        if (photoList.isEmpty()) {
            Log.i(TAG, "setResult: photoList is null or empty")
            finish()
            return
        }

        when (mMode) {
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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                timeLine.clearAnimation()
                AnimatorUtils.alpha(timeLine, timeLine.alpha, 0f, 800)
            } else {
                timeLine.clearAnimation()
                AnimatorUtils.alpha(timeLine, timeLine.alpha, 1.0f, 800)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            if (timeLine.alpha >= 0.15f) {
                val image = mPhotoAdapter.getItem(layoutManager.findFirstVisibleItemPosition())
                timeLine.text = DateUtils.formatFriendly(context, image.modifyDate * 1000)
            }
        }

    }

    companion object {
        private const val TAG = "PhotoSelectorActivity"
    }


}
