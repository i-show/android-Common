package com.ishow.common.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    protected open val _loadingDialog = MutableLiveData<Boolean>()
    val loadingDialog: LiveData<Boolean>
        get() = _loadingDialog
}