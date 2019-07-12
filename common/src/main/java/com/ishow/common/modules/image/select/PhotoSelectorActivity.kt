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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R
import com.ishow.common.app.activity.BaseBindActivity
import com.ishow.common.databinding.ActivityPhotoSelectorBinding
import com.ishow.common.entries.Folder
import com.ishow.common.entries.Photo
import com.ishow.common.utils.AnimatorUtils
import com.ishow.common.utils.DateUtils
import com.ishow.common.widget.recyclerview.itemdecoration.SpacingDecoration
import kotlinx.android.synthetic.main.activity_photo_selector.*
import java.util.*

/**
 * Created by Bright.Yu on 2017/1/23.
 * 选择照片的Activity
 */

class PhotoSelectorActivity : BaseBindActivity<ActivityPhotoSelectorBinding>(), View.OnClickListener {
    private lateinit var mPhotoAdapter: PhotoSelectorAdapter
    private lateinit var mFolderAdapter: FolderSelectorAdapter

    private var mMaxCount: Int = 0
    private var mMode: Int = 0
    private var mSelectedFolder: Folder? = null


    private val mSelectedChangedListener = object : PhotoSelectorAdapter.OnSelectedChangedListener {
        override fun onSelectedChanged(selectCount: Int) {
            /*
            if (selectCount <= 0) {
                mRightTextView.isEnabled = false
                mRightTextView.setText(R.string.complete)
            } else if (mMode == Photo.Key.MODE_SINGLE) {
                mRightTextView.isEnabled = true
                mRightTextView.setText(R.string.complete)
            } else {
                mRightTextView.isEnabled = true
                val count = getString(R.string.link_complete, selectCount, mMaxCount)
                mRightTextView.text = count
            }
            */
        }

    }

    private val mScrollListener = object : RecyclerView.OnScrollListener() {
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
            /*
            if (timeLine.alpha >= 0.15f) {
                val image = mPhotoAdapter.getItem(mLayoutManager.findFirstVisibleItemPosition())
                timeLine.text = DateUtils.formatFriendly(context, image.modifyDate * 1000)
            }
            */
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PhotoSelector)
        bindContentView(R.layout.activity_photo_selector)
        mBindingView.vm = getViewModel(PhotoSelectorViewModel::class.java)
        mBindingView.vm?.init(context = this, maxCount = mMaxCount, mode = mMode)


        mBindingView.list.addItemDecoration(SpacingDecoration(context, R.dimen.photo_selector_item_gap))
        mBindingView.list.adapter = mPhotoAdapter
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
        val intent = intent
        mMaxCount = intent.getIntExtra(Photo.Key.EXTRA_SELECT_COUNT, Photo.Key.DEFAULT_MAX_COUNT)
        mMode = intent.getIntExtra(Photo.Key.EXTRA_SELECT_MODE, Photo.Key.MODE_MULTI)
        if (mMode == Photo.Key.MODE_SINGLE) mMaxCount = 1
    }

    override fun initViews() {
        super.initViews()

        mFolderAdapter = FolderSelectorAdapter(context)

        mPhotoAdapter = PhotoSelectorAdapter(context, mMaxCount)
        mPhotoAdapter.setSelectedChangedListener { mBindingView.vm?.onPhotoSelectStatusChanged(context, it) }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.folderView -> selectPhotoFolder()
        }
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        setResult()
    }

    /**
     * 选择图片文件夹
     */
    private fun selectPhotoFolder() {
        /*
        BaseDialog dialog = new BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom)
                .fromBottom(true)
                .setAdapter(mFolderAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        Folder folder = mFolderAdapter.getItem(which);
                        updatePhotos(folder);
                    }
                }).create();
        dialog.show();
        */
    }

    private fun updatePhotos(folder: Folder) {

        if (folder == mSelectedFolder) {
            Log.i(TAG, "updatePhotos: is same folder")
            return
        }

        folder.isSelected = true
        if (mSelectedFolder != null) {
            mSelectedFolder!!.isSelected = false
        }
        mSelectedFolder = folder

        folderView.text = folder.getName()
        mPhotoAdapter.data = folder.getPhotoList()
        mFolderAdapter.notifyDataSetChanged()
        list.scrollToPosition(0)
    }


    private fun setResult() {
        val photoList = mPhotoAdapter.selectedPhotos
        when (mMode) {
            Photo.Key.MODE_SINGLE -> {
                if (photoList.isEmpty()) {
                    Log.i(TAG, "setResult: photoList is null or empty")
                    finish()
                    return
                }
                val photo = photoList[0]
                // 返回已选择的图片数据
                val singleData = Intent()
                singleData.putExtra(Photo.Key.EXTRA_RESULT, photo.getPath())
                setResult(Activity.RESULT_OK, singleData)
                finish()
            }
            Photo.Key.MODE_MULTI -> {
                if (photoList.isEmpty()) {
                    Log.i(TAG, "setResult: photoList is null or empty")
                    finish()
                    return
                }

                val photoPaths = ArrayList<String>()
                for (photo in photoList) {
                    photoPaths.add(photo.getPath())
                }

                // 返回已选择的图片数据
                val multiData = Intent()
                multiData.putStringArrayListExtra(Photo.Key.EXTRA_RESULT, photoPaths)
                setResult(Activity.RESULT_OK, multiData)
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "PhotoSelectorActivity"
    }


}
