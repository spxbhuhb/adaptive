/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.utility

import kotlin.test.Test
import kotlin.test.assertEquals

class UuidTest {

    @Test
    fun testMake() {
        val ju = java.util.UUID.randomUUID()

        val array = IntArray(4)
        array[0] = (ju.mostSignificantBits shr 32).toInt()
        array[1] = (ju.mostSignificantBits and 0xffffffff).toInt()
        array[2] = (ju.leastSignificantBits shr 32).toInt()
        array[3] = (ju.leastSignificantBits and 0xffffffff).toInt()

        val cu = UUID<Any>(array, 0)

        assertEquals(ju.toString(), cu.toString())
    }

    @Test
    fun testMake2() {

        val cu = UUID<Any>()

        val ju = java.util.UUID.fromString(cu.toString())

        assertEquals(4, ju.version())
        assertEquals(2, ju.variant())

        assertEquals(ju.toString(), cu.toString())
    }

    @Test
    fun testMakeFromString() {
        val ju = java.util.UUID.randomUUID()
        val cu = UUID<Any>(ju.toString())

        assertEquals(4, ju.version())
        assertEquals(2, ju.variant())

        assertEquals(ju.toString(), cu.toString())
    }

    @Test
    fun testLongConversion() {
        val cu = UUID<Any>("ea3d1c2b-0559-4199-96f6-45c4fcfe6ccd")
        val ju = java.util.UUID(cu.msb, cu.lsb)

        assertEquals(ju.toString(), cu.toString())
    }

    @Test
    fun toAndFromByteArray() {
        for (i in 0 .. 100) {
            val expected = UUID<Any>()
            //println(expected)
            assertEquals(expected, expected.toByteArray().toUuid())
        }
    }
}