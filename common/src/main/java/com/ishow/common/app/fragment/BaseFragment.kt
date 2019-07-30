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

import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.ishow.common.entries.status.Empty
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.dialog
import com.ishow.common.mvp.base.IViewStatus
import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.common.widget.StatusView
import com.ishow.common.widget.TopBar
import com.ishow.common.widget.loading.LoadingDialog

abstract class BaseFragment : Fragment(), StatusView.OnStatusViewListener, IViewStatus, TopBar.OnTopBarListener {

    /**
     * Loading的Dialog
     */
    protected var mLoadingDialog: LoadingDialog? = null
    /**
     * 状态的View
     */
    protected var mStatusView: StatusView? = null
    /**
     * 用来回收的Handler
     */
    protected var mHandler: Handler? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
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
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
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
                    mLoadingDialog = LoadingDialog.show(activity, mLoadingDialog)
                }
                Loading.Type.View -> {
                    mStatusView?.showLoading()
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
                    LoadingDialog.dismiss(mLoadingDialog)
                }
                Loading.Type.View -> {
                    mStatusView?.dismiss()
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
                    mStatusView?.showError()
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
        activity?.runOnUiThread { mStatusView?.showEmpty() }
    }
}
