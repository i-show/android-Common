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
import android.widget.CheckBox
import com.ishow.common.BR
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.databinding.ItemPhotoSelectorBinding
import com.ishow.common.entries.Photo
import com.ishow.common.utils.ToastUtils
import java.util.*


/**
 * Created by Bright.Yu on 2017/1/23.
 * 图片选择期的Adapter
 */

internal class PhotoSelectorAdapter(context: Context, private val maxCount: Int) : BindAdapter<Photo>(context) {
    /**
     * 获取 选中照片
     */
    private val _selectedPhotos: MutableList<Photo> = ArrayList()
    val selectedPhotos: List<Photo>
        get() = _selectedPhotos

    private var mSelectedChangedListener: ((Int) -> Unit)? = null


    init {
        addLayout(R.layout.item_photo_selector, BR.photo)
    }


    override fun onBindViewHolder(holder: BindHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder.binding is ItemPhotoSelectorBinding) {
            holder.binding.listener = actionsListener
        }
    }

    /**
     * 选中状态修改的Listener
     */
    internal interface OnSelectedChangedListener {
        fun onSelectedChanged(selectCount: Int)
    }

    fun setSelectedChangedListener(listener: ((Int) -> Unit)?) {
        mSelectedChangedListener = listener
    }


    /**
     * 选择照片
     */
    private fun selectPhoto(view: CheckBox, entry: Photo) {
        val alreadyCount = _selectedPhotos.size
        if (alreadyCount >= maxCount && !entry.isSelected) {
            val tip = context.getString(R.string.already_select_max, maxCount)
            ToastUtils.show(context, tip)
            return
        }

        entry.isSelected = !entry.isSelected
        if (entry.isSelected) {
            _selectedPhotos.add(entry)
        } else {
            _selectedPhotos.remove(entry)
        }
        view.isChecked = entry.isSelected

        mSelectedChangedListener?.let { it(selectedPhotos.size) }
    }

    private val actionsListener = object : PhotoActionListener {
        override fun onClickPhotoStatus(view: CheckBox, photo: Photo) {
            selectPhoto(view, photo)
        }
    }


}
