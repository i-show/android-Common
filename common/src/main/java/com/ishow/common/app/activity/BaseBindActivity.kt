package com.ishow.common.app.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

abstract class BaseBindActivity<T : ViewDataBinding> : BaseActivity() {
    protected lateinit var mBindingView: T

    protected open fun bindContentView(layoutId: Int): T {
        mBindingView = DataBindingUtil.setContentView(this, layoutId)
        mBindingView.lifecycleOwner = this
        return mBindingView
    }

    protected open fun <VM : ViewModel> getViewModel(viewModelClass: Class<VM>): VM {
        return ViewModelProviders.of(this).get(viewModelClass)
    }
}