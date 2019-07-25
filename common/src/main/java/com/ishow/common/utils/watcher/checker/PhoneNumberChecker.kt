package com.ishow.common.utils.watcher.checker

import android.view.View
import com.ishow.common.extensions.isPhone

class PhoneNumberChecker : ITextChecker {
    override fun check(view: View, text: String): Boolean {
        return text.isPhone()
    }
}
