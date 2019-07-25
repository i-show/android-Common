package com.ishow.common.app.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ishow.common.app.viewmodel.BaseViewModel
import com.ishow.common.extensions.inflate
import com.ishow.common.extensions.toast
import com.ishow.common.modules.binding.Event

abstract class BindActivity<T : ViewDataBinding> : BaseActivity() {
    protected lateinit var mBindingView: T

    protected open fun bindContentView(layoutId: Int): T {
        val view = inflate(layoutId)
        setContentView(view)
        mBindingView = DataBindingUtil.bind(view)!!
        mBindingView.lifecycleOwner = this
        return mBindingView
    }

    protected open fun <VM : BaseViewModel> getViewModel(viewModelClass: Class<VM>): VM {
        val vm = ViewModelProviders.of(this).get(viewModelClass)
        val activity = this@BindActivity
        vm.run {
            loadingDialog.observe(activity, Observer { changeLoadingDialogStatus(it) })
            toastMessage.observe(activity, Observer { showToast(it) })
            return vm
        }
    }

    /**
     * 改变LoadingDialog的状态
     */
    private fun changeLoadingDialogStatus(show: Boolean) {
        if (show) {
            showLoading()
        } else {
            dismissLoading()
        }
    }


    private fun showToast(event: Event<String>) {
        event.getContent()?.let { toast(it) }
    }
}