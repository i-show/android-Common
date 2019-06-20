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

package com.ishow.noah.ui.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.fastjson.JSON
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.IntentUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.log.LogManager
import com.ishow.noah.R
import com.ishow.noah.entries.Version
import com.ishow.noah.manager.VersionManager
import kotlinx.android.synthetic.main.dialog_version.*

/**
 * Created by yuhaiyang on 2017/8/1.
 * 版本信息的Dialog
 */

class VersionDialog(context: Context) : Dialog(context, R.style.Theme_Dialog_Transparent), View.OnClickListener {


    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_version)
        cancel.setOnClickListener(this)
        submit.setOnClickListener(this)
        init()
    }

    private fun init() {
        val version = VersionManager.instance.getVersion(context)
        if (version == null) {
            Log.i(TAG, "init: version is null")
            dismiss()
            return
        }


        message.text = version.getDescription(context)

        if (version.isForceUpdate) {
            cancel.visibility = View.GONE
            ignore.visibility = View.GONE
            submit.setBackgroundResource(R.drawable.base_dialog_button_whole)
        } else {
            ignore.visibility = View.VISIBLE
            cancel.visibility = View.VISIBLE
            cancel.setBackgroundResource(R.drawable.base_dialog_button_left)
            submit.setBackgroundResource(R.drawable.base_dialog_button_right)
        }
    }

    override fun show() {
        super.show()
        val window = window
        if (window == null) {
            LogManager.i(TAG, "window is null")
            return
        }
        val lp = window.attributes
        lp.width = (DeviceUtils.getScreenSize()[0] * 0.8f).toInt()
        window.attributes = lp
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancel -> {
                ignoreVersion()
                dismiss()
            }
            R.id.submit -> {
                val version = VersionManager.instance.getVersion(context)
                IntentUtils.gotoBrowser(context, version?.downloadPath)
                dismiss()
            }
        }
    }


    /**
     * 忽略当前版本
     */
    private fun ignoreVersion() {
        StorageUtils.with(context)
                .param(Version.Key.IGNORE_NOW, true)
                .save()

        if (ignore.isChecked) {
            return
        }

        val version = VersionManager.instance.getVersion(context)
        if (version == null) {
            Log.i(TAG, "init: version is null")
            return
        }

        StorageUtils.with(context)
                .param(Version.Key.IGNORE_VERSION, JSON.toJSONString(version))
                .save()
    }

    companion object {
        private const val TAG = "VersionDialog"
    }
}
