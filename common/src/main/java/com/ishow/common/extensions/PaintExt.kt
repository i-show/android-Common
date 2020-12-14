package com.ishow.common.extensions

import android.graphics.Paint

/**
 * Created by yuhaiyang on 2020/12/14.
 * Paint的扩展函数
 */
/**
 * 获取DrawText的BaseLine
 */
inline val Paint.baseLine: Int
    get() {
        val fontMetrics = fontMetricsInt
        return (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
    }