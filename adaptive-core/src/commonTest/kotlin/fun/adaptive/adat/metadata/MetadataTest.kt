/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.TestClass
import `fun`.adaptive.adat.descriptor.constraint.IntMaximum
import `fun`.adaptive.adat.descriptor.constraint.IntMinimum
import `fun`.adaptive.wireformat.fromJson
import `fun`.adaptive.wireformat.toJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MetadataTest {

    @Test
    fun basic() {
        val expected = AdatClassMetadata<TestClass>(
            version = 1,
            name = "fun.adaptive.adat.TestClass",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("someInt", 0, 0, "I"),
                AdatPropertyMetadata("someBoolean", 1, 0, "Z"),
                AdatPropertyMetadata("someIntListSet", 2, 0, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
            )
        )

        val payload = expected.toJson(AdatClassMetadata)
        val actual = payload.fromJson(AdatClassMetadata)

        assertEquals(expected, actual)
    }

    @Test
    fun intConstraints() {
        val expected = AdatClassMetadata<Any>(
            version = 1,
            name = "fun.adaptive.adat.TestClass",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata(
                    "someInt", 0, 0, "I",
                    listOf(
                        AdatDescriptorMetadata("int:minimum", "5"),
                        AdatDescriptorMetadata("int:maximum", "10")
                    )
                )
            )
        )

        val payload = expected.toJson(AdatClassMetadata)
        val actual = payload.fromJson(AdatClassMetadata)

        assertEquals(expected, actual)

        val constraints = actual.generateDescriptors()

        val min = constraints[0]
        assertTrue(min is IntMinimum)
        assertEquals(5, min.limit)

        val max = constraints[1]
        assertTrue(max is IntMaximum)
        assertEquals(10, max.limit)
    }

}