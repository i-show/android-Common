package com.ishow.noah.modules.sample.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.noah.R
import com.ishow.noah.modules.base.mvvm.AppBaseViewModel

/**
 * Created by yuhaiyang on 2019-08-20.
 * SampleMainViewModel
 */
class SampleMainViewModel(app: Application) : AppBaseViewModel(app) {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title


    override fun init() {
        super.init()
        _title.value = context.getString(R.string.sample_main)
    }

    fun updateTitle(title: String) {
        _title.value = title
    }
}