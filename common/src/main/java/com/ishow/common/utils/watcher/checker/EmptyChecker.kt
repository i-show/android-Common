package com.ishow.common.utils.watcher.checker

import android.view.View

class EmptyChecker : ITextChecker {
    override fun check(view: View, text: String?): Boolean {
        return !text.isNullOrEmpty()
    }
}
