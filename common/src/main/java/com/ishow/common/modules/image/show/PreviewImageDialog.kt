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

package com.ishow.common.modules.image.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ishow.common.R
import com.ishow.common.adapter.BindAdapter
import kotlinx.android.synthetic.main.dialog_preview_image.*

/**
 * 查看大图的Dialog
 */
class PreviewImageDialog<T> : DialogFragment() {

    var dataList: MutableList<T>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_preview_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BindAdapter<T>()
        adapter.data = dataList

        pager.adapter = adapter
    }


    fun setData(data: T) {
        dataList = mutableListOf(data)
        pager?.let { it.adapter?.notifyDataSetChanged() }
    }


}
