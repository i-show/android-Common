package com.ishow.noah.modules.sample.pulltorefresh;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.noah.modules.sample.entries.page.PageJob;
import com.ishow.noah.utils.http.AppHttpCallBack;

class SamplePullToRefreshPresenter implements SamplePullToRefreshContract.Presenter {

    private SamplePullToRefreshContract.View mView;

    SamplePullToRefreshPresenter(SamplePullToRefreshContract.View view) {
        mView = view;
    }


    @Override
    public void getList(Context context, final int pageNumber, final boolean loadingView) {
        if (loadingView) mView.showLoading(null, false);
        String url = "http://xiaomidagong.com/Home/SelectFactoryInfoType?title=&page=" + String.valueOf(pageNumber) + "&pagesize=10&type=1";
        Http.post()
                .url(url)
                .execute(new AppHttpCallBack<PageJob>(context) {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        if (loadingView) mView.showError(null, false, error.getCode());
                    }

                    @Override
                    protected void onSuccess(PageJob result) {
                        if (result == null || result.getResults().isEmpty()) {
                            if (loadingView) mView.showEmpty(null);
                        } else {
                            if (loadingView) mView.dismissLoading(false);
                            mView.updateView(result.getResults(), result.getTotalRows());
                        }
                    }
                });
    }
}