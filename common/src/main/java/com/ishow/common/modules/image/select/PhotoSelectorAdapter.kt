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

import android.content.Context
import android.util.Log
import androidx.annotation.IntRange

import com.ishow.common.adapter.BindAdapter
import com.ishow.common.databinding.ItemPhotoSelectorBinding
import com.ishow.common.entries.Photo

import java.util.ArrayList
import android.R.id.mask
import com.ishow.common.R
import com.ishow.common.utils.ToastUtils


/**
 * Created by Bright.Yu on 2017/1/23.
 * 图片选择期的Adapter
 */

internal class PhotoSelectorAdapter(context: Context) : BindAdapter<Photo>(context) {

    private val mSelectedPhotos: MutableList<Photo>
    private var mSelectedChangedListener: OnSelectedChangedListener? = null
    private var mMaxCount: Int = 0

    /**
     * 获取 选中照片
     */
    val selectedPhotos: List<Photo>
        get() = mSelectedPhotos

    init {
        mSelectedPhotos = ArrayList()
    }

    fun setMaxCount(@IntRange(from = 1) maxCount: Int) {
        mMaxCount = maxCount
    }

    override fun onBindViewHolder(holder: BindHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder.binding is ItemPhotoSelectorBinding) {
            holder.binding.position = position
            holder.binding.listener = actionsListener
        }
    }

    /**
     * 选中状态修改的Listener
     */
    internal interface OnSelectedChangedListener {
        fun onSelectedChanged(selectCount: Int)
    }

    fun setSelectedChangedListener(listener: OnSelectedChangedListener) {
        mSelectedChangedListener = listener
    }

    private fun notifySelectedChanged() {
        mSelectedChangedListener?.onSelectedChanged(mSelectedPhotos.size)
    }

    /**
     * 选择照片
     */
    private fun selectPhoto(position: Int, entry: Photo) {
        val alreadyCount = mSelectedPhotos.size
        if (alreadyCount >= mMaxCount && !entry.isSelected) {
            val tip = context.getString(R.string.already_select_max, mMaxCount)
            ToastUtils.show(context, tip)
            return
        }

        entry.isSelected = !entry.isSelected
        if (entry.isSelected) {
            mSelectedPhotos.add(entry)
        } else {
            mSelectedPhotos.remove(entry)
        }

        notifyItemChanged(position)
        notifySelectedChanged()
    }

    private val actionsListener = object : PhotoActionListener {
        override fun onClickPhotoStatus(position: Int, photo: Photo) {
            selectPhoto(position, photo)
        }
    }


}
