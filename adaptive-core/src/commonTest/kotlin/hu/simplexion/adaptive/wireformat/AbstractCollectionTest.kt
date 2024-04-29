/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.builtin.IntWireFormat
import hu.simplexion.adaptive.wireformat.builtin.ListWireFormat
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class AbstractCollectionTest<ST>(
    wireFormatProvider: WireFormatProvider
) : AbstractWireFormatTest<ST>(wireFormatProvider) {

    @Test
    fun testIntList() {
        list(intListVal, IntWireFormat)
    }

    @Test
    fun testInstanceList() {
        list(instanceListVal, B)
    }

    fun <T> list(value: List<T>, itemWireFormat: WireFormat<T>) {
        val listWireFormat = ListWireFormat(itemWireFormat)

        assertEquals(value, e { instance(1, fieldName, value, listWireFormat) } d { instance(1, fieldName, listWireFormat) })
        assertEquals(null, e { instanceOrNull(1, fieldName, null, listWireFormat) } d { instanceOrNull(1, fieldName, listWireFormat) })
        assertEquals(value, e { instanceOrNull(1, fieldName, value, listWireFormat) } d { instanceOrNull(1, fieldName, listWireFormat) })
        assertEquals(value, r { rawInstance(value, listWireFormat) } d { rawInstance(it, listWireFormat) })
    }

}