/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class TestClass(
    var someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
) : AdatClass<TestClass> {

    constructor() : this(0, false, setOf())

    override val adatCompanion = Companion

    override var adatContext : AdatContext<Any>? = null

    override fun descriptor() { // not used in this test, compiler plugin tests and the "test" module contains actual unit tests
        properties {
            someInt minimum 23 maximum 200 default 34
        }
    }

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClass)

    override fun hashCode(): Int =
        adatHashCode()

    override fun genGetValue(index: Int): Any {
        return when (index) {
            0 -> someInt
            1 -> someBoolean
            2 -> someIntListSet
            else -> invalidIndex(index)
        }
    }

    override fun genSetValue(index: Int, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        when (index) {
            0 -> someInt = value as Int
            1 -> someBoolean = value as Boolean
            2 -> someIntListSet = value as Set<List<Int>>
            else -> invalidIndex(index)
        }
    }

    companion object : AdatCompanion<TestClass> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.TestClass"

        override val adatMetadata = AdatClassMetadata<TestClass>(
            version = 1,
            name = "fun.adaptive.adat.TestClass",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata(
                    "someInt", 0, 0, "I",
                    listOf(
                        AdatDescriptorMetadata("IntDefault", "23"),
                        AdatDescriptorMetadata("IntMinimum", "0"),
                        AdatDescriptorMetadata("IntMaximum", "100")
                    )
                ),
                AdatPropertyMetadata("someBoolean", 1, 0, "Z"),
                AdatPropertyMetadata("someIntListSet", 2, 0, "Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
            )
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override val adatDescriptors = adatMetadata.generateDescriptors()

        override fun newInstance() =
            TestClass()

        @Suppress("UNCHECKED_CAST")
        override fun newInstance(values: Array<Any?>): TestClass =
            TestClass(
                values[0] as Int,
                values[1] as Boolean,
                values[2] as Set<List<Int>>
            )
    }

}