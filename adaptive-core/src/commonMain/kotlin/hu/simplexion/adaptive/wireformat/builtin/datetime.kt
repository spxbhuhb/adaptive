/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.builtin

import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds


object DurationWireFormat : WireFormat<Duration> {

    override val wireFormatName: String
        get() = "kotlin.time.Duration"

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Duration) =
        encoder
            .boolean(1, "isInfinite", value.isInfinite())
            .long(2, "inWholeNanoseconds", value.inWholeNanoseconds)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Duration {
        if (decoder == null) return Duration.ZERO
        if (decoder.boolean(1, "isInfinite")) {
            return Duration.INFINITE
        } else {
            return decoder.long(2, "inWholeNanoseconds").nanoseconds
        }
    }

}

object InstantWireFormat : WireFormat<Instant> {

    override val wireFormatName: String
        get() = "kotlinx.datetime.Instant"

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Instant {
        if (decoder == null) return Instant.DISTANT_PAST
        return Instant.fromEpochSeconds(decoder.long(1, "epochSeconds"), decoder.int(2, "nanosecondsOfSecond"))
    }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Instant) =
        encoder
            .long(1, "epochSeconds", value.epochSeconds)
            .int(2, "nanosecondsOfSecond", value.nanosecondsOfSecond)

}

object LocalDateWireFormat : WireFormat<LocalDate> {

    override val wireFormatName: String
        get() = "kotlinx.datetime.LocalDate"

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): LocalDate {
        if (decoder == null) return LocalDate.fromEpochDays(0)
        return LocalDate(
            decoder.int(1, "year"),
            decoder.int(2, "monthNumber"),
            decoder.int(3, "dayOfMonth")
        )
    }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: LocalDate) =
        encoder
            .int(1, "year", value.year)
            .int(2, "monthNumber", value.monthNumber)
            .int(3, "dayOfMonth", value.dayOfMonth)

}

object LocalDateTimeWireFormat : WireFormat<LocalDateTime> {

    override val wireFormatName: String
        get() = "kotlinx.datetime.LocalDateTime"

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: LocalDateTime) =
        encoder
            .int(1, "year", value.year)
            .int(2, "monthNumber", value.monthNumber)
            .int(3, "dayOfMonth", value.dayOfMonth)
            .int(4, "hour", value.hour)
            .int(5, "minute", value.minute)
            .int(6, "second", value.second)
            .int(7, "nanosecond", value.nanosecond)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): LocalDateTime {
        if (decoder == null) return LocalDateTime(0, 1, 1, 0, 0, 0, 0)
        return LocalDateTime(
            decoder.int(1, "year"),
            decoder.int(2, "monthNumber"),
            decoder.int(3, "dayOfMonth"),
            decoder.int(4, "hour"),
            decoder.int(5, "minute"),
            decoder.int(6, "second"),
            decoder.int(7, "nanosecond")
        )
    }

}

object LocalTimeWireFormat : WireFormat<LocalTime> {

    override val wireFormatName: String
        get() = "kotlinx.datetime.LocalTime"

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): LocalTime {
        if (decoder == null) return LocalTime.fromSecondOfDay(0)
        return LocalTime.fromNanosecondOfDay(decoder.long(1, "nanosecondOfDay"))
    }

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: LocalTime) =
        encoder
            .long(1, "nanosecondOfDay", value.toNanosecondOfDay())

}
