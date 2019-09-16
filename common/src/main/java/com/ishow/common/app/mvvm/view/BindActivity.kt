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
import java.lang.reflect.ParameterizedType

abstract class BindActivity<T : ViewDataBinding, VM : BaseViewModel> : BaseActivity() {
    protected lateinit var dataBinding: T

    @Suppress("UNCHECKED_CAST")
    private val viewModelClass: Class<VM>
        get() {
            val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
            return type as Class<VM>
        }

    protected open fun bindContentView(layoutId: Int): T {
        val view = inflate(layoutId)
        setContentView(view)
        dataBinding = DataBindingUtil.bind(view)!!
        dataBinding.lifecycleOwner = this
        bindViewModel()
        return dataBinding
    }

    private fun bindViewModel() {
        val vm = ViewModelProvider(this).get(viewModelClass)
        initViewModel(vm)
        vm.init()
    }

    protected open fun bindViewModel(cls: Class<VM>): VM {
        val vm = ViewModelProvider(this).get(cls)
        initViewModel(vm)
        vm.init()
        return vm
    }

    protected open fun initViewModel(vm: VM) {
        val activity = this@BindActivity
        vm.loadingStatus.observe(activity, Observer { changeLoadingStatus(it) })
        vm.errorStatus.observe(activity, Observer { changeErrorStatus(it) })
        vm.successStatus.observe(activity, Observer { changeSuccessStatus(it) })
        vm.emptyStatus.observe(activity, Observer { changeEmptyStatus(it) })
        vm.toastMessage.observe(activity, Observer { showToast(it) })
    }


    /**
     * 改变LoadingDialog的状态
     */
    protected fun changeLoadingStatus(loading: Event<Loading>) {
        loading.value?.let {
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
    protected fun changeErrorStatus(error: Event<Error>) {
        error.value?.let { showError(it) }
    }

    /**
     * 改变Success的状态
     */
    protected fun changeSuccessStatus(success: Event<Success>) {
        success.value?.let { showSuccess(it) }
    }

    /**
     * 改变Empty的状态
     */
    protected fun changeEmptyStatus(empty: Event<Empty>) {
        empty.value?.let { showEmpty(it) }
    }

    protected fun showToast(event: Event<String>) {
        event.value?.let { toast(it) }
    }


}