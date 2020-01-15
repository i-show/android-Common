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

package com.ishow.noah.modules.sample.detail.dialog.select

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.ishow.common.entries.utils.IUnitPicker
import com.ishow.common.entries.utils.IUnitSelect
import com.ishow.common.extensions.dp2px
import com.ishow.common.widget.dialog.picker.PickerDialog
import com.ishow.common.widget.dialog.select.SelectDialog
import com.ishow.common.widget.recyclerview.itemdecoration.ColorDecoration
import com.ishow.noah.BR
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleDataPickerBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.android.synthetic.main.fragment_sample_data_picker.*
import java.util.*

/**
 * Created by yuhaiyang on 2017/6/13.
 * 选择弹框和滚动选择额
 */

class SampleDataPickerDialogFragment : AppBindFragment<FragmentSampleDataPickerBinding, AppBaseViewModel>() {

    private val dataList: MutableList<DemoEntry>
        get() {
            val entryList = ArrayList<DemoEntry>()
            for (i in 1..15) {
                entryList.add(DemoEntry(i))
            }
            return entryList
        }

    override fun getLayout(): Int {
        return R.layout.fragment_sample_data_picker
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.select -> testSelect()
            R.id.picker -> testPicker()
        }
    }

    private fun testSelect() {
        val context = context!!
        val itemDecoration = ColorDecoration(context)
        itemDecoration.setPadding(20, 20)

        val dialog = SelectDialog<DemoEntry>(context)
        dialog.maxHeight = 200.dp2px()
        dialog.itemDecoration = itemDecoration
        dialog.adapter.addLayout(BR.item, R.layout.item_sample_dialog_selet)
        dialog.data = dataList
        dialog.setOnSelectedListener {
            select.text = it.getTitle(context)
        }
        dialog.show()
    }

    private fun testPicker() {
        val context = context!!
        val dialog = PickerDialog<DemoEntry>(context)
        dialog.setData(dataList)
        dialog.setOnSelectedListener {
            picker.text = it.getTitle(context)
        }

        dialog.show()
    }
}

//偷懒Entry 写在这里
class DemoEntry(var day: Int) : IUnitSelect,
    IUnitPicker {
    override fun gravity(): Int {
        return Gravity.START and Gravity.CENTER_VERTICAL
    }

    override fun getTitle(context: Context): String {
        return "$day 天"
    }

    override fun getSubTitle(context: Context): String? {
        return null
    }
}

