/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.polymorphic

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata.Companion.NULLABLE
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.wireformat.WireFormatRegistry

@Adat
class PolymorphicTestClass(
    var something: Any?
) : AdatClass {

    override val adatCompanion = Companion

    override var adatContext: AdatContext<*>? = null

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? PolymorphicTestClass)

    override fun hashCode(): Int =
        adatHashCode()

    override fun genGetValue(index: Int): Any? {
        return when (index) {
            0 -> something
            else -> invalidIndex(index)
        }
    }

    override fun genSetValue(index: Int, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        when (index) {
            0 -> something = value as Any
            else -> invalidIndex(index)
        }
    }

    companion object : AdatCompanion<PolymorphicTestClass> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.polymorphic.PolymorphicTestClass"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.adat.polymorphic.PolymorphicTestClass",
            flags = 0,
            properties = listOf(AdatPropertyMetadata("something", 0, NULLABLE, "Lkotlin.Any;"))
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override val adatDescriptors = adatMetadata.generateDescriptors()

        @Suppress("UNCHECKED_CAST")
        override fun newInstance(values: Array<Any?>): PolymorphicTestClass =
            PolymorphicTestClass(
                values[0]
            )

        init {
            WireFormatRegistry += adatWireFormat
        }
    }

}