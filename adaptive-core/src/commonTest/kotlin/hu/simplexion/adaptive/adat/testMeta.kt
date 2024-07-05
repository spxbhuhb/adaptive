/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata

val testMeta =
    AdatClassMetadata<TestClass>(
        version = 1,
        name = "hu.simplexion.adaptive.adat.TestClass",
        properties = listOf(
            AdatPropertyMetadata("someInt", 0, "I"),
            AdatPropertyMetadata("someBoolean", 1, "Z"),
            AdatPropertyMetadata("someIntListSet", 2, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        )
    )