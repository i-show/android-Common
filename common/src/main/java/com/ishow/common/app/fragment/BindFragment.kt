package com.ishow.common.app.fragment

import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ishow.common.app.viewmodel.BaseViewModel
import com.ishow.common.extensions.toast
import com.ishow.common.modules.binding.Event

abstract class BindFragment<T : ViewDataBinding> : BaseFragment() {
    protected lateinit var dataBinding: T

    protected open fun bindContentView(container: ViewGroup?, layoutId: Int) {
        dataBinding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.lifecycleOwner = viewLifecycleOwner
    }


    protected open fun <VM : BaseViewModel> getViewModel(viewModelClass: Class<VM>): VM {
        val vm = ViewModelProviders.of(this).get(viewModelClass)
        val fragment = this@BindFragment
        vm.run {
            loadingDialog.observe(fragment, Observer { changeLoadingDialogStatus(it) })
            toastMessage.observe(fragment, Observer { showToast(it) })
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