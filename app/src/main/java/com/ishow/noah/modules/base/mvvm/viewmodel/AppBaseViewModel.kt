package com.ishow.noah.modules.base.mvvm.viewmodel

import android.app.Application
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.noah.entries.http.AppHttpResponse


abstract class AppBaseViewModel(application: Application) : BaseViewModel(application) {

    /**
     * 前后增加Loading处理数据
     * @param autoDismiss 是否自动取消Loading
     */
    fun <T> requestResponse(
        loading: Boolean = true,
        autoDismiss: Boolean = true,
        toastError: Boolean = true,
        block: () -> AppHttpResponse<T>
    ): AppHttpResponse<T> {
        if (loading) showLoading()
        val result: AppHttpResponse<T> = block()
        if (autoDismiss) dismissLoading()
        if (toastError && !result.isSuccess()) showError(result.message)
        return result
    }

    /**
     * 前后增加Loading处理数据
     * @param autoDismiss 是否自动取消Loading
     */
    fun <T> request(loading: Boolean = true, autoDismiss: Boolean = true, toastError: Boolean = true, block: () -> AppHttpResponse<T>): T? {
        if (loading) showLoading()

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

