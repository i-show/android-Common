package com.ishow.noah.modules.base.mvvm.view

import androidx.databinding.ViewDataBinding
import com.ishow.common.app.mvvm.view.BindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel


abstract class AppBindFragment<T : ViewDataBinding, VM : AppBaseViewModel> : BindFragment<T, VM>() {
    override fun canBindViewModel(): Boolean {
        return viewModelClass != AppBaseViewModel::class.java
    }
}

