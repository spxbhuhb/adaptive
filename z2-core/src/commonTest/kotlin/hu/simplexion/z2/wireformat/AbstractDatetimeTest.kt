package hu.simplexion.z2.wireformat

import hu.simplexion.z2.wireformat.builtin.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

abstract class AbstractDatetimeTest(
    private val wireFormatProvider: WireFormatProvider
) {

    val sv = wireFormatProvider.standalone()

    @Test
    fun testDuration() {
        val expected = 10.seconds
        val wireFormat = sv.encodeInstance(expected, DurationWireFormat)
        val actual = sv.decodeInstance(wireFormat, DurationWireFormat)
        assertEquals(expected, actual)
    }

    @Test
    fun testInstant() {
        val expected = Clock.System.now()
        val wireFormat = sv.encodeInstance(expected, InstantWireFormat)
        val actual = sv.decodeInstance(wireFormat, InstantWireFormat)
        assertEquals(expected, actual)
    }

    @Test
    fun testLocalDate() {
        val expected = LocalDate(2023, 7, 27)
        val wireFormat = sv.encodeInstance(expected, LocalDateWireFormat)
        val actual = sv.decodeInstance(wireFormat, LocalDateWireFormat)
        assertEquals(expected, actual)
    }

    @Test
    fun testLocalDateTime() {
        val expected = LocalDateTime(2023, 7, 27, 15, 35, 5, 11)
        val wireFormat = sv.encodeInstance(expected, LocalDateTimeWireFormat)
        val actual = sv.decodeInstance(wireFormat, LocalDateTimeWireFormat)
        assertEquals(expected, actual)
    }

    @Test
    fun testLocalTime() {
        val expected = LocalTime(15, 35)
        val wireFormat = sv.encodeInstance(expected, LocalTimeWireFormat)
        val actual = sv.decodeInstance(wireFormat, LocalTimeWireFormat)
        assertEquals(expected, actual)
    }
}