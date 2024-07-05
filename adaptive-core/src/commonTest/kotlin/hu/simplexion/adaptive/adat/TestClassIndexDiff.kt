/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

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

        override val adatMetaData = decodeMetaData("1/hu.simplexion.adaptive.adat.TestClass/someBoolean/0/Z/someInt/1/I/someIntListSet/2/Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        override val adatWireFormat = AdatClassWireFormat(this, adatMetaData)

        override fun newInstance() = TestClassIndexDiff()
    }

}