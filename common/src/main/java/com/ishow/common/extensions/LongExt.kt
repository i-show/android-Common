package com.ishow.common.extensions

import java.time.*

/**
 * Long 转 ZonedDateTime
 */
fun Long.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return Instant.ofEpochMilli(this)
        .atZone(zoneId)
}

/**
 * Long 转 LocalDateTime
 */
@JvmOverloads
fun Long.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return toZonedDateTime(zoneId)
        .toLocalDateTime()
}

/**
 * Long 转 LocalDate
 */
@JvmOverloads
fun Long.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
    return toZonedDateTime(zoneId)
        .toLocalDate()
}

/**
 * Long 转 LocalTime
 */
@JvmOverloads
fun Long.toLocalTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalTime {
    return toZonedDateTime(zoneId)
        .toLocalTime()
}