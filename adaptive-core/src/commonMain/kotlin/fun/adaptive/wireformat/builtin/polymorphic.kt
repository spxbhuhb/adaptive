package `fun`.adaptive.wireformat.builtin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.WireFormat
import `fun`.adaptive.wireformat.WireFormatDecoder
import `fun`.adaptive.wireformat.WireFormatEncoder
import `fun`.adaptive.wireformat.WireFormatKind
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

object PolymorphicWireFormat : WireFormat<Any> {

    override val wireFormatName: String
        get() = "*"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Primitive

    @Suppress("UNCHECKED_CAST")
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Any): WireFormatEncoder =
        encoder.rawPolymorphic(value, wireFormatFor(value))

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Any =
        decoder !!.rawPolymorphic(source)

    @Suppress("UNCHECKED_CAST")
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Any?) : WireFormatEncoder {
        encoder.polymorphicOrNull(fieldNumber, fieldName, value, wireFormatFor(value))
        return encoder
    }

    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) =
        decoder.polymorphicOrNull<Any>(fieldNumber, fieldName)

    @OptIn(ExperimentalUnsignedTypes::class)
    fun <T> wireFormatFor(value : T?) : WireFormat<Any?> =
        when (value) {
            null -> UnitWireFormat

            is AdatClass -> value.adatCompanion.adatWireFormat
            is List<*> -> ListWireFormat(PolymorphicWireFormat)
            is Set<*> -> SetWireFormat(PolymorphicWireFormat)
            is String -> StringWireFormat
            is Int -> IntWireFormat
            is Long -> LongWireFormat
            is Double -> DoubleWireFormat
            is Boolean -> BooleanWireFormat

            is UUID<*> -> UuidWireFormat

            is Duration -> DurationWireFormat
            is Instant -> InstantWireFormat
            is LocalDateTime -> LocalDateTimeWireFormat
            is LocalDate -> LocalDateWireFormat
            is LocalTime -> LocalTimeWireFormat

            is ByteArray -> ByteArrayWireFormat

            is Byte -> ByteWireFormat
            is Short -> ShortWireFormat
            is Float -> FloatWireFormat
            is Char -> CharWireFormat

            is ShortArray -> ShortArrayWireFormat
            is IntArray -> IntArrayWireFormat
            is LongArray -> LongArrayWireFormat
            is FloatArray -> FloatArrayWireFormat
            is DoubleArray -> DoubleArrayWireFormat
            is BooleanArray -> BooleanArrayWireFormat
            is CharArray -> CharArrayWireFormat

            is UInt -> UIntWireFormat
            is UShort -> UShortWireFormat
            is UByte -> UByteWireFormat
            is ULong -> ULongWireFormat
            is UByteArray -> UByteArrayWireFormat
            is UShortArray -> UShortArrayWireFormat
            is UIntArray -> UIntArrayWireFormat
            is ULongArray -> ULongArrayWireFormat

            else -> unsupported("polymorphic type: ${value.let { it::class.simpleName }} is not supported yet")
        } as WireFormat<Any?>

}