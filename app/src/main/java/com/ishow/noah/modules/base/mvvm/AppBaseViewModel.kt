package com.ishow.noah.modules.base.mvvm

import android.app.Application
import com.ishow.common.app.viewmodel.BaseViewModel
import com.ishow.noah.entries.http.AppHttpResponse


abstract class AppBaseViewModel(application: Application) : BaseViewModel(application) {

    /**
     * 前后增加Loading处理数据
     * @param autoDismiss 是否自动取消Loading
     */
    fun <T> withLoading(autoDismiss: Boolean = true, block: () -> AppHttpResponse<T>): AppHttpResponse<T> {
        showLoading()
        val result: AppHttpResponse<T> = block()
        if (autoDismiss) dismissLoading()
        return result
    }

}

