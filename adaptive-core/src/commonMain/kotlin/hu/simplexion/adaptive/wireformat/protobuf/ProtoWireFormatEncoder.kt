/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.protobuf

import hu.simplexion.adaptive.utility.UUID
import hu.simplexion.adaptive.wireformat.WireFormat
import hu.simplexion.adaptive.wireformat.WireFormatEncoder
import hu.simplexion.adaptive.wireformat.WireFormatKind
import kotlin.enums.EnumEntries

/**
 * Build Protocol Buffer messages.
 *
 * Use the type-specific functions to add records and then use [pack] to get
 * the wire format message.
 */
@OptIn(ExperimentalUnsignedTypes::class)
class ProtoWireFormatEncoder : WireFormatEncoder {

    val writer = ProtoBufferWriter()

    // make only one sub encoder and re-use it, this avoids allocating the
    // buffers for each tiny piece of data again and again

    var subEncoderInner: ProtoWireFormatEncoder? = null

    val subEncoder: ProtoWireFormatEncoder
        get() = subEncoderInner?.reset() ?: ProtoWireFormatEncoder().also { subEncoderInner = it }

    fun reset(): ProtoWireFormatEncoder {
        writer.reset()
        return this
    }

    override fun pack() = writer.pack()

    // ----------------------------------------------------------------------------
    // Any
    // ----------------------------------------------------------------------------

    override fun any(fieldNumber: Int, fieldName: String, value: Any): ProtoWireFormatEncoder {
        TODO()
    }

    override fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): ProtoWireFormatEncoder {
        TODO()
    }

    override fun rawAny(value: Any): WireFormatEncoder {
        TODO()
    }

    // ----------------------------------------------------------------------------
    // Unit
    // ----------------------------------------------------------------------------

    override fun unit(fieldNumber: Int, fieldName: String, value: Unit): ProtoWireFormatEncoder {
        writer.bool(fieldNumber, true)
        return this
    }

    override fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bool(fieldNumber, true)
        }
        return this
    }

    override fun rawUnit(value: Unit): WireFormatEncoder =
        boolean(1, "", true)

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    override fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): ProtoWireFormatEncoder {
        writer.bool(fieldNumber, value)
        return this
    }

    override fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bool(fieldNumber, value)
        }
        return this
    }

    override fun rawBoolean(value: Boolean): WireFormatEncoder =
        boolean(1, "", value)

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    override fun int(fieldNumber: Int, fieldName: String, value: Int): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value)
        return this
    }

    override fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value)
        }
        return this
    }

    override fun rawInt(value: Int): WireFormatEncoder =
        int(1, "", value)

    // ----------------------------------------------------------------------------
    // Short
    // ----------------------------------------------------------------------------

    override fun short(fieldNumber: Int, fieldName: String, value: Short): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun rawShort(value: Short): WireFormatEncoder =
        short(1, "", value)

    // ----------------------------------------------------------------------------
    // Byte
    // ----------------------------------------------------------------------------

    override fun byte(fieldNumber: Int, fieldName: String, value: Byte): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun rawByte(value: Byte): WireFormatEncoder =
        byte(1, "", value)

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    override fun long(fieldNumber: Int, fieldName: String, value: Long): ProtoWireFormatEncoder {
        writer.sint64(fieldNumber, value)
        return this
    }

    override fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint64(fieldNumber, value)
        }
        return this
    }

    override fun rawLong(value: Long): WireFormatEncoder =
        long(1, "", value)

    // ----------------------------------------------------------------------------
    // Float
    // ----------------------------------------------------------------------------

    override fun float(fieldNumber: Int, fieldName: String, value: Float): ProtoWireFormatEncoder {
        writer.fixed32(fieldNumber, value.toBits().toUInt())
        return this
    }

    override fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed32(fieldNumber, value.toBits().toUInt())
        }
        return this
    }

    override fun rawFloat(value: Float): WireFormatEncoder =
        float(1, "", value)

    // ----------------------------------------------------------------------------
    // Double
    // ----------------------------------------------------------------------------

    override fun double(fieldNumber: Int, fieldName: String, value: Double): ProtoWireFormatEncoder {
        writer.fixed64(fieldNumber, value.toBits().toULong())
        return this
    }

    override fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed64(fieldNumber, value.toBits().toULong())
        }
        return this
    }

    override fun rawDouble(value: Double): WireFormatEncoder =
        double(1, "", value)

    // ----------------------------------------------------------------------------
    // Char
    // ----------------------------------------------------------------------------

    override fun char(fieldNumber: Int, fieldName: String, value: Char): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.code)
        return this
    }

    override fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.code)
        }
        return this
    }

    override fun rawChar(value: Char): WireFormatEncoder =
        char(1, "", value)

    // ----------------------------------------------------------------------------
    // BooleanArray
    // ----------------------------------------------------------------------------

    override fun booleanArray(fieldNumber: Int, fieldName: String, value: BooleanArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.bool(item)
            }
        }
        return this
    }

    override fun booleanArrayOrNull(fieldNumber: Int, fieldName: String, value: BooleanArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            booleanArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawBooleanArray(value: BooleanArray): WireFormatEncoder =
        booleanArray(1, "", value)

    // ----------------------------------------------------------------------------
    // IntArray
    // ----------------------------------------------------------------------------

    override fun intArray(fieldNumber: Int, fieldName: String, value: IntArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.sint32(item)
            }
        }
        return this
    }

    override fun intArrayOrNull(fieldNumber: Int, fieldName: String, value: IntArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            intArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawIntArray(value: IntArray): WireFormatEncoder =
        intArray(1, "", value)

    // ----------------------------------------------------------------------------
    // ShortArray
    // ----------------------------------------------------------------------------

    override fun shortArray(fieldNumber: Int, fieldName: String, value: ShortArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.sint32(item.toInt())
            }
        }
        return this
    }

    override fun shortArrayOrNull(fieldNumber: Int, fieldName: String, value: ShortArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            shortArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawShortArray(value: ShortArray): WireFormatEncoder =
        shortArray(1, "", value)

    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    override fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): ProtoWireFormatEncoder {
        writer.bytes(fieldNumber, value)
        return this
    }

    override fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bytes(fieldNumber, value)
        }
        return this
    }

    override fun rawByteArray(value: ByteArray): WireFormatEncoder =
        byteArray(1, "", value)

    // ----------------------------------------------------------------------------
    // LongArray
    // ----------------------------------------------------------------------------

    override fun longArray(fieldNumber: Int, fieldName: String, value: LongArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.sint64(item)
            }
        }
        return this
    }

    override fun longArrayOrNull(fieldNumber: Int, fieldName: String, value: LongArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            longArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawLongArray(value: LongArray): WireFormatEncoder =
        longArray(1, "", value)

    // ----------------------------------------------------------------------------
    // FloatArray
    // ----------------------------------------------------------------------------

    override fun floatArray(fieldNumber: Int, fieldName: String, value: FloatArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.fixed32(item.toBits().toUInt())
            }
        }
        return this
    }

    override fun floatArrayOrNull(fieldNumber: Int, fieldName: String, value: FloatArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            floatArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawFloatArray(value: FloatArray): WireFormatEncoder =
        floatArray(1, "", value)

    // ----------------------------------------------------------------------------
    // DoubleArray
    // ----------------------------------------------------------------------------

    override fun doubleArray(fieldNumber: Int, fieldName: String, value: DoubleArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.fixed64(item.toBits().toULong())
            }
        }
        return this
    }

    override fun doubleArrayOrNull(fieldNumber: Int, fieldName: String, value: DoubleArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            doubleArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawDoubleArray(value: DoubleArray): WireFormatEncoder =
        doubleArray(1, "", value)

    // ----------------------------------------------------------------------------
    // CharArray
    // ----------------------------------------------------------------------------

    override fun charArray(fieldNumber: Int, fieldName: String, value: CharArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.sint32(item.code)
            }
        }
        return this
    }

    override fun charArrayOrNull(fieldNumber: Int, fieldName: String, value: CharArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            charArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawCharArray(value: CharArray): WireFormatEncoder =
        charArray(1, "", value)

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    override fun string(fieldNumber: Int, fieldName: String, value: String): ProtoWireFormatEncoder {
        writer.string(fieldNumber, value)
        return this
    }

    override fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.string(fieldNumber, value)
        }
        return this
    }

    override fun rawString(value: String): WireFormatEncoder =
        string(1, "", value)

    // ----------------------------------------------------------------------------
    // Enum
    // ----------------------------------------------------------------------------

    override fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, value: E, entries: EnumEntries<E>): WireFormatEncoder {
        writer.sint32(fieldNumber, value.ordinal)
        return this
    }

    override fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, value: E?, entries: EnumEntries<E>): WireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.ordinal)
        }
        return this
    }

    override fun <E : Enum<E>> rawEnum(value: E, entries: EnumEntries<E>): WireFormatEncoder =
        enum(1, "", value, entries)

    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    override fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): ProtoWireFormatEncoder {
        writer.bytes(fieldNumber, value.toByteArray())
        return this
    }

    override fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.bytes(fieldNumber, value.toByteArray())
        }
        return this
    }

    override fun rawUuid(value: UUID<*>): WireFormatEncoder =
        uuid(1, "", value)

    // ----------------------------------------------------------------------------
    // UInt
    // ----------------------------------------------------------------------------

    override fun uInt(fieldNumber: Int, fieldName: String, value: UInt): ProtoWireFormatEncoder {
        writer.fixed32(fieldNumber, value)
        return this
    }

    override fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed32(fieldNumber, value)
        }
        return this
    }

    override fun rawUInt(value: UInt): WireFormatEncoder =
        uInt(1, "", value)

    // ----------------------------------------------------------------------------
    // UShort
    // ----------------------------------------------------------------------------

    override fun uShort(fieldNumber: Int, fieldName: String, value: UShort): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun rawUShort(value: UShort): WireFormatEncoder =
        uShort(1, "", value)

    // ----------------------------------------------------------------------------
    // UByte
    // ----------------------------------------------------------------------------

    override fun uByte(fieldNumber: Int, fieldName: String, value: UByte): ProtoWireFormatEncoder {
        writer.sint32(fieldNumber, value.toInt())
        return this
    }

    override fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.sint32(fieldNumber, value.toInt())
        }
        return this
    }

    override fun rawUByte(value: UByte): WireFormatEncoder =
        uByte(1, "", value)

    // ----------------------------------------------------------------------------
    // ULong
    // ----------------------------------------------------------------------------

    override fun uLong(fieldNumber: Int, fieldName: String, value: ULong): ProtoWireFormatEncoder {
        writer.fixed64(fieldNumber, value)
        return this
    }

    override fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            writer.fixed64(fieldNumber, value)
        }
        return this
    }

    override fun rawULong(value: ULong): WireFormatEncoder =
        uLong(1, "", value)

    // ----------------------------------------------------------------------------
    // UIntArray
    // ----------------------------------------------------------------------------

    override fun uIntArray(fieldNumber: Int, fieldName: String, value: UIntArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.fixed32(item)
            }
        }
        return this
    }

    override fun uIntArrayOrNull(fieldNumber: Int, fieldName: String, value: UIntArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uIntArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawUIntArray(value: UIntArray): WireFormatEncoder =
        uIntArray(1, "", value)

    // ----------------------------------------------------------------------------
    // UShortArray
    // ----------------------------------------------------------------------------

    override fun uShortArray(fieldNumber: Int, fieldName: String, value: UShortArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.sint32(item.toInt())
            }
        }
        return this
    }

    override fun uShortArrayOrNull(fieldNumber: Int, fieldName: String, value: UShortArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uShortArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawUShortArray(value: UShortArray): WireFormatEncoder =
        uShortArray(1, "", value)

    // ----------------------------------------------------------------------------
    // UByteArray
    // ----------------------------------------------------------------------------


    override fun uByteArray(fieldNumber: Int, fieldName: String, value: UByteArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.sint32(item.toInt())
            }
        }
        return this
    }

    override fun uByteArrayOrNull(fieldNumber: Int, fieldName: String, value: UByteArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uByteArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawUByteArray(value: UByteArray): WireFormatEncoder =
        uByteArray(1, "", value)

    // ----------------------------------------------------------------------------
    // ULongArray
    // ----------------------------------------------------------------------------

    override fun uLongArray(fieldNumber: Int, fieldName: String, value: ULongArray): ProtoWireFormatEncoder {
        sub(fieldNumber) {
            for (item in value) {
                it.fixed64(item)
            }
        }
        return this
    }

    override fun uLongArrayOrNull(fieldNumber: Int, fieldName: String, value: ULongArray?): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            uLongArray(fieldNumber, fieldName, value)
        }
        return this
    }

    override fun rawULongArray(value: ULongArray): WireFormatEncoder =
        uLongArray(1, "", value)

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    override fun <T> instance(fieldNumber: Int, fieldName: String, value: T, wireFormat: WireFormat<T>): ProtoWireFormatEncoder {
        if (wireFormat.kind == WireFormatKind.Primitive) {
            wireFormat.wireFormatEncode(this, value)
        } else {
            val bytes = subEncoder.apply { wireFormat.wireFormatEncode(this, value) }.pack()
            writer.bytes(fieldNumber, bytes)
        }
        return this
    }

    override fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, value: T?, wireFormat: WireFormat<T>): ProtoWireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            instance(fieldNumber, fieldName, value, wireFormat)
        }
        return this
    }

    override fun <T> rawInstance(value: T, wireFormat: WireFormat<T>): WireFormatEncoder =
        instance(1, "", value, wireFormat)

    override fun <T> rawInstanceOrNull(value: T?, wireFormat: WireFormat<T>): WireFormatEncoder {
        instanceOrNull(1, "", value, wireFormat)
        return this
    }

    // ----------------------------------------------------------------------------
    // Pair
    // ----------------------------------------------------------------------------

    override fun <T1, T2> pair(
        fieldNumber: Int,
        fieldName: String,
        value: Pair<T1?, T2?>,
        firstWireFormat: WireFormat<T1>,
        secondWireFormat: WireFormat<T2>,
        firstNullable: Boolean,
        secondNullable: Boolean
    ): WireFormatEncoder {
        val bytes1 = value.first?.let { subEncoder.apply { firstWireFormat.wireFormatEncode(this, it) }.pack() }
        val bytes2 = value.second?.let { subEncoder.apply { secondWireFormat.wireFormatEncode(this, it) }.pack() }

        val bytes = subEncoder.apply {

            if (bytes1 == null) {
                check(firstNullable)
                writer.bool(1 + NULL_SHIFT, true)
            } else {
                byteArray(1, "", bytes1)
            }

            if (bytes2 == null) {
                check(secondNullable)
                writer.bool(2 + NULL_SHIFT, true)
            } else {
                byteArray(2, "", bytes2)
            }

        }.pack()

        writer.bytes(fieldNumber, bytes)

        return this
    }

    override fun <T1, T2> pairOrNull(
        fieldNumber: Int,
        fieldName: String,
        value: Pair<T1?, T2?>?,
        firstWireFormat: WireFormat<T1>,
        secondWireFormat: WireFormat<T2>,
        firstNullable: Boolean,
        secondNullable: Boolean
    ): WireFormatEncoder {
        if (value == null) {
            writer.bool(fieldNumber + NULL_SHIFT, true)
        } else {
            pair(fieldNumber, fieldName, value, firstWireFormat, secondWireFormat, firstNullable, secondNullable)
        }
        return this
    }

    override fun <T1, T2> rawPair(
        value: Pair<T1?, T2?>,
        firstWireFormat: WireFormat<T1>,
        secondWireFormat: WireFormat<T2>,
        firstNullable: Boolean,
        secondNullable: Boolean
    ): WireFormatEncoder =
        pair(1, "", value, firstWireFormat, secondWireFormat, firstNullable, secondNullable)

    // -----------------------------------------------------------------------------------------
    // Utilities for classes that implement `WireFormat`
    // -----------------------------------------------------------------------------------------

    fun <T> item(value: T, wireFormat: WireFormat<T>) {
        when (wireFormat.kind) {
            WireFormatKind.Primitive -> wireFormat.wireFormatEncode(this, value)
            WireFormatKind.Collection -> instance(1, "", value, wireFormat)
            WireFormatKind.Instance -> instance(1, "", value, wireFormat)
        }
    }

    override fun <T> items(value: Collection<T?>, itemWireFormat: WireFormat<T>, nullable : Boolean): WireFormatEncoder {
        value.forEach {
            if (it == null) {
                writer.bool(1 + NULL_SHIFT, true)
            } else {
                item(it, itemWireFormat)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    fun sub(fieldNumber: Int, block: (sub: ProtoBufferWriter) -> Unit) {
        val sub = ProtoBufferWriter()
        block(sub)
        writer.bytes(fieldNumber, sub.pack())
    }

}