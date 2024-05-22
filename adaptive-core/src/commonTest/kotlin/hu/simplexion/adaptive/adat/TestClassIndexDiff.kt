/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.wireformat.AdatClassWireFormat

/**
 * This class would be generated from:
 *
 * ```kotlin
 * @Adat
 * class TestClassIndexDiff(
 *     var someBoolean : Boolean,
 *     val someInt : Int,
 *     var someIntListSet : Set<List<Int>>
 * )
 * ```
 */
@Adat
class TestClassIndexDiff(
    override val adatValues: Array<Any?>
) : AdatClass<TestClassIndexDiff> {

    constructor() : this(arrayOfNulls(3))

    constructor(someBoolean: Boolean, someInt: Int, someIntListSet : Set<List<Int>>) : this(
        arrayOf<Any?>(someInt, someBoolean, someIntListSet)
    )

    override val adatCompanion = Companion

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClassIndexDiff)

    override fun hashCode(): Int =
        adatHashCode()

    companion object : AdatCompanion<TestClassIndexDiff> {

        override val adatMetaData = decodeMetaData("1/hu.simplexion.adaptive.adat.TestClass/someBoolean/0/Z/someInt/1/I/someIntListSet/2/Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        override val adatWireFormat = AdatClassWireFormat(this, adatMetaData)

        override fun newInstance(adatValues: Array<Any?>) =
            TestClassIndexDiff(adatValues)

    }

}