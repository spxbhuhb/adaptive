/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class TestClassSignatureDiff(
    var someInt: Float,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
) : AdatClass {

    constructor() : this(0.0f, false, setOf())

    override val adatCompanion = Companion

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClassSignatureDiff)

    override fun hashCode(): Int =
        adatHashCode()

    override fun getValue(index: Int): Any {
        return when (index) {
            0 -> someInt
            1 -> someBoolean
            2 -> someIntListSet
            else -> invalidIndex(index)
        }
    }

    override fun setValue(index: Int, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        when (index) {
            0 -> someInt = value as Float
            1 -> someBoolean = value as Boolean
            2 -> someIntListSet = value as Set<List<Int>>
            else -> invalidIndex(index)
        }
    }

    companion object : AdatCompanion<TestClassSignatureDiff> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.TestClassSignatureDiff"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.adat.TestClassSignatureDiff",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("someInt", 0, 0, "F"),
                AdatPropertyMetadata("someBoolean", 1, 0, "Z"),
                AdatPropertyMetadata("someIntListSet", 2, 0, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
            )
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance() = TestClassSignatureDiff()

    }

}