package com.ishow.common.app.mvvm.viewmodel

import android.app.Application
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.ishow.common.entries.status.Empty
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.isMainThread
import com.ishow.common.extensions.mainThread
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.common.utils.StringUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    /**
     * 注意这里用的是ApplicationContext
     */
    protected val context: Context = application.applicationContext

    /**
     * Loading的状态
     */
    private val _loadingStatus = MutableLiveData<Event<Loading>>()
    val loadingStatus: LiveData<Event<Loading>>
        get() = _loadingStatus

    /**
     * 错误的状态
     */
    private val _errorStatus = MutableLiveData<Event<Error>>()
    val errorStatus: LiveData<Event<Error>>
        get() = _errorStatus

    /**
     * 成功的状态
     */
    private val _successStatus = MutableLiveData<Event<Success>>()
    val successStatus: LiveData<Event<Success>>
        get() = _successStatus

    /**
     * 空数据状态
     */
    private val _emptyStatus = MutableLiveData<Event<Empty>>()
    val emptyStatus: LiveData<Event<Empty>>
        get() = _emptyStatus

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage

    /**
     * 初始化
     */
    open fun init() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onDestroy() {
    }

    /**
     * 重试
     */
    open fun retryRequest() {}

    /**
     * 显示加载动画
     */
    fun showLoading() {
        showLoading(Loading.dialog())
    }

    fun showLoading(loading: Loading) {
        if (isMainThread()) {
            _loadingStatus.value = Event(loading)
        } else {
            mainThread { _loadingStatus.value = Event(loading) }
        }
    }

    /**
     * 隐藏加载动画
     */
    fun dismissLoading() {
        dismissLoading(Loading.dismiss())
    }

    fun dismissLoading(loading: Loading) {
        loading.status = Loading.Status.Dismiss
        if (isMainThread()) {
            _loadingStatus.value = Event(loading)
        } else {
            mainThread { _loadingStatus.value = Event(loading) }
        }
    }

    /**
     * 展示错误信息
     */
    fun showError(message: String? = StringUtils.EMPTY) {
        showError(Error.toast(message))
    }

    fun showError(@StringRes messageRes: Int) {
        showError(Error.toast(messageRes))
    }

    fun showError(error: Error) {
        if (isMainThread()) {
            _errorStatus.value = Event(error)
        } else {
            mainThread { _errorStatus.value = Event(error) }
        }
    }

    /**
     * 展示成功的信息
     */
    fun showSuccess(code: Int = 0, message: String? = StringUtils.EMPTY) {
        showSuccess(Success.new(code, message))
    }

    fun showSuccess(@StringRes messageRes: Int) {
        showSuccess(Success.new(messageRes = messageRes))
    }

    fun showSuccess(success: Success) {
        if (isMainThread()) {
            _successStatus.value = Event(success)
        } else {
            mainThread { _successStatus.value = Event(success) }
        }
    }

    /**
     * 展示成功的信息
     */
    fun showEmpty(message: String? = StringUtils.EMPTY) {
        showEmpty(Empty.new(message))
    }

    fun showEmpty(@StringRes messageRes: Int) {
        showEmpty(Empty.new(messageRes))
    }

    fun showEmpty(empty: Empty) {
        if (isMainThread()) {
            _emptyStatus.value = Event(empty)
        } else {
            mainThread { _emptyStatus.value = Event(empty) }
        }
    }

    fun toast(message: String?) {
        if (message.isNullOrEmpty()) {
            return
        }
        if (isMainThread()) {
            _toastMessage.value = Event(message)
        } else {
            mainThread { _toastMessage.value = Event(message) }
        }
    }

    fun toast(@StringRes message: Int) {
        toast(context.getString(message))
    }


}