package com.ishow.noah.utils.checker

import android.content.Context
import android.view.View
import com.ishow.common.extensions.getInteger
import com.ishow.common.utils.watcher.checker.ITextChecker
import com.ishow.noah.R

class PasswordChecker(context: Context) : ITextChecker {
    private val min: Int = context.getInteger(R.integer.min_password)
    private val max: Int = context.getInteger(R.integer.max_password)
    override fun check(view: View, text: String): Boolean {
        return text.length in min..max
    }
}
