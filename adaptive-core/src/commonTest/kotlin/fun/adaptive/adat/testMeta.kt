/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata

val testMeta =
    AdatClassMetadata<TestClass>(
        version = 1,
        name = "fun.adaptive.adat.TestClass",
        flags = 0,
        properties = listOf(
            AdatPropertyMetadata("someInt", 0, 0, "I"),
            AdatPropertyMetadata("someBoolean", 1, 0, "Z"),
            AdatPropertyMetadata("someIntListSet", 2, 0, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        )
    )