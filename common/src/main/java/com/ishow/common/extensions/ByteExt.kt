package com.ishow.common.extensions


/**
 * Byte转换成16进制的Int
 */
fun Byte.toHexInt(): Int {
    return this.toInt() and 0xFF
}