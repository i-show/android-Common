package com.ishow.common.utils.textwatcher

import android.text.Editable
import android.text.TextWatcher

class PhoneNumberTextWatcher(private val spacer: String = " ") : TextWatcher {
    private var selfChange = false

    override fun afterTextChanged(s: Editable?) {
        if (selfChange || s == null) {
            return
        }
        when (s.length) {
            4 -> format(s, 4)
            9 -> format(s, 9)
            11 -> formatAll(s)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}


    private fun format(s: Editable, length: Int) {
        val start = length - 1

        selfChange = true
        if (s.substring(start) == spacer) {
            s.delete(start, length)
        } else {
            s.insert(start, spacer)
        }
        selfChange = false
    }

    private fun formatAll(s: Editable) {
        if (!s.contains(spacer)) {
            selfChange = true
            s.insert(3, spacer)
            s.insert(8, spacer)
            selfChange = false
        }
    }
}