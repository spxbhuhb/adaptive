/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.protobuf

import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalStdlibApi::class)
class ProtoReaderTest {

    val String.ns
        get() = replace(" ", "")

    val String.value
        get() = ProtoWireFormatDecoder(this.ns.hexToByteArray())[2] !!.value

    val String.int32: Int
        get() = value.int32()

    val String.int64: Long
        get() = value.int64()

    val String.sint32: Int
        get() = value.sint32()

    val String.sint64: Long
        get() = value.sint64()

    @Test
    fun int32() {
        // unsigned values, '10' is the tag (2) and the VARINT type
        assertEquals(1, "10 01".int32)
        assertEquals(150, "10 96 01".int32)
        assertEquals(18789, "10 e5 92 01".int32)
        assertEquals(Int.MAX_VALUE, "10 ff ff ff ff 07".int32)
        assertEquals(Int.MIN_VALUE, "10 80 80 80 80 f8 ff ff ff ff 01".int32)
        assertEquals(- 1, "10 ff ff ff ff ff ff ff ff ff 01".int32)
        assertEquals(- 2, "10 fe ff ff ff ff ff ff ff ff 01".int32)
    }

    @Test
    fun varint() {
        // unsigned values, '10' is the tag (2) and the VARINT type
        assertEquals(1L, "10 01".int64)
        assertEquals(150L, "10 96 01".int64)
        assertEquals(18789L, "10 e5 92 01".int64)
        assertEquals(Long.MAX_VALUE, "10 ff ff ff ff ff ff ff ff 7f".int64)
        assertEquals(- 1, "10 ff ff ff ff ff ff ff ff ff 01".int64)
        assertEquals(- 2, "10 fe ff ff ff ff ff ff ff ff 01".int64)

        // signed values, ZigZag encoding, '10' is the tag (2) and the VARINT type
        assertEquals(1, "10 02".sint32)
        assertEquals(2, "10 04".sint32)
        assertEquals(- 1, "10 01".sint32)
        assertEquals(- 2, "10 03".sint32)
        assertEquals(150, "10 ac 02".sint32)
        assertEquals(- 150, "10 ab 02".sint32)
        assertEquals(Int.MIN_VALUE, "10 ff ff ff ff 0f".sint32)
        assertEquals(Int.MIN_VALUE + 1, "10 fd ff ff ff 0f".sint32)
        assertEquals(Int.MAX_VALUE - 1, "10 fc ff ff ff 0f".sint32)
        assertEquals(Int.MAX_VALUE, "10 fe ff ff ff 0f".sint32)

        // signed values, ZigZag encoding, '10' is the tag (2) and the VARINT type
        assertEquals(1L, "10 02".sint64)
        assertEquals(2L, "10 04".sint64)
        assertEquals(- 1L, "10 01".sint64)
        assertEquals(- 2L, "10 03".sint64)
        assertEquals(150L, "10 ac 02".sint64)
        assertEquals(- 150L, "10 ab 02".sint64)
        assertEquals(Long.MIN_VALUE, "10 ff ff ff ff ff ff ff ff ff 01".sint64)
        assertEquals(Long.MIN_VALUE + 1, "10 fd ff ff ff ff ff ff ff ff 01".sint64)
        assertEquals(Long.MAX_VALUE - 1, "10 fc ff ff ff ff ff ff ff ff 01".sint64)
        assertEquals(Long.MAX_VALUE, "10 fe ff ff ff ff ff ff ff ff 01".sint64)
    }
}