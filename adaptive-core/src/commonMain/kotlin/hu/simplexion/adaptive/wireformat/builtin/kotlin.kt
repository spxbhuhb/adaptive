/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.builtin

import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatDecoder
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatKind
import kotlin.enums.EnumEntries

object UnitWireFormat : WireFormat<Unit> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Unit): WireFormatEncoder = encoder.rawUnit(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Unit = decoder !!.rawUnit(source)
}

object BooleanWireFormat : WireFormat<Boolean> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Boolean): WireFormatEncoder = encoder.rawBoolean(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Boolean = decoder !!.rawBoolean(source)
}

object IntWireFormat : WireFormat<Int> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Int): WireFormatEncoder = encoder.rawInt(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Int = decoder !!.rawInt(source)
}

object ShortWireFormat : WireFormat<Short> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Short): WireFormatEncoder = encoder.rawShort(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Short = decoder !!.rawShort(source)
}

object ByteWireFormat : WireFormat<Byte> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Byte): WireFormatEncoder = encoder.rawByte(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Byte = decoder !!.rawByte(source)
}

object LongWireFormat : WireFormat<Long> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Long): WireFormatEncoder = encoder.rawLong(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Long = decoder !!.rawLong(source)
}

object FloatWireFormat : WireFormat<Float> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Float): WireFormatEncoder = encoder.rawFloat(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Float = decoder !!.rawFloat(source)
}

object DoubleWireFormat : WireFormat<Double> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Double): WireFormatEncoder = encoder.rawDouble(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Double = decoder !!.rawDouble(source)
}

object CharWireFormat : WireFormat<Char> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Char): WireFormatEncoder = encoder.rawChar(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Char = decoder !!.rawChar(source)
}

object BooleanArrayWireFormat : WireFormat<BooleanArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: BooleanArray): WireFormatEncoder = encoder.rawBooleanArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): BooleanArray = decoder !!.rawBooleanArray(source)
}

object IntArrayWireFormat : WireFormat<IntArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: IntArray): WireFormatEncoder = encoder.rawIntArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): IntArray = decoder !!.rawIntArray(source)
}

object ShortArrayWireFormat : WireFormat<ShortArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ShortArray): WireFormatEncoder = encoder.rawShortArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ShortArray = decoder !!.rawShortArray(source)
}

object ByteArrayWireFormat : WireFormat<ByteArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ByteArray): WireFormatEncoder = encoder.rawByteArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ByteArray = decoder !!.rawByteArray(source)
}

object LongArrayWireFormat : WireFormat<LongArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: LongArray): WireFormatEncoder = encoder.rawLongArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): LongArray = decoder !!.rawLongArray(source)
}

object FloatArrayWireFormat : WireFormat<FloatArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: FloatArray): WireFormatEncoder = encoder.rawFloatArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): FloatArray = decoder !!.rawFloatArray(source)
}

object DoubleArrayWireFormat : WireFormat<DoubleArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: DoubleArray): WireFormatEncoder = encoder.rawDoubleArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): DoubleArray = decoder !!.rawDoubleArray(source)
}

object CharArrayWireFormat : WireFormat<CharArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: CharArray): WireFormatEncoder = encoder.rawCharArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): CharArray = decoder !!.rawCharArray(source)
}

object StringWireFormat : WireFormat<String> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: String): WireFormatEncoder = encoder.rawString(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): String = decoder !!.rawString(source)
}

class EnumWireFormat<E : Enum<E>>(val entries: EnumEntries<E>) : WireFormat<E> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: E): WireFormatEncoder = encoder.rawEnum(value, entries)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): E = decoder !!.rawEnum(source, entries)
}

object UuidWireFormat : WireFormat<UUID<*>> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UUID<*>): WireFormatEncoder = encoder.rawUuid(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UUID<*> = decoder !!.rawUuid<Any>(source)
}

object UIntWireFormat : WireFormat<UInt> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UInt): WireFormatEncoder = encoder.rawUInt(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UInt = decoder !!.rawUInt(source)
}

object UShortWireFormat : WireFormat<UShort> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UShort): WireFormatEncoder = encoder.rawUShort(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UShort = decoder !!.rawUShort(source)
}

object UByteWireFormat : WireFormat<UByte> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UByte): WireFormatEncoder = encoder.rawUByte(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UByte = decoder !!.rawUByte(source)
}

object ULongWireFormat : WireFormat<ULong> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ULong): WireFormatEncoder = encoder.rawULong(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ULong = decoder !!.rawULong(source)
}

@OptIn(ExperimentalUnsignedTypes::class)
object UIntArrayWireFormat : WireFormat<UIntArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UIntArray): WireFormatEncoder = encoder.rawUIntArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UIntArray = decoder !!.rawUIntArray(source)
}

@OptIn(ExperimentalUnsignedTypes::class)
object UShortArrayWireFormat : WireFormat<UShortArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UShortArray): WireFormatEncoder = encoder.rawUShortArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UShortArray = decoder !!.rawUShortArray(source)
}

@OptIn(ExperimentalUnsignedTypes::class)
object UByteArrayWireFormat : WireFormat<UByteArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: UByteArray): WireFormatEncoder = encoder.rawUByteArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): UByteArray = decoder !!.rawUByteArray(source)
}

@OptIn(ExperimentalUnsignedTypes::class)
object ULongArrayWireFormat : WireFormat<ULongArray> {
    override val kind: WireFormatKind get() = WireFormatKind.Primitive
    override fun wireFormatEncode(encoder: WireFormatEncoder, value: ULongArray): WireFormatEncoder = encoder.rawULongArray(value)
    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): ULongArray = decoder !!.rawULongArray(source)
}

class PairWireFormat<K, V>(
    val firstWireFormat: WireFormat<K>,
    val secondWireFormat: WireFormat<V>,
    val firstNullable: Boolean,
    val secondNullable: Boolean
) : WireFormat<Pair<K?, V?>> {

    override val kind: WireFormatKind
        get() = WireFormatKind.Primitive

    override fun wireFormatEncode(encoder: WireFormatEncoder, value: Pair<K?, V?>): WireFormatEncoder =
        encoder.rawPair(value, firstWireFormat, secondWireFormat, firstNullable, secondNullable)

    override fun <ST> wireFormatDecode(source: ST, decoder: WireFormatDecoder<ST>?): Pair<K?, V?> =
        decoder !!.rawPair(source, firstWireFormat, secondWireFormat, firstNullable, secondNullable)

}