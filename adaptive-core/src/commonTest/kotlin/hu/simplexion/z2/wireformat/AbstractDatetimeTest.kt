package hu.simplexion.z2.wireformat

import hu.simplexion.z2.wireformat.builtin.*
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class AbstractDatetimeTest<ST>(
    wireFormatProvider: WireFormatProvider
) : AbstractWireFormatTest<ST>(wireFormatProvider) {


    @Test
    fun testDuration() {
        Duration.ZERO.also { assertEquals(it, actual(it, DurationWireFormat)) }
        Duration.INFINITE.also { assertEquals(it, actual(it, DurationWireFormat)) }
        10.seconds.also { assertEquals(it, actual(it, DurationWireFormat)) }
    }

    @Test
    fun testInstant() {
        Instant.DISTANT_PAST.also { assertEquals(it, actual(it, InstantWireFormat)) }
        Instant.DISTANT_FUTURE.also { assertEquals(it, actual(it, InstantWireFormat)) }
        Clock.System.now().also { assertEquals(it, actual(it, InstantWireFormat)) }
    }

    @Test
    fun testLocalDate() {
        LocalDate(2023, 7, 27).also { assertEquals(it, actual(it, LocalDateWireFormat)) }
    }

    @Test
    fun testLocalDateTime() {
        LocalDateTime(2023, 7, 27, 15, 35, 5, 11).also { assertEquals(it, actual(it, LocalDateTimeWireFormat)) }
    }

    @Test
    fun testLocalTime() {
        LocalTime(15, 35).also { assertEquals(it, actual(it, LocalTimeWireFormat)) }
    }
}