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

package com.ishow.common.app.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ishow.common.R
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


abstract class BaseActivity : AppCompatActivity(), StatusView.OnStatusViewListener, IViewStatus,
    TopBar.OnTopBarListener {


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

    protected var isActivityPaused: Boolean = false
    /**
     * Activity
     * isResumed 已经被占用..
     */
    var isActivityResumed: Boolean = false

    val context: Context
        get() = this

    //************************ 生命周期 区域*********************** //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetStatusBar()
    }


    override fun onResume() {
        super.onResume()
        isActivityResumed = true
        isActivityPaused = false
    }

    override fun onPause() {
        super.onPause()
        isActivityResumed = false
        isActivityPaused = true
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消请求接口
        Http.cancel(this)
        // 清除Handler预防内存泄露
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    //************************ 初始化 区域*********************** //
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initNecessaryData()
        initViews()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        initNecessaryData()
        initViews(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        initNecessaryData()
        initViews(view, params)
    }

    protected open fun initViews() {
        // 主动设置TopBar
        var topBarView: View? = findViewById(R.id.top_bar)
        if (topBarView == null) {
            topBarView = findViewById(R.id.topBar)
        }
        if (topBarView is TopBar) {
            topBarView.setOnTopBarListener(this)
        }

        // 主动设置statusView
        var statusView: View? = findViewById(R.id.status_view)
        if (statusView == null) {
            statusView = findViewById(R.id.statusView)
        }
        if (statusView is StatusView) {
            mStatusView = statusView
            mStatusView?.setOnStatusViewListener(this)
        }
    }

    protected open fun initViews(view: View) {}

    protected open fun initViews(view: View, params: ViewGroup.LayoutParams) {}

    /**
     * 有一些数据要在initViews之前处理的在这个方法中处理
     */
    protected open fun initNecessaryData() {}

    /**
     * TopBar的左侧点击事件
     */
    override fun onLeftClick(v: View) {
        onBackPressed()
    }

    /**
     * TopBar的右侧点击事件
     */
    override fun onRightClick(v: View) {

    }

    /**
     * TopBar的中间Title点击事件
     */
    override fun onTitleClick(v: View) {

    }

    /**
     * 用来设置全屏样式
     */
    protected open fun resetStatusBar() {}

    override fun showLoading() {
        showLoading(Loading.dialog())
    }

    override fun showLoading(loading: Loading) {
        runOnUiThread {
            when (loading.type) {
                Loading.Type.Dialog -> {
                    mLoadingDialog = LoadingDialog.show(this@BaseActivity, mLoadingDialog)
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
        runOnUiThread {
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
        runOnUiThread {
            when (error.showType) {
                Error.Type.Dialog -> {
                    if (error.message.isNullOrEmpty()) {
                        dialog(error.messageRes)
                    } else {
                        dialog(error.message!!)
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
        runOnUiThread { mStatusView?.showEmpty() }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {

    }

    companion object {
        private const val TAG = "BaseActivity"
        /**
         * Activity的TYPE
         */
        const val KEY_TYPE = "activity_type"
    }
}

