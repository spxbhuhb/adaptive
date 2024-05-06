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
        val meta = TestClass.adatMetadata
        assertEquals(testMeta, meta)

        val t1 = TestClass(12, false)

        assertEquals(t1, t1)
        assertEquals(t1, t1.copy())
        assertNotEquals(t1, TestClass(12, true))

        assertEquals(t1, TestClass.fromJson(t1.toJson()))
        assertEquals(t1, TestClass.fromProto(t1.toProto()))
    }

}

@Adat
class TestClass(
    override val adatValues: Array<Any?>
) : AdatClass<TestClass> {

    constructor() : this(arrayOfNulls(2))

    constructor(someInt: Int, someBoolean: Boolean) : this(arrayOf<Any?>(someInt, someBoolean))

    override val adatCompanion = Companion

    override fun equals(other: Any?): Boolean =
        sEquals(this, other as? TestClass)

    override fun hashCode(): Int =
        adatValues.contentHashCode()

    companion object : AdatCompanion<TestClass> {

        override val adatMetadata = decodeMetaData(encodedTestMeta)
        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance(values: Array<Any?>) =
            TestClass(values)

    }

    val someInt
        get() = int(0)

    var someBoolean
        get() = boolean(1)
        set(value) {
            adatValues[1] = value
        }

}

val testMeta =
    AdatClassMetaData<TestClass>(
        version = 1,
        name = "hu.simplexion.adaptive.sign.TestClass",
        properties = listOf(
            AdatPropertyMetaData("someInt", 0, "I"),
            AdatPropertyMetaData("someBoolean", 1, "Z")
        )
    )

val encodedTestMeta =
    JsonWireFormatProvider().encoder().rawInstance(testMeta, AdatClassMetaData).pack().decodeToString()