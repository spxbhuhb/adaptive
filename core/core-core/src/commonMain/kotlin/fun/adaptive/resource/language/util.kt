package `fun`.adaptive.resource.language

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Instant.localized() =
    toLocalDateTime(TimeZone.currentSystemDefault()).toString().replace("T", " ").substringBefore('.')

fun LocalTime.localizedHourAndMinute() =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

fun Duration.localized() =
    toString()

fun String.parseLocalizedHourAndMinuteOrNull(): LocalTime? {
    val trimmed = this.trim()

    // Check for AM/PM suffix
    val isAM = trimmed.endsWith("AM", ignoreCase = true)
    val isPM = trimmed.endsWith("PM", ignoreCase = true)

    // Remove AM/PM suffix if present
    val timeString = if (isAM || isPM) {
        trimmed.dropLast(2).trim()
    } else {
        trimmed
    }

    // Split by colon and parse hour and minute
    val parts = timeString.split(":")
    if (parts.size != 2) return null

    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null

    // Validate hour and minute values
    if (minute !in 0..59) return null

    // Handle 12-hour format with AM/PM
    val adjustedHour = when {
        isAM && hour == 12 -> 0 // 12 AM is 00:00 in 24-hour format
        isPM && hour < 12 -> hour + 12 // Add 12 to PM hours (except 12 PM)
        isPM && hour > 12 -> return null // Invalid PM hour
        hour !in 0..23 -> return null // Invalid 24-hour format
        else -> hour
    }

    return LocalTime(adjustedHour, minute)
}

fun LocalDateTime.localized() =
    "$year.${month.number.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')}" +
        " " +
        "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
