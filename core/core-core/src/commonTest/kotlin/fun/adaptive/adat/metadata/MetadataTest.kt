/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.TestClass
import `fun`.adaptive.adat.polymorphic.PolymorphicTestClass
import `fun`.adaptive.wireformat.fromJson
import `fun`.adaptive.wireformat.toJson
import kotlin.test.Test
import kotlin.test.assertEquals

class MetadataTest {

    @Test
    fun basic() {
        val expected = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.adat.metadata.TestClass",
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
    fun getPropertyMetadataTop() {
        val metadata = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.adat.metadata.TestClass",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("someInt", 0, 0, "I"),
                AdatPropertyMetadata("someBoolean", 1, 0, "Z"),
                AdatPropertyMetadata("someIntListSet", 2, 0, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
            )
        )

        assertEquals(
            null to metadata.properties[0],
            metadata.getPropertyMetadataOrNull(listOf("someInt"))
        )

        assertEquals(
            null to metadata.properties[1],
            metadata.getPropertyMetadataOrNull(listOf("someBoolean"))
        )

        assertEquals(
            metadata to metadata.properties[2],
            metadata.getPropertyMetadataOrNull(listOf("someIntListSet"), metadata)
        )
    }

    @Test
    fun getPropertyMetadataFirst() {
        val something = TestClass()
        val instance = PolymorphicTestClass(something)

        assertEquals(
            something to TestClass.adatMetadata.properties[0],
            PolymorphicTestClass.adatMetadata.getPropertyMetadataOrNull(listOf("something", "someInt"), instance)
        )

    }

}