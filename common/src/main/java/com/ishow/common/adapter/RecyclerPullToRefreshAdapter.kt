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

package com.ishow.common.adapter

import android.content.Context

import com.ishow.common.R
import com.ishow.pulltorefresh.IPullToRefreshUtils
import com.ishow.pulltorefresh.PullToRefreshView

/**
 * 上拉刷新下拉加载更多的Adapter
 */
abstract class RecyclerPullToRefreshAdapter<DATA, HOLDER : RecyclerAdapter.Holder>(context: Context) : RecyclerAdapter<DATA, HOLDER>(context), IPullToRefreshUtils<DATA> {

    /**
     * 当前页数
     */
    private var mPagerNumber = 0
    /**
     * 第一页
     */
    /**
     * 获取第一页
     */
    private var firstPager: Int = 0
    /**
     * 加载模式
     */
    private var isLoadingMoreState = false

    init {
        init()
    }

    fun init() {
        initPagerNumber()
    }

    /**
     * 初始化
     */
    override fun initPagerNumber() {
        firstPager = mContext.resources.getInteger(R.integer.default_pager_number)
        mPagerNumber = firstPager
    }

    /**
     * 重置分页
     */
    override fun resetPagerNumber() {
        mPagerNumber = firstPager
    }

    /**
     * 分页数加1
     */
    override fun plusPagerNumber() {
        mPagerNumber++
    }

    /**
     * 获取当前分页
     *
     * @return 获取当前分页
     */
    override fun getPagerNumber(): Int {
        return mPagerNumber
    }

    /**
     * 重新设置PagerNumber
     */
    override fun setPagerNumber(pagerNumber: Int) {
        mPagerNumber = pagerNumber
    }

    /**
     * 获取分页大小
     */
    override fun getPagerSize(): Int {
        return DEFAULT_PAGER_SIZE
    }

    /**
     * 获取分页大小
     */
    override fun getPagerSize(wantPagerSize: Int): Int {
        return if (wantPagerSize > pagerSize) wantPagerSize else pagerSize
    }


    override fun repairPagerNumber(datasSize: Int) {
        val defaultSize = pagerSize
        // 如果是第一页 && 返回的数据必当前分页多
        if (pagerNumber == firstPager && datasSize > defaultSize) {
            setPagerNumber(datasSize / defaultSize + firstPager)
        } else {
            plusPagerNumber()
        }
    }


    /**
     * 判断是否是加载更多模式
     */
    override fun isLoadingMoreState(): Boolean {
        return isLoadingMoreState
    }

    /**
     * 设置是否是加载更多模式
     */
    override fun setLoadingMoreState(state: Boolean) {
        isLoadingMoreState = state
    }

    override fun resetPullToRefresh(pullToRefresh: PullToRefreshView) {
        resetPullToRefresh(pullToRefresh, true)
    }

    override fun resetPullToRefresh(pullToRefresh: PullToRefreshView, isError: Boolean) {
        pullToRefresh.isRefreshEnable = true
        pullToRefresh.isLoadMoreEnable = true
        if (isLoadingMoreState()) {
            pullToRefresh.setLoadMoreNormal()
        } else {
            pullToRefresh.setRefreshNormal()
        }
    }


    override fun resolveData(pullToRefresh: PullToRefreshView, data: MutableList<DATA>?, totalCount: Int) {
        if (isLoadingMoreState()) {
            pullToRefresh.setLoadMoreSuccess()
            plusData(data)
        } else {
            pullToRefresh.setRefreshSuccess()
            this.data = data
        }
        val realCount = itemCount
        setPullToRefreshStatus(pullToRefresh, data?.size ?: 0, realCount, realCount >= totalCount)
    }

    override fun resolveData(pullToRefresh: PullToRefreshView, data: MutableList<DATA>?, isLastPage: Boolean) {
        if (isLoadingMoreState()) {
            pullToRefresh.setLoadMoreSuccess()
            plusData(data)
        } else {
            pullToRefresh.setRefreshSuccess()
            this.data = data
        }

        val realCount = itemCount
        setPullToRefreshStatus(pullToRefresh, data?.size ?: 0, realCount, isLastPage)
    }

    private fun setPullToRefreshStatus(pullToRefresh: PullToRefreshView, dataSize: Int, realCount: Int, isLastPage: Boolean) {
        // 修复pagerNumber
        repairPagerNumber(dataSize)
        pullToRefresh.isRefreshEnable = true
        when {
            realCount == 0 -> {
                pullToRefresh.isLoadMoreEnable = false
            }
            isLastPage -> {
                pullToRefresh.isLoadMoreEnable = true
                pullToRefresh.setLoadMoreEnd()
            }
            else -> {
                pullToRefresh.isLoadMoreEnable = true
            }
        }
    }

    companion object {
        /**
         * 默认一页显示的数量
         */
        private const val DEFAULT_PAGER_SIZE = 20
    }
}
