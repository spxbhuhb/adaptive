/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.builtin

import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatKind
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument
import kotlin.enums.EnumEntries

object NothingWireFormat : WireFormat<Nothing> {
    override val wireFormatName: String get() = "kotlin.Nothing"
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Nothing): WireFormatEncoder = error()
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Nothing = error()

    fun error() : Nothing {
        throw IllegalStateException("NothingWireFormat should be never be called. This is most probably an error in Adaptive. Please open an issue at https://github.com/spxbhuhb/adaptive")
    }
}

object UnitWireFormat : WireFormat<Unit> {
    override val wireFormatName: String get() = "kotlin.Unit"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Unit): WireFormatEncoder = encoder.rawUnit(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Unit = decoder !!.rawUnit(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Unit?) = encoder.unitOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.unitOrNull(fieldNumber, fieldName)
}

object BooleanWireFormat : WireFormat<Boolean> {
    override val wireFormatName: String get() = "kotlin.Boolean"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Boolean): WireFormatEncoder = encoder.rawBoolean(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Boolean = decoder !!.rawBoolean(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Boolean?) = encoder.booleanOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.booleanOrNull(fieldNumber, fieldName)
}

object IntWireFormat : WireFormat<Int> {
    override val wireFormatName: String get() = "kotlin.Int"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Int): WireFormatEncoder = encoder.rawInt(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Int = decoder !!.rawInt(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Int?) = encoder.intOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.intOrNull(fieldNumber, fieldName)
}

object ShortWireFormat : WireFormat<Short> {
    override val wireFormatName: String get() = "kotlin.Short"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Short): WireFormatEncoder = encoder.rawShort(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Short = decoder !!.rawShort(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Short?) = encoder.shortOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.shortOrNull(fieldNumber, fieldName)
}

object ByteWireFormat : WireFormat<Byte> {
    override val wireFormatName: String get() = "kotlin.Byte"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Byte): WireFormatEncoder = encoder.rawByte(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Byte = decoder !!.rawByte(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Byte?) = encoder.byteOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.byteOrNull(fieldNumber, fieldName)
}

object LongWireFormat : WireFormat<Long> {
    override val wireFormatName: String get() = "kotlin.Long"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Long): WireFormatEncoder = encoder.rawLong(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Long = decoder !!.rawLong(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Long?) = encoder.longOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.longOrNull(fieldNumber, fieldName)
}

object FloatWireFormat : WireFormat<Float> {
    override val wireFormatName: String get() = "kotlin.Float"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Float): WireFormatEncoder = encoder.rawFloat(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Float = decoder !!.rawFloat(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Float?) = encoder.floatOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.floatOrNull(fieldNumber, fieldName)
}

object DoubleWireFormat : WireFormat<Double> {
    override val wireFormatName: String get() = "kotlin.Double"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Double): WireFormatEncoder = encoder.rawDouble(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Double = decoder !!.rawDouble(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Double?) = encoder.doubleOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.doubleOrNull(fieldNumber, fieldName)
}

object CharWireFormat : WireFormat<Char> {
    override val wireFormatName: String get() = "kotlin.Char"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Char): WireFormatEncoder = encoder.rawChar(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Char = decoder !!.rawChar(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Char?) = encoder.charOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.charOrNull(fieldNumber, fieldName)
}

object BooleanArrayWireFormat : WireFormat<BooleanArray> {
    override val wireFormatName: String get() = "kotlin.BooleanArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: BooleanArray): WireFormatEncoder = encoder.rawBooleanArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): BooleanArray = decoder !!.rawBooleanArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: BooleanArray?) = encoder.booleanArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.booleanArrayOrNull(fieldNumber, fieldName)
}

object IntArrayWireFormat : WireFormat<IntArray> {
    override val wireFormatName: String get() = "kotlin.IntArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: IntArray): WireFormatEncoder = encoder.rawIntArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): IntArray = decoder !!.rawIntArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: IntArray?) = encoder.intArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.intArrayOrNull(fieldNumber, fieldName)
}

object ShortArrayWireFormat : WireFormat<ShortArray> {
    override val wireFormatName: String get() = "kotlin.ShortArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ShortArray): WireFormatEncoder = encoder.rawShortArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ShortArray = decoder !!.rawShortArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: ShortArray?) = encoder.shortArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.shortArrayOrNull(fieldNumber, fieldName)
}

object ByteArrayWireFormat : WireFormat<ByteArray> {
    override val wireFormatName: String get() = "kotlin.ByteArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ByteArray): WireFormatEncoder = encoder.rawByteArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ByteArray = decoder !!.rawByteArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: ByteArray?) = encoder.byteArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.byteArrayOrNull(fieldNumber, fieldName)
}

object LongArrayWireFormat : WireFormat<LongArray> {
    override val wireFormatName: String get() = "kotlin.LongArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: LongArray): WireFormatEncoder = encoder.rawLongArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): LongArray = decoder !!.rawLongArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: LongArray?) = encoder.longArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.longArrayOrNull(fieldNumber, fieldName)
}

object FloatArrayWireFormat : WireFormat<FloatArray> {
    override val wireFormatName: String get() = "kotlin.FloatArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: FloatArray): WireFormatEncoder = encoder.rawFloatArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): FloatArray = decoder !!.rawFloatArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: FloatArray?) = encoder.floatArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.floatArrayOrNull(fieldNumber, fieldName)
}

object DoubleArrayWireFormat : WireFormat<DoubleArray> {
    override val wireFormatName: String get() = "kotlin.DoubleArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: DoubleArray): WireFormatEncoder = encoder.rawDoubleArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): DoubleArray = decoder !!.rawDoubleArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: DoubleArray?) = encoder.doubleArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.doubleArrayOrNull(fieldNumber, fieldName)
}

object CharArrayWireFormat : WireFormat<CharArray> {
    override val wireFormatName: String get() = "kotlin.CharArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: CharArray): WireFormatEncoder = encoder.rawCharArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): CharArray = decoder !!.rawCharArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: CharArray?) = encoder.charArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.charArrayOrNull(fieldNumber, fieldName)
}

object StringWireFormat : WireFormat<String> {
    override val wireFormatName: String get() = "kotlin.String"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: String): WireFormatEncoder = encoder.rawString(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): String = decoder !!.rawString(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: String?) = encoder.stringOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.stringOrNull(fieldNumber, fieldName)
}

class EnumWireFormat<E : Enum<E>>(val entries: EnumEntries<E>) : WireFormat<E> {
    override val wireFormatName: String get() = "kotlin.Enum" // FIXME polymorphic enum
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: E): WireFormatEncoder = encoder.rawEnum(value, entries)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): E = decoder !!.rawEnum(source, entries)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: E?) = encoder.enumOrNull(fieldNumber, fieldName, value, entries)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.enumOrNull(fieldNumber, fieldName, entries)
}

object UuidWireFormat : WireFormat<UUID<*>> {
    override val wireFormatName: String get() = "hu.simplexion.adaptive.utility.UUID"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UUID<*>): WireFormatEncoder = encoder.rawUuid(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UUID<*> = decoder !!.rawUuid<Any>(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: UUID<*>?) = encoder.uuidOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uuidOrNull<Any>(fieldNumber, fieldName)
}

object UIntWireFormat : WireFormat<UInt> {
    override val wireFormatName: String get() = "kotlin.UInt"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UInt): WireFormatEncoder = encoder.rawUInt(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UInt = decoder !!.rawUInt(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: UInt?) = encoder.uIntOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uIntOrNull(fieldNumber, fieldName)
}

object UShortWireFormat : WireFormat<UShort> {
    override val wireFormatName: String get() = "kotlin.UShort"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UShort): WireFormatEncoder = encoder.rawUShort(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UShort = decoder !!.rawUShort(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: UShort?) = encoder.uShortOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uShortOrNull(fieldNumber, fieldName)
}

object UByteWireFormat : WireFormat<UByte> {
    override val wireFormatName: String get() = "kotlin.UByte"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UByte): WireFormatEncoder = encoder.rawUByte(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UByte = decoder !!.rawUByte(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: UByte?) = encoder.uByteOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uByteOrNull(fieldNumber, fieldName)
}

object ULongWireFormat : WireFormat<ULong> {
    override val wireFormatName: String get() = "kotlin.ULong"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ULong): WireFormatEncoder = encoder.rawULong(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ULong = decoder !!.rawULong(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: ULong?) = encoder.uLongOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uLongOrNull(fieldNumber, fieldName)
}

@OptIn(ExperimentalUnsignedTypes::class)
object UIntArrayWireFormat : WireFormat<UIntArray> {
    override val wireFormatName: String get() = "kotlin.UIntArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UIntArray): WireFormatEncoder = encoder.rawUIntArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UIntArray = decoder !!.rawUIntArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: UIntArray?) = encoder.uIntArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uIntArrayOrNull(fieldNumber, fieldName)
}

@OptIn(ExperimentalUnsignedTypes::class)
object UShortArrayWireFormat : WireFormat<UShortArray> {
    override val wireFormatName: String get() = "kotlin.UShortArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UShortArray): WireFormatEncoder = encoder.rawUShortArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UShortArray = decoder !!.rawUShortArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: UShortArray?) = encoder.uShortArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uShortArrayOrNull(fieldNumber, fieldName)
}

@OptIn(ExperimentalUnsignedTypes::class)
object UByteArrayWireFormat : WireFormat<UByteArray> {
    override val wireFormatName: String get() = "kotlin.UByteArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UByteArray): WireFormatEncoder = encoder.rawUByteArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UByteArray = decoder !!.rawUByteArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: UByteArray?) = encoder.uByteArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uByteArrayOrNull(fieldNumber, fieldName)
}

@OptIn(ExperimentalUnsignedTypes::class)
object ULongArrayWireFormat : WireFormat<ULongArray> {
    override val wireFormatName: String get() = "kotlin.ULongArray"
    override val wireFormatKind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ULongArray): WireFormatEncoder = encoder.rawULongArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ULongArray = decoder !!.rawULongArray(source)
    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: ULongArray?) = encoder.uLongArrayOrNull(fieldNumber, fieldName, value)
    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) = decoder.uLongArrayOrNull(fieldNumber, fieldName)
}

class PairWireFormat<T1, T2>(
   val typeArgument1: WireFormatTypeArgument<T1>,
   val typeArgument2: WireFormatTypeArgument<T2>,
) : WireFormat<Pair<T1?, T2?>> {

    override val wireFormatName: String
        get() = "kotlin.Pair"

    override val wireFormatKind: WireFormatKind
        get() = WireFormatKind.Primitive

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Pair<T1?, T2?>): WireFormatEncoder =
        encoder.rawPair(value, typeArgument1, typeArgument2)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Pair<T1?, T2?> =
        decoder !!.rawPair(source, typeArgument1, typeArgument2)

    override fun wireFormatEncode(encoder: WireFormatEncoder, fieldNumber: Int, fieldName: String, value: Pair<T1?, T2?>?) =
        encoder.pairOrNull(fieldNumber, fieldName, value, typeArgument1, typeArgument2)

    override fun <ST> wireFormatDecode(decoder: WireFormatDecoder<ST>, fieldNumber: Int, fieldName: String) =
        decoder.pairOrNull(fieldNumber, fieldName, typeArgument1, typeArgument2)
}