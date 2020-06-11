package com.ishow.common.extensions

import java.time.*
import java.util.*


fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return Instant.ofEpochMilli(this)
        .atZone(zoneId)
}

/**
 * Date 转LocalDateTime
 */
@JvmOverloads
fun Long.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return toZonedDateTime(zoneId)
        .toLocalDateTime()
}


/**
 * Date 转LocalDate
 */
@JvmOverloads
fun Long.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
    return toZonedDateTime(zoneId)
        .toLocalDate()
}


/**
 * Date 转LocalTime
 */
@JvmOverloads
fun Long.toLocalTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalTime {
    return toZonedDateTime(zoneId)
        .toLocalTime()
}