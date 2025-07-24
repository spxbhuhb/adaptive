package `fun`.adaptive.utility

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System.now
import kotlin.time.Duration
import kotlin.time.Instant
import kotlinx.datetime.minus
import kotlinx.datetime.plus

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

fun Instant.localDate() =
    this.toLocalDateTime(TimeZone.currentSystemDefault()).date

fun Instant.localTime() =
    this.toLocalDateTime(TimeZone.currentSystemDefault()).time

fun Instant.isBetween(start: Instant, end: Instant): Boolean =
    this in start .. end

fun LocalDateTime.isBetween(start: Instant, end: Instant): Boolean =
    start <= this && this <= end

fun LocalDate.isBetween(start: Instant, end: Instant): Boolean =
    start <= this && this <= end

fun LocalTime.isBetween(start: Instant, end: Instant): Boolean =
    start <= this && this <= end

val LocalDate.previousMonth: LocalDate
    get() = minus(1, DateTimeUnit.MONTH)

val LocalDate.twoMonthsBefore: LocalDate
    get() = minus(2, DateTimeUnit.MONTH)

val LocalDate.firstDayOfMonth: LocalDate
    get() = minus(day - 1, DateTimeUnit.DAY)

val LocalDate.lastDayOfMonth: LocalDate
    get() = plus(1, DateTimeUnit.MONTH).firstDayOfMonth.minus(1, DateTimeUnit.DAY)