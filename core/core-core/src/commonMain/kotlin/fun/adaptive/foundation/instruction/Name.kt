/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.instruction

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.metadata.AdatClassMetadata.Companion.IMMUTABLE
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.wireformat.WireFormatRegistry

fun name(name : String) = Name(name)

/**
 * Give a name to a fragment. Trace checks if the fragment has this instruction
 * and uses it instead of the class name if so.
 */
@Adat
class Name(val name: String) : AdatClass, AdaptiveInstruction {

    constructor() : this("<anonymous>")

    override val adatCompanion = Companion

    override var adatContext: AdatContext<*>? = null

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? Name)

    override fun hashCode(): Int =
        adatHashCode()

    override fun toString(): String =
        "Name(name=$name)"

    override fun genGetValue(index: Int): Any? {
        return when (index) {
            0 -> name
            else -> invalidIndex(index)
        }
    }

    override fun genSetValue(index: Int, value: Any?) {
        invalidIndex(index)
    }

    companion object : AdatCompanion<Name> {

        val ANONYMOUS = Name("anonymous")

        override val wireFormatName: String
            get() = "fun.adaptive.foundation.instruction.Name"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = "fun.adaptive.foundation.instruction.Name",
            flags = 0,
            properties = listOf(AdatPropertyMetadata("name", 0, IMMUTABLE, "T"))
        )

        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override val adatDescriptors = adatMetadata.generateDescriptors()

        @Suppress("UNCHECKED_CAST")
        override fun newInstance(values: Array<Any?>): Name =
            Name(values[0] as String)

        init {
            WireFormatRegistry += adatWireFormat
        }
    }

}