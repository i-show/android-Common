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
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.ishow.common.extensions.binding
import com.ishow.common.extensions.toJSON
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.IntentUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.log.LogUtils
import com.ishow.noah.R
import com.ishow.noah.databinding.DialogVersionBinding
import com.ishow.noah.entries.Version
import com.ishow.noah.manager.VersionManager
import java.io.File

/**
 * Created by yuhaiyang on 2017/8/1.
 * 版本信息的Dialog
 */

class VersionDialog(context: Context) : Dialog(context, R.style.Theme_Dialog_Transparent), View.OnClickListener {

    private val binding :DialogVersionBinding by binding()
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        binding.cancel.setOnClickListener(this)
        binding.submit.setOnClickListener(this)
        init()
    }

    private fun init() {
        val version = VersionManager.instance.getVersion()
        if (version == null) {
            Log.i(TAG, "init: version is null")
            dismiss()
            return
        }


        binding.message.text = version.getDescription(context)

        if (version.isForceUpdate) {
            binding.cancel.visibility = View.GONE
            binding.ignore.visibility = View.GONE
            binding.submit.setBackgroundResource(R.drawable.base_dialog_button_whole)
        } else {
            binding.ignore.visibility = View.VISIBLE
            binding.cancel.visibility = View.VISIBLE
            binding.cancel.setBackgroundResource(R.drawable.base_dialog_button_left)
            binding.submit.setBackgroundResource(R.drawable.base_dialog_button_right)
        }
    }

    override fun show() {
        super.show()
        val window = window
        if (window == null) {
            LogUtils.i(TAG, "window is null")
            return
        }
        val lp = window.attributes
        lp.width = (DeviceUtils.screenSize[0] * 0.8f).toInt()
        window.attributes = lp
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancel -> {
                ignoreVersion()
                dismiss()
            }
            R.id.submit -> {
                val version = VersionManager.instance.getVersion()
                IntentUtils.gotoBrowser(context, version?.downloadPath)
                dismiss()
            }
        }
    }


    /**
     * 忽略当前版本
     */
    private fun ignoreVersion() {
        StorageUtils.save(Version.Key.IGNORE_NOW, true)

        if (binding.ignore.isChecked) {
            return
        }

        val version = VersionManager.instance.getVersion()
        if (version == null) {
            Log.i(TAG, "init: version is null")
            return
        }

        StorageUtils.save(Version.Key.IGNORE_VERSION, version.toJSON())
    }

    companion object {
        private const val TAG = "VersionDialog"

        fun installApp(context: Context, file: File) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val apkUri: Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                apkUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            } else {
                apkUri = Uri.fromFile(file)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            }

            context.startActivity(intent)
        }
    }
}
