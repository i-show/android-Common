package com.ishow.common.widget.viewpager.looping;

import android.graphics.Canvas;

/**
 * Created by yuhaiyang on 2018/9/13.
 * 小点点
 */
public interface ILoopingIndicator {
    /**
     * LoopingViewPager Pager大小改变
     */
    void onViewSizeChanged(int w, int h);

    /**
     * 数据变化
     */
    void onDataSizeChanged(int count);

    /**
     * 画信息
     *
     * @param canvas          当前的画布
     * @param scrollX         横向已经移动了多少距离
     * @param count           Indicator的个数
     * @param currentPosition 当前的Position
     * @param positionOffset  移动距离
     */
    void onDraw(Canvas canvas, final int scrollX, final int count, final int currentPosition, final float positionOffset);

}
