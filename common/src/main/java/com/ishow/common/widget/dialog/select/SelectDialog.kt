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

package com.ishow.common.widget.dialog.select


import android.content.Context
import android.os.Bundle
import com.ishow.common.BR
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.entries.utils.IUnitSelect
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.common.widget.recyclerview.itemdecoration.ColorDecoration
import kotlinx.android.synthetic.main.dialog_unit_select.*

/**
 * Created by yuhaiyang on 2016/10/31.
 * 一个统一的从底下弹出的Dialog选择
 */

class SelectDialog<T : IUnitSelect>(context: Context) : BaseDialog(context, R.style.Theme_Dialog_Bottom_Transparent) {
    private val mAdapter = BindAdapter<T>()
    private var mSelectedListener: ((T) -> Unit)? = null

    var data: MutableList<T>?
        get() = mAdapter.data
        set(data) {
            mAdapter.data = data
        }

    init {
        mAdapter.addLayout(BR.itemUnitSelect, R.layout.item_dialog_selet)
        mAdapter.setOnItemClickListener {
            notifySelectCityChanged(mAdapter.getItem(it))
            dismiss()
        }
        fromBottom(bottom = true)
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_unit_select)

        val decoration = ColorDecoration(context)
        decoration.showLastDivider = false

        list.addItemDecoration(ColorDecoration(context))
        list.adapter = mAdapter
    }


    private fun notifySelectCityChanged(selected: T) {
        mSelectedListener?.let { it(selected) }
    }

    fun setOnSelectedListener(listener: ((T) -> Unit)?) {
        mSelectedListener = listener
    }

    interface OnSelectedListener<T : IUnitSelect> {
        fun onSelected(t: T)
    }
}
