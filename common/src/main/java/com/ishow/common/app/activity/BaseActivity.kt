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
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ishow.common.R
import com.ishow.common.app.mvp.IViewStatus
import com.ishow.common.entries.status.Empty
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.dialog
import com.ishow.common.extensions.toast
import com.ishow.common.manager.CCacheManager
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.common.widget.StatusView
import com.ishow.common.widget.TopBar
import com.ishow.common.widget.loading.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


abstract class BaseActivity : AppCompatActivity(), StatusView.OnStatusViewListener,
    IViewStatus,
    TopBar.OnTopBarListener {
    /**
     * Loading的Dialog
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var loadingDialog: LoadingDialog? = null
    /**
     * 状态的View
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var rootStatusView: StatusView? = null
    /**
     * 用来回收的Handler
     */
    protected var handler: Handler? = null

    protected var isActivityPaused: Boolean = false
    /**
     * Activity
     * isResumed 已经被占用..
     */
    var isActivityResumed: Boolean = false

    val context: Context
        get() = this

    val activity: BaseActivity
        get() = this

    val statusBarHeight: Int by lazy {
        DeviceUtils.getStatusBarHeight(activity)
    }

    val topBarHeight: Int by lazy {
        val typedValue = TypedValue()
        if (theme.resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        } else {
            0
        }
    }

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
        // 清除Handler预防内存泄露
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    //************************ 初始化 区域*********************** //
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        attachStatusView()
        initNecessaryData()
        initViews()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        attachStatusView()
        initNecessaryData()
        initViews()
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        attachStatusView()
        initNecessaryData()
        initViews()
    }

    /**
     * 有一些数据要在initViews之前处理的在这个方法中处理
     */
    protected open fun initNecessaryData() {}

    protected open fun initViews() {
        // 主动设置TopBar
        val topBarView: View? = findViewById(R.id.topBar)
        if (topBarView is TopBar) {
            topBarView.setOnTopBarListener(this)
        }

        // 主动设置statusView
        val statusView: View? = findViewById(R.id.statusView)
        if (statusView is StatusView) {
            this.rootStatusView = statusView
            this.rootStatusView?.setOnStatusViewListener(this)
        }
    }

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
                    loadingDialog = LoadingDialog.show(context, loadingDialog, loading.tag)
                }
                Loading.Type.View -> {
                    rootStatusView?.showLoading(loading.tag)
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
                    LoadingDialog.dismiss(loadingDialog, loading.tag)
                }
                Loading.Type.View -> {
                    rootStatusView?.dismiss(loading.tag)
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
                Error.Type.Toast -> {
                    if (error.message.isNullOrEmpty()) {
                        toast(error.messageRes)
                    } else {
                        toast(error.message!!)
                    }
                }
                Error.Type.View -> {
                    rootStatusView?.showError()
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        CCacheManager.cache(this)
    }

    override fun showSuccess() {
    }

    override fun showSuccess(success: Success) {
    }

    override fun showEmpty() {
        showEmpty(Empty.new())
    }

    override fun showEmpty(empty: Empty) {
        runOnUiThread { rootStatusView?.showEmpty() }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {

    }

    open fun attachStatusView() {
        if (!hasStatusView()) {
            return
        }
        StatusView.attachToActivity(activity, getStatusViewTopMargin())
    }

    /**
     * 获取statusView的TopMargin
     */
    open fun getStatusViewTopMargin(): Int {
        return statusBarHeight + topBarHeight
    }

    /**
     * 获取statusView的TopMargin
     */
    open fun hasStatusView(): Boolean {
        return false
    }

    /**
     * 判断是否在主线程
     */
    fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    /**
     * 通过协程  在主线程上运行
     */
    fun mainThread(block: () -> Unit) = GlobalScope.launch(Dispatchers.Main) {
        block()
    }

    companion object {
        /**
         * Activity的TYPE
         */
        const val KEY_TYPE = "activity_type"
    }
}

