package com.ishow.noah.modules.sample.pulltorefresh;

import android.content.Context;

import com.ishow.common.mvp.base.IPresenter;
import com.ishow.common.mvp.base.IView;
import com.ishow.common.mvp.base.IViewStatus;
import com.ishow.noah.modules.sample.entries.Job;

import java.util.List;

interface SamplePullToRefreshContract {

    interface View extends IView, IViewStatus {
        void updateView(List<Job> list, int count);
    }

    interface Presenter extends IPresenter {
        void getList(Context context, int pageNumber, boolean loadingView);
    }
}