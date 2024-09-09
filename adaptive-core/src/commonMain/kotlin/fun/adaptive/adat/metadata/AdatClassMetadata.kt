/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat.metadata

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.descriptor.AdatDescriptor
import `fun`.adaptive.adat.descriptor.AdatDescriptorSet
import `fun`.adaptive.adat.descriptor.DefaultDescriptorFactory
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.wireformat.fromJson

/**
 * @property  name   The fully qualified (dot separated) name of the adat class this metadata describes.
 */
@Adat
class AdatClassMetadata(
    val version: Int = 1,
    val name: String,
    val flags: Int,
    val properties: List<AdatPropertyMetadata>
) : AdatClass {

    /**
     * True then the class is mutable:
     *
     * - at least one property is `var` or has a getter
     * - **OR** at least one property has a mutable value
     */
    val isMutableClass
        inline get() = ! isImmutableClass

    /**
     * True then the whole class is immutable:
     *
     * - all properties are `val`
     * - **AND** all property values are immutable
     */
    val isImmutableClass
        get() = (flags and IMMUTABLE) != 0

    fun generateDescriptors(): Array<AdatDescriptorSet> {
        val result = mutableListOf<AdatDescriptorSet>()

        for (property in properties) {
            val propertyResult = mutableListOf<AdatDescriptor>()
            for (descriptor in property.descriptors) {
                propertyResult += DefaultDescriptorFactory.newInstance(descriptor.name, descriptor)
            }
            result += AdatDescriptorSet(property, propertyResult)
        }

        return result.toTypedArray()
    }

    operator fun get(propertyName: String): AdatPropertyMetadata =
        properties.first { it.name == propertyName }

    // --------------------------------------------------------------------------------
    // AdatClass overrides
    // --------------------------------------------------------------------------------

    override val adatCompanion: AdatCompanion<AdatClassMetadata>
        get() = AdatClassMetadata

    override fun equals(other: Any?): Boolean = adatEquals(other)
    override fun hashCode(): Int = adatHashCode()
    override fun toString(): String = adatToString()

    override fun genGetValue(index: Int): Any? =
        when (index) {
            0 -> version
            1 -> name
            2 -> flags
            3 -> properties
            else -> throw IndexOutOfBoundsException()
        }

    companion object : AdatCompanion<AdatClassMetadata> {

        /**
         * Set then the whole class is immutable:
         *
         * - all properties are `val`
         * - all property values are immutable
         */
        const val IMMUTABLE = 1

        override val wireFormatName: String
            get() = "fun.adaptive.adat.metadata.AdatClassMetadata"

        override val adatMetadata = AdatClassMetadata(
            version = 1,
            name = wireFormatName,
            flags = 0,
            properties = listOf(
                AdatPropertyMetadata("version", 0, 0, "I"),
                AdatPropertyMetadata("name", 1, 0, "T"),
                AdatPropertyMetadata("flags", 2, 0, "I"),
                AdatPropertyMetadata("properties", 3, 0, "Lkotlin.collections.List<Lfun.adaptive.adat.metadata.AdatPropertyMetadata;>;")
            )
        )

        override val adatWireFormat: AdatClassWireFormat<AdatClassMetadata>
            get() = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance(): AdatClassMetadata {
            throw UnsupportedOperationException()
        }

        override fun newInstance(values: Array<Any?>): AdatClassMetadata {
            @Suppress("UNCHECKED_CAST")
            return AdatClassMetadata(
                version = values[0] as Int,
                name = values[1] as String,
                flags = values[2] as Int,
                properties = values[3] as List<AdatPropertyMetadata>
            )
        }

        @Suppress("UNCHECKED_CAST")
        fun decodeFromString(a: String): AdatClassMetadata =
            a.encodeToByteArray().fromJson(this)

    }

}