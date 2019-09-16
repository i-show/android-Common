package com.ishow.noah.modules.base.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.entries.status.Loading
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.noah.entries.http.AppHttpResponse
import com.ishow.noah.entries.http.AppPageResponse
import com.ishow.noah.entries.http.Page

/**
 * Created by yuhaiyang on 2019-09-15.
 * Pull2Refresh的ViewModel
 */
open class Pull2RefreshViewModel<T>(app: Application) : AppBaseViewModel(app) {
    /**
     * Loading的状态
     */
    private val _pull2refreshStatus = MutableLiveData<Event<Pull2RefreshStatus>>()
    val pull2refreshStatus: LiveData<Event<Pull2RefreshStatus>>
        get() = _pull2refreshStatus

    /**
     * 分页数据
     */
    private val _pull2refreshData = MutableLiveData<MutableList<T>>()
    val pull2refreshData: LiveData<MutableList<T>>
        get() = _pull2refreshData

    fun pull2refresh(
        page: Int = 1,
        loading: Boolean = true,
        block: () -> AppPageResponse<T>
    ): Page<T>? {
        if (loading && page == 1) pull2refreshShowLoading()
        val result: AppPageResponse<T> = block()
        if (loading && page == 1) pull2refreshDismissLoading()
        return if (result.isSuccess()) {
            if (page == 1) {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshSuccess)
            } else {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreSuccess)
            }
            result.data
        } else {
            if (page == 1) {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshFailed)
            } else {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreFailed)
            }
            toast(result.message)
            null
        }
    }

    /**
     * 显示Pull2Refresh空数据之前的loading
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun pull2refreshShowLoading() = showLoading()

    /**
     * 隐藏Pull2Refresh空数据之前的loading
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun pull2refreshDismissLoading() = dismissLoading()


    enum class Pull2RefreshStatus {
        RefreshSuccess,
        RefreshFailed,
        LoadMoreSuccess,
        LoadMoreFailed
    }
}