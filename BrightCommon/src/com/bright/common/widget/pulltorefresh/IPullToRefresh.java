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
     * 设置为加载状态
     */
    void setLoadingState();

    /**
     * 设置为空的状态
     */
    void setEmptyState();

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
