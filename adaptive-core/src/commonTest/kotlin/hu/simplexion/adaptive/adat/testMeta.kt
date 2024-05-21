/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData

val testMeta =
    AdatClassMetaData<TestClass>(
        version = 1,
        name = "hu.simplexion.adaptive.adat.TestClass",
        properties = listOf(
            AdatPropertyMetaData("someInt", 0, "I"),
            AdatPropertyMetaData("someBoolean", 1, "Z"),
            AdatPropertyMetaData("someIntListSet", 2, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        )
    )