package com.ishow.common.widget.pulltorefresh

interface OnPullToRefreshStatusChangedListener {
    /**
     * 下拉刷新状态改变
     */
    fun onRefreshStatusChanged(status: Int)

    /**
     * 上拉加载更多状态改变
     */
    fun onLoadMoreStatusChanged(status: Int)
}
