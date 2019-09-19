package com.ishow.common.widget.pulltorefresh.footer

import android.view.ViewGroup

import androidx.annotation.IntDef
import kotlin.annotation.Retention
/**
 * Created by Bright.Yu on 2017/3/20.
 * PullToRefreshFooter
 */

interface IPullToRefreshFooter {

    /**
     * 设置状态
     */
    var status: Int

    @IntDef(STATUS_NORMAL, STATUS_LOADING, STATUS_FAILED, STATUS_END)
    @Retention(AnnotationRetention.SOURCE)
    annotation class FooterStatus

    /**
     * 初始化
     */
    fun init(parent: ViewGroup)

    companion object {
        /**
         * 普通状态
         */
        const val STATUS_NORMAL = 0
        /**
         * 加载状态
         */
        const val STATUS_LOADING = 1
        /**
         * 刷新失败
         */
        const val STATUS_FAILED = 2
        /**
         * 全部加载完毕
         */
        const val STATUS_END = 3
    }
}
