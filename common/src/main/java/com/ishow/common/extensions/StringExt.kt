package com.ishow.common.extensions

import com.ishow.common.utils.MathUtils
import com.ishow.common.utils.StringUtils


/**
 * 解析成人民币
 */
fun String.format2Money(scale: Int = -1, force: Boolean = false): String {
    if (this.isEmpty()) {
        return StringUtils.MONEY + StringUtils.BLANK + "0"
    }

    return if (scale > 0) {
        val money = MathUtils.rounding(this, scale, force)
        StringUtils.MONEY + StringUtils.BLANK + money
    } else {
        StringUtils.MONEY + StringUtils.BLANK + this
    }
}
