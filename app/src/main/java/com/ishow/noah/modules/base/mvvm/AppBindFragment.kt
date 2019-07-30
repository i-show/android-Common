package com.ishow.noah.modules.base.mvvm

import androidx.databinding.ViewDataBinding
import com.ishow.common.app.fragment.BindFragment


abstract class AppBindFragment<T : ViewDataBinding> : BindFragment<T>()

