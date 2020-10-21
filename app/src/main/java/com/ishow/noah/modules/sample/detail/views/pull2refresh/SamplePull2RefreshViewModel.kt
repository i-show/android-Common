package com.ishow.noah.modules.sample.detail.views.pull2refresh

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.ishow.noah.modules.base.mvvm.viewmodel.Pull2RefreshViewModel
import com.ishow.noah.modules.sample.entries.SampleTestPage
import com.ishow.noah.modules.sample.main.SampleModel
import kotlinx.coroutines.launch

/**
 * Created by yuhaiyang on 2019-09-19.
 *
 */
class SamplePull2RefreshViewModel(app: Application) : Pull2RefreshViewModel<SampleTestPage>(app) {
    private val sampleModel: SampleModel = SampleModel()

    init {
        getData(loading = true)
    }

    fun getData(page: Int = 1, loading: Boolean = false) = viewModelScope.launch {
        pull2refresh(page, loading) { sampleModel.testPage(page) }
    }

    override fun retryRequest() {
        super.retryRequest()
        getData(loading = true)
    }
}