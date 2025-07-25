package `fun`.adaptive.ui.input.duration

import kotlinx.datetime.DateTimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DurationInputViewBackendTest {

    @Test
    fun testFromDuration() {
        // Test null input
        val nullBackend = DurationInputViewBackend(null)
        val (nullAmount, nullUnit) = nullBackend.fromDuration()
        assertEquals(null, nullAmount)
        assertEquals(DateTimeUnit.SECOND, nullUnit)

        // Test weeks
        val weekBackend = DurationInputViewBackend(14.days)
        val (weekAmount, weekUnit) = weekBackend.fromDuration()
        assertEquals(2, weekAmount)
        assertEquals(DateTimeUnit.WEEK, weekUnit)

        // Test days
        val dayBackend = DurationInputViewBackend(5.days)
        val (dayAmount, dayUnit) = dayBackend.fromDuration()
        assertEquals(5, dayAmount)
        assertEquals(DateTimeUnit.DAY, dayUnit)

        // Test hours
        val hourBackend = DurationInputViewBackend(12.hours)
        val (hourAmount, hourUnit) = hourBackend.fromDuration()
        assertEquals(12, hourAmount)
        assertEquals(DateTimeUnit.HOUR, hourUnit)

        // Test minutes
        val minuteBackend = DurationInputViewBackend(45.minutes)
        val (minuteAmount, minuteUnit) = minuteBackend.fromDuration()
        assertEquals(45, minuteAmount)
        assertEquals(DateTimeUnit.MINUTE, minuteUnit)

        // Test seconds
        val secondBackend = DurationInputViewBackend(30.seconds)
        val (secondAmount, secondUnit) = secondBackend.fromDuration()
        assertEquals(30, secondAmount)
        assertEquals(DateTimeUnit.SECOND, secondUnit)

        // Test milliseconds
        val millisBackend = DurationInputViewBackend(500.milliseconds)
        val (millisAmount, millisUnit) = millisBackend.fromDuration()
        assertEquals(500, millisAmount)
        assertEquals(DateTimeUnit.MILLISECOND, millisUnit)

        // Test mixed duration that should be represented as the smaller unit
        val mixedBackend = DurationInputViewBackend(1.hours + 30.minutes)
        val (mixedAmount, mixedUnit) = mixedBackend.fromDuration()
        assertEquals(90, mixedAmount)
        assertEquals(DateTimeUnit.MINUTE, mixedUnit)
    }
}