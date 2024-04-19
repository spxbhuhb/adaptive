package hu.simplexion.z2.serialization

import hu.simplexion.z2.serialization.builtin.DurationCoder
import hu.simplexion.z2.serialization.builtin.InstantCoder
import hu.simplexion.z2.serialization.builtin.LocalDateCoder
import hu.simplexion.z2.serialization.builtin.LocalDateTimeCoder
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

abstract class AbstractDatetimeTest(
    private val serializationConfig: SerializationConfig
) {

    @Test
    fun testDuration() {
        val expected = 10.seconds
        val wireformat = serializationConfig.messageBuilder().instance(1, "", DurationCoder, expected).pack()
        val actual = serializationConfig.toMessage(wireformat).instance(1, "", DurationCoder)
        assertEquals(expected, actual)
    }

    @Test
    fun testInstant() {
        val expected = Clock.System.now()
        val wireformat = serializationConfig.messageBuilder().instance(1, "", InstantCoder, expected).pack()
        val actual = serializationConfig.toMessage(wireformat).instance(1, "", InstantCoder)
        assertEquals(expected, actual)
    }

    @Test
    fun testLocalDate() {
        val expected = LocalDate(2023, 7, 27)
        val wireformat = serializationConfig.messageBuilder().instance(1, "", LocalDateCoder, expected).pack()
        val actual = serializationConfig.toMessage(wireformat).instance(1, "", LocalDateCoder)
        assertEquals(expected, actual)
    }

    @Test
    fun testLocalDateTime() {
        val expected = LocalDateTime(2023, 7, 27, 15, 35, 5, 11)
        val wireformat = serializationConfig.messageBuilder().instance(1, "", LocalDateTimeCoder, expected).pack()
        val actual = serializationConfig.toMessage(wireformat).instance(1, "", LocalDateTimeCoder)
        assertEquals(expected, actual)
    }
}