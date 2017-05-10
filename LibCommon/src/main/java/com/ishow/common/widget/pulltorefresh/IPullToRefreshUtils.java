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

package com.ishow.common.widget.pulltorefresh;

import java.util.List;

/**
 * 这个是用来确认上拉或者下拉的工具
 */
public interface IPullToRefreshUtils<DATA> {

    /**
     * 初始化
     */
    void initPagerNumber();

    /**
     * 重置分页
     */
    void resetPagerNumber();

    /**
     * 分页数加1
     */
    void plusPagerNumber();

    /**
     * 获取当前分页
     *
     * @return 获取当前分页
     */
    int getPagerNumber();

    /**
     * 重新设置PagerNumber
     */
    void setPagerNumber(int pagerNumber);

    /**
     * 获取分页大小
     */
    int getPagerSize();

    /**
     * 获取分页大小
     */
    int getPagerSize(int wantPagerSize);

    /**
     * 修复pagerSize
     */
    void repairPagerNumber(int datasSize);

    /**
     * 判断是否是加载更多模式
     */
    boolean isLoadingMoreState();

    /**
     * 设置是否是加载更多模式
     */
    void setLoadingMoreState(boolean state);

    /**
     * 重置状态
     */
    void resetPullTorefresh(IPullToRefresh pullToRefresh);

    /**
     * 重置状态
     */
    void resetPullTorefresh(IPullToRefresh pullToRefresh, boolean isError);

    /**
     * 消化数据
     */
    void resolveData(IPullToRefresh pullToRefresh, List<DATA> datas, int totalcount);
}
