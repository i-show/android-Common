package com.ishow.common.extensions.date

import java.time.*
import java.util.*


fun Date.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
    return toInstant()
        .atZone(zoneId)
}

/**
 * Date 转LocalDateTime
 */
@JvmOverloads
fun Date.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return toZonedDateTime(zoneId)
        .toLocalDateTime()
}


/**
 * Date 转LocalDate
 */
@JvmOverloads
fun Date.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate {
    return toZonedDateTime(zoneId)
        .toLocalDate()
}


/**
 * Date 转LocalTime
 */
@JvmOverloads
fun Date.toLocalTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalTime {
    return toZonedDateTime(zoneId)
        .toLocalTime()
}