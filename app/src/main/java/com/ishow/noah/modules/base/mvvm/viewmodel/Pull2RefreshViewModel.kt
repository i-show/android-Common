package com.ishow.noah.modules.base.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.noah.entries.http.AppPageResponse

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

    fun pull2refresh(page: Int = DEFAULT_START_PAGE, loading: Boolean = true, block: () -> AppPageResponse<T>) {
        val showLoading = loading && page == DEFAULT_START_PAGE
        if (showLoading) showPull2RefreshLoading()
        val result: AppPageResponse<T> = block()

        mainThread { parseResult(result, page, showLoading) }
    }


    private fun parseResult(result: AppPageResponse<T>, page: Int, showLoading: Boolean = false) {
        if (result.isSuccess()) {
            parseSuccessResult(result, page, showLoading)
        } else {
            parseFailedResult(result, page, showLoading)
        }
    }

    private fun parseSuccessResult(result: AppPageResponse<T>, page: Int, showLoading: Boolean = false) {
        val isRefresh = page == DEFAULT_START_PAGE
        if (isRefresh) {
            _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshSuccess)
            _pull2refreshData.value = result.listData

            if (result.isLastPage == true) {
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
            if (result.isLastPage == true) {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreEnd)
            } else {
                _pull2refreshStatus.value = Event(Pull2RefreshStatus.LoadMoreSuccess)
            }
            val data = _pull2refreshData.value
            result.listData?.let { data?.addAll(it) }
            _pull2refreshData.value = data
        }

    }

    private fun parseFailedResult(result: AppPageResponse<T>, page: Int, showLoading: Boolean = false) {
        if (page == DEFAULT_START_PAGE) {
            _pull2refreshStatus.value = Event(Pull2RefreshStatus.RefreshFailed)
            if(showLoading) showError(Error.view())
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
        private const val DEFAULT_START_PAGE = 1
    }
}