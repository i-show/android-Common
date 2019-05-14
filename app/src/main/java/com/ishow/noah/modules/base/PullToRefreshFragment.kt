/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.base

import android.util.Log
import android.view.View

import com.ishow.pulltorefresh.IPullToRefreshUtils
import com.ishow.pulltorefresh.OnPullToRefreshListener
import com.ishow.pulltorefresh.PullToRefreshView


/**
 * Created by yuhaiyang on 2018/8/8.
 * 上拉加载更多下拉刷新
 */

abstract class PullToRefreshFragment : AppBaseFragment(), OnPullToRefreshListener {

    override fun onRefresh(view: PullToRefreshView) {
        val pullToRefreshUtils = getPullToRefreshUtils(view)
        if (pullToRefreshUtils == null) {
            Log.i(TAG, "onRefresh: pullToRefreshUtils is null")
            return
        }
        pullToRefreshUtils.isLoadingMoreState = false
        pullToRefreshUtils.resetPagerNumber()
        loadData(view, pullToRefreshUtils.pagerNumber, pullToRefreshUtils.pagerSize)
    }

    override fun onLoadMore(view: PullToRefreshView) {
        val pullToRefreshUtils = getPullToRefreshUtils(view)
        if (pullToRefreshUtils == null) {
            Log.i(TAG, "onLoadMore: pullToRefreshUtils is null")
            return
        }
        pullToRefreshUtils.isLoadingMoreState = true
        loadData(view, pullToRefreshUtils.pagerNumber, pullToRefreshUtils.pagerSize)
    }

    /**
     * 通知去加载数据
     *
     * @param v 可能会存在多个pulltoRefresh 所以通过v来区分
     */
    protected abstract fun loadData(v: View, pagerNumber: Int, pagerSize: Int)

    /**
     * 获取pullToRefresh的工具类
     *
     * @param v 可能会存在多个pullToRefresh 所以通过v来区分
     */
    protected abstract fun getPullToRefreshUtils(v: View): IPullToRefreshUtils<*>?

    companion object {
        private val TAG = "PullToRefreshFragment"
    }
}
