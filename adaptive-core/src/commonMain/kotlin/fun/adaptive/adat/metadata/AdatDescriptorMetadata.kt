/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
data class AdatDescriptorMetadata(
    val name: String,
    val parameters: String
) : AdatClass {

    fun asBoolean() = parameters.toBooleanStrict()

    fun asInt() = parameters.toInt()

    // --------------------------------------------------------------------------------
    // AdatClass overrides
    // --------------------------------------------------------------------------------

    override val adatCompanion: AdatCompanion<AdatDescriptorMetadata>
        get() = AdatDescriptorMetadata

    override fun equals(other: Any?): Boolean = adatEquals(other)
    override fun hashCode(): Int = adatHashCode()
    override fun toString(): String = adatToString()

    override fun genGetValue(index: Int): Any? =
        when (index) {
            0 -> name
            1 -> parameters
            else -> invalidIndex(index)
        }

    companion object : AdatCompanion<AdatDescriptorMetadata> {

        override val wireFormatName: String
            get() = "fun.adaptive.adat.metadata.AdatDescriptorMetadata"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = wireFormatName,
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("name", 0, 0, "T"),
                AdatPropertyMetadata("parameters", 1, 0, "T")
            )
        )

        override val adatWireFormat: AdatClassWireFormat<AdatDescriptorMetadata>
            get() = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance(): AdatDescriptorMetadata {
            throw UnsupportedOperationException()
        }

        override fun newInstance(values: Array<Any?>): AdatDescriptorMetadata {
            @Suppress("UNCHECKED_CAST")
            return AdatDescriptorMetadata(
                name = values[0] as String,
                parameters = values[1] as String
            )
        }
    }
}