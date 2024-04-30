/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.builtin.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

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
    fun testString() {
        assertEquals(stringVal, e { string(1, fieldName, stringVal) } d { string(1, fieldName) })
        assertEquals(null, e { stringOrNull(1, fieldName, null) } d { stringOrNull(1, fieldName) })
        assertEquals(stringVal, e { stringOrNull(1, fieldName, stringVal) } d { stringOrNull(1, fieldName) })
        assertEquals(stringVal, r { rawString(stringVal) } d { rawString(it) })
        instance(stringVal, StringWireFormat)
    }

    @Test
    fun testBooleanArray() {
        assertContentEquals(booleanArrayVal, e { booleanArray(1, fieldName, booleanArrayVal) } d { booleanArray(1, fieldName) })
        assertEquals(null, e { booleanArrayOrNull(1, fieldName, null) } d { booleanArrayOrNull(1, fieldName) })
        assertContentEquals(booleanArrayVal, e { booleanArrayOrNull(1, fieldName, booleanArrayVal) } d { booleanArrayOrNull(1, fieldName) })
        assertContentEquals(booleanArrayVal, r { rawBooleanArray(booleanArrayVal) } d { rawBooleanArray(it) })
    }

    @Test
    fun testInstance() {
        assertEquals(instanceVal, e { instance(1, fieldName, instanceVal, A) } d { instance(1, fieldName, A) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, A) } d { instanceOrNull(1, fieldName, A) })
        assertEquals(instanceVal, e { instanceOrNull(1, fieldName, instanceVal, A) } d { instanceOrNull(1, fieldName, A) })
        assertEquals(instanceVal, r { rawInstance(instanceVal, A) } d { rawInstance(it, A) })
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
        instance(value, ListWireFormat(itemWireFormat))
    }

}