package com.ishow.common.app.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.ishow.common.utils.ReflectionUtils
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.common.widget.StatusView
import java.lang.reflect.ParameterizedType

abstract class BindFragment<T : ViewDataBinding, VM : BaseViewModel> : BaseFragment() {
    protected lateinit var dataBinding: T
    private var viewModel: VM? = null

    @Suppress("UNCHECKED_CAST")
    protected val viewModelClass: Class<VM> by lazy {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        type as Class<VM>
    }

    abstract fun getLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindContentView(container, getLayout())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDataBindingValues()
        bindViewModel()
    }

    protected open fun bindContentView(container: ViewGroup?, layoutId: Int): View {
        dataBinding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun bindDataBindingValues() {
        ReflectionUtils.invokeMethod(dataBinding, "setFragment", this, javaClass)
    }

    /**
     * ViewModel绑定
     */
    private fun bindViewModel() {
        if (!canBindViewModel()) {
            return
        }
        val vm = ViewModelProvider(this).get(viewModelClass)
        viewModel = vm
        initViewModel(vm)
        vm.init()
    }

    @Suppress("unused")
    protected open fun bindViewModel(cls: Class<VM>): VM {
        val vm = ViewModelProvider(this).get(cls)
        viewModel = vm
        initViewModel(vm)
        vm.init()
        return vm
    }

    protected open fun initViewModel(vm: VM) {
        val fragment = viewLifecycleOwner
        // dataBinding 设置vm参数
        ReflectionUtils.invokeMethod(dataBinding, "setVm", vm, viewModelClass)

        vm.loadingStatus.observe(fragment, Observer { changeLoadingStatus(it) })
        vm.errorStatus.observe(fragment, Observer { changeErrorStatus(it) })
        vm.successStatus.observe(fragment, Observer { changeSuccessStatus(it) })
        vm.emptyStatus.observe(fragment, Observer { changeEmptyStatus(it) })
        vm.toastMessage.observe(fragment, Observer { showToast(it) })
    }

    /**
     * 是否可以进行自动bindViewModel
     */
    protected open fun canBindViewModel(): Boolean {
        return viewModelClass != BaseViewModel::class.java
    }

    /**
     * 改变LoadingDialog的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
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
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeErrorStatus(event: Event<Error>) {
        event.value?.let { showError(it) }
    }

    /**
     * 改变Success的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeSuccessStatus(event: Event<Success>) {
        event.value?.let { showSuccess(it) }
    }

    /**
     * 改变Empty的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeEmptyStatus(event: Event<Empty>) {
        event.value?.let { showEmpty(it) }
    }

    private fun showToast(event: Event<String>) {
        event.value?.let { toast(it) }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {
        super.onStatusClick(v, which)
        viewModel?.retryRequest()
    }
}