package com.ishow.noah.modules.base.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Loading.Companion.randomTag
import com.ishow.noah.entries.http.AppHttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class AppBaseViewModel(application: Application) : BaseViewModel(application) {

    /**
     * 前后增加Loading处理数据
     * @param autoDismiss 是否自动取消Loading
     */
    suspend fun <T> requestResponse(
        loading: Loading? = Loading.dialog(),
        loadingTag: Boolean = false,
        autoDismiss: Boolean = true,
        error: Error? = Error.toast(),
        block: suspend () -> AppHttpResponse<T>
    ): AppHttpResponse<T> {
        loading?.let {
            randomTag(it, loadingTag)
            showLoading(it)
        }
        val result: AppHttpResponse<T> = block()

        if (autoDismiss) loading?.let { dismissLoading(it) }

        if (!result.isSuccess) {
            error?.let {
                it.errorCode = result.code
                it.message = result.message
                showError(it)
            }
        }
        return result
    }

    suspend fun withLoading(
        loading: Loading? = Loading.dialog(),
        loadingTag: Boolean = false,
        autoDismiss: Boolean = true,
        block: suspend () -> Unit
    ) {
        loading?.let {
            randomTag(it, loadingTag)
            showLoading(it)
        }

        block()
        if (autoDismiss) loading?.let { dismissLoading(it) }
    }

    /**
     * 前后增加Loading处理数据
     * @param autoDismiss 是否自动取消Loading
     */
    suspend fun <T> request(
        loading: Loading? = Loading.dialog(),
        loadingTag: Boolean = false,
        autoDismiss: Boolean = true,
        error: Error? = Error.toast(),
        block: suspend () -> AppHttpResponse<T>
    ): T? {
        loading?.let {
            randomTag(it, loadingTag)
            showLoading(it)
        }

        val result: AppHttpResponse<T> = block()

        if (autoDismiss) loading?.let { dismissLoading(it) }

        return if (result.isSuccess) {
            result.data
        } else {
            error?.let {
                it.errorCode = result.code
                it.message = result.message
                showError(it)
            }
            null
        }
    }
}

