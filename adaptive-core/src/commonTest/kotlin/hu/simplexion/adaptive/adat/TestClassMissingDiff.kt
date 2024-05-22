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
 * class TestClassMissingDiff(
 *     val someInt : Int,
 *     var someOtherField : Boolean,
 *     var someIntListSet : Set<List<Int>>
 * )
 * ```
 */
@Adat
class TestClassMissingDiff(
    override val adatValues: Array<Any?>
) : AdatClass<TestClassMissingDiff> {

    constructor() : this(arrayOfNulls(3))

    constructor(someInt: Int, someBoolean: Boolean, someIntListSet : Set<List<Int>>) : this(
        arrayOf<Any?>(someInt, someBoolean, someIntListSet)
    )

    override val adatCompanion = Companion

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClassMissingDiff)

    override fun hashCode(): Int =
        adatHashCode()

    companion object : AdatCompanion<TestClassMissingDiff> {

        override val adatMetaData = decodeMetaData("1/hu.simplexion.adaptive.adat.TestClass/someInt/0/I/someOtherField/1/Z/someIntListSet/2/Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        override val adatWireFormat = AdatClassWireFormat(this, adatMetaData)

        override fun newInstance(adatValues: Array<Any?>) =
            TestClassMissingDiff(adatValues)

    }

}