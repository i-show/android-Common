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

package com.ishow.common.widget.pickview


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.ishow.common.R
import com.ishow.common.widget.TopBar
import com.ishow.common.widget.dialog.BaseDialog
import kotlinx.android.synthetic.main.dialog_date_time_picker.*

/**
 * 日期选择对话框
 */
class DateTimePickerDialog @JvmOverloads constructor(context: Context, private val mStyle: Int = DateTimePicker.Style.DATE_TIME) :
        BaseDialog(context, R.style.Theme_Dialog_DateTimePicker), TopBar.OnTopBarListener {

    private var mListener: OnSelectDateListener? = null
    private var mTime: Long = 0
    private var mTopBarString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_date_time_picker)
        // topBar.setOnTopBarListener(this)
        // topBar.setText(mTopBarString)

        picker.setStyle(mStyle)
        if (mTime != 0L) {
            picker.setCurrentDate(mTime)
        }
    }

    override fun setTitle(@StringRes resid: Int) {
        mTopBarString = context.getString(resid)
    }

    fun setDate(time: Long) {
        if (time == 0L) {
            return
        }
        mTime = time
    }

    override fun onLeftClick(v: View) {
        dismiss()
    }

    override fun onRightClick(v: View) {
        mListener?.onSelected(picker.currentTimeInMillis)
        dismiss()
    }

    override fun onTitleClick(v: View) {

    }

    fun setSelectDateListener(listener: OnSelectDateListener) {
        mListener = listener
    }

    interface OnSelectDateListener {
        fun onSelected(time: Long)
    }
}