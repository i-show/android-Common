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

package com.bright.common.widget.pulltorefresh;

/**
 * 抽象提取
 */
public interface IPullToRefresh {
    /**
     * 是否可以下拉刷新
     */
    void setPullRefreshEnable(boolean enable);

    /**
     * 是否可以上拉加载更多
     */
    void setPullLoadEnable(boolean enable);

    /**
     * 自动加载更多
     */
    void setAutoLoadEnable(boolean enable);
    
    /**
     * 设置为空的状态
     */
    void setEmptyState(int state);

    /**
     * 停止刷新
     */
    void stopRefresh();

    /**
     * 刷新成功
     */
    void setRefreshSuccess();

    /**
     * 刷新失败
     */
    void setRefreshFail();

    /**
     * 停止加载更多
     */
    void stopLoadMore();

    /**
     * 已经加载完毕
     */
    void setLoadEnd();

    /**
     * 自动刷新
     */
    void autoRefresh();
}
