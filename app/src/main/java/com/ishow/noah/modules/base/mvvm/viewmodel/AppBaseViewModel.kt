package com.ishow.noah.modules.base.mvvm.viewmodel

import android.app.Application
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Loading.Companion.randomTag
import com.ishow.noah.entries.http.AppHttpResponse


abstract class AppBaseViewModel(application: Application) : BaseViewModel(application) {

    /**
     * 前后增加Loading处理数据
     * @param autoDismiss 是否自动取消Loading
     */
    fun <T> requestResponse(
        loading: Loading? = Loading.dialog(),
        loadingTag: Boolean = false,
        autoDismiss: Boolean = true,
        error: Error? = Error.toast(),
        block: () -> AppHttpResponse<T>
    ): AppHttpResponse<T> {
        loading?.let {
            randomTag(it, loadingTag)
            showLoading(it)
        }
        val result: AppHttpResponse<T> = block()
        
        if (autoDismiss) loading?.let { dismissLoading(it) }

        if (!result.isSuccess()) {
            error?.let {
                it.message = result.message
                showError(it)
            }
        }
        return result
    }

    /**
     * 前后增加Loading处理数据
     * @param autoDismiss 是否自动取消Loading
     */
    fun <T> request(
        loading: Loading? = Loading.dialog(),
        autoDismiss: Boolean = true,
        toastError: Boolean = true,
        block: () -> AppHttpResponse<T>
    ): T? {
        loading?.let { showLoading(it) }

        val result: AppHttpResponse<T> = block()
        if (autoDismiss) dismissLoading()

        return if (result.isSuccess()) {
            result.data
        } else {
            if (toastError) toast(result.message)
            null
        }
    }
}

