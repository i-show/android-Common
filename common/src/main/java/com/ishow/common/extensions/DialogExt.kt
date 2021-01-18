package com.ishow.common.extensions

import android.app.Dialog
import androidx.viewbinding.ViewBinding
import com.ishow.common.extensions.binding.DialogBinding


/**
 * 方便获取ViewDataBinding
 */
inline fun <reified T : ViewBinding> Dialog.binding() = DialogBinding(T::class.java, context.inflater)