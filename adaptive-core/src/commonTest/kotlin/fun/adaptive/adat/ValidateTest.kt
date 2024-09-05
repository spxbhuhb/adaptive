/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import kotlin.test.Test
import kotlin.test.assertTrue

class ValidateTest {

    @Test
    fun basicValid() {
        val sl = setOf(listOf(1, 2), listOf(3, 4))
        val t = TestClass(34, false, sl)

        assertTrue(t.validate().isValid)
    }

    @Test
    fun basicInvalid() {
        val sl = setOf(listOf(1, 2), listOf(3, 4))
        val t = TestClass(200, false, sl)

        t.validate()

        assertTrue(! t.validate().isValid)
    }
}