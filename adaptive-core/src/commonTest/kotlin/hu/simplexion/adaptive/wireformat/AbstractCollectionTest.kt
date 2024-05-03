/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.builtin.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

abstract class AbstractCollectionTest<ST>(
    wireFormatProvider: WireFormatProvider
) : AbstractWireFormatTest<ST>(wireFormatProvider) {

    fun <T> array(value: Array<T?>, itemWireFormat: WireFormat<T>) {
        val arrayWireFormat = ArrayWireFormat(itemWireFormat)

        assertContentEquals(value, e { instance(1, fieldName, value, arrayWireFormat) } d { instance(1, fieldName, arrayWireFormat) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, arrayWireFormat) } d { instanceOrNull(1, fieldName, arrayWireFormat) })
        assertContentEquals(value, e { instanceOrNull(1, fieldName, value, arrayWireFormat) } d { instanceOrNull(1, fieldName, arrayWireFormat) })
        assertContentEquals(value, r { rawInstance(value, arrayWireFormat) } d { rawInstance(it, arrayWireFormat) })
    }

    @Test
    fun testIntArray() {
        array(intListVal.toTypedArray(), IntWireFormat)
    }

    @Test
    fun testInstanceArray() {
        array(instanceListVal.toTypedArray(), B)
    }

    fun <T> list(value: List<T?>, itemWireFormat: WireFormat<T>, nullable : Boolean = false) {
        val listWireFormat = ListWireFormat(itemWireFormat, nullable)

        assertEquals(value, e { instance(1, fieldName, value, listWireFormat) } d { instance(1, fieldName, listWireFormat) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, listWireFormat) } d { instanceOrNull(1, fieldName, listWireFormat) })
        assertEquals(value, e { instanceOrNull(1, fieldName, value, listWireFormat) } d { instanceOrNull(1, fieldName, listWireFormat) })
        assertEquals(value, r { rawInstance(value, listWireFormat) } d { rawInstance(it, listWireFormat) })
    }

    @Test
    fun testIntList() {
        list(intListVal, IntWireFormat)
    }

    @Test
    fun testInstanceList() {
        list(instanceListVal, B)
    }

    @Test
    fun testInstanceListWithNull() {
        list(instanceListValWithNull, B, true)
        assertFailsWith(IllegalStateException::class) {
            list(instanceListValWithNull, B, false)
        }
    }

    fun <T> set(value: Set<T?>, itemWireFormat: WireFormat<T>, nullable : Boolean = false) {
        val setWireFormat = SetWireFormat(itemWireFormat, nullable)

        assertEquals(value, e { instance(1, fieldName, value, setWireFormat) } d { instance(1, fieldName, setWireFormat) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, setWireFormat) } d { instanceOrNull(1, fieldName, setWireFormat) })
        assertEquals(value, e { instanceOrNull(1, fieldName, value, setWireFormat) } d { instanceOrNull(1, fieldName, setWireFormat) })
        assertEquals(value, r { rawInstance(value, setWireFormat) } d { rawInstance(it, setWireFormat) })
    }

    @Test
    fun testIntSet() {
        set(intListVal.toSet(), IntWireFormat)
    }

    @Test
    fun testInstanceSet() {
        set(instanceListVal.toSet(), B)
    }

    @Test
    fun testInstanceSetWithNull() {
        set(instanceListValWithNull.toSet(), B, true)
        assertFailsWith(IllegalStateException::class) {
            set(instanceListValWithNull.toSet(), B, false)
        }
    }

    fun <K,V> map(value: Map<K?,V?>, keyWireFormat: WireFormat<K>, valueWireFormat: WireFormat<V>, keyNullable : Boolean = false, valueNullable : Boolean = false) {
        val mapWireFormat = MapWireFormat(keyWireFormat, valueWireFormat, keyNullable, valueNullable)

        assertEquals(value, e { instance(1, fieldName, value, mapWireFormat) } d { instance(1, fieldName, mapWireFormat) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, mapWireFormat) } d { instanceOrNull(1, fieldName, mapWireFormat) })
        assertEquals(value, e { instanceOrNull(1, fieldName, value, mapWireFormat) } d { instanceOrNull(1, fieldName, mapWireFormat) })
        assertEquals(value, r { rawInstance(value, mapWireFormat) } d { rawInstance(it, mapWireFormat) })
    }

    @Test
    fun testIntMap() {
        map(intListVal.toSet().associateBy { it.toString() }, StringWireFormat, IntWireFormat)
    }

    @Test
    fun testInstanceMap() {
        map(instanceListVal.associateBy { it }, B, B)
    }

    @Test
    fun testInstanceMapWithNull() {
        map(instanceListValWithNull.associateBy { it }, B, B, true, true)

        assertFailsWith(IllegalStateException::class) {
            map(instanceListValWithNull.associateBy { it }, B, B, false, true)
        }

        assertFailsWith(IllegalStateException::class) {
            map(instanceListValWithNull.associateBy { it }, B, B, true, false)
        }

        assertFailsWith(IllegalStateException::class) {
            map(instanceListValWithNull.associateBy { it }, B, B, false, false)
        }
    }

}