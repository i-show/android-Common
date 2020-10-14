package com.ishow.noah.modules.sample.detail.utils.file.save

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * Created by xxx on 2020-09-21.
 */
class SampleFileSaveViewModel (app: Application) : AppBaseViewModel(app) {

    private val _test = MutableLiveData<String>()
    val test: LiveData<String>
        get() = _test

}