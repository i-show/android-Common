/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.base;

import android.util.Log;
import android.view.View;

import com.ishow.pulltorefresh.IPullToRefreshUtils;
import com.ishow.pulltorefresh.OnPullToRefreshListener;
import com.ishow.pulltorefresh.PullToRefreshView;


/**
 * 上拉加载更多下拉刷新
 */

public abstract class PullToRefreshFragment extends AppBaseFragment implements OnPullToRefreshListener {
    private static final String TAG = "PullToRefreshFragment";

    @Override
    public void onRefresh(PullToRefreshView view) {
        IPullToRefreshUtils pullToRefreshUtils = getPullToRefreshUtils(view);
        if (pullToRefreshUtils == null) {
            Log.i(TAG, "onRefresh: pullToRefreshUtils is null");
            return;
        }
        pullToRefreshUtils.setLoadingMoreState(false);
        pullToRefreshUtils.resetPagerNumber();
        loadData(view, pullToRefreshUtils.getPagerNumber(), pullToRefreshUtils.getPagerSize());
    }

    @Override
    public void onLoadMore(PullToRefreshView view) {
        IPullToRefreshUtils pullToRefreshUtils = getPullToRefreshUtils(view);
        if (pullToRefreshUtils == null) {
            Log.i(TAG, "onLoadMore: pullToRefreshUtils is null");
            return;
        }
        pullToRefreshUtils.setLoadingMoreState(true);
        loadData(view, pullToRefreshUtils.getPagerNumber(), pullToRefreshUtils.getPagerSize());
    }

    /**
     * 通知去加载数据
     *
     * @param v 可能会存在多个pulltoRefresh 所以通过v来区分
     */
    protected abstract void loadData(View v, int pagerNumber, int pagerSize);

    /**
     * 获取pulltoRefresh的工具类
     *
     * @param v 可能会存在多个pulltoRefresh 所以通过v来区分
     */
    protected abstract IPullToRefreshUtils getPullToRefreshUtils(View v);
}
