package com.ishow.noah.modules.sample.pulltorefresh

import android.content.Context
import com.ishow.common.entries.status.Error
import com.ishow.common.entries.status.Loading

import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.noah.modules.sample.entries.page.PageJob
import com.ishow.noah.utils.http.AppHttpCallBack

internal class SamplePullToRefreshPresenter(private val mView: SamplePullToRefreshContract.View) :
    SamplePullToRefreshContract.Presenter {


    override fun getList(context: Context, pageNumber: Int, loadingView: Boolean) {
        if (loadingView) mView.showLoading(Loading.view())
        val url = "http://xiaomidagong.com/Home/SelectFactoryInfoType?title=&page=$pageNumber&pagesize=10"
        Http.post()
            .url(url)
            .execute(object : AppHttpCallBack<PageJob>(context) {
                override fun onFailed(error: HttpError) {
                    if (loadingView) mView.showError(Error.view(error.message))
                }

                override fun onSuccess(result: PageJob?) {
                    if (result == null || result.getResults().isEmpty()) {
                        if (loadingView) mView.showEmpty()
                    } else {
                        if (loadingView) mView.dismissLoading()
                        mView.updateView(result.getResults(), result.getTotalRows())
                    }
                }
            })
    }
}