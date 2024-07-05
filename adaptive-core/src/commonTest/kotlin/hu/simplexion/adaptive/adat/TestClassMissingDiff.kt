/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class TestClassMissingDiff(
    var someInt: Int,
    var someOtherField: Boolean,
    var someIntListSet: Set<List<Int>>
) : AdatClass<TestClassMissingDiff> {

    constructor() : this(0, false, setOf())

    override val adatCompanion = Companion

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClassMissingDiff)

    override fun hashCode(): Int =
        adatHashCode()

    override fun getValue(index: Int): Any {
        return when (index) {
            0 -> someInt
            1 -> someOtherField
            2 -> someIntListSet
            else -> invalidIndex(index)
        }
    }

    override fun setValue(index: Int, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        when (index) {
            0 -> someInt = value as Int
            1 -> someOtherField = value as Boolean
            2 -> someIntListSet = value as Set<List<Int>>
            else -> invalidIndex(index)
        }
    }

    companion object : AdatCompanion<TestClassMissingDiff> {

        override val adatMetaData = decodeMetaData("1/hu.simplexion.adaptive.adat.TestClass/someInt/0/I/someOtherField/1/Z/someIntListSet/2/Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        override val adatWireFormat = AdatClassWireFormat(this, adatMetaData)

        override fun newInstance() = TestClassMissingDiff()

    }

}