package hu.simplexion.z2.serialization.builtin

import hu.simplexion.z2.serialization.InstanceDecoder
import hu.simplexion.z2.serialization.InstanceEncoder
import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.MessageBuilder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds


object DurationCoder : InstanceDecoder<Duration>, InstanceEncoder<Duration> {

    override fun decodeInstance(message: Message?): Duration {
        if (message == null) return Duration.ZERO
        return message.long(1, "").nanoseconds
    }

    override fun encodeInstance(builder: MessageBuilder, value: Duration): ByteArray =
        builder
            .subBuilder()
            .long(1, "inWholeNanoseconds", value.inWholeNanoseconds)
            .pack()

}

object InstantCoder : InstanceDecoder<Instant>, InstanceEncoder<Instant> {

    override fun decodeInstance(message: Message?): Instant {
        if (message == null) return Instant.DISTANT_PAST
        return Instant.fromEpochSeconds(message.long(1, "epochSeconds"), message.int(2, "nanosecondsOfSecond"))
    }

    override fun encodeInstance(builder: MessageBuilder, value: Instant): ByteArray =
        builder
            .subBuilder()
            .long(1, "epochSeconds", value.epochSeconds)
            .int(2, "nanosecondsOfSecond", value.nanosecondsOfSecond)
            .pack()

}

object LocalDateCoder : InstanceDecoder<LocalDate>, InstanceEncoder<LocalDate> {

    override fun decodeInstance(message: Message?): LocalDate {
        if (message == null) return LocalDate.fromEpochDays(0)
        return LocalDate(
            message.int(1, "year"),
            message.int(2, "monthNumber"),
            message.int(3, "dayOfMonth")
        )
    }

    override fun encodeInstance(builder: MessageBuilder, value: LocalDate): ByteArray =
        builder
            .subBuilder()
            .int(1, "year", value.year)
            .int(2, "monthNumber", value.monthNumber)
            .int(3, "dayOfMonth", value.dayOfMonth)
            .pack()

}

object LocalDateTimeCoder : InstanceDecoder<LocalDateTime>, InstanceEncoder<LocalDateTime> {

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

    override fun encodeInstance(builder: MessageBuilder, value: LocalDateTime): ByteArray =
        builder
            .subBuilder()
            .int(1, "year", value.year)
            .int(2, "monthNumber", value.monthNumber)
            .int(3, "dayOfMonth", value.dayOfMonth)
            .int(4, "hour", value.hour)
            .int(5, "minute", value.minute)
            .int(6, "second", value.second)
            .int(7, "nanosecond", value.nanosecond)
            .pack()

}

object LocalTimeCoder : InstanceDecoder<LocalTime>, InstanceEncoder<LocalTime> {

    override fun decodeInstance(message: Message?): LocalTime {
        if (message == null) return LocalTime.fromSecondOfDay(0)
        return LocalTime.fromNanosecondOfDay(message.long(1, "toNanosecondOfDay"))
    }

    override fun encodeInstance(builder: MessageBuilder, value: LocalTime): ByteArray =
        builder
            .subBuilder()
            .long(1, "toNanosecondOfDay", value.toNanosecondOfDay())
            .pack()

}
