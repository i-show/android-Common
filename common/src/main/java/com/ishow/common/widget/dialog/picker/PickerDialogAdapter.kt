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


import com.ishow.common.entries.utils.IUnitPicker
import com.ishow.common.widget.pickview.adapter.PickerAdapter

import java.util.ArrayList


internal class PickerDialogAdapter<T : IUnitPicker>(private val mContext: Context) : PickerAdapter<T>() {

    private var mData: MutableList<T> = ArrayList()

    var data: MutableList<T>
        get() = mData
        set(data) {
            mData = data
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): T {
        return mData[position]
    }

    override fun getItemText(position: Int): String {
        val picker = mData[position]
        return picker.getTitle(mContext)
    }

}