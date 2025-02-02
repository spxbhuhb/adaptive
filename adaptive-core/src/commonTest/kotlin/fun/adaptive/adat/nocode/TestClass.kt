/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.nocode

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatDescriptorMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class TestClass(
    val someInt: Int,
    val someString: String
) : AdatClass {

    override val adatCompanion = Companion

    override var adatContext: AdatContext<*>? = null

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? TestClass)

    override fun hashCode(): Int =
        adatHashCode()

    override fun genGetValue(index: Int): Any {
        return when (index) {
            0 -> someInt
            1 -> someString
            else -> invalidIndex(index)
        }
    }

    override fun genSetValue(index: Int, value: Any?) {
        invalidIndex(index)
    }

    companion object : AdatCompanion<TestClass> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.nocode.TestClass"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.adat.nocode.TestClass",
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("someInt", 0, 0, "I"),
                AdatPropertyMetadata("someString", 1, 0, "T"),
            )
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override val adatDescriptors = adatMetadata.generateDescriptors()

        @Suppress("UNCHECKED_CAST")
        override fun newInstance(values: Array<Any?>): TestClass =
            TestClass(
                values[0] as Int,
                values[1] as String
            )
    }

}