/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpersTest {

    @Test
    fun testEncodeDecodeProto() {
        val expected = A(true, 12, "abc")

        val provider = ProtoWireFormatProvider()
        val p = provider.encode(expected, A)
        val actual = provider.decode(p, A)
        assertEquals(expected, actual)
    }

    @Test
    fun testEncodeDecodeJson() {
        val expected = A(true, 12, "abc")

        val provider = JsonWireFormatProvider()
        val p = provider.encode(expected, A)
        val actual = provider.decode(p, A)
        assertEquals(expected, actual)
    }

}