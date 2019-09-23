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

package com.ishow.noah.modules.sample.detail.views.loading

import android.util.Log
import android.view.View
import com.ishow.common.widget.loading.LoadingDialog

import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleLoadingDialogBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.GlobalScope
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

        }
    }

    private fun requestLoading() {
        requestLoading1()
        requestLoading2()
        requestLoading3()
    }

    private fun requestLoading1() {
        loadingDialog = LoadingDialog.show(context, loadingDialog, "tag1")
        GlobalScope.launch {
            delay(5000)
            Log.i(TAG, "requestLoading1: dismiss1")
            mainThread { LoadingDialog.dismiss(loadingDialog, "tag1") }
        }
    }

    private fun requestLoading2() {
        loadingDialog = LoadingDialog.show(context, loadingDialog, "tag2")
        GlobalScope.launch {
            delay(10000)
            Log.i(TAG, "requestLoading1: dismiss2")
            mainThread { LoadingDialog.dismiss(loadingDialog, "tag2") }
        }
    }

    private fun requestLoading3() {
        loadingDialog = LoadingDialog.show(context, loadingDialog, "tag3")
        GlobalScope.launch {
            delay(15000)
            Log.i(TAG, "requestLoading1: dismiss3")
            mainThread { LoadingDialog.dismiss(loadingDialog, "tag3") }
        }
    }

    companion object {
        private const val TAG = "Sample"
    }

}
