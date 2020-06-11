package com.ishow.common.extensions.date

import com.ishow.common.utils.DateUtils
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by yuhaiyang on 2020/6/11.
 * LocalDate扩展方法
 */

/**
 * LocalDateTime转毫秒
 * @param zoneId 当前的时区信息
 */
@JvmOverloads
fun LocalDateTime.toEpochMilli(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return atZone(zoneId)
        .toInstant()
        .toEpochMilli()
}


/**
 * LocalDateTime转秒
 * @param zoneId 当前的时区信息
 */
@JvmOverloads
fun LocalDateTime.toEpochSecond(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return atZone(zoneId)
        .toEpochSecond()
}

/**
 * format 样式
 * @param mode 要Format的样式
 */
@JvmOverloads
fun LocalDateTime.format(mode: String = DateUtils.FORMAT_YMDHMS): String {
    return DateUtils.format(this, mode)
}
