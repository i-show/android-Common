package com.ishow.common.app.viewmodel

import android.app.Application
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishow.common.modules.binding.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * 注意这里用的是ApplicationContext
     */
    protected val context: Context = application.applicationContext
    /**
     * LoadingDialog的状态
     */
    private val _loadingDialog = MutableLiveData<Boolean>()
    val loadingDialog: LiveData<Boolean>
        get() = _loadingDialog

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>>
        get() = _toastMessage


    fun showLoading() {
        if (isMainThread()) {
            _loadingDialog.value = true
        } else {
            runOnMainThread { _loadingDialog.value = true }
        }
    }

    fun dismissLoading() {
        if (isMainThread()) {
            _loadingDialog.value = false
        } else {
            runOnMainThread { _loadingDialog.value = false }
        }
    }

    fun showToast(message: String?) {
        if (message.isNullOrEmpty()) {
            return
        }
        if (isMainThread()) {
            _toastMessage.value = Event(message)
        } else {
            runOnMainThread { _toastMessage.value = Event(message) }
        }
    }

    /**
     * 判断是否在主线程
     */
    fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    /**
     * 通过协程  在主线程上运行
     */
    private fun runOnMainThread(block: () -> Unit) = GlobalScope.launch(Dispatchers.Main) {
        block()
    }
}