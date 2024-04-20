package hu.simplexion.z2.wireformat.builtin

import hu.simplexion.z2.wireformat.Message
import hu.simplexion.z2.wireformat.MessageBuilder
import hu.simplexion.z2.wireformat.WireFormat
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds


object DurationCoder : WireFormat<Duration> {

    override fun decodeInstance(message: Message?): Duration {
        if (message == null) return Duration.ZERO
        return message.long(1, "inWholeNanoseconds").nanoseconds
    }

    override fun encodeInstance(builder: MessageBuilder, value: Duration) =
        builder
            .startInstance()
            .long(1, "inWholeNanoseconds", value.inWholeNanoseconds)
            .endInstance()
}

object InstantCoder : WireFormat<Instant> {

    override fun decodeInstance(message: Message?): Instant {
        if (message == null) return Instant.DISTANT_PAST
        return Instant.fromEpochSeconds(message.long(1, "epochSeconds"), message.int(2, "nanosecondsOfSecond"))
    }

    override fun encodeInstance(builder: MessageBuilder, value: Instant) =
        builder
            .startInstance()
            .long(1, "epochSeconds", value.epochSeconds)
            .int(2, "nanosecondsOfSecond", value.nanosecondsOfSecond)
            .endInstance()

}

object LocalDateCoder : WireFormat<LocalDate> {

    override fun decodeInstance(message: Message?): LocalDate {
        if (message == null) return LocalDate.fromEpochDays(0)
        return LocalDate(
            message.int(1, "year"),
            message.int(2, "monthNumber"),
            message.int(3, "dayOfMonth")
        )
    }

    override fun encodeInstance(builder: MessageBuilder, value: LocalDate) =
        builder
            .startInstance()
            .int(1, "year", value.year)
            .int(2, "monthNumber", value.monthNumber)
            .int(3, "dayOfMonth", value.dayOfMonth)
            .endInstance()

}

object LocalDateTimeCoder : WireFormat<LocalDateTime> {

    override fun decodeInstance(message: Message?): LocalDateTime {
        if (message == null) return LocalDateTime(0, 1, 1, 0, 0, 0, 0)
        return LocalDateTime(
            message.int(1, "year"),
            message.int(2, "monthNumber"),
            message.int(3, "dayOfMonth"),
            message.int(4, "hour"),
            message.int(5, "minute"),
            message.int(6, "second"),
            message.int(7, "nanosecond")
        )
    }

    override fun encodeInstance(builder: MessageBuilder, value: LocalDateTime) =
        builder
            .startInstance()
            .int(1, "year", value.year)
            .int(2, "monthNumber", value.monthNumber)
            .int(3, "dayOfMonth", value.dayOfMonth)
            .int(4, "hour", value.hour)
            .int(5, "minute", value.minute)
            .int(6, "second", value.second)
            .int(7, "nanosecond", value.nanosecond)
            .endInstance()

}

object LocalTimeCoder : WireFormat<LocalTime> {

    override fun decodeInstance(message: Message?): LocalTime {
        if (message == null) return LocalTime.fromSecondOfDay(0)
        return LocalTime.fromNanosecondOfDay(message.long(1, "nanosecondOfDay"))
    }

    override fun encodeInstance(builder: MessageBuilder, value: LocalTime) =
        builder
            .startInstance()
            .long(1, "nanosecondOfDay", value.toNanosecondOfDay())
            .endInstance()

}
