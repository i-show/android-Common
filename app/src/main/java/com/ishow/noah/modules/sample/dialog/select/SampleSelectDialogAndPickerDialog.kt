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

package com.ishow.noah.modules.sample.dialog.select

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.ishow.common.entries.utils.IUnitPicker
import com.ishow.common.entries.utils.IUnitSelect
import com.ishow.common.widget.dialog.picker.PickerDialog
import com.ishow.common.widget.dialog.select.SelectDialog
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_sample_dialog_select_and_picker.*

import java.util.ArrayList

/**
 * Created by yuhaiyang on 2017/6/13.
 * 选择弹框和滚动选择额
 */

class SampleSelectDialogAndPickerDialog : AppBaseActivity(), View.OnClickListener {

    private val datas: MutableList<DemoEntry>
        get() {
            val entryList = ArrayList<DemoEntry>()
            for (i in 1..15) {
                entryList.add(DemoEntry(i))
            }
            return entryList
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_dialog_select_and_picker)
    }

    override fun initViews() {
        super.initViews()
        select.setOnClickListener(this)
        picker.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.select -> testSelect()
            R.id.picker -> testPicker()
        }
    }

    private fun testSelect() {
        val dialog = SelectDialog<DemoEntry>(this)
        dialog.data = datas
        dialog.setOnSelectedListener {
            select.text = it.getTitle(this@SampleSelectDialogAndPickerDialog)
        }
        dialog.show()
    }

    private fun testPicker() {
        val dialog = PickerDialog<DemoEntry>(this)
        dialog.setData(datas)
        dialog.setOnSelectedListener {
            picker.text = it.getTitle(context)
        }

        dialog.show()
    }


    //偷懒Entry 写在这里
    private inner class DemoEntry internal constructor(internal var day: Int) : IUnitSelect, IUnitPicker {

        override fun getTitle(context: Context): String {
            return "$day 天"
        }

        override fun getSubTitle(context: Context): String? {
            return null
        }
    }

}
