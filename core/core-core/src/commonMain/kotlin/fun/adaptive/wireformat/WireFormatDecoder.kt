/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.signature.WireFormatTypeArgument
import kotlin.enums.EnumEntries

@OptIn(ExperimentalUnsignedTypes::class)
interface WireFormatDecoder<ST> {

    // -----------------------------------------------------------------------------------------
    // Special Types
    // -----------------------------------------------------------------------------------------

    fun any(fieldNumber: Int, fieldName: String): Any

    fun anyOrNull(fieldNumber: Int, fieldName: String): Any?

    fun rawAny(source: ST): Any

    // ---

    fun unit(fieldNumber: Int, fieldName: String): Unit

    fun unitOrNull(fieldNumber: Int, fieldName: String): Unit?

    fun rawUnit(source: ST)

    // -----------------------------------------------------------------------------------------
    // Primitives
    // -----------------------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, fieldName: String): Boolean

    fun booleanOrNull(fieldNumber: Int, fieldName: String): Boolean?

    fun rawBoolean(source: ST): Boolean

    // ----

    fun int(fieldNumber: Int, fieldName: String): Int

    fun intOrNull(fieldNumber: Int, fieldName: String): Int?

    fun rawInt(source: ST): Int

    // ----

    fun short(fieldNumber: Int, fieldName: String): Short

    fun shortOrNull(fieldNumber: Int, fieldName: String): Short?

    fun rawShort(source: ST): Short

    // ----

    fun byte(fieldNumber: Int, fieldName: String): Byte

    fun byteOrNull(fieldNumber: Int, fieldName: String): Byte?

    fun rawByte(source: ST): Byte

    // ----

    fun long(fieldNumber: Int, fieldName: String): Long

    fun longOrNull(fieldNumber: Int, fieldName: String): Long?

    fun rawLong(source: ST): Long

    // ----

    fun float(fieldNumber: Int, fieldName: String): Float

    fun floatOrNull(fieldNumber: Int, fieldName: String): Float?

    fun rawFloat(source: ST): Float

    // ----

    fun double(fieldNumber: Int, fieldName: String): Double

    fun doubleOrNull(fieldNumber: Int, fieldName: String): Double?

    fun rawDouble(source: ST): Double

    // ----

    fun char(fieldNumber: Int, fieldName: String): Char

    fun charOrNull(fieldNumber: Int, fieldName: String): Char?

    fun rawChar(source: ST): Char

    // -----------------------------------------------------------------------------------------
    // Arrays
    // -----------------------------------------------------------------------------------------

    fun booleanArray(fieldNumber: Int, fieldName: String): BooleanArray

    fun booleanArrayOrNull(fieldNumber: Int, fieldName: String): BooleanArray?

    fun rawBooleanArray(source: ST): BooleanArray

    // ----

    fun intArray(fieldNumber: Int, fieldName: String): IntArray

    fun intArrayOrNull(fieldNumber: Int, fieldName: String): IntArray?

    fun rawIntArray(source: ST): IntArray

    // ----

    fun shortArray(fieldNumber: Int, fieldName: String): ShortArray

    fun shortArrayOrNull(fieldNumber: Int, fieldName: String): ShortArray?

    fun rawShortArray(source: ST): ShortArray

    // ----

    fun byteArray(fieldNumber: Int, fieldName: String): ByteArray

    fun byteArrayOrNull(fieldNumber: Int, fieldName: String): ByteArray?

    fun rawByteArray(source: ST): ByteArray

    // ----

    fun longArray(fieldNumber: Int, fieldName: String): LongArray

    fun longArrayOrNull(fieldNumber: Int, fieldName: String): LongArray?

    fun rawLongArray(source: ST): LongArray

    // ----

    fun floatArray(fieldNumber: Int, fieldName: String): FloatArray

    fun floatArrayOrNull(fieldNumber: Int, fieldName: String): FloatArray?

    fun rawFloatArray(source: ST): FloatArray

    // ----

    fun doubleArray(fieldNumber: Int, fieldName: String): DoubleArray

    fun doubleArrayOrNull(fieldNumber: Int, fieldName: String): DoubleArray?

    fun rawDoubleArray(source: ST): DoubleArray

    // ----

    fun charArray(fieldNumber: Int, fieldName: String): CharArray

    fun charArrayOrNull(fieldNumber: Int, fieldName: String): CharArray?

    fun rawCharArray(source: ST): CharArray

    // -----------------------------------------------------------------------------------------
    // Built-in Types
    // -----------------------------------------------------------------------------------------

    fun string(fieldNumber: Int, fieldName: String): String

    fun stringOrNull(fieldNumber: Int, fieldName: String): String?

    fun rawString(source: ST): String

    // ----

    fun <E : Enum<E>> enum(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E

    fun <E : Enum<E>> enumOrNull(fieldNumber: Int, fieldName: String, entries: EnumEntries<E>): E?

    fun <E : Enum<E>> rawEnum(source: ST, entries: EnumEntries<E>): E

    // ----

    fun <T> uuid(fieldNumber: Int, fieldName: String): UUID<T>

    fun <T> uuidOrNull(fieldNumber: Int, fieldName: String): UUID<T>?

    fun <T> rawUuid(source: ST): UUID<T>

    // -----------------------------------------------------------------------------------------
    // Unsigned Primitives
    // -----------------------------------------------------------------------------------------

    fun uInt(fieldNumber: Int, fieldName: String): UInt

    fun uIntOrNull(fieldNumber: Int, fieldName: String): UInt?

    fun rawUInt(source: ST): UInt

    // ----

    fun uShort(fieldNumber: Int, fieldName: String): UShort

    fun uShortOrNull(fieldNumber: Int, fieldName: String): UShort?

    fun rawUShort(source: ST): UShort

    // ----

    fun uByte(fieldNumber: Int, fieldName: String): UByte

    fun uByteOrNull(fieldNumber: Int, fieldName: String): UByte?

    fun rawUByte(source: ST): UByte

    // ----

    fun uLong(fieldNumber: Int, fieldName: String): ULong

    fun uLongOrNull(fieldNumber: Int, fieldName: String): ULong?

    fun rawULong(source: ST): ULong

    // -----------------------------------------------------------------------------------------
    // Unsigned Arrays
    // -----------------------------------------------------------------------------------------

    fun uIntArray(fieldNumber: Int, fieldName: String): UIntArray

    fun uIntArrayOrNull(fieldNumber: Int, fieldName: String): UIntArray?

    fun rawUIntArray(source: ST): UIntArray

    // ----

    fun uShortArray(fieldNumber: Int, fieldName: String): UShortArray

    fun uShortArrayOrNull(fieldNumber: Int, fieldName: String): UShortArray?

    fun rawUShortArray(source: ST): UShortArray

    // ----

    fun uByteArray(fieldNumber: Int, fieldName: String): UByteArray

    fun uByteArrayOrNull(fieldNumber: Int, fieldName: String): UByteArray?

    fun rawUByteArray(source: ST): UByteArray

    // ----

    fun uLongArray(fieldNumber: Int, fieldName: String): ULongArray

    fun uLongArrayOrNull(fieldNumber: Int, fieldName: String): ULongArray?

    fun rawULongArray(source: ST): ULongArray

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T

    fun <T> instanceOrNull(fieldNumber: Int, fieldName: String, wireFormat: WireFormat<T>): T?

    fun <T> rawInstance(source: ST, wireFormat: WireFormat<T>): T

    fun <T> asInstance(wireFormat: WireFormat<T>): T

    fun <T> asInstanceOrNull(wireFormat: WireFormat<T>): T?

    // -----------------------------------------------------------------------------------------
    // Polymorphic Instance
    // -----------------------------------------------------------------------------------------

    fun <T> polymorphic(fieldNumber: Int, fieldName: String): T

    fun <T> polymorphicOrNull(fieldNumber: Int, fieldName: String): T?

    fun <T> rawPolymorphic(source: ST): T

    fun <T> asPolymorphic(): T

    fun <T> asPolymorphicOrNull(): T?
    
    // -----------------------------------------------------------------------------------------
    // Pair
    // -----------------------------------------------------------------------------------------

    fun <T1,T2> pair(
        fieldNumber: Int,
        fieldName: String,
        typeArgument1: WireFormatTypeArgument<T1>,
        typeArgument2: WireFormatTypeArgument<T2>
    ): Pair<T1?,T2?>

    fun <T1,T2> pairOrNull(
        fieldNumber: Int,
        fieldName: String,
        typeArgument1: WireFormatTypeArgument<T1>,
        typeArgument2: WireFormatTypeArgument<T2>
    ): Pair<T1?,T2?>?

    fun <T1,T2> rawPair(
        source: ST,
        typeArgument1: WireFormatTypeArgument<T1>,
        typeArgument2: WireFormatTypeArgument<T2>
    ): Pair<T1?,T2?>

    // -----------------------------------------------------------------------------------------
    // Utilities for classes that implement `WireFormat`
    // -----------------------------------------------------------------------------------------

    fun <T> items(source: ST, typeArgument: WireFormatTypeArgument<T>): MutableList<T?>

    // -----------------------------------------------------------------------------------------
    // Debug / trace utilities
    // -----------------------------------------------------------------------------------------

    fun dump(): String

}