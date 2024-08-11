package `fun`.adaptive.utility

import kotlinx.datetime.*
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration

fun instant() = now()

fun zeroDuration() =
    Duration.ZERO

fun localDateTime(): LocalDateTime =
    now().toLocalDateTime(TimeZone.currentSystemDefault())

fun localDate(): LocalDate =
    now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun localTime(): LocalTime =
    now().toLocalDateTime(TimeZone.currentSystemDefault()).time

operator fun LocalDateTime.compareTo(other: Instant) =
    this.toInstant(TimeZone.currentSystemDefault()).compareTo(other)

operator fun LocalDate.compareTo(other: Instant) =
    LocalDateTime(this, LocalTime(0, 0)).toInstant(TimeZone.currentSystemDefault()).compareTo(other)

operator fun LocalTime.compareTo(other: Instant) =
    LocalDateTime(localDate(), this).toInstant(TimeZone.currentSystemDefault()).compareTo(other)

operator fun Instant.compareTo(other: LocalDateTime) =
    this.compareTo(other.toInstant(TimeZone.currentSystemDefault()))

operator fun Instant.compareTo(other: LocalDate) =
    this.compareTo(LocalDateTime(other, LocalTime(0, 0)).toInstant(TimeZone.currentSystemDefault()))

operator fun Instant.compareTo(other: LocalTime) =
    this.compareTo(LocalDateTime(localDate(), other).toInstant(TimeZone.currentSystemDefault()))

fun Instant.isBetween(start: Instant, end: Instant): Boolean =
    this in start .. end

fun LocalDateTime.isBetween(start: Instant, end: Instant): Boolean =
    start <= this && this <= end

fun LocalDate.isBetween(start: Instant, end: Instant): Boolean =
    start <= this && this <= end

fun LocalTime.isBetween(start: Instant, end: Instant): Boolean =
    start <= this && this <= end