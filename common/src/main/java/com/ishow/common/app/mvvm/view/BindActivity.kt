package com.ishow.common.app.mvvm.view

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ishow.common.app.activity.BaseActivity
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.status.Empty
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.inflate
import com.ishow.common.extensions.toast
import com.ishow.common.modules.binding.Event

abstract class BindActivity<T : ViewDataBinding> : BaseActivity() {
    protected lateinit var dataBinding: T

    protected open fun bindContentView(layoutId: Int): T {
        val view = inflate(layoutId)
        setContentView(view)
        dataBinding = DataBindingUtil.bind(view)!!
        dataBinding.lifecycleOwner = this
        return dataBinding
    }

    protected open fun <VM : BaseViewModel> getViewModel(viewModelClass: Class<VM>): VM {
        val vm = ViewModelProvider(this).get(viewModelClass)
        val activity = this@BindActivity
        vm.run {
            loadingStatus.observe(activity, Observer { changeLoadingStatus(it) })
            errorStatus.observe(activity, Observer { changeErrorStatus(it) })
            successStatus.observe(activity, Observer { changeSuccessStatus(it) })
            emptyStatus.observe(activity, Observer { changeEmptyStatus(it) })
            toastMessage.observe(activity, Observer { showToast(it) })
            return vm
        }
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