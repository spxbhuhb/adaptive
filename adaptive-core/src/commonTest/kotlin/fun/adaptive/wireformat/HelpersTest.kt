/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat

import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.api.Proto
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpersTest {

    @Test
    fun testEncodeDecodeProto() {
        val expected = A(true, 12, "abc")

        val provider = Proto
        val p = provider.encode(expected, A)
        val actual = provider.decode(p, A)
        assertEquals(expected, actual)
    }

    @Test
    fun testEncodeDecodeJson() {
        val expected = A(true, 12, "abc")

        val provider = Json
        val p = provider.encode(expected, A)
        val actual = provider.decode(p, A)
        assertEquals(expected, actual)
    }

}