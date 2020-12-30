package com.ishow.noah.modules.base.mvvm.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.extensions.mainThread
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.noah.entries.http.AppPageResponse
import kotlinx.coroutines.launch

/**
 * Created by yuhaiyang on 2019-09-15.
 * Pull2Refresh的ViewModel
 */
open class Pull2RefreshViewModel(app: Application) : AppBaseViewModel(app) {
    /**
     * Loading的状态
     */
    private val _pull2refreshStatus = MutableLiveData<Event<Pull2RefreshStatus>>()
    val pull2refreshStatus: LiveData<Event<Pull2RefreshStatus>>
        get() = _pull2refreshStatus


    open fun onLoadData(v: View, pager: Int, refresh: Boolean) {

    }

    fun <T> pull2refresh(
        page: Int = DEFAULT_START_PAGE,
        liveData: MutableLiveData<MutableList<T>>,
        loading: Boolean = true,
        block: suspend () -> AppPageResponse<T>
    ) {
        val showLoading = loading && page == DEFAULT_START_PAGE
        if (showLoading) showPull2RefreshLoading()

        viewModelScope.launch {
            val result: AppPageResponse<T> = block()
            parseResult(result, page, showLoading, liveData)

            if (!result.isSuccess) {
                if (showLoading) showError(Error.view(result.code, result.message))
            }
        }
    }


    private fun <T> parseResult(
        result: AppPageResponse<T>,
        page: Int,
        showLoading: Boolean = false,
        liveData: MutableLiveData<MutableList<T>>
    ) {
        if (result.isSuccess) {
            parseSuccessResult(result, page, showLoading, liveData)
        } else {
            parseFailedResult(result, page, showLoading)
        }
    }

    private fun <T> parseSuccessResult(
        result: AppPageResponse<T>,
        page: Int,
        showLoading: Boolean = false,
        liveData: MutableLiveData<MutableList<T>>? = null
    ) {
        val isRefresh = page == DEFAULT_START_PAGE
        if (isRefresh) {
            _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshSuccess)
            liveData?.value = result.listData

            if (result.isLastPage) {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreEnd)
            } else {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreNormal)
            }

            if (result.listData.isNullOrEmpty()) {
                showEmpty()
            } else if (showLoading) {
                dismissPull2RefreshLoading()
            }

        } else {
            if (result.isLastPage) {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreEnd)
            } else {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreSuccess)
            }
            val data = liveData?.value ?: mutableListOf()
            result.listData?.let { data.addAll(it) }
            liveData?.value = data
        }

    }


    internal fun notifyRefreshSuccess() = mainThread {
        _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshSuccess)
    }

    internal fun notifyRefreshFailed() = mainThread {
        _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshFailed)
    }

    internal fun notifyLoadEnd() = mainThread {
        _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreEnd)
    }

    internal fun notifyLoadSuccess() = mainThread {
        _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreSuccess)
    }

    internal fun notifyLoadFailed() = mainThread {
        _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreFailed)
    }

    private fun <T> parseFailedResult(result: AppPageResponse<T>, page: Int, showLoading: Boolean = false) {
        if (page == DEFAULT_START_PAGE) {
            _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshFailed)
            if (showLoading) showError(Error.view())
        } else {
            _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreFailed)
        }
        toast(result.message)
    }

    /**
     * 显示Pull2Refresh空数据之前的loading
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected open fun showPull2RefreshLoading() = showLoading(Loading.view())

    /**
     * 隐藏Pull2Refresh空数据之前的loading
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected open fun dismissPull2RefreshLoading() = dismissLoading(Loading.dismissView())


    enum class Pull2RefreshStatus {
        RefreshSuccess,
        RefreshFailed,
        LoadMoreNormal,
        LoadMoreSuccess,
        LoadMoreFailed,
        LoadMoreEnd,
    }

    companion object {
        const val DEFAULT_START_PAGE = 1
    }
}