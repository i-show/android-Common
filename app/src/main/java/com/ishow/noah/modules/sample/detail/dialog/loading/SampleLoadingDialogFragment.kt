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

package com.ishow.noah.modules.sample.detail.dialog.loading

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ishow.common.extensions.delay
import com.ishow.common.widget.loading.LoadingDialog
import com.ishow.noah.App

import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleLoadingDialogBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Bright.Yu on 2017/2/8.
 * 权限测试
 */

class SampleLoadingDialogFragment : AppBindFragment<FSampleLoadingDialogBinding, AppBaseViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_loading_dialog

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.request1 -> requestLoading()
            R.id.custom -> showCustomLoading()

        }
    }

    private fun showCustomLoading() {
        LoadingDialog.customLayout = R.layout.dialog_custom_loading
        loadingDialog = LoadingDialog.show(context, loadingDialog, "tag1")

        delay(5000) {
            LoadingDialog.dismiss(loadingDialog, "tag1")
        }

    }

    private fun requestLoading() {
        LoadingDialog.customLayout = null

        requestLoading1()
        requestLoading2()
        requestLoading3()
    }

    private fun requestLoading1() {
        loadingDialog = LoadingDialog.show(context, loadingDialog, "tag1")
        lifecycleScope.launch {
            delay(5000)
            Log.i(TAG, "requestLoading1: dismiss1")
            mainThread { LoadingDialog.dismiss(loadingDialog, "tag1") }
        }
    }

    private fun requestLoading2() {
        loadingDialog = LoadingDialog.show(context, loadingDialog, "tag2")
        lifecycleScope.launch {
            delay(10000)
            Log.i(TAG, "requestLoading1: dismiss2")
            mainThread { LoadingDialog.dismiss(loadingDialog, "tag2") }
        }
    }

    private fun requestLoading3() {
        loadingDialog = LoadingDialog.show(context, loadingDialog, "tag3")
        lifecycleScope.launch {
            delay(15000)
            Log.i(TAG, "requestLoading1: dismiss3")
            mainThread { LoadingDialog.dismiss(loadingDialog, "tag3") }
        }
    }

    companion object {
        private const val TAG = "Sample"
    }

}
