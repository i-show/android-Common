package com.ishow.common.extensions.binding

import android.app.Dialog
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * DialogBinding 实现类
 */
class DialogBinding<BINDING : ViewBinding>(classes: Class<BINDING>, inflater: LayoutInflater) :
    BindingProperty<Dialog, BINDING>(classes, inflater) {
    override fun binding(thisRef: Dialog, viewBinding: BINDING) {
        thisRef.setContentView(viewBinding.root)
    }
}