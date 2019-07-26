package com.ishow.common.app.viewmodel

import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.modules.binding.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            mainThread { _loadingDialog.value = true }
        }
    }

    fun dismissLoading() {
        if (isMainThread()) {
            _loadingDialog.value = false
        } else {
            mainThread { _loadingDialog.value = false }
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
}