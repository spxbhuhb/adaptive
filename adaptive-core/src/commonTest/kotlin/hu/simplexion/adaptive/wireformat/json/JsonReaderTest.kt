/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json

import hu.simplexion.adaptive.wireformat.json.elements.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonReaderTest {

    @Test
    fun basic() {
        val root = JsonBufferReader("""{"i" : 12}""".encodeToByteArray()).read()
        assertTrue(root is JsonObject)
        assertEquals(1, root.entries.size)
        assertEquals(12, root.entries["i"]!!.asInt)
    }
}