package com.ishow.noah.modules.base.mvvm

import androidx.databinding.ViewDataBinding
import com.ishow.common.app.mvvm.view.BindFragment


abstract class AppBindFragment<T : ViewDataBinding> : BindFragment<T>()

