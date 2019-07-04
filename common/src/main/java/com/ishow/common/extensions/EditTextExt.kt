package com.ishow.common.extensions

import android.widget.EditText
import com.ishow.common.utils.StringUtils

fun EditText.trimText(): String {
    return this.text?.toString()?.trim { it <= ' ' } ?: StringUtils.EMPTY
}