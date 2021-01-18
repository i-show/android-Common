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

package com.ishow.common.widget.dialog.picker


import android.content.Context
import android.os.Bundle
import android.view.View
import com.ishow.common.R
import com.ishow.common.databinding.DialogUnitPickerBinding
import com.ishow.common.entries.utils.IUnitPicker
import com.ishow.common.extensions.binding
import com.ishow.common.widget.TopBar
import com.ishow.common.widget.dialog.BaseDialog

/**
 * Created by yuhaiyang on 2016/10/31.
 * Picker 选择的Dialog
 */

class PickerDialog<T : IUnitPicker>(context: Context) : BaseDialog(context, R.style.Theme_Dialog_Bottom_Transparent),
    TopBar.OnTopBarListener {
    private val mAdapter: PickerDialogAdapter<T> = PickerDialogAdapter(getContext())
    private var mPickedListener: ((T) -> Unit?)? = null
    private var mCurrentPosition: Int = 0
    private val binding: DialogUnitPickerBinding by binding()

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromBottom(true)
        binding.topBar.setOnTopBarListener(this)
        binding.picker.setAdapter(mAdapter)
        binding.picker.currentPosition = mCurrentPosition
    }

    fun setData(data: MutableList<T>) {
        mAdapter.data = data
    }

    fun setCurrentPosition(position: Int) {
        mCurrentPosition = position
        binding.picker.currentPosition = mCurrentPosition
    }

    fun setOnSelectedListener(listener: (T) -> Unit) {
        mPickedListener = listener
    }

    interface OnPickedListener<T : IUnitPicker> {
        fun onSelectPicked(t: T)
    }

    override fun onLeftClick(v: View) {
        dismiss()
    }

    override fun onRightClick(v: View) {
        val position = binding.picker.currentPosition
        val item = mAdapter.getItem(position)
        mPickedListener?.let { it(item) }
        dismiss()
    }

    override fun onTitleClick(v: View) {

    }
}
