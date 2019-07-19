package com.ishow.common.app.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.ishow.common.app.viewmodel.BaseViewModel

abstract class BaseBindActivity<T : ViewDataBinding> : BaseActivity() {
    protected lateinit var mBindingView: T

    protected open fun bindContentView(layoutId: Int): T {
        mBindingView = DataBindingUtil.setContentView(this, layoutId)
        mBindingView.lifecycleOwner = this
        return mBindingView
    }

    protected open fun <VM : BaseViewModel> getViewModel(viewModelClass: Class<VM>): VM {
        val vm = ViewModelProviders.of(this).get(viewModelClass)
        val activity = this@BaseBindActivity
        vm.run {
            loadingDialog.observe(activity, Observer { if (it) showLoading() else dismissLoading() })
        }
        return vm
    }
}