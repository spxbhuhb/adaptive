/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json

import `fun`.adaptive.wireformat.json.elements.JsonObject
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

    @Test
    fun stringEscape() {
        test("""{ "s" : "\"" }""", "\"")
        test("""{ "s" : "\\" }""", "\\")
        test("""{ "s" : "\n" }""", "\n")
        test("""{ "s" : "\r" }""", "\r")
        test("""{ "s" : "\t" }""", "\t")
        test("""{ "s" : "\u0000" }""", "\u0000")
        test("""{ "s" : "\u0008" }""", "\u0008")
        test("""{ "s" : "\u0020" }""", " ")
        test("""{ "s" : "árvíztűrő tükörfúrógép" }""", "árvíztűrő tükörfúrógép")
    }

    fun test(source : String, expected: String) {
        val root = JsonBufferReader(source.encodeToByteArray()).read()
        assertTrue(root is JsonObject)
        assertEquals(1, root.entries.size)
        assertEquals(expected, root.entries["s"]!!.asString)
    }
}