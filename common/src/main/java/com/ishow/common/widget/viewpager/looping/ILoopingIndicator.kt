package com.ishow.common.widget.viewpager.looping

import android.graphics.Canvas

/**
 * Created by yuhaiyang on 2018/9/13.
 * 小点点
 */
interface ILoopingIndicator {
    /**
     * LoopingViewPager Pager大小改变
     */
    fun onViewSizeChanged(w: Int, h: Int)

    /**
     * 数据变化
     */
    fun onDataSizeChanged(count: Int)

    /**
     * 画信息
     *
     * @param canvas          当前的画布
     * @param scrollX         横向已经移动了多少距离
     * @param count           Indicator的个数
     * @param currentPosition 当前的Position
     * @param positionOffset  移动距离
     */
    fun onDraw(canvas: Canvas, scrollX: Int, count: Int, currentPosition: Int, positionOffset: Float)

}
