/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class TestClassSignatureDiff(
    var someInt: Float,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
) : AdatClass<TestClassSignatureDiff> {

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
            get() = "hu.simplexion.adaptive.adat.TestClassSignatureDiff"

        override val adatMetadata = AdatClassMetadata<TestClassSignatureDiff>(
            version = 1,
            name = "hu.simplexion.adaptive.adat.TestClassSignatureDiff",
            properties = listOf(
                AdatPropertyMetadata("someInt", 0, "F"),
                AdatPropertyMetadata("someBoolean", 1, "Z"),
                AdatPropertyMetadata("someIntListSet", 2, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
            )
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance() = TestClassSignatureDiff()

    }

}