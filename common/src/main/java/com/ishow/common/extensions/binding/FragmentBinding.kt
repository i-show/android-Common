package com.ishow.common.extensions.binding

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ishow.common.extensions.observerDestroyed

/**
 * Fragment Binding 实现 一键获取的实现类
 */
class FragmentBinding<BINDING : ViewBinding>(classes: Class<BINDING>, fragment: Fragment) :
    BindingProperty<Fragment, BINDING>(classes, fragment.layoutInflater) {

    init {
        fragment.lifecycle.observerDestroyed { destroyed() }
    }

    override fun binding(thisRef: Fragment, viewBinding: BINDING) {
        if (viewBinding is ViewDataBinding) viewBinding.lifecycleOwner = thisRef
    }
}