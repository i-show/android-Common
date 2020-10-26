package com.ishow.noah.modules.sample.main

import com.ishow.noah.entries.http.AppPageResponse
import com.ishow.noah.manager.RetrofitManager
import com.ishow.noah.modules.base.mvvm.model.AppBaseModel
import com.ishow.noah.modules.sample.entries.SampleTestPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SampleModel : AppBaseModel() {
    private val httpService = RetrofitManager.instance.appService

    /**
     * testPage
     */
    suspend fun testPage(page: Int): AppPageResponse<SampleTestPage> = withContext(Dispatchers.IO) {
        return@withContext httpService.testPage(page, 20)
    }

}