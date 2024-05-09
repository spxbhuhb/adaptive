/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.builtin.*
import hu.simplexion.adaptive.wireformat.signature.WireFormatTypeArgument
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalUnsignedTypes::class)
abstract class AbstractKotlinTest<ST>(
    wireFormatProvider: WireFormatProvider
) : AbstractWireFormatTest<ST>(wireFormatProvider) {

    @Test
    fun testUnit() {
        assertEquals(unitVal, e { unit(1, fieldName, unitVal) } d { unit(1, fieldName) })
        assertEquals(null, e { unitOrNull(1, fieldName, null) } d { unitOrNull(1, fieldName) })
        assertEquals(unitVal, e { unitOrNull(1, fieldName, unitVal) } d { unitOrNull(1, fieldName) })
        assertEquals(unitVal, r { rawUnit(unitVal) } d { rawUnit(it) })
        instance(unitVal, UnitWireFormat)
    }

    @Test
    fun testBoolean() {
        assertEquals(booleanVal, e { boolean(1, fieldName, booleanVal) } d { boolean(1, fieldName) })
        assertEquals(null, e { booleanOrNull(1, fieldName, null) } d { booleanOrNull(1, fieldName) })
        assertEquals(booleanVal, e { booleanOrNull(1, fieldName, booleanVal) } d { booleanOrNull(1, fieldName) })
        assertEquals(booleanVal, r { rawBoolean(booleanVal) } d { rawBoolean(it) })
        instance(booleanVal, BooleanWireFormat)
    }

    @Test
    fun testInt() {
        assertEquals(intVal, e { int(1, fieldName, intVal) } d { int(1, fieldName) })
        assertEquals(null, e { intOrNull(1, fieldName, null) } d { intOrNull(1, fieldName) })
        assertEquals(intVal, e { intOrNull(1, fieldName, intVal) } d { intOrNull(1, fieldName) })
        assertEquals(intVal, r { rawInt(intVal) } d { rawInt(it) })
        instance(intVal, IntWireFormat)
    }

    @Test
    fun testShort() {
        assertEquals(shortVal, e { short(1, fieldName, shortVal) } d { short(1, fieldName) })
        assertEquals(null, e { shortOrNull(1, fieldName, null) } d { shortOrNull(1, fieldName) })
        assertEquals(shortVal, e { shortOrNull(1, fieldName, shortVal) } d { shortOrNull(1, fieldName) })
        assertEquals(shortVal, r { rawShort(shortVal) } d { rawShort(it) })
        instance(shortVal, ShortWireFormat)
    }

    @Test
    fun testByte() {
        assertEquals(byteVal, e { byte(1, fieldName, byteVal) } d { byte(1, fieldName) })
        assertEquals(null, e { byteOrNull(1, fieldName, null) } d { byteOrNull(1, fieldName) })
        assertEquals(byteVal, e { byteOrNull(1, fieldName, byteVal) } d { byteOrNull(1, fieldName) })
        assertEquals(byteVal, r { rawByte(byteVal) } d { rawByte(it) })
        instance(byteVal, ByteWireFormat)
    }

    @Test
    fun testLong() {
        assertEquals(longVal, e { long(1, fieldName, longVal) } d { long(1, fieldName) })
        assertEquals(null, e { longOrNull(1, fieldName, null) } d { longOrNull(1, fieldName) })
        assertEquals(longVal, e { longOrNull(1, fieldName, longVal) } d { longOrNull(1, fieldName) })
        assertEquals(longVal, r { rawLong(longVal) } d { rawLong(it) })
        instance(longVal, LongWireFormat)
    }

    @Test
    fun testFloat() {
        // JavaScript's implementation of float does not convert the bits properly, the seconds assert
        // of these actually fail with AssertionError: Expected <12.34>, actual <12.34000015258789>.
        //
        //         assertEquals(12.34f.toBits(), 0x414570a4)
        //         assertEquals(12.34f, Float.fromBits(0x414570a4))
        //

        val epsilon = 0.000001f
        assertTrue(abs(floatVal - (e { float(1, fieldName, floatVal) } d { float(1, fieldName) })) < epsilon)
        assertEquals(null, e { floatOrNull(1, fieldName, null) } d { floatOrNull(1, fieldName) })
        assertTrue(abs(floatVal - (e { floatOrNull(1, fieldName, floatVal) } d { floatOrNull(1, fieldName) })!!) < epsilon)
        assertTrue(abs(floatVal - (r { rawFloat(floatVal) } d { rawFloat(it) })) < epsilon)
    }

    @Test
    fun testDouble() {
        assertEquals(doubleVal, e { double(1, fieldName, doubleVal) } d { double(1, fieldName) })
        assertEquals(null, e { doubleOrNull(1, fieldName, null) } d { doubleOrNull(1, fieldName) })
        assertEquals(doubleVal, e { doubleOrNull(1, fieldName, doubleVal) } d { doubleOrNull(1, fieldName) })
        assertEquals(doubleVal, r { rawDouble(doubleVal) } d { rawDouble(it) })
        instance(doubleVal, DoubleWireFormat)
    }

    @Test
    fun testChar() {
        assertEquals(charVal, e { char(1, fieldName, charVal) } d { char(1, fieldName) })
        assertEquals(null, e { charOrNull(1, fieldName, null) } d { charOrNull(1, fieldName) })
        assertEquals(charVal, e { charOrNull(1, fieldName, charVal) } d { charOrNull(1, fieldName) })
        assertEquals(charVal, r { rawChar(charVal) } d { rawChar(it) })
        instance(charVal, CharWireFormat)
    }

    @Test
    fun testBooleanArray() {
        assertContentEquals(booleanArrayVal, e { booleanArray(1, fieldName, booleanArrayVal) } d { booleanArray(1, fieldName) })
        assertEquals(null, e { booleanArrayOrNull(1, fieldName, null) } d { booleanArrayOrNull(1, fieldName) })
        assertContentEquals(booleanArrayVal, e { booleanArrayOrNull(1, fieldName, booleanArrayVal) } d { booleanArrayOrNull(1, fieldName) })
        assertContentEquals(booleanArrayVal, r { rawBooleanArray(booleanArrayVal) } d { rawBooleanArray(it) })
        assertContentEquals(booleanArrayVal, e { instance(1, fieldName, booleanArrayVal, BooleanArrayWireFormat) } d { instance(1, fieldName, BooleanArrayWireFormat) })
    }

    @Test
    fun testIntArray() {
        assertContentEquals(intArrayVal, e { intArray(1, fieldName, intArrayVal) } d { intArray(1, fieldName) })
        assertEquals(null, e { intArrayOrNull(1, fieldName, null) } d { intArrayOrNull(1, fieldName) })
        assertContentEquals(intArrayVal, e { intArrayOrNull(1, fieldName, intArrayVal) } d { intArrayOrNull(1, fieldName) })
        assertContentEquals(intArrayVal, r { rawIntArray(intArrayVal) } d { rawIntArray(it) })
        assertContentEquals(intArrayVal, e { instance(1, fieldName, intArrayVal, IntArrayWireFormat) } d { instance(1, fieldName, IntArrayWireFormat) })
    }

    @Test
    fun testShortArray() {
        assertContentEquals(shortArrayVal, e { shortArray(1, fieldName, shortArrayVal) } d { shortArray(1, fieldName) })
        assertEquals(null, e { shortArrayOrNull(1, fieldName, null) } d { shortArrayOrNull(1, fieldName) })
        assertContentEquals(shortArrayVal, e { shortArrayOrNull(1, fieldName, shortArrayVal) } d { shortArrayOrNull(1, fieldName) })
        assertContentEquals(shortArrayVal, r { rawShortArray(shortArrayVal) } d { rawShortArray(it) })
        assertContentEquals(shortArrayVal, e { instance(1, fieldName, shortArrayVal, ShortArrayWireFormat) } d { instance(1, fieldName, ShortArrayWireFormat) })
    }

    @Test
    fun testByteArray() {
        assertContentEquals(byteArrayVal, e { byteArray(1, fieldName, byteArrayVal) } d { byteArray(1, fieldName) })
        assertEquals(null, e { byteArrayOrNull(1, fieldName, null) } d { byteArrayOrNull(1, fieldName) })
        assertContentEquals(byteArrayVal, e { byteArrayOrNull(1, fieldName, byteArrayVal) } d { byteArrayOrNull(1, fieldName) })
        assertContentEquals(byteArrayVal, r { rawByteArray(byteArrayVal) } d { rawByteArray(it) })
        assertContentEquals(byteArrayVal, e { instance(1, fieldName, byteArrayVal, ByteArrayWireFormat) } d { instance(1, fieldName, ByteArrayWireFormat) })
    }

    @Test
    fun testLongArray() {
        assertContentEquals(longArrayVal, e { longArray(1, fieldName, longArrayVal) } d { longArray(1, fieldName) })
        assertEquals(null, e { longArrayOrNull(1, fieldName, null) } d { longArrayOrNull(1, fieldName) })
        assertContentEquals(longArrayVal, e { longArrayOrNull(1, fieldName, longArrayVal) } d { longArrayOrNull(1, fieldName) })
        assertContentEquals(longArrayVal, r { rawLongArray(longArrayVal) } d { rawLongArray(it) })
        assertContentEquals(longArrayVal, e { instance(1, fieldName, longArrayVal, LongArrayWireFormat) } d { instance(1, fieldName, LongArrayWireFormat) })
    }

    @Test
    fun testFloatArray() {
        assertContentEquals(floatArrayVal, e { floatArray(1, fieldName, floatArrayVal) } d { floatArray(1, fieldName) })
        assertEquals(null, e { floatArrayOrNull(1, fieldName, null) } d { floatArrayOrNull(1, fieldName) })
        assertContentEquals(floatArrayVal, e { floatArrayOrNull(1, fieldName, floatArrayVal) } d { floatArrayOrNull(1, fieldName) })
        assertContentEquals(floatArrayVal, r { rawFloatArray(floatArrayVal) } d { rawFloatArray(it) })
        assertContentEquals(floatArrayVal, e { instance(1, fieldName, floatArrayVal, FloatArrayWireFormat) } d { instance(1, fieldName, FloatArrayWireFormat) })
    }

    @Test
    fun testDoubleArray() {
        assertContentEquals(doubleArrayVal, e { doubleArray(1, fieldName, doubleArrayVal) } d { doubleArray(1, fieldName) })
        assertEquals(null, e { doubleArrayOrNull(1, fieldName, null) } d { doubleArrayOrNull(1, fieldName) })
        assertContentEquals(doubleArrayVal, e { doubleArrayOrNull(1, fieldName, doubleArrayVal) } d { doubleArrayOrNull(1, fieldName) })
        assertContentEquals(doubleArrayVal, r { rawDoubleArray(doubleArrayVal) } d { rawDoubleArray(it) })
        assertContentEquals(doubleArrayVal, e { instance(1, fieldName, doubleArrayVal, DoubleArrayWireFormat) } d { instance(1, fieldName, DoubleArrayWireFormat) })
    }

    @Test
    fun testCharArray() {
        assertContentEquals(charArrayVal, e { charArray(1, fieldName, charArrayVal) } d { charArray(1, fieldName) })
        assertEquals(null, e { charArrayOrNull(1, fieldName, null) } d { charArrayOrNull(1, fieldName) })
        assertContentEquals(charArrayVal, e { charArrayOrNull(1, fieldName, charArrayVal) } d { charArrayOrNull(1, fieldName) })
        assertContentEquals(charArrayVal, r { rawCharArray(charArrayVal) } d { rawCharArray(it) })
        assertContentEquals(charArrayVal, e { instance(1, fieldName, charArrayVal, CharArrayWireFormat) } d { instance(1, fieldName, CharArrayWireFormat) })
    }

    @Test
    fun testString() {
        assertEquals(stringVal, e { string(1, fieldName, stringVal) } d { string(1, fieldName) })
        assertEquals(null, e { stringOrNull(1, fieldName, null) } d { stringOrNull(1, fieldName) })
        assertEquals(stringVal, e { stringOrNull(1, fieldName, stringVal) } d { stringOrNull(1, fieldName) })
        assertEquals(stringVal, r { rawString(stringVal) } d { rawString(it) })
        instance(stringVal, StringWireFormat)
    }

    @Test
    fun testEnum() {
        assertEquals(enumVal, e { enum(1, fieldName, enumVal, E.entries) } d { enum(1, fieldName, E.entries) })
        assertEquals(null, e { enumOrNull(1, fieldName, null, E.entries) } d { enumOrNull(1, fieldName, E.entries) })
        assertEquals(enumVal, e { enumOrNull(1, fieldName, enumVal, E.entries) } d { enumOrNull(1, fieldName, E.entries) })
        assertEquals(enumVal, r { rawEnum(enumVal, E.entries) } d { rawEnum(it, E.entries) })
        instance(enumVal, EnumWireFormat(E.entries))
    }

    @Test
    fun testUuid() {
        assertEquals(uuidVal, e { uuid(1, fieldName, uuidVal) } d { uuid(1, fieldName) })
        assertEquals(null, e { uuidOrNull(1, fieldName, null) } d { uuidOrNull<Any>(1, fieldName) })
        assertEquals(uuidVal, e { uuidOrNull(1, fieldName, uuidVal) } d { uuidOrNull(1, fieldName) })
        assertEquals(uuidVal, r { rawUuid(uuidVal) } d { rawUuid(it) })
        instance(uuidVal, UuidWireFormat)
    }

    @Test
    fun testUInt() {
        assertEquals(uIntVal, e { uInt(1, fieldName, uIntVal) } d { uInt(1, fieldName) })
        assertEquals(null, e { uIntOrNull(1, fieldName, null) } d { uIntOrNull(1, fieldName) })
        assertEquals(uIntVal, e { uIntOrNull(1, fieldName, uIntVal) } d { uIntOrNull(1, fieldName) })
        assertEquals(uIntVal, r { rawUInt(uIntVal) } d { rawUInt(it) })
        instance(uIntVal, UIntWireFormat)
    }

    @Test
    fun testUByte() {
        assertEquals(uByteVal, e { uByte(1, fieldName, uByteVal) } d { uByte(1, fieldName) })
        assertEquals(null, e { uByteOrNull(1, fieldName, null) } d { uByteOrNull(1, fieldName) })
        assertEquals(uByteVal, e { uByteOrNull(1, fieldName, uByteVal) } d { uByteOrNull(1, fieldName) })
        assertEquals(uByteVal, r { rawUByte(uByteVal) } d { rawUByte(it) })
        instance(uByteVal, UByteWireFormat)
    }

    @Test
    fun testUShort() {
        assertEquals(uShortVal, e { uShort(1, fieldName, uShortVal) } d { uShort(1, fieldName) })
        assertEquals(null, e { uShortOrNull(1, fieldName, null) } d { uShortOrNull(1, fieldName) })
        assertEquals(uShortVal, e { uShortOrNull(1, fieldName, uShortVal) } d { uShortOrNull(1, fieldName) })
        assertEquals(uShortVal, r { rawUShort(uShortVal) } d { rawUShort(it) })
        instance(uShortVal, UShortWireFormat)
    }

    @Test
    fun testULong() {
        assertEquals(uLongVal, e { uLong(1, fieldName, uLongVal) } d { uLong(1, fieldName) })
        assertEquals(null, e { uLongOrNull(1, fieldName, null) } d { uLongOrNull(1, fieldName) })
        assertEquals(uLongVal, e { uLongOrNull(1, fieldName, uLongVal) } d { uLongOrNull(1, fieldName) })
        assertEquals(uLongVal, r { rawULong(uLongVal) } d { rawULong(it) })
        instance(uLongVal, ULongWireFormat)
    }

    @Test
    fun testUIntArray() {
        assertContentEquals(uIntArrayVal, e { uIntArray(1, fieldName, uIntArrayVal) } d { uIntArray(1, fieldName) })
        assertEquals(null, e { uIntArrayOrNull(1, fieldName, null) } d { uIntArrayOrNull(1, fieldName) })
        assertContentEquals(uIntArrayVal, e { uIntArrayOrNull(1, fieldName, uIntArrayVal) } d { uIntArrayOrNull(1, fieldName) })
        assertContentEquals(uIntArrayVal, r { rawUIntArray(uIntArrayVal) } d { rawUIntArray(it) })
        assertContentEquals(uIntArrayVal, e { instance(1, fieldName, uIntArrayVal, UIntArrayWireFormat) } d { instance(1, fieldName, UIntArrayWireFormat) })
    }
    
    @Test
    fun testUByteArray() {
        assertContentEquals(uByteArrayVal, e { uByteArray(1, fieldName, uByteArrayVal) } d { uByteArray(1, fieldName) })
        assertEquals(null, e { uByteArrayOrNull(1, fieldName, null) } d { uByteArrayOrNull(1, fieldName) })
        assertContentEquals(uByteArrayVal, e { uByteArrayOrNull(1, fieldName, uByteArrayVal) } d { uByteArrayOrNull(1, fieldName) })
        assertContentEquals(uByteArrayVal, r { rawUByteArray(uByteArrayVal) } d { rawUByteArray(it) })
        assertContentEquals(uByteArrayVal, e { instance(1, fieldName, uByteArrayVal, UByteArrayWireFormat) } d { instance(1, fieldName, UByteArrayWireFormat) })
    }
    
    @Test
    fun testUShortArray() {
        assertContentEquals(uShortArrayVal, e { uShortArray(1, fieldName, uShortArrayVal) } d { uShortArray(1, fieldName) })
        assertEquals(null, e { uShortArrayOrNull(1, fieldName, null) } d { uShortArrayOrNull(1, fieldName) })
        assertContentEquals(uShortArrayVal, e { uShortArrayOrNull(1, fieldName, uShortArrayVal) } d { uShortArrayOrNull(1, fieldName) })
        assertContentEquals(uShortArrayVal, r { rawUShortArray(uShortArrayVal) } d { rawUShortArray(it) })
        assertContentEquals(uShortArrayVal, e { instance(1, fieldName, uShortArrayVal, UShortArrayWireFormat) } d { instance(1, fieldName, UShortArrayWireFormat) })
    }
    
    @Test
    fun testULongArray() {
        assertContentEquals(uLongArrayVal, e { uLongArray(1, fieldName, uLongArrayVal) } d { uLongArray(1, fieldName) })
        assertEquals(null, e { uLongArrayOrNull(1, fieldName, null) } d { uLongArrayOrNull(1, fieldName) })
        assertContentEquals(uLongArrayVal, e { uLongArrayOrNull(1, fieldName, uLongArrayVal) } d { uLongArrayOrNull(1, fieldName) })
        assertContentEquals(uLongArrayVal, r { rawULongArray(uLongArrayVal) } d { rawULongArray(it) })
        assertContentEquals(uLongArrayVal, e { instance(1, fieldName, uLongArrayVal, ULongArrayWireFormat) } d { instance(1, fieldName, ULongArrayWireFormat) })
    }

    @Test
    fun testInstance() {
        assertEquals(instanceVal, e { instance(1, fieldName, instanceVal, A) } d { instance(1, fieldName, A) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, A) } d { instanceOrNull(1, fieldName, A) })
        assertEquals(instanceVal, e { instanceOrNull(1, fieldName, instanceVal, A) } d { instanceOrNull(1, fieldName, A) })
        assertEquals(instanceVal, r { rawInstance(instanceVal, A) } d { rawInstance(it, A) })
    }

    @Test
    fun testPair() {
        val ita = WireFormatTypeArgument(IntWireFormat, false)
        val sta = WireFormatTypeArgument(StringWireFormat, false)
        assertEquals(pairVal, e { pair(1, fieldName, pairVal, ita, sta) } d { pair(1, fieldName, ita, sta) })
        assertEquals(null, e { pairOrNull(1, fieldName, null, ita, sta) } d { pairOrNull(1, fieldName, ita, sta) })
        assertEquals(pairVal, e { pairOrNull(1, fieldName, pairVal, ita, sta) } d { pairOrNull(1, fieldName, ita, sta) })
        assertEquals(pairVal, r { rawPair(pairVal, ita, sta) } d { rawPair(it, ita, sta) })
        instance(pairVal, PairWireFormat(ita, sta))
    }

    @Test
    fun testIntList() {
        list(intListVal, IntWireFormat)
    }

    @Test
    fun testInstanceList() {
        list(instanceListVal, B)
    }

    fun <T> instance(value: T, wireFormat: WireFormat<T>) {
        assertEquals(value, e { instance(1, fieldName, value, wireFormat) } d { instance(1, fieldName, wireFormat) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, wireFormat) } d { instanceOrNull(1, fieldName, wireFormat) })
        assertEquals(value, e { instanceOrNull(1, fieldName, value, wireFormat) } d { instanceOrNull(1, fieldName, wireFormat) })
        assertEquals(value, r { rawInstance(value, wireFormat) } d { rawInstance(it, wireFormat) })
    }

    fun <T> list(value: List<T>, itemWireFormat: WireFormat<T>) {
        instance(value, ListWireFormat(WireFormatTypeArgument(itemWireFormat, false)))
    }

}