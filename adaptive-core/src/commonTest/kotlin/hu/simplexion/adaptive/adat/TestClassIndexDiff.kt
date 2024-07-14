/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetadata
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetadata
import hu.simplexion.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class TestClassIndexDiff(
    var someBoolean: Boolean,
    var someInt: Int,
    var someIntListSet: Set<List<Int>>
) : AdatClass<TestClassIndexDiff> {

    constructor() : this(false, 0, setOf())

    override val adatCompanion = Companion

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClassIndexDiff)

    override fun hashCode(): Int =
        adatHashCode()

    override fun getValue(index: Int): Any {
        return when (index) {
            0 -> someBoolean
            1 -> someInt
            2 -> someIntListSet
            else -> invalidIndex(index)
        }
    }

    override fun setValue(index: Int, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        when (index) {
            0 -> someBoolean = value as Boolean
            1 -> someInt = value as Int
            2 -> someIntListSet = value as Set<List<Int>>
            else -> invalidIndex(index)
        }
    }

    companion object : AdatCompanion<TestClassIndexDiff> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.adat.TestClassIndexDiff"

        override val adatMetadata = AdatClassMetadata<TestClassIndexDiff>(
            version = 1,
            name = "hu.simplexion.adaptive.adat.TestClassIndexDiff",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("someBoolean", 0, 0, "Z"),
                AdatPropertyMetadata("someInt", 1, 0, "I"),
                AdatPropertyMetadata("someIntListSet", 2, 0, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
            )
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance() = TestClassIndexDiff()
    }

}