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

package com.ishow.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ishow.common.R;
import com.ishow.common.widget.StatusView;
import com.ishow.common.widget.pulltorefresh.IPullToRefresh;
import com.ishow.common.widget.pulltorefresh.IPullToRefreshUtils;

import java.util.List;

/**
 * 上拉刷新下拉加载更多的Adapter
 */
public abstract class ListPullToRefreshAdapter<DATA, HOLDER extends ListAdapter.Holder> extends ListAdapter<DATA, HOLDER> implements IPullToRefreshUtils<DATA> {
    private static final String TAG = "ListPullToRefreshAdapte";
    /**
     * 默认一页显示的数量
     */
    private static final int DEFAULT_PAGER_SIZE = 10;

    /**
     * 当前页数
     */
    private int mPagerNumber = 1;
    /**
     * 第一页
     */
    private int mFirstPager;
    /**
     * 加载模式
     */
    private boolean isLoadingMoreState = false;

    public ListPullToRefreshAdapter(Context context) {
        super(context);
        initPagerNumber();
    }

    /**
     * 初始化
     */
    @Override
    public void initPagerNumber() {
        mFirstPager = mContext.getResources().getInteger(R.integer.default_pager_number);
        mPagerNumber = mFirstPager;
    }

    /**
     * 重置分页
     */
    @Override
    public void resetPagerNumber() {
        mPagerNumber = mFirstPager;
    }

    /**
     * 分页数加1
     */
    @Override
    public void plusPagerNumber() {
        mPagerNumber++;
    }

    /**
     * 获取当前分页
     *
     * @return 获取当前分页
     */
    @Override
    public int getPagerNumber() {
        return mPagerNumber;
    }

    /**
     * 重新设置PagerNumber
     */
    @Override
    public void setPagerNumber(int pagerNumber) {
        mPagerNumber = pagerNumber;
    }

    /**
     * 获取分页大小
     */
    @Override
    public int getPagerSize() {
        return DEFAULT_PAGER_SIZE;
    }

    /**
     * 获取分页大小
     */
    @Override
    public int getPagerSize(int wantPagerSize) {
        return wantPagerSize > getPagerSize() ? wantPagerSize : getPagerSize();
    }


    @Override
    public void repairPagerNumber(int datasSize) {
        int defaultSize = getPagerSize();
        // 如果是第一页 && 返回的数据必当前分页多
        if (getPagerNumber() == mFirstPager && datasSize > defaultSize) {
            setPagerNumber(datasSize / defaultSize + mFirstPager);
        } else {
            plusPagerNumber();
        }
    }


    /**
     * 判断是否是加载更多模式
     */
    public boolean isLoadingMoreState() {
        return isLoadingMoreState;
    }

    /**
     * 设置是否是加载更多模式
     */
    public void setLoadingMoreState(boolean state) {
        isLoadingMoreState = state;
    }

    public void resetPullTorefresh(IPullToRefresh pullToRefresh) {
        resetPullTorefresh(pullToRefresh, true);
    }

    public void resetPullTorefresh(IPullToRefresh pullToRefresh, boolean isError) {
        pullToRefresh.setPullRefreshEnable(true);
        pullToRefresh.setPullLoadEnable(true);
        pullToRefresh.setEmptyState(isError ? StatusView.STATUS_ERROR : StatusView.STATUS_EMPTY);
        if (isLoadingMoreState()) {
            pullToRefresh.stopLoadMore();
        } else {
            pullToRefresh.setRefreshFail();
        }
    }

    @Override
    public void resolveData(IPullToRefresh pullToRefresh, @NonNull List<DATA> datas, int totalcount) {
        if (isLoadingMoreState()) {
            pullToRefresh.stopLoadMore();
            plusData(datas);
        } else {
            setData(datas);
            pullToRefresh.setRefreshSuccess();
        }
        // 修复pagernumber
        repairPagerNumber(datas.size());

        final int realCount = getRealCount();

        pullToRefresh.setPullRefreshEnable(true);
        pullToRefresh.setEmptyState(StatusView.STATUS_EMPTY);

        if (realCount == 0) {
            pullToRefresh.setPullLoadEnable(false);
        } else if (realCount >= totalcount) {
            pullToRefresh.setPullLoadEnable(true);
            pullToRefresh.setLoadEnd();
        } else {
            pullToRefresh.setPullLoadEnable(true);
        }
    }
}
