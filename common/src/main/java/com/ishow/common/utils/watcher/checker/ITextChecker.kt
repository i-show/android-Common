package com.ishow.common.utils.watcher.checker

import android.view.View

interface ITextChecker {
    /**
     * 检测文本内容
     * 当前检测的View
     */
    fun check(view: View, text: String): Boolean
}
