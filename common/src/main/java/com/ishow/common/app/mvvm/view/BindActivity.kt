package com.ishow.common.app.mvvm.view

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ishow.common.app.activity.BaseActivity
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

abstract class BindActivity<BINDING : ViewDataBinding, VM : BaseViewModel> : BaseActivity() {

    /**
     * ViewDataBinding 的实现类
     */
    protected lateinit var binding: BINDING

    /**
     * ViewModel的实现类
     */
    protected var vm: VM? = null

    @Suppress("UNCHECKED_CAST")
    private val viewModelClass: Class<VM> by lazy {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        type as Class<VM>
    }

    protected open fun bindContentView(layoutId: Int): BINDING {
        val inflate = LayoutInflater.from(this)
        binding = DataBindingUtil.inflate(inflate, layoutId, null , false)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        bindDataBindingValues()
        bindViewModel()
        return binding
    }

    /**
     * 扩展方法：后面可以添加固定内容
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun bindDataBindingValues() {
    }

    /**
     * ViewModel绑定
     */
    private fun bindViewModel() {
        val vm = ViewModelProvider(this).get(viewModelClass)
        this.vm = vm
        initViewModel(vm)
        vm.init()
    }

    @Suppress("unused")
    protected open fun bindViewModel(cls: Class<VM>): VM {
        val vm = ViewModelProvider(this).get(cls)
        this.vm = vm
        initViewModel(vm)
        vm.init()
        return vm
    }

    /**
     * 初始化ViewModel
     */
    protected open fun initViewModel(vm: VM) {
        val activity = this@BindActivity
        // dataBinding 设置vm参数
        ReflectionUtils.invokeMethod(binding, "setVm", vm, viewModelClass, false)

        lifecycle.addObserver(vm)
        vm.loadingStatus.observe(activity, { changeLoadingStatus(it) })
        vm.errorStatus.observe(activity, { changeErrorStatus(it) })
        vm.successStatus.observe(activity, { changeSuccessStatus(it) })
        vm.emptyStatus.observe(activity, { changeEmptyStatus(it) })
        vm.toastMessage.observe(activity, { showToast(it) })
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.let { lifecycle.removeObserver(it) }
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
    protected fun changeErrorStatus(error: Event<Error>) {
        error.value?.let { showError(it) }
    }

    /**
     * 改变Success的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeSuccessStatus(success: Event<Success>) {
        success.value?.let { showSuccess(it) }
    }

    /**
     * 改变Empty的状态
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun changeEmptyStatus(empty: Event<Empty>) {
        empty.value?.let { showEmpty(it) }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun showToast(event: Event<String>) {
        event.value?.let { toast(it) }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {
        super.onStatusClick(v, which)
        vm?.retryRequest()
    }

}