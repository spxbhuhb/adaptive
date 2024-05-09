/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat

import hu.simplexion.adaptive.adat.metadata.AdatClassMetaData
import hu.simplexion.adaptive.adat.metadata.AdatPropertyMetaData
import hu.simplexion.adaptive.adat.wireformat.AdatClassWireFormat
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AdatTest {

    @Test
    fun basic() {
        val meta = TestClass.adatMetaData
        assertEquals(testMeta, meta)

        val sl = setOf(listOf(1,2),listOf(3,4))

        val t1 = TestClass(12, false, sl)

        assertEquals(t1, t1)
        assertEquals(t1, t1.copy())
        assertNotEquals(t1, TestClass(12, true, sl))

        assertEquals(t1, TestClass.fromJson(t1.toJson()))
        assertEquals(t1, TestClass.fromProto(t1.toProto()))
    }

}

/**
 * This class would be generated from:
 *
 * ```kotlin
 * @Adat
 * class TestClass(
 *     val someInt : Int,
 *     var someBoolean : Boolean,
 *     var someIntListSet : Set<List<Int>>
 * )
 * ```
 */
@Adat
class TestClass(
    override val adatValues: Array<Any?>
) : AdatClass<TestClass> {

    constructor() : this(arrayOfNulls(3))

    constructor(someInt: Int, someBoolean: Boolean, someIntListSet : Set<List<Int>>) : this(
        arrayOf<Any?>(someInt, someBoolean, someIntListSet)
    )

    override val adatCompanion = Companion

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClass)

    override fun hashCode(): Int =
        adatHashCode()

    companion object : AdatCompanion<TestClass> {

        override val adatMetaData = decodeMetaData("1/hu.simplexion.adaptive.adat.TestClass/someInt/0/I/someBoolean/1/Z/someIntListSet/2/Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        override val adatWireFormat = AdatClassWireFormat(this, adatMetaData)

        override fun newInstance(adatValues: Array<Any?>) =
            TestClass(adatValues)

    }

    val someInt
        get() = int(0)

    var someBoolean
        get() = boolean(1)
        set(value) {
            adatValues[1] = value
        }

    @Suppress("UNCHECKED_CAST")
    var someIntListSet
        get() = adatValues[2] as Set<List<Int>>
        set(value) {
            adatValues[2] = value
        }

}

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

