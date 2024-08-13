package `fun`.adaptive.utility

import kotlinx.datetime.Instant
import java.time.ZonedDateTime
import java.util.Calendar

fun Calendar.toKotlinInstant(): Instant {
    val zonedDateTime = ZonedDateTime.ofInstant(toInstant(), this.timeZone.toZoneId())
    val javaInstant = zonedDateTime.toInstant()
    return Instant.fromEpochSeconds(javaInstant.epochSecond, javaInstant.nano)
}

fun Instant.toJavaCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.toEpochMilliseconds()
    return calendar
}