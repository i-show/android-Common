package com.ishow.common.extensions

import android.view.View
import com.ishow.common.utils.MathUtils

/**
 * 人民币
 */
const val MONEY = "￥"

/**
 * 解析成人民币
 */
fun String.format2Money(scale: Int = -1, force: Boolean = false): String {
    if(this.isEmpty()){
        return MONEY + "0"
    }

    return if (scale > 0) {
        val money = MathUtils.rounding(this, scale, force)
        MONEY + money
    } else {
        MONEY + this
    }
}
