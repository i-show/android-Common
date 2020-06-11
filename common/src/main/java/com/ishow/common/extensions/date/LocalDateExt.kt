package com.ishow.common.extensions.date

import java.time.LocalDate
import java.time.ZoneId

/**
 * Created by yuhaiyang on 2020/6/11.
 * LocalDate
 */

/**
 * LocalDate 转毫秒
 * @param zoneId 当前的时区信息
 *
 * 注意 这个是0分0时0秒
 */
@JvmOverloads
fun LocalDate.toEpochMilli(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()
}

/**
 * LocalDate 转秒
 * @param zoneId 当前的时区信息
 *
 * 注意 这个是0分0时0秒
 */
@JvmOverloads
fun LocalDate.toEpochSecond(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return atStartOfDay(zoneId)
        .toEpochSecond()
}