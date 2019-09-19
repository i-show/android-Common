package com.ishow.common.widget.pulltorefresh.headers

import androidx.annotation.IntDef
import android.view.View
import android.view.ViewGroup

import com.ishow.common.widget.pulltorefresh.AbsAnimatorListener
import kotlin.annotation.Retention

/**
 * Created by Bright.Yu on 2017/3/20.
 * PullToRefreshHeader
 */

interface IPullToRefreshHeader {

    /**
     * 获取Header
     */
    val view: View

    /**
     * 当前状态
     */
    var status: Int

    /**
     * 获取移动的距离
     */
    val movingDistance: Int

    /**
     * 获取下拉的最大高度
     */
    val maxPullDownHeight: Int

    /**
     * 获取Header的高度
     */
    val headerHeight: Int

    /**
     * 判断当前移动距离是否是有效距离
     */
    val isEffectiveDistance: Boolean

    @IntDef(STATUS_NORMAL, STATUS_READY, STATUS_REFRESHING, STATUS_SUCCESS, STATUS_FAILED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class HeaderStatus

    /**
     * 移动
     *
     * @param parent 父View
     * @param offset 当前事件移动的距离
     * @return header移动的距离
     */
    fun moving(parent: ViewGroup, offset: Int): Int

    /**
     * 刷新中....
     */
    fun refreshing(parent: ViewGroup, listener: AbsAnimatorListener?): Int

    /**
     * 取消刷新
     */
    fun cancelRefresh(parent: ViewGroup): Int

    /**
     * 刷新成功
     */
    fun refreshSuccess(parent: ViewGroup): Int

    /**
     * 刷新失败
     */
    fun refreshFailed(parent: ViewGroup): Int

    companion object {

        /**
         * 正常状态
         */
        const val STATUS_NORMAL = 0
        /**
         * 准备状态
         */
        const val STATUS_READY = 1
        /**
         * 正在刷新
         */
        const val STATUS_REFRESHING = 2
        /**
         * 刷新成功
         */
        const val STATUS_SUCCESS = 3
        /**
         * 刷新失败
         */
        const val STATUS_FAILED = 4
    }
}
