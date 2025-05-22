/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class TestClass2(
    val instance : TestClass,
    val instanceOrNull : TestClass2? = null,
    val list: List<TestClass2> = emptyList(),
    val listOrNull : List<TestClass2>? = null,
    val set : Set<TestClass2> = emptySet(),
    val setOrNull : Set<TestClass2>? = null,
    val map: Map<String, TestClass2> = emptyMap(),
    val mapOrNull: Map<String, TestClass2>? = null
) : AdatClass {

    override val adatCompanion = Companion

    override var adatContext: AdatContext<*>? = null

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClass2)

    override fun hashCode(): Int =
        adatHashCode()

    override fun genGetValue(index: Int): Any? {
        return when (index) {
            0 -> instance
            1 -> instanceOrNull
            2 -> list
            3 -> listOrNull
            4 -> set
            5 -> setOrNull
            6 -> map
            7 -> mapOrNull
            else -> invalidIndex(index)
        }
    }

    companion object : AdatCompanion<TestClass2> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.TestClass2"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.adat.TestClass2",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("instance", 0, 0, "Lfun.adaptive.adat.TestClass;"),
                AdatPropertyMetadata("instanceOrNull", 1, 0, "Lfun.adaptive.adat.TestClass2;?"),
                AdatPropertyMetadata("list", 2, 0, "Ljava.util.List<Lfun.adaptive.adat.TestClass2;>;"),
                AdatPropertyMetadata("listOrNull", 3, 0, "Ljava.util.List<Lfun.adaptive.adat.TestClass2;>;?"),
                AdatPropertyMetadata("set", 4, 0, "Ljava.util.Set<Lfun.adaptive.adat.TestClass2;>;"),
                AdatPropertyMetadata("setOrNull", 5, 0, "Ljava.util.Set<Lfun.adaptive.adat.TestClass2;>;?"),
                AdatPropertyMetadata("map", 6, 0, "Ljava.util.Map<Ljava.lang.String;Lfun.adaptive.adat.TestClass2;>;"),
                AdatPropertyMetadata("mapOrNull", 7, 0, "Ljava.util.Map<Ljava.lang.String;Lfun.adaptive.adat.TestClass2;>;?")
            )
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override val adatDescriptors = adatMetadata.generateDescriptors()

        @Suppress("UNCHECKED_CAST")
        override fun newInstance(values: Array<Any?>): TestClass2 =
            TestClass2(
                values[0] as TestClass,
                values[1] as TestClass2?,
                values[2] as List<TestClass2>,
                values[3] as List<TestClass2>?,
                values[4] as Set<TestClass2>,
                values[5] as Set<TestClass2>?,
                values[6] as Map<String, TestClass2>,
                values[7] as Map<String, TestClass2>?
            )
    }

}