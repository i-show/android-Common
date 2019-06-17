/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.adapter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.ishow.common.R;
import com.ishow.pulltorefresh.IPullToRefreshUtils;
import com.ishow.pulltorefresh.PullToRefreshView;

import java.util.List;

/**
 * 上拉刷新下拉加载更多的Adapter
 */
public abstract class RecyclerPullToRefreshAdapter<DATA, HOLDER extends RecyclerAdapter.Holder> extends RecyclerAdapter<DATA, HOLDER> implements IPullToRefreshUtils<DATA> {
    private static final String TAG = "RecyclerPullToRefreshAdapter";
    /**
     * 默认一页显示的数量
     */
    private static final int DEFAULT_PAGER_SIZE = 20;

    /**
     * 当前页数
     */
    private int mPagerNumber = 0;
    /**
     * 第一页
     */
    private int mFirstPager;
    /**
     * 加载模式
     */
    private boolean isLoadingMoreState = false;

    public RecyclerPullToRefreshAdapter(Context context) {
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
     * 获取第一页
     */
    public int getFirstPager(){
        return mFirstPager;
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

    public void resetPullToRefresh(PullToRefreshView pullToRefresh) {
        resetPullToRefresh(pullToRefresh, true);
    }

    public void resetPullToRefresh(PullToRefreshView pullToRefresh, boolean isError) {
        pullToRefresh.setRefreshEnable(true);
        pullToRefresh.setLoadMoreEnable(true);
        if (isLoadingMoreState()) {
            pullToRefresh.setLoadMoreNormal();
        } else {
            pullToRefresh.setRefreshNormal();
        }
    }


    @Override
    public void resolveData(PullToRefreshView pullToRefresh, List<DATA> data, int totalCount) {
        if (isLoadingMoreState()) {
            pullToRefresh.setLoadMoreSuccess();
            plusData(data);
        } else {
            pullToRefresh.setRefreshSuccess();
            setData(data);
        }
        final int realCount = getRealCount();
        setPullToRefreshStatus(pullToRefresh, data == null ? 0 : data.size(), realCount, realCount >= totalCount);
    }

    @Override
    public void resolveData(PullToRefreshView pullToRefresh, List<DATA> data, boolean isLastPage) {
        if (isLoadingMoreState()) {
            pullToRefresh.setLoadMoreSuccess();
            plusData(data);
        } else {
            pullToRefresh.setRefreshSuccess();
            setData(data);
        }

        final int realCount = getRealCount();
        setPullToRefreshStatus(pullToRefresh, data == null ? 0 : data.size(), realCount, isLastPage);
    }

    private void setPullToRefreshStatus(PullToRefreshView pullToRefresh, int dataSize, int realCount, boolean isLastPage) {
        // 修复pagerNumber
        repairPagerNumber(dataSize);
        pullToRefresh.setRefreshEnable(true);
        if (realCount == 0) {
            pullToRefresh.setLoadMoreEnable(false);
        } else if (isLastPage) {
            pullToRefresh.setLoadMoreEnable(true);
            pullToRefresh.setLoadMoreEnd();
        } else {
            pullToRefresh.setLoadMoreEnable(true);
        }
    }
}
