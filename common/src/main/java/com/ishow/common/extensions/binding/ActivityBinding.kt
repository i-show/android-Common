package com.ishow.common.extensions.binding

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.ishow.common.extensions.inflater
import com.ishow.common.extensions.observerDestroyed

/**
 * ActivityBinding 一键获取的实现类
 */
class ActivityBinding<BINDING : ViewBinding>(classes: Class<BINDING>, val activity: AppCompatActivity) :
    BindingProperty<AppCompatActivity, BINDING>(classes, activity.inflater) {

    init {
        activity.lifecycle.observerDestroyed { destroyed() }
    }

    override fun binding(thisRef: AppCompatActivity, viewBinding: BINDING) {
        if (viewBinding is ViewDataBinding) viewBinding.lifecycleOwner = thisRef
        thisRef.setContentView(viewBinding.root)
    }
}