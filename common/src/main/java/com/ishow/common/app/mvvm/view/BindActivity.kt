package com.ishow.common.app.mvvm.view

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ishow.common.app.activity.BaseActivity
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.status.Empty
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.inflate
import com.ishow.common.extensions.toast
import com.ishow.common.utils.databinding.bus.Event

abstract class BindActivity<T : ViewDataBinding> : BaseActivity() {
    protected lateinit var dataBinding: T

    protected open fun bindContentView(layoutId: Int): T {
        val view = inflate(layoutId)
        setContentView(view)
        dataBinding = DataBindingUtil.bind(view)!!
        dataBinding.lifecycleOwner = this
        return dataBinding
    }

    protected open fun <VM : BaseViewModel> bindViewModel(cls: Class<VM>, block: ((VM) -> Unit)? = null): VM {
        val vm = ViewModelProvider(this).get(cls)
        val activity = this@BindActivity
        vm.loadingStatus.observe(activity, Observer { changeLoadingStatus(it) })
        vm.errorStatus.observe(activity, Observer { changeErrorStatus(it) })
        vm.successStatus.observe(activity, Observer { changeSuccessStatus(it) })
        vm.emptyStatus.observe(activity, Observer { changeEmptyStatus(it) })
        vm.toastMessage.observe(activity, Observer { showToast(it) })
        vm.init()
        block?.let { it(vm) }
        return vm
    }


    /**
     * 改变LoadingDialog的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeLoadingStatus(loading: Event<Loading>) {
        loading.getContent()?.let {
            if (it.status == Loading.Status.Show) {
                showLoading(it)
            } else {
                dismissLoading(it)
            }
        }
    }

    /**
     * 改变Error的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeErrorStatus(error: Event<Error>) {
        error.getContent()?.let { showError(it) }
    }

    /**
     * 改变Success的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeSuccessStatus(success: Event<Success>) {
        success.getContent()?.let { showSuccess(it) }
    }

    /**
     * 改变Empty的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeEmptyStatus(empty: Event<Empty>) {
        empty.getContent()?.let { showEmpty(it) }
    }

    private fun showToast(event: Event<String>) {
        event.getContent()?.let { toast(it) }
    }
}