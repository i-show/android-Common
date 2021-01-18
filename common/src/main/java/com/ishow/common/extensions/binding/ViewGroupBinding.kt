package com.ishow.common.extensions.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * ViewGroup中ViewBinding实现
 */
class ViewGroupBinding<BINDING : ViewBinding>(classes: Class<BINDING>, inflater: LayoutInflater) :
    BindingProperty<ViewGroup, BINDING>(classes, inflater) {

    override fun binding(thisRef: ViewGroup, viewBinding: BINDING) {
        thisRef.addView(viewBinding.root)
    }
}