/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.utility.UUID
import kotlin.enums.EnumEntries

/**
 * Interface for building serialized messages. Protobuf needs field number
 * JSON needs field name, hence passing both.
 */
@OptIn(ExperimentalUnsignedTypes::class)
interface WireFormatEncoder {

    fun pack(): ByteArray

    // ----------------------------------------------------------------------------
    // Special Types
    // ----------------------------------------------------------------------------

    fun any(fieldNumber: Int, fieldName: String, value: Any): WireFormatEncoder

    fun anyOrNull(fieldNumber: Int, fieldName: String, value: Any?): WireFormatEncoder

    fun rawAny(value: Any): WireFormatEncoder

    // ----

    fun unit(fieldNumber: Int, fieldName: String, value: Unit): WireFormatEncoder

    fun unitOrNull(fieldNumber: Int, fieldName: String, value: Unit?): WireFormatEncoder

    fun rawUnit(value: Unit): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Primitives
    // ----------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, fieldName: String, value: Boolean): WireFormatEncoder

    fun booleanOrNull(fieldNumber: Int, fieldName: String, value: Boolean?): WireFormatEncoder

    fun rawBoolean(value: Boolean): WireFormatEncoder

    // ----

    fun int(fieldNumber: Int, fieldName: String, value: Int): WireFormatEncoder

    fun intOrNull(fieldNumber: Int, fieldName: String, value: Int?): WireFormatEncoder

    fun rawInt(value: Int): WireFormatEncoder

    // ----

    fun short(fieldNumber: Int, fieldName: String, value: Short): WireFormatEncoder

    fun shortOrNull(fieldNumber: Int, fieldName: String, value: Short?): WireFormatEncoder

    fun rawShort(value: Short): WireFormatEncoder

    // ----

    fun byte(fieldNumber: Int, fieldName: String, value: Byte): WireFormatEncoder

    fun byteOrNull(fieldNumber: Int, fieldName: String, value: Byte?): WireFormatEncoder

    fun rawByte(value: Byte): WireFormatEncoder

    // ----

    fun long(fieldNumber: Int, fieldName: String, value: Long): WireFormatEncoder

    fun longOrNull(fieldNumber: Int, fieldName: String, value: Long?): WireFormatEncoder

    fun rawLong(value: Long): WireFormatEncoder

    // ----

    fun float(fieldNumber: Int, fieldName: String, value: Float): WireFormatEncoder

    fun floatOrNull(fieldNumber: Int, fieldName: String, value: Float?): WireFormatEncoder

    fun rawFloat(value: Float): WireFormatEncoder

    // ----

    fun double(fieldNumber: Int, fieldName: String, value: Double): WireFormatEncoder

    fun doubleOrNull(fieldNumber: Int, fieldName: String, value: Double?): WireFormatEncoder

    fun rawDouble(value: Double): WireFormatEncoder

    // ----

    fun char(fieldNumber: Int, fieldName: String, value: Char): WireFormatEncoder

    fun charOrNull(fieldNumber: Int, fieldName: String, value: Char?): WireFormatEncoder

    fun rawChar(value: Char): WireFormatEncoder

    // -----------------------------------------------------------------------------------------
    // Arrays
    // -----------------------------------------------------------------------------------------

    fun booleanArray(fieldNumber: Int, fieldName: String, value: BooleanArray): WireFormatEncoder

    fun booleanArrayOrNull(fieldNumber: Int, fieldName: String, value: BooleanArray?): WireFormatEncoder

    fun rawBooleanArray(value: BooleanArray): WireFormatEncoder

    // ----

    fun intArray(fieldNumber: Int, fieldName: String, value: IntArray): WireFormatEncoder

    fun intArrayOrNull(fieldNumber: Int, fieldName: String, value: IntArray?): WireFormatEncoder

    fun rawIntArray(value: IntArray): WireFormatEncoder

    // ----

    fun shortArray(fieldNumber: Int, fieldName: String, value: ShortArray): WireFormatEncoder

    fun shortArrayOrNull(fieldNumber: Int, fieldName: String, value: ShortArray?): WireFormatEncoder

    fun rawShortArray(value: ShortArray): WireFormatEncoder

    // ----

    fun byteArray(fieldNumber: Int, fieldName: String, value: ByteArray): WireFormatEncoder

    fun byteArrayOrNull(fieldNumber: Int, fieldName: String, value: ByteArray?): WireFormatEncoder

    fun rawByteArray(value: ByteArray): WireFormatEncoder

    // ----

    fun longArray(fieldNumber: Int, fieldName: String, value: LongArray): WireFormatEncoder

    fun longArrayOrNull(fieldNumber: Int, fieldName: String, value: LongArray?): WireFormatEncoder

    fun rawLongArray(value: LongArray): WireFormatEncoder

    // ----

    fun floatArray(fieldNumber: Int, fieldName: String, value: FloatArray): WireFormatEncoder

    fun floatArrayOrNull(fieldNumber: Int, fieldName: String, value: FloatArray?): WireFormatEncoder

    fun rawFloatArray(value: FloatArray): WireFormatEncoder

    // ----

    fun doubleArray(fieldNumber: Int, fieldName: String, value: DoubleArray): WireFormatEncoder

    fun doubleArrayOrNull(fieldNumber: Int, fieldName: String, value: DoubleArray?): WireFormatEncoder

    fun rawDoubleArray(value: DoubleArray): WireFormatEncoder

    // ----

    fun charArray(fieldNumber: Int, fieldName: String, value: CharArray): WireFormatEncoder

    fun charArrayOrNull(fieldNumber: Int, fieldName: String, value: CharArray?): WireFormatEncoder

    fun rawCharArray(value: CharArray): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Built-in Types
    // ----------------------------------------------------------------------------

    fun string(fieldNumber: Int, fieldName: String, value: String): WireFormatEncoder

    fun stringOrNull(fieldNumber: Int, fieldName: String, value: String?): WireFormatEncoder

    fun rawString(value: String): WireFormatEncoder

    // ----

    fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, value: E, entries: EnumEntries<E>): WireFormatEncoder

    fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, value: E?, entries: EnumEntries<E>): WireFormatEncoder

    fun <E : Enum<E>> rawEnum(value: E, entries: EnumEntries<E>): WireFormatEncoder

    // ----

    fun uuid(fieldNumber: Int, fieldName: String, value: UUID<*>): WireFormatEncoder

    fun uuidOrNull(fieldNumber: Int, fieldName: String, value: UUID<*>?): WireFormatEncoder

    fun rawUuid(value: UUID<*>): WireFormatEncoder

    // -----------------------------------------------------------------------------------------
    // Unsigned Primitives
    // -----------------------------------------------------------------------------------------

    fun uInt(fieldNumber: Int, fieldName: String, value: UInt): WireFormatEncoder

    fun uIntOrNull(fieldNumber: Int, fieldName: String, value: UInt?): WireFormatEncoder

    fun rawUInt(value: UInt): WireFormatEncoder

    // ----

    fun uShort(fieldNumber: Int, fieldName: String, value: UShort): WireFormatEncoder

    fun uShortOrNull(fieldNumber: Int, fieldName: String, value: UShort?): WireFormatEncoder

    fun rawUShort(value: UShort): WireFormatEncoder

    // ----

    fun uByte(fieldNumber: Int, fieldName: String, value: UByte): WireFormatEncoder

    fun uByteOrNull(fieldNumber: Int, fieldName: String, value: UByte?): WireFormatEncoder

    fun rawUByte(value: UByte): WireFormatEncoder

    // ----

    fun uLong(fieldNumber: Int, fieldName: String, value: ULong): WireFormatEncoder

    fun uLongOrNull(fieldNumber: Int, fieldName: String, value: ULong?): WireFormatEncoder

    fun rawULong(value: ULong): WireFormatEncoder

    // -----------------------------------------------------------------------------------------
    // Unsigned Arrays
    // -----------------------------------------------------------------------------------------

    fun uIntArray(fieldNumber: Int, fieldName: String, value: UIntArray): WireFormatEncoder

    fun uIntArrayOrNull(fieldNumber: Int, fieldName: String, value: UIntArray?): WireFormatEncoder

    fun rawUIntArray(value: UIntArray): WireFormatEncoder

    // ----

    fun uShortArray(fieldNumber: Int, fieldName: String, value: UShortArray): WireFormatEncoder

    fun uShortArrayOrNull(fieldNumber: Int, fieldName: String, value: UShortArray?): WireFormatEncoder

    fun rawUShortArray(value: UShortArray): WireFormatEncoder

    // ----

    fun uByteArray(fieldNumber: Int, fieldName: String, value: UByteArray): WireFormatEncoder

    fun uByteArrayOrNull(fieldNumber: Int, fieldName: String, value: UByteArray?): WireFormatEncoder

    fun rawUByteArray(value: UByteArray): WireFormatEncoder

    // ----

    fun uLongArray(fieldNumber: Int, fieldName: String, value: ULongArray): WireFormatEncoder

    fun uLongArrayOrNull(fieldNumber: Int, fieldName: String, value: ULongArray?): WireFormatEncoder

    fun rawULongArray(value: ULongArray): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Instance
    // ----------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, fieldName: String, value: T, wireFormat: WireFormat<T>): WireFormatEncoder

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, value: T?, wireFormat: WireFormat<T>): WireFormatEncoder

    fun <T> rawInstance(value: T, wireFormat: WireFormat<T>): WireFormatEncoder

    fun <T> rawInstanceOrNull(value: T?, wireFormat: WireFormat<T>): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Pair
    // ----------------------------------------------------------------------------

    fun <T1,T2> pair(
        fieldNumber: Int,
        fieldName: String,
        value: Pair<T1?, T2?>,
        firstWireFormat: WireFormat<T1>,
        secondWireFormat: WireFormat<T2>,
        firstNullable: Boolean,
        secondNullable: Boolean
    ): WireFormatEncoder

    fun <T1,T2> pairOrNull(
        fieldNumber: Int,
        fieldName: String,
        value: Pair<T1?, T2?>?,
        firstWireFormat: WireFormat<T1>,
        secondWireFormat: WireFormat<T2>,
        firstNullable: Boolean,
        secondNullable: Boolean
    ): WireFormatEncoder

    fun <T1,T2> rawPair(
        value: Pair<T1?, T2?>,
        firstWireFormat: WireFormat<T1>,
        secondWireFormat: WireFormat<T2>,
        firstNullable: Boolean,
        secondNullable: Boolean
    ): WireFormatEncoder

    // ----------------------------------------------------------------------------
    // Utilities for classes that implement `WireFormat`
    // ----------------------------------------------------------------------------

    fun <T> items(value: Collection<T?>, itemWireFormat: WireFormat<T>, nullable : Boolean): WireFormatEncoder


}