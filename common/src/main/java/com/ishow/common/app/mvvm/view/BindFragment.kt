package com.ishow.common.app.mvvm.view

import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ishow.common.app.fragment.BaseFragment
import com.ishow.common.app.mvvm.viewmodel.BaseViewModel
import com.ishow.common.entries.status.Empty
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.toast
import com.ishow.common.utils.databinding.bus.Event

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
        val vm = ViewModelProvider(this).get(viewModelClass)
        val fragment = this@BindFragment
        vm.run {
            loadingStatus.observe(fragment, Observer { changeLoadingStatus(it) })
            errorStatus.observe(fragment, Observer { changeErrorStatus(it) })
            successStatus.observe(fragment, Observer { changeSuccessStatus(it) })
            emptyStatus.observe(fragment, Observer { changeEmptyStatus(it) })
            toastMessage.observe(fragment, Observer { showToast(it) })
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