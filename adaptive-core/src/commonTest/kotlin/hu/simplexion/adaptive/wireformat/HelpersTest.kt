/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.decode
import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.encode
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpersTest {

    @Test
    fun testEncodeDecode() {
        val expected = A(true, 12, "abc")
        val p = encode(expected, A)
        val actual = decode(p, A)
        assertEquals(expected, actual)
    }

}