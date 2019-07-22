package com.ishow.common.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    /**
     * LoadingDialog的状态
     */
    protected open val loadingDialogStatus = MutableLiveData<Boolean>()
    val loadingDialog: LiveData<Boolean>
        get() = loadingDialogStatus
}