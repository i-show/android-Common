package com.ishow.common.utils.watcher.checker

import android.view.View


class PasswordChecker (val min: Int, val max: Int = Int.MAX_VALUE) : ITextChecker {
    override fun check(view: View, text: String): Boolean {
        return text.length in min..max
    }
}
