/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.protobuf

import kotlin.test.Test
import kotlin.test.assertEquals

class ProtoWriterTest {

    @OptIn(ExperimentalStdlibApi::class)
    fun proto(builder: ProtoBufferWriter.() -> Unit): String {
        val writer = ProtoBufferWriter()
        writer.builder()
        return writer.pack().toHexString(0, writer.size)
    }

    val String.ns
        get() = replace(" ", "")

    @Test
    fun int32() {
        // unsigned values
        assertEquals("10 01".ns, proto { int32(2, 1) })
        assertEquals("10 96 01".ns, proto { int32(2, 150) })
        assertEquals("10 e5 92 01".ns, proto { int32(2, 18789) })
        assertEquals("10 ff ff ff ff 07".ns, proto { int32(2, Int.MAX_VALUE) })
    }

    @Test
    fun sint32() {
        // signed values, ZigZag encoding
        assertEquals("10 02".ns, proto { sint32(2, 1) })
        assertEquals("10 04".ns, proto { sint32(2, 2) })
        assertEquals("10 01".ns, proto { sint32(2, - 1) })
        assertEquals("10 03".ns, proto { sint32(2, - 2) })
        assertEquals("10 ac 02".ns, proto { sint32(2, 150) })
        assertEquals("10 ab 02".ns, proto { sint32(2, - 150) })
        assertEquals("10 ff ff ff ff 0f".ns, proto { sint32(2, Int.MIN_VALUE) })
        assertEquals("10 fd ff ff ff 0f".ns, proto { sint32(2, Int.MIN_VALUE + 1) })
        assertEquals("10 fc ff ff ff 0f".ns, proto { sint32(2, Int.MAX_VALUE - 1) })
        assertEquals("10 fe ff ff ff 0f".ns, proto { sint32(2, Int.MAX_VALUE) })
    }

    @Test
    fun int64() {
        // unsigned values
        assertEquals("10 01".ns, proto { int64(2, 1L) })
        assertEquals("10 96 01".ns, proto { int64(2, 150L) })
        assertEquals("10 e5 92 01".ns, proto { int64(2, 18789L) })
        assertEquals("10 ff ff ff ff ff ff ff ff 7f".ns, proto { int64(2, Long.MAX_VALUE) })
    }

    @Test
    fun sint64() {
        // signed values, ZigZag encoding
        assertEquals("10 02".ns, proto { sint64(2, 1L) })
        assertEquals("10 04".ns, proto { sint64(2, 2L) })
        assertEquals("10 01".ns, proto { sint64(2, - 1L) })
        assertEquals("10 03".ns, proto { sint64(2, - 2L) })
        assertEquals("10 ac 02".ns, proto { sint64(2, 150L) })
        assertEquals("10 ab 02".ns, proto { sint64(2, - 150L) })
        assertEquals("10 ff ff ff ff ff ff ff ff ff 01".ns, proto { sint64(2, Long.MIN_VALUE) })
        assertEquals("10 fd ff ff ff ff ff ff ff ff 01".ns, proto { sint64(2, Long.MIN_VALUE + 1) })
        assertEquals("10 fc ff ff ff ff ff ff ff ff 01".ns, proto { sint64(2, Long.MAX_VALUE - 1) })
        assertEquals("10 fe ff ff ff ff ff ff ff ff 01".ns, proto { sint64(2, Long.MAX_VALUE) })
    }
}