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

package com.ishow.common.app.fragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.ishow.common.R
import com.ishow.common.app.mvp.IViewStatus
import com.ishow.common.entries.status.Empty
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.dialog
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.common.widget.StatusView
import com.ishow.common.widget.TopBar
import com.ishow.common.widget.loading.LoadingDialog

abstract class BaseFragment : Fragment(), StatusView.OnStatusViewListener, IViewStatus, TopBar.OnTopBarListener {

    /**
     * Loading的Dialog
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var loadingDialog: LoadingDialog? = null
    /**
     * 状态的View
     */
    private var statusView: StatusView? = null
    /**
     * 用来回收的Handler
     */
    protected var handler: Handler? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statusView: View? = view.findViewById(R.id.statusView)
        if (statusView is StatusView) {
            this.statusView = statusView
            this.statusView?.setOnStatusViewListener(this)
        }
    }

    //************************ 重写 各种事件区域*********************** //

    /**
     * TopBar的左侧点击事件
     */
    override fun onLeftClick(v: View) {}

    /**
     * TopBar的右侧点击事件
     */
    override fun onRightClick(v: View) {}

    /**
     * TopBar的中间Title点击事件
     */
    override fun onTitleClick(v: View) {}


    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }


    override fun onStatusClick(v: View, which: StatusView.Which) {
    }

    override fun showLoading() {
        showLoading(Loading.dialog())
    }

    override fun showLoading(loading: Loading) {
        activity?.runOnUiThread {
            when (loading.type) {
                Loading.Type.Dialog -> {
                    loadingDialog = LoadingDialog.show(activity, loadingDialog)
                }
                Loading.Type.View -> {
                    statusView?.showLoading()
                }
            }
        }
    }

    override fun dismissLoading() {
        dismissLoading(Loading.dialog())
    }

    override fun dismissLoading(loading: Loading) {
        activity?.runOnUiThread {
            when (loading.type) {
                Loading.Type.Dialog -> {
                    LoadingDialog.dismiss(loadingDialog)
                }
                Loading.Type.View -> {
                    statusView?.dismiss()
                }
            }
        }
    }

    override fun showError(error: Error) {
        activity?.runOnUiThread {
            when (error.showType) {
                Error.Type.Dialog -> {
                    if (error.message.isNullOrEmpty()) {
                        dialog(error.messageRes)
                    } else {
                        dialog(error.message)
                    }
                }
                Error.Type.View -> {
                    statusView?.showError()
                }
            }
        }
    }

    override fun showSuccess() {
    }

    override fun showSuccess(success: Success) {
    }

    override fun showEmpty() {
        showEmpty(Empty.new())
    }

    override fun showEmpty(empty: Empty) {
        activity?.runOnUiThread { statusView?.showEmpty() }
    }
}
