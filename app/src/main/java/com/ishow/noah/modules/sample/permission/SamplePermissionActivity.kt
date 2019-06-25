/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.sample.permission

import android.Manifest
import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.toast

import com.ishow.common.utils.permission.PermissionDenied
import com.ishow.common.utils.permission.PermissionGranted
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity

/**
 * Created by Bright.Yu on 2017/2/8.
 * 权限测试
 */

class SamplePermissionActivity : AppBaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_permission)
    }

    override fun initViews() {
        super.initViews()

        val view = findViewById<View>(R.id.sample_select_permission_sdcard)
        view.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sample_select_permission_sdcard -> requestReadingSdcardPermission()
        }
    }

    private fun requestReadingSdcardPermission() {
        PermissionManager.with(this)
            .requestCode(REQUEST_READING_SDCARD_PERMISSION)
            .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .send()
    }

    @PermissionGranted(REQUEST_READING_SDCARD_PERMISSION)
    private fun hasReadingSdcardPermission() {
        toast("已经获取到权限")
    }

    @PermissionDenied(REQUEST_READING_SDCARD_PERMISSION)
    private fun readingSdcardPermissionDenied() {
        toast("权限已经被拒绝")
    }

    companion object {
        /**
         * reading sdcard
         */
        private const val REQUEST_READING_SDCARD_PERMISSION = 100
    }

}
